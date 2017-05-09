package com.dell.cpsd.paqx.fru.rest.dto;

import com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery.DataCenterDto;

import java.util.List;

/**
 * Created by kenefj on 21/04/17.
 */
public class vCenterSystemProperties {
    private List<DataCenterDto> dataCenters;

    public void setDataCenters(final List<DataCenterDto> dataCenters) {
        this.dataCenters = dataCenters;
    }
}
