package com.dell.cpsd.paqx.fru.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
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
@Table(name = "AUDIT")
@DiscriminatorColumn(name = "CLASSIFICATION")
public abstract class Audit extends AbstractElement implements Serializable, Auditable
{

    @Id
    @Column(name = "AUDIT_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long auditId;

    @Column(name = "COLLECTED_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date collectedTime;

    @Column(name = "COLLECTION_SENT_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date collectionSentTime;

    @Column(name = "RECEIVED_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date receivedTime;

    @Column(name = "MESSAGE_ID")
    private String messageId;

    public Audit()
    {
    }

    public Audit(Date collectedTime, Date collectionSentTime, Date receivedTime, String messageId)
    {
        this.collectedTime = collectedTime;
        this.collectionSentTime = collectionSentTime;
        this.receivedTime = receivedTime;
        this.messageId = messageId;
    }

    public void setCollectedTime(final Date collectedTime)
    {
        this.collectedTime = collectedTime;
    }

    public void setCollectionSentTime(final Date collectionSentTime)
    {
        this.collectionSentTime = collectionSentTime;
    }

    public void setReceivedTime(final Date receivedTime)
    {
        this.receivedTime = receivedTime;
    }

    public void setMessageId(final String messageId)
    {
        this.messageId = messageId;
    }

    public Date getCollectedTime()
    {
        return collectedTime;
    }

    public Date getCollectionSentTime()
    {
        return collectionSentTime;
    }

    public Date getReceivedTime()
    {
        return receivedTime;
    }

    public String getMessageId()
    {
        return messageId;
    }

    public Long getAuditId()
    {
        return auditId;
    }

    @Override
    public Object getPersistedId()
    {
        return getAuditId();
    }
}