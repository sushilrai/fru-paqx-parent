package com.dell.cpsd.paqx.fru.domain;

/**
 * <p>
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * @since SINCE-TBD
 */
public interface Identifiable
{
    void setIdentifier(String identifier);

    String getIdentifier();

    void setIpAddress(String ipAddress);

    String getIpAddress();

    void setSerialNumber(String serialNumber);

    String getSerialNumber();
}
