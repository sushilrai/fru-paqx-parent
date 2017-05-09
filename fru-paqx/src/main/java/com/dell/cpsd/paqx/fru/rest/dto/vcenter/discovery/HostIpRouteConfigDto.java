/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
public class HostIpRouteConfigDto
{
    private String defaultGateway;
    private String defaultGatewayDevice;
    private String ipV6DefaultGateway;
    private String ipV6DefaultGatewayDevice;

    public String getDefaultGateway()
    {
        return defaultGateway;
    }

    public void setDefaultGateway(final String defaultGateway)
    {
        this.defaultGateway = defaultGateway;
    }

    public String getDefaultGatewayDevice()
    {
        return defaultGatewayDevice;
    }

    public void setDefaultGatewayDevice(final String defaultGatewayDevice)
    {
        this.defaultGatewayDevice = defaultGatewayDevice;
    }

    public String getIpV6DefaultGateway()
    {
        return ipV6DefaultGateway;
    }

    public void setIpV6DefaultGateway(final String ipV6DefaultGateway)
    {
        this.ipV6DefaultGateway = ipV6DefaultGateway;
    }

    public String getIpV6DefaultGatewayDevice()
    {
        return ipV6DefaultGatewayDevice;
    }

    public void setIpV6DefaultGatewayDevice(final String ipV6DefaultGatewayDevice)
    {
        this.ipV6DefaultGatewayDevice = ipV6DefaultGatewayDevice;
    }
}
