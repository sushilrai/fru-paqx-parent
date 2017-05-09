package com.dell.cpsd.paqx.fru.domain;

import com.dell.cpsd.common.logging.ILogger;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
@Table
public class Subcomponent extends AbstractElement implements Identifiable, Defineable
{

    @Id
    @Column(name = "SUBCOMPONENT_ID", unique = true, nullable = false)
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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "subComponentParent", orphanRemoval = true)
    private Set<SubComponentVersion> versions = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "SUBCOMPONENT_SUBCOMPONENT",
            joinColumns = {
                    @JoinColumn(name = "SUBCOMPONENT_ID", nullable = false, updatable = false)
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "SUBCOMPONENT_SUBCOMPONENT_ID", nullable = false, updatable = false)
            })
    private List<Subcomponent> subComponents = new ArrayList<>();

    @ManyToMany(mappedBy = "subComponents", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Subcomponent> parentComponents = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL)
    private Device device;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "subComponentParent", orphanRemoval = true)
    private SubComponentAudit audit;

    private Subcomponent()
    {
        this.audit = new SubComponentAudit();
        audit.setParent(this);
    }

    public Subcomponent(String uuid, String elementType)
    {
        this();
        this.uuid = uuid;
        this.elementType = elementType;
    }

    public Device getDevice()
    {
        return device;
    }

    public List<Subcomponent> getParentComponents()
    {
        return parentComponents;
    }

    public String getUuid()
    {
        return uuid;
    }

    public String getElementType()
    {
        return elementType;
    }

    public String getProductFamily()
    {
        return productFamily;
    }

    public void setProductFamily(String productFamily)
    {
        this.productFamily = productFamily;
    }

    public String getProduct()
    {
        return product;
    }

    public void setProduct(String product)
    {
        this.product = product;
    }

    public String getModelFamily()
    {
        return modelFamily;
    }

    public void setModelFamily(String modelFamily)
    {
        this.modelFamily = modelFamily;
    }

    public String getModel()
    {
        return model;
    }

    public void setModel(String model)
    {
        this.model = model;
    }

    public String getIdentifier()
    {
        return identifier;
    }

    public void setIdentifier(String identifier)
    {
        this.identifier = identifier;
    }

    public String getIpAddress()
    {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress)
    {
        this.ipAddress = ipAddress;
    }

    public String getSerialNumber()
    {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber)
    {
        this.serialNumber = serialNumber;
    }

    public Set<SubComponentVersion> getVersions()
    {
        return versions;
    }

    public void removeVersion(final SubComponentVersion version)
    {
        this.versions.remove(version);
    }

    public void addVersion(final SubComponentVersion version)
    {
        if (!exists(this.versions, v -> v.getVersionType() == version.getVersionType()))
        {
            version.setParent(this);
            this.versions.add(version);
        }
    }

    public List<Subcomponent> getSubcomponents()
    {
        return subComponents;
    }

    public void removeSubcomponent(final Subcomponent subcomponent)
    {
        this.subComponents.remove(subcomponent);
    }

    public void addSubComponent(final Subcomponent subComponent)
    {
        if (!exists(this.subComponents, s -> StringUtils.equals(s.getUuid(), subComponent.getUuid())))
        {
            subComponent.addParentComponent(this);
            this.subComponents.add(subComponent);
        }
    }

    public void addParentComponent(final Subcomponent subComponent)
    {
        if (!exists(parentComponents, s -> StringUtils.equals(s.getUuid(), subComponent.getUuid())))
        {
            this.parentComponents.add(subComponent);
        }
    }

    public void setDevice(final Device device)
    {
        this.device = device;
    }

    public SubComponentAudit getAudit()
    {
        return audit;
    }

    @Override
    public Object getPersistedId()
    {
        return getUuid();
    }
}