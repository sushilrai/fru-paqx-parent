/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
public class DistributedVirtualSwicthPortConnectionDto {
    private String portKey;
    private String portGroupId;

    public String getPortKey() {
        return portKey;
    }

    public void setPortKey(final String portKey) {
        this.portKey = portKey;
    }

    public String getPortGroupId() {
        return portGroupId;
    }

    public void setPortGroupId(final String portGroupId) {
        this.portGroupId = portGroupId;
    }
}
