/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.rest.repository;

import com.dell.cpsd.paqx.fru.domain.Datacenter;
import com.dell.cpsd.paqx.fru.domain.FruJob;
import com.dell.cpsd.paqx.fru.domain.ScaleIOData;
import com.dell.cpsd.paqx.fru.domain.VCenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.UUID;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
@Repository
public class H2DataServiceRepository implements DataServiceRepository
{
    private static final Logger LOG = LoggerFactory.getLogger(H2DataServiceRepository.class);

    @PersistenceContext
    EntityManager entityManager;

    @Override
    @Transactional
    public Long saveScaleIOData(final UUID jobId, final ScaleIOData data)
    {
        TypedQuery<FruJob> query = entityManager.createQuery("from FruJob where JOB_ID = :jobid", FruJob.class);

        FruJob fruJob = null;

        try
        {
            fruJob = query.setParameter("jobid", jobId.toString()).getSingleResult();
        }
        catch (NoResultException e)
        {
            LOG.error("No result", e);
        }

        if (fruJob == null)
        {
            fruJob = new FruJob(jobId.toString(), data, null);
            entityManager.persist(fruJob);
        }
        else
        {
            fruJob.setScaleIO(data);
            entityManager.merge(fruJob);
        }
        return fruJob.getUuid();
    }

    @Override
    @Transactional
    public Long saveVCenterData(final UUID jobId, final VCenter data)
    {
        TypedQuery<FruJob> query = entityManager.createQuery("from FruJob where JOB_ID = :jobid", FruJob.class);

        FruJob fruJob = null;

        try
        {
            fruJob = query.setParameter("jobid", jobId.toString()).getSingleResult();
        }
        catch (NoResultException e)
        {
            LOG.error("No result", e);
        }

        if (fruJob == null)
        {
            fruJob = new FruJob(jobId.toString(), null, data);
            entityManager.persist(fruJob);
        }
        else
        {
            fruJob.setVcenter(data);
            entityManager.merge(fruJob);
        }
        return fruJob.getUuid();
    }
}