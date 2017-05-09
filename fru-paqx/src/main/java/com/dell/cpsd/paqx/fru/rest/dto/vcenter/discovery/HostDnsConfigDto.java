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
public class HostDnsConfigDto
{
    private Boolean dhcp;
    private String  domainName;
    private String  hostName;
    private List<String> ipAddresses   = new ArrayList<>();
    private List<String> searchDomains = new ArrayList<>();

    public Boolean getDhcp()
    {
        return dhcp;
    }

    public void setDhcp(final Boolean dhcp)
    {
        this.dhcp = dhcp;
    }

    public String getDomainName()
    {
        return domainName;
    }

    public void setDomainName(final String domainName)
    {
        this.domainName = domainName;
    }

    public String getHostName()
    {
        return hostName;
    }

    public void setHostName(final String hostName)
    {
        this.hostName = hostName;
    }

    public List<String> getIpAddresses()
    {
        return ipAddresses;
    }

    public void setIpAddresses(final List<String> ipAddresses)
    {
        this.ipAddresses = ipAddresses;
    }

    public List<String> getSearchDomains()
    {
        return searchDomains;
    }

    public void setSearchDomains(final List<String> searchDomains)
    {
        this.searchDomains = searchDomains;
    }
}
