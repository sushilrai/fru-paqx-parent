package com.dell.cpsd.paqx.fru.domain;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * @since SINCE-TBD
 */
@Entity
@Table(name = "DEVICE")
public class Device extends AbstractElement implements Identifiable, Defineable
{

    @Id
    @Column(name = "DEVICE_ID", unique = true, nullable = false)
    private String uuid;

    @Column(name = "ELEMENT_TYPE")
    private String elementType;

    @Column(name = "IDENTIFIER")
    private String identifier;

    @Column(name = "IP_ADDRESS")
    private String ipAddress;

    @Column(name = "SERIAL_NUMBER")
    private String serialNumber;

    @Column(name = "PRODUCT_FAMILY")
    private String productFamily;

    @Column(name = "PRODUCT")
    private String product;

    @Column(name = "MODEL_FAMILY")
    private String modelFamily;

    @Column(name = "MODEL")
    private String model;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "DEVICE_CONTAINMENT_GROUP",
            joinColumns = {
                    @JoinColumn(name = "DEVICE_ID", nullable = false, updatable = false)
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "CONTAINMENT_GROUP_ID", nullable = false, updatable = false)
            })
    private List<Group> groups = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "device", orphanRemoval = true)
    private List<Subcomponent> subcomponents = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "deviceParent", orphanRemoval = true)
    private Set<DeviceVersion> versions = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "deviceParent", orphanRemoval = true)
    private DeviceAudit audit;

    private Device()
    {
        audit = new DeviceAudit();
        audit.setParent(this);
    }

    public Device(String uuid, String elementType)
    {
        this();
        this.uuid = uuid;
        this.elementType = elementType;
    }

    public String getUuid()
    {
        return uuid;
    }


    public String getElementType()
    {
        return elementType;
    }

    @Override
    public String getProductFamily()
    {
        return productFamily;
    }

    @Override
    public void setProductFamily(String productFamily)
    {
        this.productFamily = productFamily;
    }

    @Override
    public String getProduct()
    {
        return product;
    }

    @Override
    public void setProduct(String product)
    {
        this.product = product;
    }

    @Override
    public String getModelFamily()
    {
        return modelFamily;
    }

    @Override
    public void setModelFamily(String modelFamily)
    {
        this.modelFamily = modelFamily;
    }

    @Override
    public String getModel()
    {
        return model;
    }

    @Override
    public void setModel(String model)
    {
        this.model = model;
    }

    @Override
    public String getIdentifier()
    {
        return identifier;
    }

    @Override
    public void setIdentifier(String identifier)
    {
        this.identifier = identifier;
    }

    @Override
    public String getIpAddress()
    {
        return ipAddress;
    }

    @Override
    public void setIpAddress(String ipAddress)
    {
        this.ipAddress = ipAddress;
    }

    @Override
    public String getSerialNumber()
    {
        return serialNumber;
    }

    @Override
    public void setSerialNumber(String serialNumber)
    {
        this.serialNumber = serialNumber;
    }

    public List<Subcomponent> getSubcomponents()
    {
        return subcomponents;
    }

    public void removeSubcomponent(final Subcomponent subcomponent)
    {
        this.subcomponents.remove(subcomponent);
    }

    public void addSubComponent(final Subcomponent subComponent)
    {
        if (!exists(this.subcomponents, s -> StringUtils.equals(s.getUuid(), subComponent.getUuid())))
        {
            subComponent.setDevice(this);
            this.subcomponents.add(subComponent);
        }
    }

    public Set<DeviceVersion> getVersions()
    {
        return versions;
    }

    public List<Group> getGroups()
    {
        return groups;
    }

    public void addGroup(final Group group)
    {
        if (!exists(this.groups, s -> StringUtils.equals(s.getUuid(), group.getUuid())))
        {
            group.addDevice(this);
            this.groups.add(group);
        }
    }

    public void removeVersion(final DeviceVersion version)
    {
        this.versions.remove(version);
    }

    public void addVersion(final DeviceVersion version)
    {
        if (!exists(this.versions, v -> v.getVersionType() == version.getVersionType()))
        {
            version.setParent(this);
            this.versions.add(version);
        }
    }

    public DeviceAudit getAudit()
    {
        return audit;
    }

    @Override
    public Object getPersistedId()
    {
        return getUuid();
    }
}