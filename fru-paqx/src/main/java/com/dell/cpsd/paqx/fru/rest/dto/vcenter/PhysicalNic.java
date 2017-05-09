/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.rest.dto.vcenter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotNull;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"device", "key", "mac"})
public class PhysicalNic
{

    /**
     * (Required)
     */
    @JsonProperty("device")
    @NotNull
    private String device;
    /**
     * (Required)
     */
    @JsonProperty("key")
    @NotNull
    private String key;
    /**
     * (Required)
     */
    @JsonProperty("mac")
    @NotNull
    private String mac;

    /**
     * No args constructor for use in serialization
     */
    public PhysicalNic()
    {
    }

    /**
     * @param device
     * @param key
     * @param mac
     */
    public PhysicalNic(String device, String key, String mac)
    {
        super();
        this.device = device;
        this.key = key;
        this.mac = mac;
    }

    /**
     * (Required)
     *
     * @return The device
     */
    @JsonProperty("device")
    public String getDevice()
    {
        return device;
    }

    /**
     * (Required)
     *
     * @param device The device
     */
    @JsonProperty("device")
    public void setDevice(String device)
    {
        this.device = device;
    }

    public PhysicalNic withDevice(String device)
    {
        this.device = device;
        return this;
    }

    /**
     * (Required)
     *
     * @return The key
     */
    @JsonProperty("key")
    public String getKey()
    {
        return key;
    }

    /**
     * (Required)
     *
     * @param key The key
     */
    @JsonProperty("key")
    public void setKey(String key)
    {
        this.key = key;
    }

    public PhysicalNic withKey(String key)
    {
        this.key = key;
        return this;
    }

    /**
     * (Required)
     *
     * @return The mac
     */
    @JsonProperty("mac")
    public String getMac()
    {
        return mac;
    }

    /**
     * (Required)
     *
     * @param mac The mac
     */
    @JsonProperty("mac")
    public void setMac(String mac)
    {
        this.mac = mac;
    }

    public PhysicalNic withMac(String mac)
    {
        this.mac = mac;
        return this;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(device).append(key).append(mac).toHashCode();
    }

    @Override
    public boolean equals(Object other)
    {
        if (other == this)
        {
            return true;
        }
        if ((other instanceof PhysicalNic) == false)
        {
            return false;
        }
        PhysicalNic rhs = ((PhysicalNic) other);
        return new EqualsBuilder().append(device, rhs.device).append(key, rhs.key).append(mac, rhs.mac).isEquals();
    }

}
