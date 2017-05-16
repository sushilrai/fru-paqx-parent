/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.service;


import com.dell.cpsd.paqx.fru.domain.Host;
import com.dell.cpsd.paqx.fru.domain.ScaleIOData;
import com.dell.cpsd.paqx.fru.dto.FRUSystemData;
import com.dell.cpsd.paqx.fru.dto.ScaleIORemoveDto;
import com.dell.cpsd.paqx.fru.rest.dto.vCenterSystemProperties;
import com.dell.cpsd.paqx.fru.rest.repository.DataServiceRepository;
import com.dell.cpsd.paqx.fru.rest.representation.HostRepresentation;
import com.dell.cpsd.paqx.fru.transformers.DiscoveryInfoToVCenterSystemPropertiesTransformer;
import com.dell.cpsd.paqx.fru.transformers.HostListToHostRepresentationTransformer;
import com.dell.cpsd.paqx.fru.transformers.SDSListDtoToRemoveScaleIOMessageTransformer;
import com.dell.cpsd.paqx.fru.transformers.ScaleIORestToScaleIODomainTransformer;
import com.dell.cpsd.storage.capabilities.api.SIONodeRemoveRequestMessage;
import com.dell.cpsd.storage.capabilities.api.ScaleIOSystemDataRestRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
@Service
public class DataServiceImpl implements DataService {

    private final Map<UUID, FRUSystemData> jobIdFRUSystemData = new HashMap<>();

    @Autowired
    DataServiceRepository repository;

    @Autowired
    ScaleIORestToScaleIODomainTransformer scaleIORestToScaleIODomainTransformer;

    @Autowired
    private DiscoveryInfoToVCenterSystemPropertiesTransformer discoveryInfoToVCenterSystemPropertiesTransformer;

    @Autowired
    HostListToHostRepresentationTransformer hostListToHostRepresentationTransformer;

    @Autowired
    private SDSListDtoToRemoveScaleIOMessageTransformer sdsListDtoToRemoveScaleIOMessageTransformer;

    @Autowired
    public DataServiceImpl(DataServiceRepository repository, ScaleIORestToScaleIODomainTransformer scaleIORestToScaleIODomainTransformer, DiscoveryInfoToVCenterSystemPropertiesTransformer discoveryInfoToVCenterSystemPropertiesTransformer,
            HostListToHostRepresentationTransformer hostListToHostRepresentationTransformer, SDSListDtoToRemoveScaleIOMessageTransformer sdsListDtoToRemoveScaleIOMessageTransformer)
    {
        this.repository=repository;
        this.scaleIORestToScaleIODomainTransformer=scaleIORestToScaleIODomainTransformer;
        this.discoveryInfoToVCenterSystemPropertiesTransformer=discoveryInfoToVCenterSystemPropertiesTransformer;
        this.hostListToHostRepresentationTransformer=hostListToHostRepresentationTransformer;
        this.sdsListDtoToRemoveScaleIOMessageTransformer = sdsListDtoToRemoveScaleIOMessageTransformer;
    }

    @Override
    public FRUSystemData getData(UUID jobId) {
        return ensureSystemDataExists(jobId);
    }

    @Override
    public void saveScaleioData(final UUID jobId, final ScaleIOSystemDataRestRep scaleIOSystemDataRestRep) {
        ScaleIOData data = scaleIORestToScaleIODomainTransformer.transform(scaleIOSystemDataRestRep);
        repository.saveScaleIOData(jobId,data);
        FRUSystemData fruSystemData = ensureSystemDataExists(jobId);
        fruSystemData.setScaleIOData(scaleIOSystemDataRestRep);
    }

    @Override
    public void saveVcenterData(UUID jobId, vCenterSystemProperties vcenterSystemProperties) {
        FRUSystemData fruSystemData = ensureSystemDataExists(jobId);
        fruSystemData.setvCenterSystem(vcenterSystemProperties);
    }

    @Override
    public List<HostRepresentation> getVCenterHosts(String jobId)
    {
        List<Host> hostList=repository.getVCenterHosts(jobId);
        return hostListToHostRepresentationTransformer.transform(hostList);
    }

    @Override
    public SIONodeRemoveRequestMessage getSDSHostsToRemoveFromHostRepresentation(String jobId, HostRepresentation selectedHost,
            final String scaleIOEndpoint, final String scaleIOPassword, final String scaleIOUserName)
    {
       ScaleIORemoveDto hostList=repository.getScaleIORemoveDtoForSelectedHost(jobId, selectedHost, scaleIOUserName, scaleIOPassword, scaleIOEndpoint);
        return sdsListDtoToRemoveScaleIOMessageTransformer.transform(hostList);
    }

    private FRUSystemData ensureSystemDataExists(UUID jobId) {
        FRUSystemData fruSystemData = jobIdFRUSystemData.get(jobId);
        if (fruSystemData == null) {
            fruSystemData = new FRUSystemData();
            jobIdFRUSystemData.put(jobId, fruSystemData);
        }
        return fruSystemData;
    }
}
