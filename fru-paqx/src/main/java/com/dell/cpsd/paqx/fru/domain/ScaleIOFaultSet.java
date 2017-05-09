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
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne(cascade = CascadeType.ALL)
    private ScaleIOProtectionDomain protectionDomain;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "faultSet", orphanRemoval = true)
    private List<ScaleIOSDS>        sdsList = new ArrayList<>();

    public ScaleIOFaultSet(final String id, final String faultSetName)
    {
        this.id=id;
        this.name=faultSetName;
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

    public void setProtectionDomain(final ScaleIOProtectionDomain protectionDomain)
    {
        this.protectionDomain = protectionDomain;
    }

    public void addSDS(final ScaleIOSDS sds)
    {
        this.sdsList.add(sds);
    }
}
