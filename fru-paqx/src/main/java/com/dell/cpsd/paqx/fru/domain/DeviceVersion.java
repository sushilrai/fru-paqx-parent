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
public abstract class DeviceVersion extends Version
{

    @ManyToOne
    private Device deviceParent;

    public Device getParent()
    {
        return deviceParent;
    }

    public void setParent(final Device parent)
    {
        this.deviceParent = parent;
    }

    public DeviceVersion()
    {
    }

    public DeviceVersion(final String versionString)
    {
        super(versionString);
    }

    @Entity
    @DiscriminatorValue("DEVICE_FIRMWARE")
    public static class DeviceFirmwareVersion extends DeviceVersion
    {
        public DeviceFirmwareVersion()
        {
            super();
        }

        public DeviceFirmwareVersion(final String versionString)
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
    @DiscriminatorValue("DEVICE_HARDWARE")
    public static class DeviceHardwareVersion extends DeviceVersion
    {
        public DeviceHardwareVersion()
        {
        }

        public DeviceHardwareVersion(final String versionString)
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
    @DiscriminatorValue("DEVICE_SOFTWARE")
    public static class DeviceSoftwareVersion extends DeviceVersion
    {
        public DeviceSoftwareVersion()
        {
        }

        public DeviceSoftwareVersion(final String versionString)
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