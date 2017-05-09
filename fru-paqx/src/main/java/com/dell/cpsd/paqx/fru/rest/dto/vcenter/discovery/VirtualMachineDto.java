/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
public class VirtualMachineDto
{
    private String       id;
    private String       name;
    private GuestInfoDto guestInfo;

    public GuestInfoDto getGuestInfo()
    {
        return guestInfo;
    }

    public void setGuestInfo(final GuestInfoDto guestInfo)
    {
        this.guestInfo = guestInfo;
    }

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
}
