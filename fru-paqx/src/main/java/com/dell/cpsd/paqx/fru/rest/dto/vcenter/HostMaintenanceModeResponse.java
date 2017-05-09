package com.dell.cpsd.paqx.fru.rest.dto.vcenter;

/**
 * TODO: Document usage.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * </p>
 *
 * @version 1.0
 * @since 1.0
 */
public class HostMaintenanceModeResponse
{
    private final String status;

    public HostMaintenanceModeResponse(final String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    @Override
    public String toString()
    {
        return "HostMaintenanceModeResponse{" + "status='" + status + '\'' + '}';
    }
}
