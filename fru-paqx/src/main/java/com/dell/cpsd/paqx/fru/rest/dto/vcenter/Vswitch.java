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
@JsonPropertyOrder({"key", "mtu", "name"})
public class Vswitch
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
    @JsonProperty("mtu")
    @NotNull
    private Long   mtu;
    /**
     * (Required)
     */
    @JsonProperty("name")
    @NotNull
    private String name;

    /**
     * No args constructor for use in serialization
     */
    public Vswitch()
    {
    }

    /**
     * @param name
     * @param key
     * @param mtu
     */
    public Vswitch(String key, Long mtu, String name)
    {
        super();
        this.key = key;
        this.mtu = mtu;
        this.name = name;
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

    public Vswitch withKey(String key)
    {
        this.key = key;
        return this;
    }

    /**
     * (Required)
     *
     * @return The mtu
     */
    @JsonProperty("mtu")
    public Long getMtu()
    {
        return mtu;
    }

    /**
     * (Required)
     *
     * @param mtu The mtu
     */
    @JsonProperty("mtu")
    public void setMtu(Long mtu)
    {
        this.mtu = mtu;
    }

    public Vswitch withMtu(Long mtu)
    {
        this.mtu = mtu;
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

    public Vswitch withName(String name)
    {
        this.name = name;
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
        return new HashCodeBuilder().append(key).append(mtu).append(name).toHashCode();
    }

    @Override
    public boolean equals(Object other)
    {
        if (other == this)
        {
            return true;
        }
        if ((other instanceof Vswitch) == false)
        {
            return false;
        }
        Vswitch rhs = ((Vswitch) other);
        return new EqualsBuilder().append(key, rhs.key).append(mtu, rhs.mtu).append(name, rhs.name).isEquals();
    }

}
