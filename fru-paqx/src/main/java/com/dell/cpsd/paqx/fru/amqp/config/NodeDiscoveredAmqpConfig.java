/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 */

package com.dell.cpsd.paqx.fru.amqp.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This configuration class contains just the exchanges and queues needed to satisfy the endpoint registry functionality.
 * NOTE: The bean names must be unique among all of the AMQP Config classes.  If not, exchanges and queues that have duplicate names may not get instantiated properly.
 */
@Configuration
public class NodeDiscoveredAmqpConfig
{
    public static final String EXCHANGE_RACKHD_ADAPTER_NODE_EVENT = "exchange.dell.cpsd.adapter.rackhd.node.discovered.event";

    // Queue names
    public static final String NODE_DISCOVERED_EVENT_QUEUE = "queue.dell.cpsd.frupaqx.node.discovered-event";

    @Bean
    FanoutExchange nodeDiscoveredEventExchange()
    {
        return new FanoutExchange(EXCHANGE_RACKHD_ADAPTER_NODE_EVENT);
    }

    @Bean
    Queue nodeDiscoveredQueue()
    {
        return new Queue(NODE_DISCOVERED_EVENT_QUEUE, false);
    }

    @Bean
    Binding endpointAddedBinding()
    {
        return BindingBuilder.bind(nodeDiscoveredQueue()).to(nodeDiscoveredEventExchange());
    }
}
