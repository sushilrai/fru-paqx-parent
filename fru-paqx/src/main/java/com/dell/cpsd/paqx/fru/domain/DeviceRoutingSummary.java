package com.dell.cpsd.paqx.fru.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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
@Table(name = "DEVICE_ROUTING_SUMMARY")
public class DeviceRoutingSummary extends AbstractElement
{

    @EmbeddedId
    private PrimaryId id;

    @ManyToOne(cascade = {CascadeType.ALL})
    private Group group;

    public DeviceRoutingSummary()
    {
    }

    public DeviceRoutingSummary(String systemUuid, Group group, String deviceUuid)
    {
        this.group = group;
        this.id = new PrimaryId(systemUuid, group.getUuid(), deviceUuid);
    }

    public PrimaryId getId()
    {
        return id;
    }

    public Group getGroup()
    {
        return group;
    }

    public void setGroup(Group group)
    {
        this.group = group;
    }

    public String getSystemUuid()
    {
        return id.getSystemUuid();
    }

    public String getDeviceUuid()
    {
        return id.getDeviceUuid();
    }

    @Override
    public Object getPersistedId()
    {
        return getId();
    }

    @Embeddable
    public static class PrimaryId implements Serializable
    {

        @Column(name = "PK_SYSTEM_UUID")
        private String systemUuid;

        @Column(name = "PK_GROUP_UUID")
        private String groupUuid;

        @Column(name = "PK_DEVICE_UUID")
        private String deviceUuid;

        public PrimaryId()
        {
        }

        public PrimaryId(String systemUuid, String groupUuid, String deviceUuid)
        {
            this.systemUuid = systemUuid;
            this.groupUuid = groupUuid;
            this.deviceUuid = deviceUuid;
        }

        public String getSystemUuid()
        {
            return systemUuid;
        }

        public String getGroupUuid()
        {
            return groupUuid;
        }

        public String getDeviceUuid()
        {
            return deviceUuid;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            PrimaryId primaryId = (PrimaryId) o;

            if (systemUuid != null ? !systemUuid.equals(primaryId.systemUuid) : primaryId.systemUuid != null)
                return false;
            if (groupUuid != null ? !groupUuid.equals(primaryId.groupUuid) : primaryId.groupUuid != null)
                return false;
            return deviceUuid != null ? deviceUuid.equals(primaryId.deviceUuid) : primaryId.deviceUuid == null;

        }

        @Override
        public int hashCode()
        {
            int result = systemUuid != null ? systemUuid.hashCode() : 0;
            result = 31 * result + (groupUuid != null ? groupUuid.hashCode() : 0);
            result = 31 * result + (deviceUuid != null ? deviceUuid.hashCode() : 0);
            return result;
        }
    }
}