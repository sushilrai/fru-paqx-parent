/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
public class DistributedVirtualSwitchHostMemberConfigInfoDto {
    private String hostId;
    private DistributedVirtualSwitchHostMemberPnicBackingDto distributedVirtualSwitchHostMemberPnicBacking;

    public String getHostId() {
        return hostId;
    }

    public void setHostId(final String hostId) {
        this.hostId = hostId;
    }

    public DistributedVirtualSwitchHostMemberPnicBackingDto getDistributedVirtualSwitchHostMemberPnicBacking() {
        return distributedVirtualSwitchHostMemberPnicBacking;
    }

    public void setDistributedVirtualSwitchHostMemberPnicBacking(
            final DistributedVirtualSwitchHostMemberPnicBackingDto distributedVirtualSwitchHostMemberPnicBacking) {
        this.distributedVirtualSwitchHostMemberPnicBacking = distributedVirtualSwitchHostMemberPnicBacking;
    }
}
