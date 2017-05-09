/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
public class DistributedVirtualSwitchHostMemberPnicSpecDto {
    private String uplinkPortKey;
    private String portGroupId;
    private String pnicDevice;

    public String getUplinkPortKey() {
        return uplinkPortKey;
    }

    public void setUplinkPortKey(final String uplinkPortKey) {
        this.uplinkPortKey = uplinkPortKey;
    }

    public String getPortGroupId() {
        return portGroupId;
    }

    public void setPortGroupId(final String portGroupId) {
        this.portGroupId = portGroupId;
    }

    public String getPnicDevice() {
        return pnicDevice;
    }

    public void setPnicDevice(final String pnicDevice) {
        this.pnicDevice = pnicDevice;
    }
}
