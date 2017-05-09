package com.dell.cpsd.paqx.fru.domain;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by kenefj on 03/05/17.
 */
@Entity
public abstract class ScaleIOSDSStatusElementInfo extends ScaleIOSDSElementInfo
{
    public ScaleIOSDSStatusElementInfo(final String id9, final int i, final String version1, final String slave, final String s,
            final String s1)
    {
        super(id9, i, version1, slave,s);
        this.status=s1;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(final String status)
    {
        this.status = status;
    }

    @Column(name="SDS_ELEMENT_STATUS")
    private String status;
}
