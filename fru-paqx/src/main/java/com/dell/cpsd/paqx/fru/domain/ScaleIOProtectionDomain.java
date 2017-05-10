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
@Table(name="SCALE_IO_PROTETION_DOMAIN")
public class ScaleIOProtectionDomain
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "PROTECTION_DOMAIN_UUID", unique = true, nullable = false)
    private Long uuid;

    @Column(name = "PROTECTION_DOMAIN_ID")
    private String id;

    @Column(name = "PROTECTION_DOMAIN_NAME")
    private String name;

    @Column(name = "PROTECTION_DOMAIN_STATE")
    private String                protectionDomainState;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "protectionDomain", orphanRemoval = true)
    private List<ScaleIOFaultSet> faultSets = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "protectionDomain", orphanRemoval = true)
    private List<ScaleIOSDS> sdsList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "protectionDomain", orphanRemoval = true)
    private List<ScaleIOStoragePool> storagePools = new ArrayList<>();

    @ManyToOne(cascade=CascadeType.ALL)
    private ScaleIOData scaleIOData;

    public ScaleIOProtectionDomain(final String id, final String protectionDomainName, final String state)
    {
        this.id=id;
        this.name=protectionDomainName;
        this.protectionDomainState=state;
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

    public String getProtectionDomainState()
    {
        return protectionDomainState;
    }

    public void setProtectionDomainState(final String protectionDomainState)
    {
        this.protectionDomainState = protectionDomainState;
    }

    public void addFaultSet(final ScaleIOFaultSet faultSet)
    {
        this.faultSets.add(faultSet);
    }

    public void addSDS(final ScaleIOSDS sds)
    {
        this.sdsList.add(sds);
    }

    public void setScaleIOData(final ScaleIOData scaleIOData)
    {
        this.scaleIOData = scaleIOData;
    }

    public void addStoragePool(final ScaleIOStoragePool storagePool)
    {
        storagePools.add(storagePool);
    }
}
