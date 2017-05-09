
package com.dell.cpsd.paqx.fru.domain;

/**
 * <p>
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * @since SINCE-TBD
 */
public enum GroupType
{
    COMPUTE, NETWORK, STORAGE, VIRTUALIZATION, MANAGEMENT;

    public static GroupType fromValue(String value)
    {
        for (GroupType type : GroupType.values())
        {
            if (type.toString().equalsIgnoreCase(value))
            {
                return type;
            }
        }
        return null;
    }
}
