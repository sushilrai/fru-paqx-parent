/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.dto;

import com.dell.cpsd.paqx.fru.rest.dto.vCenterSystemProperties;
import com.dell.cpsd.storage.capabilities.api.ScaleIOSystemDataRestRep;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
@XmlRootElement
public class FRUSystemData {
    @XmlElement
    private ScaleIOSystemDataRestRep scaleIOData;

    @XmlElement
    private vCenterSystemProperties vCenterSystem;

    public ScaleIOSystemDataRestRep getScaleIOData() {
        return scaleIOData;
    }

    public void setScaleIOData(final ScaleIOSystemDataRestRep scaleIOData) {
        this.scaleIOData = scaleIOData;
    }

    public vCenterSystemProperties getvCenterSystem() {
        return vCenterSystem;
    }

    public void setvCenterSystem(final vCenterSystemProperties vCenterSystem) {
        this.vCenterSystem = vCenterSystem;
    }
}
