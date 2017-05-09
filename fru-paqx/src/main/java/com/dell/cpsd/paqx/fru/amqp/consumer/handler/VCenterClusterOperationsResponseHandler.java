/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 */

package com.dell.cpsd.paqx.fru.amqp.consumer.handler;

import com.dell.cpsd.common.rabbitmq.consumer.error.ErrorTransformer;
import com.dell.cpsd.common.rabbitmq.consumer.handler.DefaultMessageHandler;
import com.dell.cpsd.common.rabbitmq.message.HasMessageProperties;
import com.dell.cpsd.common.rabbitmq.validators.DefaultMessageValidator;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.ClusterOperationResponse;
import com.dell.cpsd.virtualization.capabilities.api.ClusterOperationResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.dell.cpsd.paqx.fru.amqp.config.RabbitConfig.EXCHANGE_FRU_RESPONSE;

/**
 * TODO: Document usage.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * </p>
 *
 * @version 1.0
 * @since 1.0
 */
public class VCenterClusterOperationsResponseHandler extends DefaultMessageHandler<ClusterOperationResponseMessage>
        implements AsyncAcknowledgement<ClusterOperationResponse>
{
    private static final Logger                                                   LOG           = LoggerFactory
            .getLogger(VCenterClusterOperationsResponseHandler.class);
    private              Map<String, CompletableFuture<ClusterOperationResponse>> asyncRequests = new HashMap<>();

    public VCenterClusterOperationsResponseHandler(ErrorTransformer<HasMessageProperties<?>> errorTransformer)
    {
        super(ClusterOperationResponseMessage.class, new DefaultMessageValidator<>(), EXCHANGE_FRU_RESPONSE, errorTransformer);
    }

    @Override
    protected void executeOperation(final ClusterOperationResponseMessage clusterOperationResponseMessage) throws Exception
    {
        LOG.info("Received message {}", clusterOperationResponseMessage);
        final String correlationId = clusterOperationResponseMessage.getMessageProperties().getCorrelationId();

        final ClusterOperationResponse clusterOperationResponse = new ClusterOperationResponse(
                clusterOperationResponseMessage.getStatus().value());

        final CompletableFuture<ClusterOperationResponse> completableFuture = asyncRequests.get(correlationId);

        LOG.info("Completing expectation for  {} {}", correlationId, completableFuture);

        if (completableFuture != null)
        {
            final boolean complete = completableFuture.complete(clusterOperationResponse);
            LOG.info("Completed expectation for  {} {} {}", correlationId, completableFuture, complete);
            asyncRequests.remove(correlationId);
        }
    }

    @Override
    public CompletableFuture<ClusterOperationResponse> register(final String correlationId)
    {
        LOG.info("Setting expectation for  {}", correlationId);
        CompletableFuture<ClusterOperationResponse> completableFuture = new CompletableFuture<>();
        completableFuture.whenComplete((systemRest, throwable) -> asyncRequests.remove(correlationId));
        asyncRequests.put(correlationId, completableFuture);
        return completableFuture;
    }
}
