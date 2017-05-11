package com.dell.cpsd.paqx.fru.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "DV_PORTGROUP")
public class DVPortGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "UUID", unique = true, nullable = false)
    private Long uuid;

    @Column(name = "NAME")
    private String name;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "dvPortGroupList")
    private List<VMNetwork> vmNetworkList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dvPortGroup")
    private List<VirtualNicDVPortGroup> virtualNicDVPortGroups;

    public DVPortGroup(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<VMNetwork> getVmNetworkList() {
        return vmNetworkList;
    }

    public void setVmNetworkList(List<VMNetwork> vmNetworkList) {
        this.vmNetworkList = vmNetworkList;
    }
}
