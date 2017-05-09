/**
 *
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 *
 */
package com.dell.cpsd.paqx.fru.transformers;

import com.dell.cpsd.paqx.fru.rest.dto.vCenterSystemProperties;
import com.dell.cpsd.virtualization.capabilities.api.Datacenter;
import com.dell.cpsd.virtualization.capabilities.api.DiscoveryResponseInfoMessage;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

/**
 *
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 *
 */
public class DiscoveryInfoToVCenterSystemPropertiesTransformerTest
{
    @Test
    public void testNull()
    {
        DiscoveryInfoToVCenterSystemPropertiesTransformer transformer=new DiscoveryInfoToVCenterSystemPropertiesTransformer();
        final vCenterSystemProperties result = transformer.transform(null);
        assertTrue(result==null);

    }

    @Test
    public void testResponseInfoMessageWithEmptyDataCenters()
    {
        DiscoveryInfoToVCenterSystemPropertiesTransformer transformer=new DiscoveryInfoToVCenterSystemPropertiesTransformer();
        final vCenterSystemProperties result = transformer.transform(new DiscoveryResponseInfoMessage());
        assertTrue(result!=null);
    }

    @Test
    public void testResponseInfoMessageWithOneDataCenter()
    {
        DiscoveryInfoToVCenterSystemPropertiesTransformer transformer=new DiscoveryInfoToVCenterSystemPropertiesTransformer();
        final DiscoveryResponseInfoMessage message = new DiscoveryResponseInfoMessage();
        final ArrayList<Datacenter> dataCenterList = new ArrayList<>();
        dataCenterList.add(new Datacenter());
        message.setDatacenters(dataCenterList);
        final vCenterSystemProperties result = transformer.transform(message);
        assertTrue(result!=null);
    }
}