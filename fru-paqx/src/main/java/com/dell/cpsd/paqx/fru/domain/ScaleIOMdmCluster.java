package com.dell.cpsd.paqx.fru.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kenefj on 03/05/17.
 */
@Entity
@Table(name = "SCALEIO_MDM_CLUSTER")
public class ScaleIOMdmCluster
{
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Id
    @Column(name = "MDM_CLUSTER_UUID", unique = true, nullable = false)
    private Long uuid;

    @Column(name = "MDM_CLUSTER_ID", unique = true, nullable = false)
    private String id;

    @Column(name = "MDM_CLUSTER_NAME")
    private String name;

    @Column(name = "MDM_CLUSTER_STATE")
    private String clusterState;

    @Column(name = "MDM_CLUSTER_MODE")
    private String clusterMode;

    @Column(name = "MDM_CLUSTER_GOOD_NODES_NUM")
    private Integer goodNodesNum;

    @Column(name = "MDM_CLUSTER_GOOD_REPLICAS_NUM")
    private Integer goodReplicasNum;

@OneToOne(optional=false, mappedBy="mdmCluster", cascade=CascadeType.ALL)
    private ScaleIOData scaleIOData;

    public ScaleIOMdmCluster(final String id8, final String clusterName, final String clusterState, final String clusterMode, final int i,
            final int i1)
    {
        this.id=id8;
        this.name=clusterName;
        this.clusterState=clusterState;
        this.clusterMode=clusterMode;
        this.goodNodesNum=i;
        this.goodReplicasNum=i1;
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

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public String getClusterState()
    {
        return clusterState;
    }

    public void setClusterState(final String clusterState)
    {
        this.clusterState = clusterState;
    }

    public String getClusterMode()
    {
        return clusterMode;
    }

    public void setClusterMode(final String clusterMode)
    {
        this.clusterMode = clusterMode;
    }

    public Integer getGoodNodesNum()
    {
        return goodNodesNum;
    }

    public void setGoodNodesNum(final Integer goodNodesNum)
    {
        this.goodNodesNum = goodNodesNum;
    }

    public Integer getGoodReplicasNum()
    {
        return goodReplicasNum;
    }

    public void setGoodReplicasNum(final Integer goodReplicasNum)
    {
        this.goodReplicasNum = goodReplicasNum;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mdmCluster", orphanRemoval = true)
    List<ScaleIOSDSElementInfo> slaveElementInfo = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mdmCluster", orphanRemoval = true)
    List<ScaleIOSDSElementInfo> tiebreakerElementInfo = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mdmCluster", orphanRemoval = true)
    List<ScaleIOSDSElementInfo> masterElementInfo = new ArrayList<>();

    public void setScaleIOData(final ScaleIOData scaleIOData)
    {
        this.scaleIOData = scaleIOData;
    }

    public ScaleIOData getScaleIOData()
    {
        return scaleIOData;
    }

    public void addSlave(final ScaleIOSlaveElementInfo slave)
    {
        this.slaveElementInfo.add(slave);
    }

    public void addMaster(final ScaleIOMasterElementInfo slave)
    {
        this.masterElementInfo.add(slave);
    }

    public void addTiebreaker(final ScaleIOTiebreakerElementInfo slave)
    {
        this.tiebreakerElementInfo.add(slave);
    }
}
