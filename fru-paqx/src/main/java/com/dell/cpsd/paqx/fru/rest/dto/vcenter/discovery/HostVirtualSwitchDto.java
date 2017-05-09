/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
public class HostVirtualSwitchDto
{
    private String                   key;
    private String                   name;
    private HostVirtualSwitchSpecDto hostVirtualSwitchSpec;

    public String getKey()
    {
        return key;
    }

    public void setKey(final String key)
    {
        this.key = key;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public HostVirtualSwitchSpecDto getHostVirtualSwitchSpec()
    {
        return hostVirtualSwitchSpec;
    }

    public void setHostVirtualSwitchSpec(final HostVirtualSwitchSpecDto hostVirtualSwitchSpec)
    {
        this.hostVirtualSwitchSpec = hostVirtualSwitchSpec;
    }
}
