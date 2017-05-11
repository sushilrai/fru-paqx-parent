package com.dell.cpsd.paqx.fru.rest.repository;

import com.dell.cpsd.paqx.fru.domain.ScaleIOData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.UUID;

/**
 * Created by kenefj on 10/05/2017.
 */
@Repository
public class H2DataServiceRepository implements DataServiceRepository
{
    @PersistenceContext
    EntityManager entityManager;

    @Override
    @Transactional
    public void saveScaleIOData(final UUID jobId, final ScaleIOData data)
    {
        entityManager.persist(data);
    }
}
