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
public class DatastoreDto
{
    private String id;
    private String name;
    private List<String> vmIds   = new ArrayList<>();
    private List<String> hostIds = new ArrayList<>();
    private DatastoreSummaryDto datastoreSummary;

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

    public List<String> getHostIds()
    {
        return hostIds;
    }

    public void setHostIds(final List<String> hostIds)
    {
        this.hostIds = hostIds;
    }

    public DatastoreSummaryDto getDatastoreSummary()
    {
        return datastoreSummary;
    }

    public void setDatastoreSummary(final DatastoreSummaryDto datastoreSummary)
    {
        this.datastoreSummary = datastoreSummary;
    }
}
