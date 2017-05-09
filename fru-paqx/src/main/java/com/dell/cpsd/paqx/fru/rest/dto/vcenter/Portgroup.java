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
@JsonPropertyOrder({"key", "name", "vlanId", "vswitchName"})
public class Portgroup
{

    /**
     * (Required)
     */
    @JsonProperty("key")
    @NotNull
    private String key;
    /**
     * (Required)
     */
    @JsonProperty("name")
    @NotNull
    private String name;
    /**
     * (Required)
     */
    @JsonProperty("vlanId")
    @NotNull
    private Long   vlanId;
    /**
     * (Required)
     */
    @JsonProperty("vswitchName")
    @NotNull
    private String vswitchName;

    /**
     * No args constructor for use in serialization
     */
    public Portgroup()
    {
    }

    /**
     * @param vswitchName
     * @param vlanId
     * @param name
     * @param key
     */
    public Portgroup(String key, String name, Long vlanId, String vswitchName)
    {
        super();
        this.key = key;
        this.name = name;
        this.vlanId = vlanId;
        this.vswitchName = vswitchName;
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

    public Portgroup withKey(String key)
    {
        this.key = key;
        return this;
    }

    /**
     * (Required)
     *
     * @return The name
     */
    @JsonProperty("name")
    public String getName()
    {
        return name;
    }

    /**
     * (Required)
     *
     * @param name The name
     */
    @JsonProperty("name")
    public void setName(String name)
    {
        this.name = name;
    }

    public Portgroup withName(String name)
    {
        this.name = name;
        return this;
    }

    /**
     * (Required)
     *
     * @return The vlanId
     */
    @JsonProperty("vlanId")
    public Long getVlanId()
    {
        return vlanId;
    }

    /**
     * (Required)
     *
     * @param vlanId The vlanId
     */
    @JsonProperty("vlanId")
    public void setVlanId(Long vlanId)
    {
        this.vlanId = vlanId;
    }

    public Portgroup withVlanId(Long vlanId)
    {
        this.vlanId = vlanId;
        return this;
    }

    /**
     * (Required)
     *
     * @return The vswitchName
     */
    @JsonProperty("vswitchName")
    public String getVswitchName()
    {
        return vswitchName;
    }

    /**
     * (Required)
     *
     * @param vswitchName The vswitchName
     */
    @JsonProperty("vswitchName")
    public void setVswitchName(String vswitchName)
    {
        this.vswitchName = vswitchName;
    }

    public Portgroup withVswitchName(String vswitchName)
    {
        this.vswitchName = vswitchName;
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
        return new HashCodeBuilder().append(key).append(name).append(vlanId).append(vswitchName).toHashCode();
    }

    @Override
    public boolean equals(Object other)
    {
        if (other == this)
        {
            return true;
        }
        if ((other instanceof Portgroup) == false)
        {
            return false;
        }
        Portgroup rhs = ((Portgroup) other);
        return new EqualsBuilder().append(key, rhs.key).append(name, rhs.name).append(vlanId, rhs.vlanId)
                .append(vswitchName, rhs.vswitchName).isEquals();
    }

}
