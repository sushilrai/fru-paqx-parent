/**
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.domain;

/**
 * <p>
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * @since SINCE-TBD
 */
public interface Persistable
{
    boolean isPersisted();

    Object getPersistedId();
}
