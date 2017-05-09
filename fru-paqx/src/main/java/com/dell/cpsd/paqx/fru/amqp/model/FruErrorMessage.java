/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.amqp.model;

import com.dell.cpsd.common.rabbitmq.annotation.Message;
import com.dell.cpsd.common.rabbitmq.message.HasErrors;
import com.dell.cpsd.common.rabbitmq.message.HasMessageProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Error message.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Message(value = "com.dell.cpsd.fru.paqx.error", version = "1.0")
@JsonPropertyOrder({"messageProperties", "errors"})
@Deprecated
public class FruErrorMessage implements HasErrors<Error>, HasMessageProperties<MessageProperties> {

    /**
     * AMQP properties properties
     * <p>
     * AMQP properties.
     * (Required)
     */
    @JsonProperty("messageProperties")
    @JsonPropertyDescription("")
    @Valid
    @NotNull
    private MessageProperties messageProperties;
    /**
     * (Required)
     */
    @JsonProperty("errors")
    @Valid
    @NotNull
    private List<Error> errors = new ArrayList<Error>();

    /**
     * No args constructor for use in serialization
     */
    public FruErrorMessage() {
    }

    /**
     * @param messageProperties
     * @param errors
     */
    public FruErrorMessage(MessageProperties messageProperties, List<Error> errors) {
        super();
        this.messageProperties = messageProperties;
        this.errors = errors;
    }

    /**
     * AMQP properties properties
     * <p>
     * AMQP properties.
     * (Required)
     *
     * @return The messageProperties
     */
    @JsonProperty("messageProperties")
    public MessageProperties getMessageProperties() {
        return messageProperties;
    }

    /**
     * AMQP properties properties
     * <p>
     * AMQP properties.
     * (Required)
     *
     * @param messageProperties The messageProperties
     */
    @JsonProperty("messageProperties")
    public void setMessageProperties(MessageProperties messageProperties) {
        this.messageProperties = messageProperties;
    }

    public FruErrorMessage withMessageProperties(MessageProperties messageProperties) {
        this.messageProperties = messageProperties;
        return this;
    }

    /**
     * (Required)
     *
     * @return The errors
     */
    @JsonProperty("errors")
    public List<Error> getErrors() {
        return errors;
    }

    /**
     * (Required)
     *
     * @param errors The errors
     */
    @JsonProperty("errors")
    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }

    public FruErrorMessage withErrors(List<Error> errors) {
        this.errors = errors;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(messageProperties).append(errors).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof FruErrorMessage) == false) {
            return false;
        }
        FruErrorMessage rhs = ((FruErrorMessage) other);
        return new EqualsBuilder().append(messageProperties, rhs.messageProperties).append(errors, rhs.errors).isEquals();
    }

}
