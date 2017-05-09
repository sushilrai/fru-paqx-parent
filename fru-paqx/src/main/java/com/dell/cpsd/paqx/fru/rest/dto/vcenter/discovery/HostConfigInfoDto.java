/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
public class HostConfigInfoDto
{
    private HostNetworkInfoDto hostNetworkInfo;

    public HostNetworkInfoDto getHostNetworkInfo()
    {
        return hostNetworkInfo;
    }

    public void setHostNetworkInfo(final HostNetworkInfoDto hostNetworkInfo)
    {
        this.hostNetworkInfo = hostNetworkInfo;
    }
}
