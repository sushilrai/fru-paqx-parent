/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.rest.representation;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
@XmlRootElement
public class AboutRepresentation
{
    private final Map<String, Capability> required = new HashMap<>();
    private final List<Node>              nodes    = new ArrayList<>();

    public void addRequiredCapability(String capability)
    {
        required.put(capability, new Capability(capability));
    }

    public Collection<Capability> getRequiredCapabilities()
    {
        return required.values();
    }

    public void capabilityMetBy(String capability, String provider)
    {
        if (required.containsKey(capability))
        {
            required.get(capability).fullfilledBy(provider);
        }
    }

    public List<Node> getNodes()
    {
        return nodes;
    }

    public void addNode(final String id, final String name, final String type)
    {
        nodes.add(new Node(id, name, type));
    }
}

class Capability
{
    private final String capability;
    private Set<String> fullfilledBy = new HashSet<>();

    public Capability(final String capability)
    {
        this.capability = capability;
    }

    public String getCapability()
    {
        return capability;
    }

    public void fullfilledBy(String provider)
    {
        fullfilledBy.add(provider);
    }

    public Set<String> getFullfilledBy()
    {
        return fullfilledBy;
    }
}

class Node
{
    private final String id;
    private final String name;
    private final String type;

    Node(final String id, final String name, final String type)
    {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public String getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getType()
    {
        return type;
    }
}