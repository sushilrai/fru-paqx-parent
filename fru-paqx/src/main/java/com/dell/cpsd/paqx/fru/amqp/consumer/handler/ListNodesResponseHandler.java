/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.amqp.consumer.handler;

import com.dell.cpsd.common.rabbitmq.consumer.error.ErrorTransformer;
import com.dell.cpsd.common.rabbitmq.consumer.handler.DefaultMessageHandler;
import com.dell.cpsd.common.rabbitmq.message.HasMessageProperties;
import com.dell.cpsd.common.rabbitmq.validators.DefaultMessageValidator;
import com.dell.cpsd.paqx.fru.amqp.model.ListNodesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static com.dell.cpsd.paqx.fru.amqp.config.RabbitConfig.EXCHANGE_FRU_RESPONSE;

/**
 * Handles incoming ListNodesResponse messages.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 */
public class ListNodesResponseHandler extends DefaultMessageHandler<ListNodesResponse> {
    private static final Logger LOG = LoggerFactory.getLogger(RequestResponseMatcher.class);

    private final RequestResponseMatcher requestResponseMatcher;

    @Autowired
    public ListNodesResponseHandler(ErrorTransformer<HasMessageProperties<?>> errorTransformer,
                                    final RequestResponseMatcher requestResponseMatcher) {
        super(ListNodesResponse.class, new DefaultMessageValidator<>(), EXCHANGE_FRU_RESPONSE, errorTransformer);
        this.requestResponseMatcher = requestResponseMatcher;
    }

    @Override
    protected void executeOperation(ListNodesResponse message) throws Exception {
        String correlationId = message.getMessageProperties().getCorrelationId();
        requestResponseMatcher.received(correlationId, message);
    }
}
