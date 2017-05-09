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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"hostname", "physical_nics", "portgroups", "virtual_nics", "vswitches"})
public class HostNetwork
{

    /**
     * (Required)
     */
    @JsonProperty("hostname")
    @NotNull
    private String hostname;
    /**
     * (Required)
     */
    @JsonProperty("physical_nics")
    @Valid
    @NotNull
    private List<PhysicalNic> physicalNics = new ArrayList<PhysicalNic>();
    /**
     * (Required)
     */
    @JsonProperty("portgroups")
    @Valid
    @NotNull
    private List<Portgroup>   portgroups   = new ArrayList<Portgroup>();
    /**
     * (Required)
     */
    @JsonProperty("virtual_nics")
    @Valid
    @NotNull
    private List<VirtualNic>  virtualNics  = new ArrayList<VirtualNic>();
    /**
     * (Required)
     */
    @JsonProperty("vswitches")
    @Valid
    @NotNull
    private List<Vswitch>     vswitches    = new ArrayList<Vswitch>();

    /**
     * No args constructor for use in serialization
     */
    public HostNetwork()
    {
    }

    /**
     * @param portgroups
     * @param hostname
     * @param physicalNics
     * @param virtualNics
     * @param vswitches
     */
    public HostNetwork(String hostname, List<PhysicalNic> physicalNics, List<Portgroup> portgroups, List<VirtualNic> virtualNics,
            List<Vswitch> vswitches)
    {
        super();
        this.hostname = hostname;
        this.physicalNics = physicalNics;
        this.portgroups = portgroups;
        this.virtualNics = virtualNics;
        this.vswitches = vswitches;
    }

    /**
     * (Required)
     *
     * @return The hostname
     */
    @JsonProperty("hostname")
    public String getHostname()
    {
        return hostname;
    }

    /**
     * (Required)
     *
     * @param hostname The hostname
     */
    @JsonProperty("hostname")
    public void setHostname(String hostname)
    {
        this.hostname = hostname;
    }

    public HostNetwork withHostname(String hostname)
    {
        this.hostname = hostname;
        return this;
    }

    /**
     * (Required)
     *
     * @return The physicalNics
     */
    @JsonProperty("physical_nics")
    public List<PhysicalNic> getPhysicalNics()
    {
        return physicalNics;
    }

    /**
     * (Required)
     *
     * @param physicalNics The physical_nics
     */
    @JsonProperty("physical_nics")
    public void setPhysicalNics(List<PhysicalNic> physicalNics)
    {
        this.physicalNics = physicalNics;
    }

    public HostNetwork withPhysicalNics(List<PhysicalNic> physicalNics)
    {
        this.physicalNics = physicalNics;
        return this;
    }

    /**
     * (Required)
     *
     * @return The portgroups
     */
    @JsonProperty("portgroups")
    public List<Portgroup> getPortgroups()
    {
        return portgroups;
    }

    /**
     * (Required)
     *
     * @param portgroups The portgroups
     */
    @JsonProperty("portgroups")
    public void setPortgroups(List<Portgroup> portgroups)
    {
        this.portgroups = portgroups;
    }

    public HostNetwork withPortgroups(List<Portgroup> portgroups)
    {
        this.portgroups = portgroups;
        return this;
    }

    /**
     * (Required)
     *
     * @return The virtualNics
     */
    @JsonProperty("virtual_nics")
    public List<VirtualNic> getVirtualNics()
    {
        return virtualNics;
    }

    /**
     * (Required)
     *
     * @param virtualNics The virtual_nics
     */
    @JsonProperty("virtual_nics")
    public void setVirtualNics(List<VirtualNic> virtualNics)
    {
        this.virtualNics = virtualNics;
    }

    public HostNetwork withVirtualNics(List<VirtualNic> virtualNics)
    {
        this.virtualNics = virtualNics;
        return this;
    }

    /**
     * (Required)
     *
     * @return The vswitches
     */
    @JsonProperty("vswitches")
    public List<Vswitch> getVswitches()
    {
        return vswitches;
    }

    /**
     * (Required)
     *
     * @param vswitches The vswitches
     */
    @JsonProperty("vswitches")
    public void setVswitches(List<Vswitch> vswitches)
    {
        this.vswitches = vswitches;
    }

    public HostNetwork withVswitches(List<Vswitch> vswitches)
    {
        this.vswitches = vswitches;
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
        return new HashCodeBuilder().append(hostname).append(physicalNics).append(portgroups).append(virtualNics).append(vswitches)
                .toHashCode();
    }

    @Override
    public boolean equals(Object other)
    {
        if (other == this)
        {
            return true;
        }
        if ((other instanceof HostNetwork) == false)
        {
            return false;
        }
        HostNetwork rhs = ((HostNetwork) other);
        return new EqualsBuilder().append(hostname, rhs.hostname).append(physicalNics, rhs.physicalNics).append(portgroups, rhs.portgroups)
                .append(virtualNics, rhs.virtualNics).append(vswitches, rhs.vswitches).isEquals();
    }

}
