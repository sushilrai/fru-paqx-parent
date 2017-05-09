package com.dell.cpsd.paqx.fru.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
public class System extends AbstractElement implements Identifiable, Defineable
{

    @Id
    @Column(name = "SYSTEM_ID", unique = true, nullable = false)
    private String uuid;

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

    @ManyToMany(mappedBy = "systems", cascade = {CascadeType.ALL})
    private List<Group> groups = new ArrayList();

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "SYSTEM_CONTAINMENT_GROUP",
            joinColumns = {
                    @JoinColumn(name = "SYSTEM_ID", nullable = false, updatable = false)
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "CONTAINMENT_GROUP_ID", nullable = false, updatable = false)
            })
    private List<Group> parentGroups = new ArrayList<>();

    private System()
    {
    }

    public System(String uuid)
    {
        this.uuid = uuid;
    }



    public String getUuid()
    {
        return uuid;
    }

    public void setProductFamily(String productFamily)
    {
        this.productFamily = productFamily;
    }

    public void setProduct(String product)
    {
        this.product = product;
    }

    public void setModelFamily(String modelFamily)
    {
        this.modelFamily = modelFamily;
    }

    public void setModel(String model)
    {
        this.model = model;
    }

    public String getModel()
    {
        return model;
    }

    public String getProduct()
    {
        return product;
    }

    public String getProductFamily()
    {
        return productFamily;
    }

    public String getModelFamily()
    {
        return modelFamily;
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

    public void addGroup(final Group group)
    {
        Optional<Group> exists = this.groups.stream()
                .filter(v -> v.getUuid() == group.getUuid())
                .findAny();

        if (!exists.isPresent())
        {
            groups.add(group);
        }
    }

    public List<Group> getParentGroups()
    {
        return parentGroups;
    }

    public void addParentGroup(final Group group)
    {
        Optional<Group> exists = this.parentGroups.stream()
                .filter(v -> v.getUuid() == group.getUuid())
                .findAny();

        if (!exists.isPresent())
        {
            parentGroups.add(group);
        }
    }

    public List<Group> getGroups()
    {
        return groups;
    }

    @Override
    public Object getPersistedId()
    {
        return getUuid();
    }
}