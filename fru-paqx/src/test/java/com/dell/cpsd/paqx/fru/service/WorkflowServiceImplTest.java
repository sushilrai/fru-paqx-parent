/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.service;

import com.dell.cpsd.paqx.fru.rest.domain.Job;
import com.dell.cpsd.paqx.fru.rest.repository.InMemoryJobRepository;
import com.dell.cpsd.paqx.fru.valueobject.NextStep;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
public class WorkflowServiceImplTest
{
    InMemoryJobRepository inMemoryJobRepository = new InMemoryJobRepository();
    WorkflowServiceImpl workflowServiceUnderTest = new WorkflowServiceImpl(inMemoryJobRepository);

    @Test
    public void firstQuantaStepIsInitial() {
        Job initialJob = workflowServiceUnderTest.createWorkflow("quanta-d51b-fru");
        assertNotNull(initialJob);
        assertNotNull(initialJob.getId());
    }

    @Test
    public void walkExpectedSteps() {
        Job initialJob = workflowServiceUnderTest.createWorkflow("quanta-d51b-fru");

        NextStep nextStep = workflowServiceUnderTest.findNextStep("quanta-d51b-fru", initialJob.getCurrentStep());
        assertNotNull(nextStep);

    }
}
