package com.dell.cpsd.paqx.fru.transformers;

import com.dell.cpsd.paqx.fru.domain.*;
import com.dell.cpsd.paqx.fru.domain.Cluster;
import com.dell.cpsd.paqx.fru.domain.Datacenter;
import com.dell.cpsd.paqx.fru.domain.Datastore;
import com.dell.cpsd.paqx.fru.domain.HostDnsConfig;
import com.dell.cpsd.paqx.fru.domain.HostIpRouteConfig;
import com.dell.cpsd.virtualization.capabilities.api.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DiscoveryInfoToVCenterDomainTransformer {

    public VCenter transform(final DiscoveryResponseInfoMessage discoveryResponseInfoMessage)
    {
        if (discoveryResponseInfoMessage == null)
        {
            return null;
        }

        // Create the VCenter object
        // TODO: Add vcenter properties
        VCenter returnVal = new VCenter("change-me", "change-me");

        // Transform and link datacenters
        List<Datacenter> datacenters = discoveryResponseInfoMessage.getDatacenters()
                .stream().filter(Objects::nonNull)
                .map(datacenter -> transformDatacenter(datacenter, returnVal))
                .collect(Collectors.toList());
        returnVal.setDatacenterList(datacenters);

        return returnVal;
    }


    private Datacenter transformDatacenter(com.dell.cpsd.virtualization.capabilities.api.Datacenter datacenter, VCenter vCenter)
    {
        if (datacenter == null){
            return null;
        }
        Datacenter returnVal = new Datacenter();
        returnVal.setId(datacenter.getId());
        returnVal.setName(datacenter.getName());

        // Transform and link datastores
        List<Datastore> datastores = datacenter.getDatastores()
                .stream().filter(Objects::nonNull)
                .map(datastore -> transformDatastore(datastore, returnVal))
                .collect(Collectors.toList());
        returnVal.setDatastoreList(datastores);


        // Tranform and link dvswitches


        // Transform and link clusters
        List<Cluster> clusters = datacenter.getClusters()
                .stream().filter(Objects::nonNull)
                .map(cluster -> transformCluster(cluster, returnVal))
                .collect(Collectors.toList());
        returnVal.setClusterList(clusters);

        // FK link
        returnVal.setvCenter(vCenter);

        return returnVal;
    }

    private Datastore transformDatastore(com.dell.cpsd.virtualization.capabilities.api.Datastore datastore, Datacenter datacenter)
    {
        if (datastore == null){
            return null;
        }
        Datastore returnVal = new Datastore();
        returnVal.setId(datastore.getId());
        returnVal.setName(datastore.getName());
        returnVal.setType(datastore.getDatastoreSummary().getType());
        returnVal.setUrl(datastore.getDatastoreSummary().getUrl());

        // FK link
        returnVal.setDatacenter(datacenter);

        return returnVal;
    }

    private Cluster transformCluster(com.dell.cpsd.virtualization.capabilities.api.Cluster cluster, Datacenter datacenter)
    {
        if (cluster == null){
            return null;
        }
        Cluster returnVal = new Cluster();
        returnVal.setId(cluster.getId());
        returnVal.setName(cluster.getName());

        // Transform and link hosts
        List<Host> hosts = cluster.getHosts()
                .stream().filter(Objects::nonNull)
                .map(hostSystem -> transformHost(hostSystem, returnVal)).collect(Collectors.toList());

        returnVal.setHostList(hosts);

        // FK link
        returnVal.setDatacenter(datacenter);

        return returnVal;
    }

    private Host transformHost(HostSystem hostSystem, Cluster cluster)
    {
        if (hostSystem == null){
            return null;
        }
        Host returnVal = new Host();
        returnVal.setId(hostSystem.getId());
        returnVal.setName(hostSystem.getName());

        // TODO: Add host power state and serial
        //returnVal.setPowerState(hostSystem.getPowerState());
        //returnVal.setSerialNumber(hostSystem.getSerialNumber());

        // Transform and link HostDnsConfig
        HostDnsConfig hostDnsConfig = transformHostDnsConfig(hostSystem.getHostConfigInfo().getHostNetworkInfo().getHostDnsConfig(), returnVal);
        returnVal.setHostDnsConfig(hostDnsConfig);

        // Transform and link HostIpRouteConfig
        HostIpRouteConfig hostIpRouteConfig = transformHostIpRouteConfig(hostSystem.getHostConfigInfo().getHostNetworkInfo().getHostIpRouteConfig(), returnVal);
        returnVal.setHostIpRouteConfig(hostIpRouteConfig);

        // Transform and link HostVirtualSwitch
        List<VSwitch> vSwitches = hostSystem.getHostConfigInfo().getHostNetworkInfo().getVswitchs()
                .stream().filter(Objects::nonNull)
                .map(hostVirtualSwitch -> transformVSwitch(hostVirtualSwitch, returnVal)).collect(Collectors.toList());
        returnVal.setvSwitchList(vSwitches);

        // FK Link
        returnVal.setCluster(cluster);

        return returnVal;
    }

    private HostDnsConfig transformHostDnsConfig(com.dell.cpsd.virtualization.capabilities.api.HostDnsConfig hostDnsConfig, Host host)
    {
        if (hostDnsConfig == null){
            return null;
        }
        HostDnsConfig returnVal = new HostDnsConfig();
        returnVal.setDhcp(hostDnsConfig.getDhcp());
        returnVal.setDomainName(hostDnsConfig.getDomainName());
        returnVal.setHostname(hostDnsConfig.getHostName());
        returnVal.setSearchDomains(hostDnsConfig.getSearchDomains());
        returnVal.setDnsConfigIPs(hostDnsConfig.getIpAddresses());

        // FK link
        returnVal.setHost(host);

        return returnVal;
    }

    private HostIpRouteConfig transformHostIpRouteConfig(com.dell.cpsd.virtualization.capabilities.api.HostIpRouteConfig hostIpRouteConfig, Host host)
    {
        if (hostIpRouteConfig == null){
            return null;
        }
        HostIpRouteConfig returnVal = new HostIpRouteConfig();
        returnVal.setDefaultGateway(hostIpRouteConfig.getDefaultGateway());
        returnVal.setDefaultGatewayDevice(hostIpRouteConfig.getDefaultGatewayDevice());
        returnVal.setIpV6defaultGateway(hostIpRouteConfig.getIpV6DefaultGateway());
        returnVal.setIpV6defaultGatewayDevice(hostIpRouteConfig.getIpV6DefaultGatewayDevice());

        // FK link
        returnVal.setHost(host);

        return returnVal;
    }

    private VSwitch transformVSwitch(HostVirtualSwitch hostVirtualSwitch, Host host)
    {
        if (hostVirtualSwitch == null){
            return null;
        }
        VSwitch returnVal = new VSwitch();
        returnVal.setId(hostVirtualSwitch.getKey());
        returnVal.setName(hostVirtualSwitch.getName());
        returnVal.setAllowPromiscuous(hostVirtualSwitch.getHostVirtualSwitchSpec().getHostNetworkPolicy().getHostNetworkSecurityPolicy().getAllowPromiscuous());

        // FK link
        returnVal.setHost(host);

        return returnVal;
    }

}
