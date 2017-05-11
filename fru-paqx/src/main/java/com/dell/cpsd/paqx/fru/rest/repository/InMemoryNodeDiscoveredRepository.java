/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.rest.repository;

import com.dell.converged.capabilities.compute.discovered.nodes.api.NodeEventDataDiscovered;
import com.dell.cpsd.paqx.fru.rest.domain.Job;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * <p>
 * TODO: Replace with JPA/Hibernate
 */
@Repository
public class InMemoryNodeDiscoveredRepository implements NodeDiscoveredRepository {
    private final Map<String, NodeEventDataDiscovered> nodeEventDataDiscoveredMap = new HashMap<>();

    @Override
    public void save(final NodeEventDataDiscovered nodeEventDataDiscovered) {
        nodeEventDataDiscoveredMap.put(nodeEventDataDiscovered.getNodeId(), nodeEventDataDiscovered);
    }

    @Override
    public NodeEventDataDiscovered[] findAll() {
        NodeEventDataDiscovered[] results = new NodeEventDataDiscovered[nodeEventDataDiscoveredMap.size()];
        return nodeEventDataDiscoveredMap.values().toArray(results);
    }

    @Override
    public NodeEventDataDiscovered find(final String nodeiID) {
        return nodeEventDataDiscoveredMap.get(nodeiID);
    }
}
