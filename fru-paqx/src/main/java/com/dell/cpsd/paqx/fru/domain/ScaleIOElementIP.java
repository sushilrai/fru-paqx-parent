package com.dell.cpsd.paqx.fru.domain;

        import javax.persistence.CascadeType;
        import javax.persistence.Column;
        import javax.persistence.Entity;
        import javax.persistence.GeneratedValue;
        import javax.persistence.GenerationType;
        import javax.persistence.Id;
        import javax.persistence.Inheritance;
        import javax.persistence.InheritanceType;
        import javax.persistence.ManyToOne;
        import javax.persistence.Table;

/**
 * Created by kenefj on 03/05/17.
 */
@Entity
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@Table(name="SCALEIO_ELEMENT_IP")
public abstract class ScaleIOElementIP
{
    public Long getUuid()
    {
        return uuid;
    }

    public void setUuid(final Long uuid)
    {
        this.uuid = uuid;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "IP_UUID", unique = true, nullable = false)
    private Long uuid;

    @Column(name = "IP_ID", unique = true, nullable = false)
    private String id;

    @Column(name = "IP")
    private String ip;

    public String getId()
    {
        return id;
    }

    public void setId(final String id)
    {
        this.id = id;
    }

    public String getIp()
    {
        return ip;
    }

    public void setIp(final String ip)
    {
        this.ip = ip;
    }

    public ScaleIOMdmCluster getMdmCluster()
    {
        return mdmCluster;
    }

    public void setMdmCluster(final ScaleIOMdmCluster mdmCluster)
    {
        this.mdmCluster = mdmCluster;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public ScaleIOMdmCluster mdmCluster;


}

