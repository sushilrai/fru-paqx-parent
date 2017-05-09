package com.dell.cpsd.paqx.fru.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kenefj on 03/05/17.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ELEMENT_TYPE")
@Table(name = "SCALEIO_SDS_ELEMENT_INFO")
public abstract class ScaleIOSDSElementInfo
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "SDS_ELEMENT_UUID", unique = true, nullable = false)
    private Long uuid;


    public ScaleIOSDSElementInfo(final String id9, final int i, final String version1, final String slave, final String s)
    {
        this.id = id9;
        this.port = i;
        this.versionInfo = version1;
        this.name = slave;
        this.role = s;
    }

    public Long getUuid()
    {
        return uuid;
    }

    public void setUuid(final Long uuid)
    {
        this.uuid = uuid;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    ScaleIOMdmCluster mdmCluster;

    @Column(name = "SDS_ELEMENT_ID", unique = true, nullable = false)
    private String id;

    @Column(name = "SDS_ELEMENT_PORT")
    private Integer port;

    @Column(name = "SDS_ELEMENT_VERSION_INFO")
    private String versionInfo;

    @Column(name = "SDS_ELEMENT_NAME")
    private String name;

    @Column(name = "SDS_ELEMENT_ROLE")
    private String role;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sdsElementInfo", orphanRemoval = true)
    List<ScaleIOIP> managementIPs;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sdsElementInfo", orphanRemoval = true)
    List<ScaleIOIP> ips = new ArrayList<>();

//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mdmCluster", orphanRemoval = true)
//    List<ScaleIOElementIP> managementIPs=new ArrayList<>();
//
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mdmCluster", orphanRemoval = true)
//    List<ScaleIOElementIP> ips = new ArrayList<>();

    public ScaleIOMdmCluster getMdmCluster()
    {
        return mdmCluster;
    }

    public void setMdmCluster(final ScaleIOMdmCluster mdmCluster)
    {
        this.mdmCluster = mdmCluster;
    }

    public void addIP(final ScaleIOIP id16)
    {
        this.ips.add(id16);
    }
}
