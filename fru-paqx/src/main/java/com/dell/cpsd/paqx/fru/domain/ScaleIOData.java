package com.dell.cpsd.paqx.fru.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.List;

/**
 * Created by kenefj on 03/05/17.
 */
@Entity
@Table(name = "SCALEIO_DATA")
public class ScaleIOData
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "SCALEIO_UUID", unique = true, nullable = false)
    private Long uuid;

    @Column(name = "SCALEIO_ID", unique=true, nullable=false)
    private String id;

    @Column(name = "SCALEIO_NAME")
    private String name;

    @Column(name = "SCALEIO_INSTALLID")
    private String installId;

    @Column(name = "SCALEIO_MDM_MODE")
    private String mdmMode;

    @Column(name = "SCALEIO_SYSTEM_VERSION_NAME")
    private String systemVersionName;

    @Column(name = "SCALEIO_MDM_CLUSTER_STATE")
    private String mdmClusterState;

    @Column(name = "SCALEIO_VERSION")
    private String version;

    @OneToOne(cascade=CascadeType.ALL,optional=false)
//    @JoinColumn(
//            name="MDM_CLUSTER_UUID", unique=true, nullable=false, updatable=false)
    private ScaleIOMdmCluster mdmCluster=null;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "scaleIOData", orphanRemoval = true)
    List<ScaleIOSDC> sdcList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "scaleIOData", orphanRemoval = true)
    List<ScaleIOSDS> sdsList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "scaleIOData", orphanRemoval = true)
    List<ScaleIOIP> tiebreakerScaleIOList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "scaleIOData", orphanRemoval = true)
    List<ScaleIOIP> primaryMDMIPList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "scaleIOData", orphanRemoval = true)
    List<ScaleIOIP> secondaryMDMIPList;

    public ScaleIOData(final String id1, final String scaleIODataName, final String scaleIODataInstallID, final String mdmMode,
            final String systemVersionName, final String mdmClusterState, final String version)
    {
        this.id=id1;
        this.name=scaleIODataName;
        this.installId=scaleIODataInstallID;
        this.mdmMode=mdmMode;
        this.systemVersionName=systemVersionName;
        this.mdmClusterState=mdmClusterState;
        this.version=version;
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

    public String getInstallId()
    {
        return installId;
    }

    public void setInstallId(final String installId)
    {
        this.installId = installId;
    }

    public String getMdmMode()
    {
        return mdmMode;
    }

    public void setMdmMode(final String mdmMode)
    {
        this.mdmMode = mdmMode;
    }

    public String getSystemVersionName()
    {
        return systemVersionName;
    }

    public void setSystemVersionName(final String systemVersionName)
    {
        this.systemVersionName = systemVersionName;
    }

    public String getMdmClusterState()
    {
        return mdmClusterState;
    }

    public void setMdmClusterState(final String mdmClusterState)
    {
        this.mdmClusterState = mdmClusterState;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(final String version)
    {
        this.version = version;
    }

    public ScaleIOMdmCluster getMdmCluster()
    {
        return mdmCluster;
    }

    public void setMdmCluster(final ScaleIOMdmCluster mdmCluster)
    {
        this.mdmCluster = mdmCluster;
    }

    public List<ScaleIOSDC> getSdcList()
    {
        return sdcList;
    }

    public void setSdcList(final List<ScaleIOSDC> sdcList)
    {
        this.sdcList = sdcList;
    }

    public List<ScaleIOSDS> getSdsList()
    {
        return sdsList;
    }

    public void setSdsList(final List<ScaleIOSDS> sdsList)
    {
        this.sdsList = sdsList;
    }

    public List<ScaleIOIP> getTiebreakerScaleIOList()
    {
        return tiebreakerScaleIOList;
    }

    public void setTiebreakerScaleIOList(final List<ScaleIOIP> tiebreakerScaleIOList)
    {
        this.tiebreakerScaleIOList = tiebreakerScaleIOList;
    }

    public List<ScaleIOIP> getPrimaryMDMIPList()
    {
        return primaryMDMIPList;
    }

    public void setPrimaryMDMIPList(final List<ScaleIOIP> primaryMDMIPList)
    {
        this.primaryMDMIPList = primaryMDMIPList;
    }

    public List<ScaleIOIP> getSecondaryMDMIPList()
    {
        return secondaryMDMIPList;
    }

    public void setSecondaryMDMIPList(final List<ScaleIOIP> secondaryMDMIPList)
    {
        this.secondaryMDMIPList = secondaryMDMIPList;
    }
}
