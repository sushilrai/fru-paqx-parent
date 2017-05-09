/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
public class HostNetworkPolicyDto
{
    private HostNetworkSecurityPolicyDto hostNetworkSecurityPolicy;

    public HostNetworkSecurityPolicyDto getHostNetworkSecurityPolicy()
    {
        return hostNetworkSecurityPolicy;
    }

    public void setHostNetworkSecurityPolicy(final HostNetworkSecurityPolicyDto hostNetworkSecurityPolicy)
    {
        this.hostNetworkSecurityPolicy = hostNetworkSecurityPolicy;
    }
}
