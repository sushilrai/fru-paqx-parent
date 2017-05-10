package com.dell.cpsd.paqx.fru.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kenefj on 03/05/17.
 */
@Entity
@Table(name="SCALEIO_STORAGE_POOL")
public class ScaleIOStoragePool
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "STORAGE_POOL_UUID", unique = true, nullable = false)
    private Long uuid;

    @Column(name = "STORAGE_POOL_ID")
    private String id;

    @Column(name = "STORAGE_POOL_NAME")
    private String name;

    @Column(name = "STORAGE_POOL_CAPACITY_AVILABLE")
    private Integer capacityAvailableForVolumeAllocationInKb;

    @Column(name = "STORAGE_POOL_MAX_CAPACITY_AVILABLE")
    private Integer maxCapacityInKb;

    @Column(name = "STORAGE_POOL_NUM_VOLUMES")
    private Integer numOfVolumes;

    @ManyToOne(cascade = CascadeType.ALL)
    private ScaleIOProtectionDomain protectionDomain;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "storagePool", orphanRemoval = true)
    private List<ScaleIODevice>     devices = new ArrayList<>();

    public ScaleIOStoragePool(final String id, final String storagePoolName, final int capacityAvailableForVolumeAllocationInKb,
            final int maxCapacityInKb, final int numOfVolumes)
    {
        this.id = id;
        this.name = storagePoolName;
        this. capacityAvailableForVolumeAllocationInKb = capacityAvailableForVolumeAllocationInKb;
        this.maxCapacityInKb = maxCapacityInKb;
        this.numOfVolumes = numOfVolumes;
    }

    public void setProtectionDomain(final ScaleIOProtectionDomain protectionDomain)
    {
        this.protectionDomain = protectionDomain;
    }

    public void addDevice(final ScaleIODevice device)
    {
        this.devices.add(device);
    }
}
