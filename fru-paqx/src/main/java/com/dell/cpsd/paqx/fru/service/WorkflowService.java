/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.service;

import com.dell.cpsd.paqx.fru.rest.domain.Job;
import com.dell.cpsd.paqx.fru.valueobject.NextStep;

import java.util.UUID;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
public interface WorkflowService {
    Job createWorkflow(String workflowType);

    NextStep findNextStep(String workflowType, String currentStep);

    Job[] findActiveJobs();

    Job findJob(UUID jobId);

    Job advanceToNextStep(Job job, String currentStep);
}
