/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
public class HostNetworkInfoDto
{
    private HostDnsConfigDto     hostDnsConfig;
    private HostIpRouteConfigDto hostIpRouteConfig;
    private List<PhysicalNicDto>       pnics    = new ArrayList<>();
    private List<HostVirtualNicDto>    vnics    = new ArrayList<>();
    private List<HostVirtualSwitchDto> vswitchs = new ArrayList<>();

    public HostDnsConfigDto getHostDnsConfig()
    {
        return hostDnsConfig;
    }

    public void setHostDnsConfig(final HostDnsConfigDto hostDnsConfig)
    {
        this.hostDnsConfig = hostDnsConfig;
    }

    public HostIpRouteConfigDto getHostIpRouteConfig()
    {
        return hostIpRouteConfig;
    }

    public void setHostIpRouteConfig(final HostIpRouteConfigDto hostIpRouteConfig)
    {
        this.hostIpRouteConfig = hostIpRouteConfig;
    }

    public List<PhysicalNicDto> getPnics()
    {
        return pnics;
    }

    public void setPnics(final List<PhysicalNicDto> pnics)
    {
        this.pnics = pnics;
    }

    public List<HostVirtualNicDto> getVnics()
    {
        return vnics;
    }

    public void setVnics(final List<HostVirtualNicDto> vnics)
    {
        this.vnics = vnics;
    }

    public List<HostVirtualSwitchDto> getVswitchs()
    {
        return vswitchs;
    }

    public void setVswitchs(final List<HostVirtualSwitchDto> vswitchs)
    {
        this.vswitchs = vswitchs;
    }
}
