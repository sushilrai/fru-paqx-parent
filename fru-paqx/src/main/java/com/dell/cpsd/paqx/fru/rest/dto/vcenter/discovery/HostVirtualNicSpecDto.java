/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
public class HostVirtualNicSpecDto
{
    private String                                    mac;
    private HostIpConfigDto                           hostIpConfig;
    private DistributedVirtualSwicthPortConnectionDto distributedVirtualSwitchPortConnection;

    public String getMac()
    {
        return mac;
    }

    public void setMac(final String mac)
    {
        this.mac = mac;
    }

    public HostIpConfigDto getHostIpConfig()
    {
        return hostIpConfig;
    }

    public void setHostIpConfig(final HostIpConfigDto hostIpConfig)
    {
        this.hostIpConfig = hostIpConfig;
    }

    public DistributedVirtualSwicthPortConnectionDto getDistributedVirtualSwitchPortConnection()
    {
        return distributedVirtualSwitchPortConnection;
    }

    public void setDistributedVirtualSwitchPortConnection(
            final DistributedVirtualSwicthPortConnectionDto distributedVirtualSwitchPortConnection)
    {
        this.distributedVirtualSwitchPortConnection = distributedVirtualSwitchPortConnection;
    }
}
