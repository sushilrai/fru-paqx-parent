package com.dell.cpsd.paqx.fru.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
    private String protectionDomainState;

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
}
