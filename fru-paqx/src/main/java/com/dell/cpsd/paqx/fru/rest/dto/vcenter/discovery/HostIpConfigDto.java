/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
public class HostIpConfigDto
{
    private Boolean dhcp;
    private String  ipAddress;
    private String  subnetMask;

    public Boolean getDhcp()
    {
        return dhcp;
    }

    public void setDhcp(final Boolean dhcp)
    {
        this.dhcp = dhcp;
    }

    public String getIpAddress()
    {
        return ipAddress;
    }

    public void setIpAddress(final String ipAddress)
    {
        this.ipAddress = ipAddress;
    }

    public String getSubnetMask()
    {
        return subnetMask;
    }

    public void setSubnetMask(final String subnetMask)
    {
        this.subnetMask = subnetMask;
    }
}
