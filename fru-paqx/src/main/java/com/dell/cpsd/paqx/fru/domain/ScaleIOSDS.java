package com.dell.cpsd.paqx.fru.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by kenefj on 03/05/17.
 */
@Entity
@Table(name="SCALEIO_SDS")
public class ScaleIOSDS
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "SDS_UUID", unique = true, nullable = false)
    private Long uuid;

    @Column(name = "SDS_ID", unique = true, nullable = false)
    private String id;

    @Column(name = "SDS_NAME")
    private String name;

    @Column(name = "SDS_STATE")
    private String sdsState;

    @Column(name = "SDS_PORT")
    private String port;

//    public String getUuid()
//    {
//        return uuid;
//    }
//
//    public void setUuid(final String uuid)
//    {
//        this.uuid = uuid;
//    }

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

    public String getSdsState()
    {
        return sdsState;
    }

    public void setSdsState(final String sdsState)
    {
        this.sdsState = sdsState;
    }

    public String getPort()
    {
        return port;
    }

    public void setPort(final String port)
    {
        this.port = port;
    }

    public ScaleIOData getScaleIOData()
    {
        return scaleIOData;
    }

    public void setScaleIOData(final ScaleIOData scaleIOData)
    {
        this.scaleIOData = scaleIOData;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    private ScaleIOData scaleIOData;
}
