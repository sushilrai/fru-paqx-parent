package com.dell.cpsd.paqx.fru.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    private int port;

    @ManyToOne(cascade = CascadeType.ALL)
    private ScaleIOFaultSet faultSet;

    @ManyToOne(cascade = CascadeType.ALL)
    private ScaleIOProtectionDomain protectionDomain;

    @ManyToOne(cascade = CascadeType.ALL)
    private ScaleIOData scaleIOData;

    @OneToMany(cascade= CascadeType.ALL, orphanRemoval = true, mappedBy = "sds")
    private List<ScaleIORoleIP> roleIPs = new ArrayList<>();

    @OneToMany(cascade= CascadeType.ALL, orphanRemoval = true, mappedBy = "sds")
    private List<ScaleIODevice> devices = new ArrayList<>();

    public ScaleIOSDS(final String id, final String name, final String sdsState, final int port)
    {
        this.id=id;
        this.name=name;
        this.sdsState=sdsState;
        this.port=port;
    }

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

    public int getPort()
    {
        return port;
    }

    public void setPort(final int port)
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

    public void setFaultSet(final ScaleIOFaultSet faultSet)
    {
        this.faultSet = faultSet;
    }

    public void setProtectionDomain(final ScaleIOProtectionDomain protectionDomain)
    {
        this.protectionDomain = protectionDomain;
    }

    public void addRoleIP(final ScaleIORoleIP roleIP1)
    {
        this.roleIPs.add(roleIP1);
    }

    public void addDevice(final ScaleIODevice device)
    {
        this.devices.add(device);
    }
}
