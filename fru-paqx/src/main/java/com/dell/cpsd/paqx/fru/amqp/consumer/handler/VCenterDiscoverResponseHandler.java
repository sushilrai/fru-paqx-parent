/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.amqp.consumer.handler;

import com.dell.cpsd.common.rabbitmq.consumer.error.ErrorTransformer;
import com.dell.cpsd.common.rabbitmq.consumer.handler.DefaultMessageHandler;
import com.dell.cpsd.common.rabbitmq.message.HasMessageProperties;
import com.dell.cpsd.common.rabbitmq.validators.DefaultMessageValidator;
import com.dell.cpsd.paqx.fru.rest.dto.vCenterSystemProperties;
import com.dell.cpsd.paqx.fru.transformers.DiscoveryInfoToVCenterSystemPropertiesTransformer;
import com.dell.cpsd.virtualization.capabilities.api.DiscoveryResponseInfoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.dell.cpsd.paqx.fru.amqp.config.RabbitConfig.EXCHANGE_FRU_RESPONSE;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
public class VCenterDiscoverResponseHandler extends DefaultMessageHandler<DiscoveryResponseInfoMessage>
        implements AsyncAcknowledgement<vCenterSystemProperties> {
    private static final Logger LOG = LoggerFactory.getLogger(VCenterDiscoverResponseHandler.class);
    private final DiscoveryInfoToVCenterSystemPropertiesTransformer discoveryInfoToVCenterSystemProperties;
    private Map<String, CompletableFuture<vCenterSystemProperties>> asyncRequests = new HashMap<>();

    @Autowired
    public VCenterDiscoverResponseHandler(ErrorTransformer<HasMessageProperties<?>> errorTransformer,
                                          DiscoveryInfoToVCenterSystemPropertiesTransformer discoveryInfoToVCenterSystemProperties) {
        super(DiscoveryResponseInfoMessage.class, new DefaultMessageValidator<>(), EXCHANGE_FRU_RESPONSE, errorTransformer);
        this.discoveryInfoToVCenterSystemProperties = discoveryInfoToVCenterSystemProperties;
    }

    @Override
    protected void executeOperation(final DiscoveryResponseInfoMessage discoveryResponseInfoMessage) throws Exception {
        LOG.info("Received message {}", discoveryResponseInfoMessage);
        final String correlationId = discoveryResponseInfoMessage.getMessageProperties().getCorrelationId();

        final vCenterSystemProperties vCenterSystemProperties = discoveryInfoToVCenterSystemProperties
                .transform(discoveryResponseInfoMessage);

        final CompletableFuture<vCenterSystemProperties> completableFuture = asyncRequests.get(correlationId);

        LOG.info("Completing expectation for  {} {}", correlationId, completableFuture);

        if (completableFuture != null) {
            final boolean complete = completableFuture.complete(vCenterSystemProperties);
            LOG.info("Completed expectation for  {} {} {}", correlationId, completableFuture, complete);
            asyncRequests.remove(correlationId);
        }
    }

    @Override
    public CompletableFuture<vCenterSystemProperties> register(final String correlationId) {
        LOG.info("Setting expectation for  {}", correlationId);
        CompletableFuture<vCenterSystemProperties> completableFuture = new CompletableFuture<>();
        completableFuture.whenComplete((systemRest, throwable) -> asyncRequests.remove(correlationId));
        asyncRequests.put(correlationId, completableFuture);
        return completableFuture;
    }
}
