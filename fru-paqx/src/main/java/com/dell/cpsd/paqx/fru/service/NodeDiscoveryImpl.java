/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.service;

import com.dell.cpsd.hdp.capability.registry.api.Capability;
import com.dell.cpsd.hdp.capability.registry.client.CapabilityRegistryException;
import com.dell.cpsd.paqx.fru.amqp.consumer.handler.RequestResponseMatcher;
import com.dell.cpsd.paqx.fru.amqp.model.ListNodesRequest;
import com.dell.cpsd.paqx.fru.amqp.model.ListNodesResponse;
import com.dell.cpsd.paqx.fru.amqp.model.MessageProperties;
import com.dell.cpsd.service.common.client.exception.ServiceTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
@Service
public class NodeDiscoveryImpl implements NodeDiscovery
{
    private static final Logger LOG = LoggerFactory.getLogger(NodeDiscoveryImpl.class);

    private final RabbitTemplate         rabbitTemplate;
    private final RequestResponseMatcher requestResponseMatcher;
    private final String                 replyTo;
    private final FruService             fruService;

    @Autowired
    public NodeDiscoveryImpl(final RabbitTemplate rabbitTemplate, final RequestResponseMatcher requestResponseMatcher,
            @Qualifier("replyTo") String replyTo, final FruService fruService)
    {
        this.rabbitTemplate = rabbitTemplate;
        this.requestResponseMatcher = requestResponseMatcher;
        this.replyTo = replyTo;
        this.fruService = fruService;
    }

    /**
     * Looks up and invokes a Control Plane Provider to meet the required capability.
     *
     * @return A CompletableFuture that will be completed when the response is received from the Control Plane Provider
     */
    @Override
    public CompletableFuture<List<ListNodesResponse.Node>> discover()
    {
        final String requiredCapability = "rackhd-list-nodes";

        try
        {
            final List<Capability> matchedCapabilities = fruService.findMatchingCapabilities(requiredCapability);
            if (matchedCapabilities.isEmpty())
            {
                LOG.info("No matching capability found for capability [{}]", requiredCapability);
                return CompletableFuture.completedFuture(null);
            }

            final Capability matchedCapability = matchedCapabilities.stream().findFirst().get();
            LOG.debug("Found capability {}", matchedCapability.getProfile());

            final Map<String, String> amqpProperties = fruService.declareBinding(matchedCapability, replyTo);

            final String correlationId = UUID.randomUUID().toString();
            final ListNodesRequest requestMessage = new ListNodesRequest();

            requestMessage.setMessageProperties(new MessageProperties(new Date(), correlationId, replyTo));
            final CompletableFuture<List<ListNodesResponse.Node>> promise = new CompletableFuture<>();
            requestResponseMatcher.onNext(correlationId, promise);

            final String requestExchange = amqpProperties.get("request-exchange");
            final String requestRoutingKey = amqpProperties.get("request-routing-key");

            rabbitTemplate.convertAndSend(requestExchange, requestRoutingKey, requestMessage);
            return promise;
        }
        catch (CapabilityRegistryException | ServiceTimeoutException e)
        {
            return CompletableFuture.completedFuture(Collections.emptyList());
        }
    }

}
