/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
public class PhysicalNicDto
{
    private String device;
    private String driver;
    private String mac;
    private String pci;

    public String getDevice()
    {
        return device;
    }

    public void setDevice(final String device)
    {
        this.device = device;
    }

    public String getDriver()
    {
        return driver;
    }

    public void setDriver(final String driver)
    {
        this.driver = driver;
    }

    public String getMac()
    {
        return mac;
    }

    public void setMac(final String mac)
    {
        this.mac = mac;
    }

    public String getPci()
    {
        return pci;
    }

    public void setPci(final String pci)
    {
        this.pci = pci;
    }
}
