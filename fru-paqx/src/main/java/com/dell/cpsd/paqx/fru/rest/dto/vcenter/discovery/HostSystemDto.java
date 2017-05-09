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
public class HostSystemDto
{
    private String id;
    private String name;
    private List<String> vmIds        = new ArrayList<>();
    private List<String> datastoreIds = new ArrayList<>();
    private List<String> networkIds   = new ArrayList<>();
    private HostConfigInfoDto hostConfigInfo;

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

    public List<String> getVmIds()
    {
        return vmIds;
    }

    public void setVmIds(final List<String> vmIds)
    {
        this.vmIds = vmIds;
    }

    public List<String> getDatastoreIds()
    {
        return datastoreIds;
    }

    public void setDatastoreIds(final List<String> datastoreIds)
    {
        this.datastoreIds = datastoreIds;
    }

    public List<String> getNetworkIds()
    {
        return networkIds;
    }

    public void setNetworkIds(final List<String> networkIds)
    {
        this.networkIds = networkIds;
    }

    public HostConfigInfoDto getHostConfigInfo()
    {
        return hostConfigInfo;
    }

    public void setHostConfigInfo(final HostConfigInfoDto hostConfigInfo)
    {
        this.hostConfigInfo = hostConfigInfo;
    }
}
