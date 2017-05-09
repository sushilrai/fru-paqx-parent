package com.dell.cpsd.paqx.fru.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.io.Serializable;

/**
 * <p>
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * @since SINCE-TBD
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "VERSION_TYPE")
public abstract class Version extends AbstractElement implements Serializable
{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "VERSION_ID", unique = true, nullable = false)
    private Long id;

    @Column(name = "VERSION")
    private String version;

    public Version()
    {
    }

    public Version(String version)
    {
        this.version = version;
    }

    public Long getId()
    {
        return this.id;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(final String version)
    {
        this.version = version;
    }

    @Override
    public Object getPersistedId()
    {
        return getId();
    }

    public abstract VersionType getVersionType();


}