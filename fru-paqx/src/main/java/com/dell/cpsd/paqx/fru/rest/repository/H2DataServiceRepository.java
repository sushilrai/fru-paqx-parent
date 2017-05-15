/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.rest.repository;

import com.dell.cpsd.paqx.fru.domain.Cluster;
import com.dell.cpsd.paqx.fru.domain.Datacenter;
import com.dell.cpsd.paqx.fru.domain.FruJob;
import com.dell.cpsd.paqx.fru.domain.Host;
import com.dell.cpsd.paqx.fru.domain.ScaleIOData;
import com.dell.cpsd.paqx.fru.domain.ScaleIORoleIP;
import com.dell.cpsd.paqx.fru.domain.ScaleIOSDS;
import com.dell.cpsd.paqx.fru.domain.VCenter;
import com.dell.cpsd.paqx.fru.dto.SDSListDto;
import com.dell.cpsd.paqx.fru.rest.representation.HostRepresentation;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
@Repository
public class H2DataServiceRepository implements DataServiceRepository
{
    private static final Logger LOG = LoggerFactory.getLogger(H2DataServiceRepository.class);

    @PersistenceContext
    EntityManager entityManager;

    @Override
    @Transactional
    public Long saveScaleIOData(final UUID jobId, final ScaleIOData data)
    {
        TypedQuery<FruJob> query = entityManager.createQuery("from FruJob where JOB_ID = :jobid", FruJob.class);

        FruJob fruJob = null;

        try
        {
            fruJob = query.setParameter("jobid", jobId.toString()).getSingleResult();
        }
        catch (NoResultException e)
        {
            LOG.error("No result", e);
        }

        if (fruJob == null)
        {
            fruJob = new FruJob(jobId.toString(), data, null);
            entityManager.persist(fruJob);
        }
        else
        {
            fruJob.setScaleIO(data);
            entityManager.merge(fruJob);
        }
        return fruJob.getUuid();
    }

    @Override
    @Transactional
    public Long saveVCenterData(final UUID jobId, final VCenter data)
    {
        TypedQuery<FruJob> query = entityManager.createQuery("from FruJob where JOB_ID = :jobid", FruJob.class);

        FruJob fruJob = null;

        try
        {
            fruJob = query.setParameter("jobid", jobId.toString()).getSingleResult();
        }
        catch (NoResultException e)
        {
            LOG.error("No result", e);
        }

        if (fruJob == null)
        {
            fruJob = new FruJob(jobId.toString(), null, data);
            entityManager.persist(fruJob);
        }
        else
        {
            fruJob.setVcenter(data);
            entityManager.merge(fruJob);
        }
        return fruJob.getUuid();
    }

    @Override
    public List<Host> getVCenterHosts(final String jobId)
    {
        List<Host> hostList = null;
        TypedQuery<FruJob> query = entityManager.createQuery("from FruJob where JOB_ID = :jobid", FruJob.class);

        FruJob fruJob = null;

        try
        {
            fruJob = query.setParameter("jobid", jobId).getSingleResult();
        }
        catch (NoResultException e)
        {
            LOG.error("No result", e);
        }

        if (fruJob !=null)
        {
            VCenter vCenter = fruJob.getVcenter();

            if (vCenter != null)
            {

                //Now need to find all of the VCenter hosts.
                List<Datacenter> dataCenterList = vCenter.getDatacenterList();

                if (dataCenterList != null)
                {
                    hostList = dataCenterList.stream().filter(Objects::nonNull).map(x -> x.getClusterList()).filter(Objects::nonNull).flatMap(y -> y.stream()).filter(Objects::nonNull).map(z -> z.getHostList()).filter(Objects::nonNull)
                            .flatMap(a -> a.stream()).filter(Objects::nonNull).collect(Collectors.toList());
                }
            }
        }
        return hostList;
    }

    private List<Object[]> correlateSDSDataWithScaleIOData()
    {
        //SDSUUID, VM_IP, VM_ID, HOST_ID
        //SCALEIO_SDS.SDS_UUID
        //SCALEIO_IP_LIST.SDS_IP
        //VM_IP.IP_ADDRESS
        //
        //select sds.sds_uuid, sds.
        //Do the SQL HERE
        String query="SELECT scaleio_ip_list.sds_sds_uuid, vm_ip.ip_address, virtual_machine.vm_id, virtual_machine.host_uuid "
                + "FROM vm_ip JOIN scaleio_ip_list ON scaleio_ip_list.sds_ip = vm_ip.ip_address JOIN vm_guest_network "
                + "ON vm_ip.vmnetwork_uuid = vm_guest_network.uuid "
                + "JOIN virtual_machine ON vm_guest_network.virtualmachine_uuid = virtual_machine.uuid;";
        Query q = entityManager.createNativeQuery("SELECT a.firstname, a.lastname FROM Author a");
        return q.getResultList();

    }


    @Override
    public List<SDSListDto> getScaleIODataForSelectedHost(final String jobId, final HostRepresentation selectedHost)
    {
        for (Object[] columns : correlateSDSDataWithScaleIOData())
        {
            //TODO: Will casting work?

            String sdsUuid = (String)columns[0];
            String vmIpAddress = (String)columns[1];
            String vmId=(String)columns[2];
            String hostUuid=(String)columns[3];

            //TODO:
            //Create SDSListDto (that looks like SIORemoveRequesTMessage) with the following
            //private String userName;
            //private String password;
            //private String servicePortNumber;
            //private String apiPortNumber;
            //and an object containing:
            ////            private String scaleioInterface;
            ////            private String scaleioVolumeName;
            ////            private String mdmHosts;
            ////            private String sdsHosts;
            ////            private String sdcHosts;

            //Create a list of these then return them.


        }

        return null;
    }
}