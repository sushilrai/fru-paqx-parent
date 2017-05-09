package com.dell.cpsd.paqx.fru.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by kenefj on 03/05/17.
 */
@Entity
@Table(name="SCALEIO_FAULTSET")
public class ScaleIOFaultSet
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "FAULTSET_UUID", unique = true, nullable = false)
    private Long uuid;

    @Column(name = "FAULTSET_ID")
    private String id;

    @Column(name = "FAULTSET_NAME")
    private String name;

    public Long getUuid()
    {
        return uuid;
    }

    public void setUuid(final Long uuid)
    {
        this.uuid = uuid;
    }

    public String getId()
    {
        return id;
    }

    public void setId(final String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }
}
