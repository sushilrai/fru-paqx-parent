/**
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 */
package com.dell.cpsd.paqx.fru.domain;

import java.util.Date;

/**
 * <p>
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * @since SINCE-TBD
 */
public interface Auditable
{
    void setCollectedTime(Date collectedTime);

    Date getCollectedTime();

    void setCollectionSentTime(Date collectionSentTime);

    Date getCollectionSentTime();

    void setReceivedTime(Date receivedTime);

    Date getReceivedTime();

    void setMessageId(String messageId);

    String getMessageId();
}
