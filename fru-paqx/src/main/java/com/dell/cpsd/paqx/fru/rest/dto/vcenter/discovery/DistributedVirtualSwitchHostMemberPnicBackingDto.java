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
public class DistributedVirtualSwitchHostMemberPnicBackingDto {
    private List<DistributedVirtualSwitchHostMemberPnicSpecDto> spec = new ArrayList<>();

    public List<DistributedVirtualSwitchHostMemberPnicSpecDto> getSpec() {
        return spec;
    }

    public void setSpec(final List<DistributedVirtualSwitchHostMemberPnicSpecDto> spec) {
        this.spec = spec;
    }
}
