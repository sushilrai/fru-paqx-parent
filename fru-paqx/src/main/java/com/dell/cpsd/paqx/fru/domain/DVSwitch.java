package com.dell.cpsd.paqx.fru.domain;

import javax.persistence.*;

/**
 * Created by britney2k on 5/9/17.
 */
@Entity
@Table(name = "DVSWITCH")
public class DVSwitch {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "UUID", unique = true, nullable = false)
    private Long uuid;

    @Column(name = "DVSWITCH_ID", unique=true, nullable=false)
    private String id;

    @Column(name = "DVSWITCH_NAME")
    private String name;

    @Column(name = "ALLOW_PROMISCUOUS")
    private boolean allowPromiscuous;

    public DVSwitch(String id, String name, boolean allowPromiscuous) {
        this.id = id;
        this.name = name;
        this.allowPromiscuous = allowPromiscuous;
    }

    // DVSwitch maps back to one datacenter
    @ManyToOne(cascade = CascadeType.ALL)
    private Datacenter datacenter;

    // DVSwitch can have several networks
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dVSwitch", orphanRemoval = true)
//    private List<Network> networkList = new ArrayList<>();

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAllowPromiscuous() {
        return allowPromiscuous;
    }

    public void setAllowPromiscuous(boolean allowPromiscuous) {
        this.allowPromiscuous = allowPromiscuous;
    }

    public Datacenter getDatacenter() {
        return datacenter;
    }

    public void setDatacenter(Datacenter datacenter) {
        this.datacenter = datacenter;
    }

//    public List<Network> getNetworkList() {
//        return networkList;
//    }
//    public void addNetwork(Network network) {
//        this.networkList.add(network);
//    }
//
//    public void setNetworkList(List<Network> networkList) {
//        this.networkList = networkList;
//    }
}
