/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.service;


import com.dell.cpsd.paqx.fru.dto.FRUSystemData;
import com.dell.cpsd.paqx.fru.rest.dto.vCenterSystemProperties;
import com.dell.cpsd.storage.capabilities.api.ScaleIOSystemDataRestRep;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
@Service
public class DataServiceImpl implements DataService {

    private final Map<UUID, FRUSystemData> jobIdFRUSystemData = new HashMap<>();

    @Override
    public FRUSystemData getData(UUID jobId) {
        return ensureSystemDataExists(jobId);
    }

    @Override
    public void saveScaleioData(final UUID jobId, final ScaleIOSystemDataRestRep scaleIOSystemDataRestRep) {
        FRUSystemData fruSystemData = ensureSystemDataExists(jobId);
        fruSystemData.setScaleIOData(scaleIOSystemDataRestRep);
    }

    @Override
    public void saveVcenterData(UUID jobId, vCenterSystemProperties vcenterSystemProperties) {
        FRUSystemData fruSystemData = ensureSystemDataExists(jobId);
        fruSystemData.setvCenterSystem(vcenterSystemProperties);
    }

    private FRUSystemData ensureSystemDataExists(UUID jobId) {
        FRUSystemData fruSystemData = jobIdFRUSystemData.get(jobId);
        if (fruSystemData == null) {
            fruSystemData = new FRUSystemData();
            jobIdFRUSystemData.put(jobId, fruSystemData);
        }
        return fruSystemData;
    }
}
