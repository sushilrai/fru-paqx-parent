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
@JsonPropertyOrder({"device", "ip", "key", "subnet_mask"})
public class VirtualNic
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
    @JsonProperty("ip")
    @NotNull
    private String ip;
    /**
     * (Required)
     */
    @JsonProperty("key")
    @NotNull
    private String key;
    /**
     * (Required)
     */
    @JsonProperty("subnet_mask")
    @NotNull
    private String subnetMask;

    /**
     * No args constructor for use in serialization
     */
    public VirtualNic()
    {
    }

    /**
     * @param ip
     * @param subnetMask
     * @param device
     * @param key
     */
    public VirtualNic(String device, String ip, String key, String subnetMask)
    {
        super();
        this.device = device;
        this.ip = ip;
        this.key = key;
        this.subnetMask = subnetMask;
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

    public VirtualNic withDevice(String device)
    {
        this.device = device;
        return this;
    }

    /**
     * (Required)
     *
     * @return The ip
     */
    @JsonProperty("ip")
    public String getIp()
    {
        return ip;
    }

    /**
     * (Required)
     *
     * @param ip The ip
     */
    @JsonProperty("ip")
    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public VirtualNic withIp(String ip)
    {
        this.ip = ip;
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

    public VirtualNic withKey(String key)
    {
        this.key = key;
        return this;
    }

    /**
     * (Required)
     *
     * @return The subnetMask
     */
    @JsonProperty("subnet_mask")
    public String getSubnetMask()
    {
        return subnetMask;
    }

    /**
     * (Required)
     *
     * @param subnetMask The subnet_mask
     */
    @JsonProperty("subnet_mask")
    public void setSubnetMask(String subnetMask)
    {
        this.subnetMask = subnetMask;
    }

    public VirtualNic withSubnetMask(String subnetMask)
    {
        this.subnetMask = subnetMask;
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
        return new HashCodeBuilder().append(device).append(ip).append(key).append(subnetMask).toHashCode();
    }

    @Override
    public boolean equals(Object other)
    {
        if (other == this)
        {
            return true;
        }
        if ((other instanceof VirtualNic) == false)
        {
            return false;
        }
        VirtualNic rhs = ((VirtualNic) other);
        return new EqualsBuilder().append(device, rhs.device).append(ip, rhs.ip).append(key, rhs.key).append(subnetMask, rhs.subnetMask)
                .isEquals();
    }

}
