/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
public class HostNetworkSecurityPolicyDto
{
    private Boolean allowPromiscuous;

    public Boolean getAllowPromiscuous()
    {
        return allowPromiscuous;
    }

    public void setAllowPromiscuous(final Boolean allowPromiscuous)
    {
        this.allowPromiscuous = allowPromiscuous;
    }
}
