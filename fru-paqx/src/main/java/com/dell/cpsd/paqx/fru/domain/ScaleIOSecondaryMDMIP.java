
package com.dell.cpsd.paqx.fru.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by kenefj on 03/05/17.
 */
@Entity
@DiscriminatorValue("SECONDARY")
public class ScaleIOSecondaryMDMIP extends ScaleIOIP
{

    public ScaleIOSecondaryMDMIP(final String id, final String ip)
    {
        super(id, ip);
    }
}
