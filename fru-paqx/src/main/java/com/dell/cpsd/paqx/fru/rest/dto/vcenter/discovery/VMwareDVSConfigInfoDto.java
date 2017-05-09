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
public class VMwareDVSConfigInfoDto
{
    private DVPortSettingDto DVPortSetting;
    private List<DistributedVirtualSwitchHostMemberDto> hostMembers = new ArrayList<>();

    public DVPortSettingDto getDVPortSetting()
    {
        return DVPortSetting;
    }

    public void setDVPortSetting(final DVPortSettingDto DVPortSetting)
    {
        this.DVPortSetting = DVPortSetting;
    }

    public List<DistributedVirtualSwitchHostMemberDto> getHostMembers()
    {
        return hostMembers;
    }

    public void setHostMembers(final List<DistributedVirtualSwitchHostMemberDto> hostMembers)
    {
        this.hostMembers = hostMembers;
    }
}
