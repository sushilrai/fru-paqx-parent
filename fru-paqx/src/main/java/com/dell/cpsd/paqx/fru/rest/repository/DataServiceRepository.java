package com.dell.cpsd.paqx.fru.rest.repository;

import com.dell.cpsd.paqx.fru.domain.ScaleIOData;

import java.util.UUID;

/**
 * Created by kenefj on 10/05/2017.
 */
public interface DataServiceRepository
{
    void saveScaleIOData(UUID jobId, ScaleIOData data);
}
