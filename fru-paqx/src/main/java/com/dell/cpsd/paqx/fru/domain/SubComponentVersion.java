package com.dell.cpsd.paqx.fru.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * The configuration for the compliance data service message consumer.
 * <p>
 * <p/>
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * <p/>
 *
 * @version 1.0
 * @since SINCE-TBD
 */
@Entity
@Table
public abstract class SubComponentVersion extends Version
{

    @ManyToOne
    private Subcomponent subComponentParent;

    public Subcomponent getParent()
    {
        return subComponentParent;
    }

    public void setParent(final Subcomponent parent)
    {
        this.subComponentParent = parent;
    }

    public SubComponentVersion()
    {
        super();
    }

    public SubComponentVersion(final String versionString)
    {
        super(versionString);
    }

    @Entity
    @DiscriminatorValue("SUBCOMPONENT_FIRMWARE")
    public static class SubComponentFirmwareVersion extends SubComponentVersion
    {
        public SubComponentFirmwareVersion()
        {
            super();
        }

        public SubComponentFirmwareVersion(final String versionString)
        {
            super(versionString);
        }

        @Override
        public VersionType getVersionType()
        {
            return VersionType.FIRMWARE;
        }
    }

    @Entity
    @DiscriminatorValue("SUBCOMPONENT_HARDWARE")
    public static class SubComponentHardwareVersion extends SubComponentVersion
    {
        public SubComponentHardwareVersion()
        {
        }

        public SubComponentHardwareVersion(final String versionString)
        {
            super(versionString);
        }

        @Override
        public VersionType getVersionType()
        {
            return VersionType.HARDWARE;
        }
    }

    @Entity
    @DiscriminatorValue("SUBCOMPONENT_SOFTWARE")
    public static class SubComponentSoftwareVersion extends SubComponentVersion
    {
        public SubComponentSoftwareVersion()
        {
        }

        public SubComponentSoftwareVersion(final String versionString)
        {
            super(versionString);
        }

        @Override
        public VersionType getVersionType()
        {
            return VersionType.SOFTWARE;
        }
    }
}