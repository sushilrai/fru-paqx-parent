package com.dell.cpsd.paqx.fru.domain;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * @since SINCE-TBD
 */
@Entity
@Table(name = "CONTAINMENT_GROUP")
public class Group extends AbstractElement
{
    @Id
    @Column(name = "CONTAINMENT_GROUP_ID", unique = true, nullable = false)
    private String uuid;

    @Enumerated(EnumType.STRING)
    private GroupType type;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "CONTAINMENT_GROUP_CONTAINMENT_GROUP",
            joinColumns = {
                @JoinColumn(name = "PARENTGROUPS_CONTAINMENT_GROUP_ID", nullable = false, updatable = false)
            },
            inverseJoinColumns = {
                @JoinColumn(name = "CONTAINMENT_GROUP_ID", nullable = false, updatable = false)
            })
    private List<Group> subGroups = new ArrayList<>();

    @ManyToMany(mappedBy = "subGroups", cascade = {CascadeType.ALL})
    private List<Group> parentGroups = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "CONTAINMENT_GROUP_SYSTEM",
            joinColumns = {
                    @JoinColumn(name = "CONTAINMENT_GROUP_ID", nullable = false, updatable = false)
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "SYSTEM_ID", nullable = false, updatable = false)
            })
    private List<System> systems = new ArrayList<>();

    @OneToMany(mappedBy = "groups", cascade = {CascadeType.ALL})
    private List<Device> devices = new ArrayList<>();

    @OneToMany(mappedBy = "group", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<DeviceRoutingSummary> deviceRoutingSummaries = new ArrayList<>();

    public Group()
    {
    }

    public Group(String uuid, GroupType type)
    {
        this.uuid = uuid;
        this.type = type;
    }

    public List<Group> getParentGroups()
    {
        return parentGroups;
    }

    public List<Group> getSubGroups()
    {
        return subGroups;
    }

    public String getUuid()
    {
        return uuid;
    }

    public GroupType getType()
    {
        return type;
    }

    public List<System> getSystems()
    {
        return systems;
    }

    public List<DeviceRoutingSummary> getDeviceRoutingSummaries()
    {
        return deviceRoutingSummaries;
    }

    public void addParentGroup(final Group parentGroup)
    {
        if (!exists(this.parentGroups, g -> StringUtils.equals(g.getUuid(), parentGroup.getUuid())))
        {
            parentGroups.add(parentGroup);
        }
    }

    public void addChildGroup(final Group childGroup)
    {
        if (!exists(this.subGroups, g -> StringUtils.equals(g.getUuid(), childGroup.getUuid())))
        {
            subGroups.add(childGroup);
        }
    }

    public void addSystem(final System system)
    {
        if (!exists(this.systems, s -> StringUtils.equals(s.getUuid(), system.getUuid())))
        {
            system.addGroup(this);
            this.systems.add(system);
        }
    }

    public void addDeviceRoutingSummary(final DeviceRoutingSummary device)
    {
        if (!exists(this.deviceRoutingSummaries, d -> StringUtils.equals(d.getDeviceUuid(), device.getDeviceUuid())))
        {
            deviceRoutingSummaries.add(device);
        }
    }

    public void addDevice(final Device device)
    {
        if (!exists(this.devices, d -> StringUtils.equals(d.getUuid(), device.getUuid())))
        {
            devices.add(device);
        }
    }

    public void setType(final GroupType type)
    {
        this.type = type;
    }

    @Override
    public Object getPersistedId()
    {
        return getUuid();
    }
}