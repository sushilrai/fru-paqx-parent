package com.dell.cpsd.paqx.fru.domain;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * <p>
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * @since SINCE-TBD
 */
@Entity
@Table
@DiscriminatorValue("DEVICE_AUDIT")
public class DeviceAudit extends Audit
{


    @OneToOne(cascade = {CascadeType.ALL}, orphanRemoval = true)
    private Device deviceParent;

    public Device getParent()
    {
        return deviceParent;
    }

    public void setParent(final Device parent)
    {
        this.deviceParent = parent;
    }

    public DeviceAudit()
    {
    }

    public DeviceAudit(Date collectedTime, Date collectionSentTime, Date receivedTime, String messageId)
    {
        super(collectedTime, collectionSentTime, receivedTime, messageId);
    }
}