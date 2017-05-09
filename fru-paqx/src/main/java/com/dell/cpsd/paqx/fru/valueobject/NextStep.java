/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.valueobject;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
public class NextStep
{
    private final String  nextStep;
    private final boolean isFinalStep;

    public NextStep(final String nextStep)
    {
        this(nextStep, false);
    }

    public NextStep(final String nextStep, final boolean isFinalStep)
    {
        this.nextStep = nextStep;
        this.isFinalStep = isFinalStep;
    }

    public String getNextStep()
    {
        return nextStep;
    }

    public boolean isFinalStep()
    {
        return isFinalStep;
    }
}
