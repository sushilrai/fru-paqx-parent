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
    public ScaleIOMasterElementInfo(final String id, final int port, final String versionInfo, final String name, final String role)
    {
        super(id, port, versionInfo, name, role);
    }
}
