/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.rest.domain;

import com.dell.cpsd.paqx.fru.rest.dto.EndpointCredentials;

import java.util.UUID;

/**
 * Workflow step.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 */
public class Job {
    private UUID id;
    private String workflow;
    private String currentStep;
    private int currentStepNumber;
    private EndpointCredentials rackhdCredentials;
    private EndpointCredentials coprhdCredentials;
    private EndpointCredentials vcenterCredentials;
    private EndpointCredentials scaleIOCredentials;

    public Job(final UUID id, final String workflow, final String currentStep) {
        this.id = id;
        this.workflow = workflow;
        this.currentStep = currentStep;
        this.currentStepNumber = 1;
    }

    public UUID getId() {
        return id;
    }

    public String getCurrentStep() {
        return currentStep;
    }

    public String getWorkflow() {
        return workflow;
    }

    public void changeToNextStep(final String nextStep) {
        currentStep = nextStep;
    }

    public int getCurrentStepNumber() {
        return currentStepNumber;
    }

    public void addRackhdCredentials(final EndpointCredentials rackhdCredentials) {
        this.rackhdCredentials = rackhdCredentials;
    }

    public EndpointCredentials getRackhdCredentials() {
        return rackhdCredentials;
    }

    public EndpointCredentials getCoprhdCredentials() {
        return coprhdCredentials;
    }

    public void addCoprhdCredentials(final EndpointCredentials coprhdCredentials) {
        this.coprhdCredentials = coprhdCredentials;
    }

    public void addScaleIOCredentials(final EndpointCredentials scaleIOCredentials) {
        this.scaleIOCredentials = scaleIOCredentials;
    }

    public EndpointCredentials getScaleIOCredentials() {
        return scaleIOCredentials;
    }

    public EndpointCredentials getVcenterCredentials() {
        return vcenterCredentials;
    }

    public void addVcenterCredentials(final EndpointCredentials vcenterCredentials) {
        this.vcenterCredentials = vcenterCredentials;
    }
}
