/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.service;


import com.dell.cpsd.paqx.fru.dto.FRUSystemData;
import com.dell.cpsd.paqx.fru.rest.dto.vCenterSystemProperties;
import com.dell.cpsd.storage.capabilities.api.ScaleIOSystemDataRestRep;

import java.util.UUID;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
public interface DataService {
    FRUSystemData getData(UUID jobId);

    void saveScaleioData(final UUID jobId, final ScaleIOSystemDataRestRep scaleIOSystemDataRestRep);

    void saveVcenterData(UUID jobId, vCenterSystemProperties vcenterSystemProperties);

}
