/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
package com.dell.cpsd.paqx.fru.service;

import com.dell.cpsd.paqx.fru.dto.FRUSystemData;
import com.dell.cpsd.storage.capabilities.api.ScaleIOSystemDataRestRep;
import org.junit.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;

/**
 *
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 *
 */
public class DataServiceImplTest {
    DataService dataServiceUnderTest = new DataServiceImpl();

    @Test
    public void getData() {
        UUID jobId = UUID.randomUUID();
        dataServiceUnderTest.saveScaleioData(jobId, new ScaleIOSystemDataRestRep());
        FRUSystemData taskData = dataServiceUnderTest.getData(jobId);
        assertNotNull(taskData);
    }
}
