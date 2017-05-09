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
@DiscriminatorValue("SUBCOMPONENT_AUDIT")
public class SubComponentAudit extends Audit
{

    @OneToOne(cascade = {CascadeType.ALL}, orphanRemoval = true)
    private Subcomponent subComponentParent;

    public Subcomponent getParent()
    {
        return subComponentParent;
    }

    public void setParent(final Subcomponent parent)
    {
        this.subComponentParent = parent;
    }

    public SubComponentAudit()
    {
    }

    public SubComponentAudit(Date collectedTime, Date collectionSentTime, Date receivedTime, String messageId)
    {
        super(collectedTime, collectionSentTime, receivedTime, messageId);
    }
}