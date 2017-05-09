/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.amqp.consumer.handler;

import com.dell.cpsd.common.rabbitmq.consumer.error.ErrorTransformer;
import com.dell.cpsd.common.rabbitmq.consumer.handler.DefaultMessageHandler;
import com.dell.cpsd.common.rabbitmq.message.HasMessageProperties;
import com.dell.cpsd.common.rabbitmq.validators.DefaultMessageValidator;
import com.dell.cpsd.storage.capabilities.api.ListStorageResponseMessage;
import com.dell.cpsd.storage.capabilities.api.ScaleIOSystemDataRestRep;
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
public class ListStorageResponseHandler extends DefaultMessageHandler<ListStorageResponseMessage>
        implements AsyncAcknowledgement<ScaleIOSystemDataRestRep> {
    private static final Logger LOG = LoggerFactory
            .getLogger(ListStorageResponseHandler.class);
    private Map<String, CompletableFuture<ScaleIOSystemDataRestRep>> asyncRequests = new HashMap<>();

    @Autowired
    public ListStorageResponseHandler(ErrorTransformer<HasMessageProperties<?>> errorTransformer) {
        super(ListStorageResponseMessage.class, new DefaultMessageValidator<>(), EXCHANGE_FRU_RESPONSE, errorTransformer);
    }

    @Override
    protected void executeOperation(final ListStorageResponseMessage listStorageResponseMessage) throws Exception {
        LOG.info("Received message {}", listStorageResponseMessage);
        final String correlationId = listStorageResponseMessage.getMessageProperties().getCorrelationId();

        final ScaleIOSystemDataRestRep scaleIOSystemDataRestRep = listStorageResponseMessage.getScaleIOSystemDataRestRep();

        final CompletableFuture<ScaleIOSystemDataRestRep> completableFuture = asyncRequests.get(correlationId);

        LOG.info("Completing expectation for  {} {}", correlationId, completableFuture);

        // TODO: write to data service
        if (completableFuture != null) {
            final boolean complete = completableFuture.complete(scaleIOSystemDataRestRep);
            LOG.info("Completed expectation for  {} {} {}", correlationId, completableFuture, complete);
            asyncRequests.remove(correlationId);
        }
    }

    @Override
    public CompletableFuture<ScaleIOSystemDataRestRep> register(final String correlationId) {
        LOG.info("Setting expectation for  {}", correlationId);
        CompletableFuture<ScaleIOSystemDataRestRep> completableFuture = new CompletableFuture<>();
        completableFuture.whenComplete((systemRest, throwable) -> asyncRequests.remove(correlationId));
        asyncRequests.put(correlationId, completableFuture);
        return completableFuture;
    }
}
