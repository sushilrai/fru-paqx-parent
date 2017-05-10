package com.dell.cpsd.paqx.fru.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by kenefj on 03/05/17.
 */
@Entity
@Table(name="SDC_DEVICE")
public class ScaleIODevice
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "DEVICE_UUID", unique = true, nullable = false)
    private Long uuid;

    @Column(name = "DEVICE_ID")
    private String id;

    @Column(name = "DEVICE_NAME", unique = true, nullable = false)
    private String name;

    @Column(name = "DEVICE_CURRENT_PATH_NAME", unique = true, nullable = false)
    private String deviceCurrentPathName;

    @ManyToOne(cascade = CascadeType.ALL)
    private ScaleIOStoragePool storagePool;

    @ManyToOne(cascade = CascadeType.ALL)
    private ScaleIOSDS sds;

    public ScaleIODevice(final String id, final String scaleIOName, final String deviceCurrentPathName)
    {
        this.id = id;
        this.name = scaleIOName;
        this.deviceCurrentPathName = deviceCurrentPathName;
    }

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

    public String getDeviceCurrentPathName()
    {
        return deviceCurrentPathName;
    }

    public void setDeviceCurrentPathName(final String deviceCurrentPathName)
    {
        this.deviceCurrentPathName = deviceCurrentPathName;
    }

    public void setStoragePool(final ScaleIOStoragePool storagePool)
    {
        this.storagePool = storagePool;
    }

    public void setSds(final ScaleIOSDS sds)
    {
        this.sds = sds;
    }
}