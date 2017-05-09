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
@Table(name="SCALE_IO_SDC")
public class ScaleIOSDC
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "SDC_UUID", unique = true, nullable = false)
    private Long uuid;

    @Column(name = "SDC_ID", unique = true, nullable = false)
    private String id;

    @Column(name = "SDC_NAME")
    private String name;

    @Column(name = "SDC_IP")
    private String sdcIp;

    @Column(name = "SDC_GUID")
    private String sdcGuid;

    @Column(name = "SDC_MDM_CONNECTION_STATE")
    private String mdmConnectionState;

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
