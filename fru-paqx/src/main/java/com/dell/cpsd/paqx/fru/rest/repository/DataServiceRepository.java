/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
package com.dell.cpsd.paqx.fru.rest.repository;

import com.dell.cpsd.paqx.fru.domain.ScaleIOData;
import com.dell.cpsd.paqx.fru.domain.VCenter;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
public interface DataServiceRepository
{
    @Transactional
    Long saveScaleIOData(UUID jobId, ScaleIOData data);

    @Transactional
    Long saveVCenterData(UUID jobId, VCenter data);
}
