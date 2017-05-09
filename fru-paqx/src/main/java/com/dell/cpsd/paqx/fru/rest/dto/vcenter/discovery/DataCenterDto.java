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
public class DataCenterDto
{
    private String name;
    private String id;
    private List<VirtualMachineDto> vms        = new ArrayList<>();
    private List<NetworkDto>        networks   = new ArrayList<>();
    private List<DvSwitchDto>       dvSwitches = new ArrayList<>();
    private List<DatastoreDto>      datastores = new ArrayList<>();
    private List<ClusterDto>        clusters   = new ArrayList<>();

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public String getId()
    {
        return id;
    }

    public void setId(final String id)
    {
        this.id = id;
    }

    public List<VirtualMachineDto> getVms()
    {
        return vms;
    }

    public void setVms(final List<VirtualMachineDto> vms)
    {
        this.vms = vms;
    }

    public List<NetworkDto> getNetworks()
    {
        return networks;
    }

    public void setNetworks(final List<NetworkDto> networks)
    {
        this.networks = networks;
    }

    public List<DvSwitchDto> getDvSwitches()
    {
        return dvSwitches;
    }

    public void setDvSwitches(final List<DvSwitchDto> dvSwitches)
    {
        this.dvSwitches = dvSwitches;
    }

    public List<DatastoreDto> getDatastores()
    {
        return datastores;
    }

    public void setDatastores(final List<DatastoreDto> datastores)
    {
        this.datastores = datastores;
    }

    public List<ClusterDto> getClusters()
    {
        return clusters;
    }

    public void setClusters(final List<ClusterDto> clusters)
    {
        this.clusters = clusters;
    }
}
