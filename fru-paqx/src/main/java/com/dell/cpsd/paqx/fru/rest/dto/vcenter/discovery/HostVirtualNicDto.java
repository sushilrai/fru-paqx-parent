/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
public class HostVirtualNicDto
{
    private String                device;
    private String                port;
    private String                portGroup;
    private HostVirtualNicSpecDto hostVirtualNicSpec;

    public String getDevice()
    {
        return device;
    }

    public void setDevice(final String device)
    {
        this.device = device;
    }

    public String getPort()
    {
        return port;
    }

    public void setPort(final String port)
    {
        this.port = port;
    }

    public String getPortGroup()
    {
        return portGroup;
    }

    public void setPortGroup(final String portGroup)
    {
        this.portGroup = portGroup;
    }

    public HostVirtualNicSpecDto getHostVirtualNicSpec()
    {
        return hostVirtualNicSpec;
    }

    public void setHostVirtualNicSpec(final HostVirtualNicSpecDto hostVirtualNicSpec)
    {
        this.hostVirtualNicSpec = hostVirtualNicSpec;
    }
}
