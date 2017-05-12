/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
@Entity
@Table(name = "JOB")
public class FruJob
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "JOB_UUID", unique = true, nullable = false)
    private Long uuid;

    @Column(name = "JOB_ID")
    private String id;

    @OneToOne(cascade = CascadeType.ALL)
    private ScaleIOData scaleIOData;

    @OneToOne(cascade = CascadeType.ALL)
    private Datacenter vcenter;

    public FruJob(final String id, final ScaleIOData scaleIO, final Datacenter vcenter)
    {
        this.id = id;
        this.scaleIOData = scaleIO;
        this.vcenter = vcenter;
    }

    public Long getUuid()
    {
        return uuid;
    }

    public String getId()
    {
        return id;
    }

    public void setId(final String id)
    {
        this.id = id;
    }

    public ScaleIOData getScaleIO()
    {
        return scaleIOData;
    }

    public void setScaleIO(final ScaleIOData scaleIO)
    {
        this.scaleIOData = scaleIO;
    }

    public Datacenter getVcenter()
    {
        return vcenter;
    }

    public void setVcenter(final Datacenter vcenter)
    {
        this.vcenter = vcenter;
    }
}
