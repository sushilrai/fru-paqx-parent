package com.dell.cpsd.paqx.fru.domain;

import javax.persistence.*;

/**
 * Created by britney2k on 5/10/17.
 */
@Entity
@Table(name = "NETWORK")
public class Network {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "UUID", unique = true, nullable = false)
    private Long uuid;

    @Column(name = "NETWORK_ID", unique=true, nullable=false)
    private String id;

    @Column(name = "NETWORK_NAME")
    private String name;


}
