package com.dell.cpsd.paqx.fru.domain;

import org.apache.commons.lang.StringUtils;


/**
 * <p>
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * @since SINCE-TBD
 */
public enum VersionType
{
    HARDWARE, SOFTWARE, FIRMWARE;

    public static VersionType fromValue(String value)
    {
        for (VersionType versionType : VersionType.values())
        {
            //// @TODO THIS IS TEMPORARY HACK FOR SPRINT 3 DEMO
            if (StringUtils.startsWithIgnoreCase(value, versionType.toString()))
            {
                return versionType;
            }
        }
        return null;
    }
}

