/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.service;

import com.dell.cpsd.hdp.capability.registry.api.Capability;
import com.dell.cpsd.hdp.capability.registry.api.CapabilityProvider;
import com.dell.cpsd.hdp.capability.registry.api.EndpointProperty;
import com.dell.cpsd.hdp.capability.registry.client.CapabilityRegistryException;
import com.dell.cpsd.hdp.capability.registry.client.ICapabilityRegistryLookupManager;
import com.dell.cpsd.hdp.capability.registry.client.callback.ListCapabilityProvidersResponse;
import com.dell.cpsd.paqx.fru.amqp.consumer.handler.RequestResponseMatcher;
import com.dell.cpsd.paqx.fru.amqp.model.ListNodesRequest;
import com.dell.cpsd.paqx.fru.amqp.model.ListNodesResponse;
import com.dell.cpsd.paqx.fru.amqp.model.MessageProperties;
import com.dell.cpsd.service.common.client.exception.ServiceTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
@Service
public class NodeDiscoveryImpl implements NodeDiscovery {
    private static final Logger LOG = LoggerFactory.getLogger(NodeDiscoveryImpl.class);

    private final ICapabilityRegistryLookupManager capabilityRegistryLookupManager;
    private final RabbitTemplate rabbitTemplate;
    private final AmqpAdmin amqpAdmin;
    private final Queue responseQueue;
    private final RequestResponseMatcher requestResponseMatcher;
    private final String replyTo;

    /**
     * @param capabilityRegistryLookupManager
     * @param rabbitTemplate
     * @param amqpAdmin
     * @param responseQueue
     * @param requestResponseMatcher
     * @param replyTo
     */
    @Autowired
    public NodeDiscoveryImpl(final ICapabilityRegistryLookupManager capabilityRegistryLookupManager, final RabbitTemplate rabbitTemplate,
                             final AmqpAdmin amqpAdmin, @Qualifier("responseQueue") final Queue responseQueue,
                             final RequestResponseMatcher requestResponseMatcher, @Qualifier("replyTo") String replyTo) {
        this.capabilityRegistryLookupManager = capabilityRegistryLookupManager;
        this.rabbitTemplate = rabbitTemplate;
        this.amqpAdmin = amqpAdmin;
        this.responseQueue = responseQueue;
        this.requestResponseMatcher = requestResponseMatcher;
        this.replyTo = replyTo;
    }

    /**
     * Looks up and invokes a Control Plane Provider to meet the required capability.
     *
     * @return A CompletableFuture that will be completed when the response is received from the Control Plane Provider
     */
    @Override
    public CompletableFuture<List<ListNodesResponse.Node>> discover() {
        final String requiredCapability = "rackhd-list-nodes";
        try {
            final ListCapabilityProvidersResponse listCapabilityProvidersResponse = capabilityRegistryLookupManager
                    .listCapabilityProviders(TimeUnit.SECONDS.toMillis(5));

            for (final CapabilityProvider capabilityProvider : listCapabilityProvidersResponse.getResponse()) {
                for (final Capability capability : capabilityProvider.getCapabilities()) {
                    LOG.debug("Found capability {}", capability.getProfile());

                    if (requiredCapability.equals(capability.getProfile())) {
                        LOG.debug("Found matching capability {}", capability.getProfile());
                        final List<EndpointProperty> endpointProperties = capability.getProviderEndpoint().getEndpointProperties();
                        final Map<String, String> amqpProperties = endpointProperties.stream()
                                .collect(Collectors.toMap(EndpointProperty::getName, EndpointProperty::getValue));

                        final String requestExchange = amqpProperties.get("request-exchange");
                        final String requestRoutingKey = amqpProperties.get("request-routing-key");

                        final TopicExchange responseExchange = new TopicExchange(amqpProperties.get("response-exchange"));
                        final String responseRoutingKey = amqpProperties.get("response-routing-key").replace("{replyTo}", "." + replyTo);

                        amqpAdmin.declareBinding(BindingBuilder.bind(responseQueue).to(responseExchange).with(responseRoutingKey));

                        LOG.debug("Adding binding {} {}", responseExchange.getName(), responseRoutingKey);

                        final UUID correlationId = UUID.randomUUID();
                        ListNodesRequest requestMessage = new ListNodesRequest();
                        requestMessage.setMessageProperties(
                                new MessageProperties().withCorrelationId(correlationId.toString()).withReplyTo(replyTo)
                                        .withTimestamp(new Date()));

                        final CompletableFuture<List<ListNodesResponse.Node>> promise = new CompletableFuture<>();
                        requestResponseMatcher.onNext(correlationId.toString(), promise);

                        rabbitTemplate.convertAndSend(requestExchange, requestRoutingKey, requestMessage);

                        LOG.debug("Returning promise");

                        return promise;
                    }
                }
            }
        } catch (CapabilityRegistryException e) {
            LOG.error("Failed while looking up Capability Registry for {}", requiredCapability, e);
        } catch (ServiceTimeoutException e) {
            LOG.error("Service timed out while querying Capability Registry");
        }
        LOG.error("Unable to find required capability: {}", requiredCapability);
        return CompletableFuture.completedFuture(Collections.emptyList());
    }
}
