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
public class GuestInfoDto
{
    private String guestFullName;
    private String guestId;
    private String hostName;
    private List<GuestNicInfoDto> guestNicInfo = new ArrayList<>();

    public String getGuestFullName()
    {
        return guestFullName;
    }

    public void setGuestFullName(final String guestFullName)
    {
        this.guestFullName = guestFullName;
    }

    public String getGuestId()
    {
        return guestId;
    }

    public void setGuestId(final String guestId)
    {
        this.guestId = guestId;
    }

    public String getHostName()
    {
        return hostName;
    }

    public void setHostName(final String hostName)
    {
        this.hostName = hostName;
    }

    public List<GuestNicInfoDto> getGuestNicInfo()
    {
        return guestNicInfo;
    }

    public void setGuestNicInfo(List<GuestNicInfoDto> guestNicInfo)
    {
        this.guestNicInfo = guestNicInfo;
    }
}
