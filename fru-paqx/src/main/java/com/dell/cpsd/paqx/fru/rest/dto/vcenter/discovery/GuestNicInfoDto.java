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
public class GuestNicInfoDto
{
    private Boolean connected;
    private List<String> ipAddresses = new ArrayList<>();
    private String macAddress;
    private String networkId;

    public Boolean getConnected()
    {
        return connected;
    }

    public void setConnected(final Boolean connected)
    {
        this.connected = connected;
    }

    public List<String> getIpAddresses()
    {
        return ipAddresses;
    }

    public void setIpAddresses(final List<String> ipAddresses)
    {
        this.ipAddresses = ipAddresses;
    }

    public String getMacAddress()
    {
        return macAddress;
    }

    public void setMacAddress(final String macAddress)
    {
        this.macAddress = macAddress;
    }

    public String getNetworkId()
    {
        return networkId;
    }

    public void setNetworkId(final String networkId)
    {
        this.networkId = networkId;
    }
}
