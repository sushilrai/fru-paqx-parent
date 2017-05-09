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
public class DvSwitchDto
{
    private String id;
    private String name;
    private String uuid;
    private List<String> portGroupIds = new ArrayList<>();
    private VMwareDVSConfigInfoDto VMwareDVSConfigInfo;

    public String getId()
    {
        return id;
    }

    public void setId(final String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public String getUuid()
    {
        return uuid;
    }

    public void setUuid(final String uuid)
    {
        this.uuid = uuid;
    }

    public List<String> getPortGroupIds()
    {
        return portGroupIds;
    }

    public void setPortGroupIds(final List<String> portGroupIds)
    {
        this.portGroupIds = portGroupIds;
    }

    public VMwareDVSConfigInfoDto getVMwareDVSConfigInfo()
    {
        return VMwareDVSConfigInfo;
    }

    public void setVMwareDVSConfigInfo(final VMwareDVSConfigInfoDto VMwareDVSConfigInfo)
    {
        this.VMwareDVSConfigInfo = VMwareDVSConfigInfo;
    }
}
