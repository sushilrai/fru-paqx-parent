/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.rest.repository;

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
public class InMemoryJobRepository implements JobRepository {
    private final Map<UUID, Job> jobs = new HashMap<>();

    @Override
    public void save(final Job job) {
        jobs.put(job.getId(), job);
    }

    @Override
    public Job[] findAll() {
        Job[] results = new Job[jobs.size()];
        return jobs.values().toArray(results);
    }

    @Override
    public Job find(final UUID jobId) {
        return jobs.get(jobId);
    }
}
