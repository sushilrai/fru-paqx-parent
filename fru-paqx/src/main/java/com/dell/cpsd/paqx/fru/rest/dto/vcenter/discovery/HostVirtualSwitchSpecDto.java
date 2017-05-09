/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
public class HostVirtualSwitchSpecDto
{
    private HostNetworkPolicyDto networkPolicy;

    public HostNetworkPolicyDto getNetworkPolicy()
    {
        return networkPolicy;
    }

    public void setNetworkPolicy(final HostNetworkPolicyDto networkPolicy)
    {
        this.networkPolicy = networkPolicy;
    }
}
