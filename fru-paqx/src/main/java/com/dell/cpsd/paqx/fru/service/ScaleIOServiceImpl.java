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
import com.dell.cpsd.paqx.fru.amqp.consumer.handler.AsyncAcknowledgement;
import com.dell.cpsd.paqx.fru.rest.dto.EndpointCredentials;
import com.dell.cpsd.service.common.client.exception.ServiceTimeoutException;
import com.dell.cpsd.storage.capabilities.api.ListStorageRequestMessage;
import com.dell.cpsd.storage.capabilities.api.MessageProperties;
import com.dell.cpsd.storage.capabilities.api.ScaleIOSystemDataRestRep;
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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
@Service
public class ScaleIOServiceImpl implements ScaleIOService {
    private static final Logger LOG = LoggerFactory.getLogger(ScaleIOServiceImpl.class);

    private final ICapabilityRegistryLookupManager capabilityRegistryLookupManager;
    private final RabbitTemplate rabbitTemplate;
    private final AmqpAdmin amqpAdmin;
    private final Queue responseQueue;
    private final AsyncAcknowledgement asyncAcknowledgement;
    private final String replyTo;

    @Autowired
    public ScaleIOServiceImpl(final ICapabilityRegistryLookupManager capabilityRegistryLookupManager, final RabbitTemplate rabbitTemplate,
                              final AmqpAdmin amqpAdmin, final Queue responseQueue,
                              @Qualifier(value = "listStorageResponseHandler") final AsyncAcknowledgement asyncAcknowledgement, final String replyTo) {
        this.capabilityRegistryLookupManager = capabilityRegistryLookupManager;
        this.rabbitTemplate = rabbitTemplate;
        this.amqpAdmin = amqpAdmin;
        this.responseQueue = responseQueue;
        this.asyncAcknowledgement = asyncAcknowledgement;
        this.replyTo = replyTo;
    }

    public CompletableFuture<ScaleIOSystemDataRestRep> listStorage(final EndpointCredentials scaleIOCredentials) {
        final String requiredCapability = "coprhd-list-storage";
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
                        ListStorageRequestMessage requestMessage = new ListStorageRequestMessage();
                        MessageProperties messageProperties = new MessageProperties();
                        messageProperties.setCorrelationId((correlationId.toString()));
                        messageProperties.setReplyTo(replyTo);
                        messageProperties.setTimestamp(new Date());
                        requestMessage.setMessageProperties(messageProperties);

                        try {
                            new URL(scaleIOCredentials.getEndpointUrl());
                        } catch (MalformedURLException e) {
                            final CompletableFuture<ScaleIOSystemDataRestRep> promise = new CompletableFuture<>();
                            promise.completeExceptionally(e);
                            return promise;
                        }
                        requestMessage.setEndpointURL(scaleIOCredentials.getEndpointUrl());
                        requestMessage.setPassword(scaleIOCredentials.getPassword());
                        requestMessage.setUserName(scaleIOCredentials.getUsername());

                        final CompletableFuture<ScaleIOSystemDataRestRep> promise = asyncAcknowledgement.register(correlationId.toString());

                        rabbitTemplate.convertAndSend(requestExchange, requestRoutingKey, requestMessage);

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
        return CompletableFuture.completedFuture(null);

    }
}
