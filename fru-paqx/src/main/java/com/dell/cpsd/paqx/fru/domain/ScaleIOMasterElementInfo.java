package com.dell.cpsd.paqx.fru.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by kenefj on 03/05/17.
 */
@Entity
@DiscriminatorValue("ELEMENT_MASTER")
public class ScaleIOMasterElementInfo extends ScaleIOSDSElementInfo
{
    public ScaleIOMasterElementInfo(final String id9, final int i, final String version1, final String slave, final String s)
    {
        super(id9, i, version1, slave, s);
    }
}
