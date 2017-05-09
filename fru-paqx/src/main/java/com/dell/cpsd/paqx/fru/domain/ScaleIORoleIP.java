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
@Table(name="SCALEIO_IP_LIST")
public class ScaleIORoleIP
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "IP_LIST_UUID", unique = true, nullable = false)
    private Long uuid;

    @Column(name = "SDS_ROLE")
    private String role;

    @Column(name = "SDS_IP")
    private String ip;

    @ManyToOne(cascade= CascadeType.ALL)
    private ScaleIOSDS sds;

    public ScaleIORoleIP(final String role, final String ip)
    {
        this.role=role;
        this.ip=ip;
    }

    public Long getUuid()
    {
        return uuid;
    }

    public void setUuid(final Long uuid)
    {
        this.uuid = uuid;
    }

    public String getRole()
    {
        return role;
    }

    public void setRole(final String role)
    {
        this.role = role;
    }

    public String getIp()
    {
        return ip;
    }

    public void setIp(final String ip)
    {
        this.ip = ip;
    }

    public void setSds(final ScaleIOSDS sds)
    {
        this.sds = sds;
    }
}
