/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.amqp.model;

import com.dell.cpsd.common.rabbitmq.annotation.Message;
import com.dell.cpsd.common.rabbitmq.message.HasMessageProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Capability request message
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 */
@Message(value = "com.dell.cpsd.list.nodes.response", version = "1.0")
public class ListNodesResponse implements HasMessageProperties<MessageProperties> {
    @JsonProperty("messageProperties")
    @JsonPropertyDescription("")
    //    @Valid
    @NotNull
    private MessageProperties messageProperties;

    @JsonProperty("nodes")
    private List<Node> nodes;

    @Override
    public MessageProperties getMessageProperties() {
        return messageProperties;
    }

    @Override
    public void setMessageProperties(MessageProperties messageProperties) {
        this.messageProperties = messageProperties;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(final List<Node> nodes) {
        this.nodes = nodes;
    }

    public static class Node {
        private String id;
        private String name;
        private String type;

        public String getId() {
            return id;
        }

        public void setId(final String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(final String type) {
            this.type = type;
        }
    }
}