package com.dell.cpsd.paqx.fru.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by kenefj on 03/05/17.
 */
@Entity
@DiscriminatorValue("SLAVE")
public class ScaleIOSlaveScaleIOIP extends ScaleIOIP
{

    public ScaleIOSlaveScaleIOIP(final String id2, final String s)
    {
        super(id2, s);
    }
}
