/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.amqp.model;

import com.dell.cpsd.common.rabbitmq.message.ErrorContainer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Error for error AMQP message.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"code", "message"})
@Deprecated
public class Error implements ErrorContainer {
    /**
     * Error code
     * <p>
     * One of predefined error codes.
     */
    @JsonProperty("code")
    @JsonPropertyDescription("")
    private String code;
    /**
     * Error message
     * <p>
     * Error details.
     */
    @JsonProperty("message")
    @JsonPropertyDescription("")
    private String message;

    /**
     * No args constructor for use in serialization
     */
    public Error() {
    }

    /**
     * @param code
     * @param message
     */
    public Error(String code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    /**
     * Error code
     * <p>
     * One of predefined error codes.
     *
     * @return The code
     */
    @JsonProperty("code")
    public String getCode() {
        return code;
    }

    /**
     * Error code
     * <p>
     * One of predefined error codes.
     *
     * @param code The code
     */
    @JsonProperty("code")
    public void setCode(String code) {
        this.code = code;
    }

    public Error withCode(String code) {
        this.code = code;
        return this;
    }

    /**
     * Error message
     * <p>
     * Error details.
     *
     * @return The message
     */
    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    /**
     * Error message
     * <p>
     * Error details.
     *
     * @param message The message
     */
    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    public Error withMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(code).append(message).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Error) == false) {
            return false;
        }
        Error rhs = ((Error) other);
        return new EqualsBuilder().append(code, rhs.code).append(message, rhs.message).isEquals();
    }
}
