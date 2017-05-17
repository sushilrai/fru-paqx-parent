/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.rest;

import com.dell.cpsd.paqx.fru.dto.ConsulRegistryResult;
import com.dell.cpsd.paqx.fru.rest.domain.Job;
import com.dell.cpsd.paqx.fru.rest.dto.EndpointCredentials;
import com.dell.cpsd.paqx.fru.rest.dto.StartWorkflowRequest;
import com.dell.cpsd.paqx.fru.rest.dto.VCenterHostPowerOperationStatus;
import com.dell.cpsd.paqx.fru.rest.dto.vCenterSystemProperties;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.ClusterOperationResponse;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.DestroyVmResponse;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.HostMaintenanceModeResponse;
import com.dell.cpsd.paqx.fru.rest.representation.JobRepresentation;
import com.dell.cpsd.paqx.fru.service.DataService;
import com.dell.cpsd.paqx.fru.service.ScaleIOService;
import com.dell.cpsd.paqx.fru.service.WorkflowService;
import com.dell.cpsd.paqx.fru.service.vCenterService;
import com.dell.cpsd.paqx.fru.valueobject.NextStep;
import com.dell.cpsd.storage.capabilities.api.ScaleIOSystemDataRestRep;
import com.dell.cpsd.virtualization.capabilities.api.ClusterOperationResponseMessage;
import com.dell.cpsd.virtualization.capabilities.api.DestroyVMResponseMessage;
import com.dell.cpsd.virtualization.capabilities.api.HostMaintenanceModeResponseMessage;
import com.dell.cpsd.virtualization.capabilities.api.HostPowerOperationResponseMessage;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Workflow resource.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 */
@Api
@Component
@Path("/workflow")
@Produces(MediaType.APPLICATION_JSON)
public class WorkflowResource {
    private static final Logger LOG = LoggerFactory.getLogger(WorkflowResource.class);

    private final WorkflowService workflowService;
    private final ScaleIOService scaleIOService;
    private final vCenterService vcenterService;
    private final DataService dataService;

    @Autowired
    public WorkflowResource(final WorkflowService workflowService, final ScaleIOService scaleIOService, final vCenterService vCenterService,
                            final DataService dataService) {
        this.workflowService = workflowService;
        this.scaleIOService = scaleIOService;
        this.vcenterService = vCenterService;
        this.dataService = dataService;
    }

    /**
     * Lists all active workflows
     *
     * @return
     */
    @GET
    public Response listActiveWorkflows(@Context UriInfo uriInfo) {
        final Job[] activeJobs = workflowService.findActiveJobs();

        List<Link> links = new ArrayList<>();
        for (final Job activeJob : activeJobs) {
            final Link link = Link.fromUriBuilder(uriInfo.getBaseUriBuilder().path("workflow").path(activeJob.getId().toString())).build();

            links.add(link);
        }

        return Response.ok().links(links.toArray(new Link[]{})).build();
    }

    /**
     * TODO: Currently a mock interface
     *
     * @param startWorkflowRequest
     * @param uriInfo
     * @return
     */
    @POST
    public Response startWorkflow(StartWorkflowRequest startWorkflowRequest, @Context UriInfo uriInfo) {
        final Job job = workflowService.createWorkflow(startWorkflowRequest.getWorkflow());
        final JobRepresentation jobRepresentation = new JobRepresentation(job);

        final NextStep nextStep = workflowService.findNextStep(job.getWorkflow(), job.getCurrentStep());

        if (nextStep != null)
        {
            workflowService.advanceToNextStep(job, "workflowInitiated");
            jobRepresentation.addLink(createNextStepLink(uriInfo, job, nextStep.getNextStep()));
        }

        return Response.created(uriInfo.getBaseUriBuilder().path("workflow").path(job.getId().toString()).build()).entity(jobRepresentation)
                .build();
    }

    @GET
    @Path("{jobId}")
    public Response getJob(@PathParam("jobId") String jobId, @Context UriInfo uriInfo) {
        final Job job = workflowService.findJob(UUID.fromString(jobId));
        final JobRepresentation jobRepresentation = new JobRepresentation(job);
        if (!"completed".equals(job.getCurrentStep())) {
            jobRepresentation.addLink(createNextStepLink(uriInfo, job, job.getCurrentStep()), findMethodFromStep((job.getCurrentStep())));
        }

        return Response.ok(jobRepresentation).build();
    }

    @POST
    @Path("{jobId}/{step}")
    public Response step(@PathParam("jobId") String jobId, @PathParam("step") String step, @Context UriInfo uriInfo) {
        final String thisStep = findStepFromPath(uriInfo);
        final Job job = workflowService.findJob(UUID.fromString(jobId));
        final JobRepresentation jobRepresentation = new JobRepresentation(job);

        jobRepresentation.addLink(createRetryStepLink(uriInfo, job, thisStep));

        // TODO : place holder for logic steps

        final NextStep nextStep = workflowService.findNextStep(job.getWorkflow(), thisStep);
        if (nextStep != null) {
            workflowService.advanceToNextStep(job, thisStep);
            jobRepresentation.addLink(createNextStepLink(uriInfo, job, nextStep.getNextStep()), findMethodFromStep(nextStep.getNextStep()));
        }

        return Response.ok(jobRepresentation).build();
    }

    @POST
    @Consumes("application/vnd.dellemc.rackhd.endpoint+json")
    @Path("{jobId}/{step}")
    public Response captureRackHD(@PathParam("jobId") String jobId, @PathParam("step") String step, @Context UriInfo uriInfo,
                                  EndpointCredentials rackhdCredentials) {
        final String thisStep = findStepFromPath(uriInfo);
        final Job job = workflowService.findJob(UUID.fromString(jobId));
        final JobRepresentation jobRepresentation = new JobRepresentation(job);

        final URL url;
        try {
            url = new URL(rackhdCredentials.getEndpointUrl());
        } catch (MalformedURLException e) {
            jobRepresentation.addLink(createRetryStepLink(uriInfo, job, thisStep));
            return Response.status(Response.Status.BAD_REQUEST).entity(jobRepresentation).build();
        }

        job.addRackhdCredentials(rackhdCredentials);

        final NextStep nextStep = workflowService.findNextStep(job.getWorkflow(), thisStep);
        if (nextStep != null) {
            workflowService.advanceToNextStep(job, thisStep);
            jobRepresentation.addLink(createNextStepLink(uriInfo, job, nextStep.getNextStep()), findMethodFromStep(nextStep.getNextStep()));
        }

        return Response.ok(jobRepresentation).build();
    }

    @POST
    @Consumes("application/vnd.dellemc.coprhd.endpoint+json")
    @Path("{jobId}/{step}")
    public void captureCoprHD(@Suspended final AsyncResponse asyncResponse, @PathParam("jobId") String jobId,
            @PathParam("step") String step, @Context UriInfo uriInfo, EndpointCredentials coprhdCredentials) {
        asyncResponse.setTimeoutHandler(asyncResponse1 -> asyncResponse1
                .resume(Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("{\"status\":\"timeout\"}").build()));
        asyncResponse.setTimeout(10, TimeUnit.SECONDS);

        final String thisStep = findStepFromPath(uriInfo);
        final Job job = workflowService.findJob(UUID.fromString(jobId));
        final JobRepresentation jobRepresentation = new JobRepresentation(job);

        try {
            new URL(coprhdCredentials.getEndpointUrl());
        } catch (MalformedURLException e) {
            LOG.warn("Invalid URL found {}", coprhdCredentials.getEndpointUrl());
            jobRepresentation.addLink(createRetryStepLink(uriInfo, job, thisStep));
            jobRepresentation.setLastResponse(e.getLocalizedMessage());
            Response.status(Response.Status.BAD_REQUEST).entity(jobRepresentation).build();
            return;
        }

        final CompletableFuture<ConsulRegistryResult> consulRegistryResultCompletableFuture = scaleIOService
                .requestConsulRegistration(coprhdCredentials);
        consulRegistryResultCompletableFuture.thenAccept(consulRegistryResult ->
        {
            if (consulRegistryResult.isSuccess()) {
                LOG.info("Consul registration successfully completed");

                job.addCoprhdCredentials(coprhdCredentials);

                final NextStep nextStep = workflowService.findNextStep(job.getWorkflow(), thisStep);
                if (nextStep != null) {
                    workflowService.advanceToNextStep(job, thisStep);
                    jobRepresentation
                            .addLink(createNextStepLink(uriInfo, job, nextStep.getNextStep()), findMethodFromStep(nextStep.getNextStep()));
                }
                asyncResponse.resume(Response.ok(jobRepresentation).build());
            } else {
                LOG.info("Consul registration failed {}", consulRegistryResult.getDescription());
                jobRepresentation.addLink(createRetryStepLink(uriInfo, job, thisStep));
                jobRepresentation.setLastResponse(consulRegistryResult.getDescription());
                asyncResponse.resume(Response.status(Response.Status.BAD_REQUEST).entity(jobRepresentation).build());
            }
        });
    }

    @POST
    @Consumes("application/vnd.dellemc.vcenter.endpoint+json")
    @Path("{jobId}/{step}")
    public void capturevCenter(@Suspended final AsyncResponse asyncResponse, @PathParam("jobId") String jobId,
                               @PathParam("step") String step, @Context UriInfo uriInfo, EndpointCredentials vCenterCredentials) {
        asyncResponse.setTimeoutHandler(asyncResponse1 -> asyncResponse1
                .resume(Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("{\"status\":\"timeout\"}").build()));
        asyncResponse.setTimeout(10, TimeUnit.SECONDS);

        final String thisStep = findStepFromPath(uriInfo);
        final Job job = workflowService.findJob(UUID.fromString(jobId));
        final JobRepresentation jobRepresentation = new JobRepresentation(job);

        try {
            new URL(vCenterCredentials.getEndpointUrl());
        } catch (MalformedURLException e) {
            LOG.warn("Invalid URL found {}", vCenterCredentials.getEndpointUrl());
            jobRepresentation.addLink(createRetryStepLink(uriInfo, job, thisStep));
            jobRepresentation.setLastResponse(e.getLocalizedMessage());
            Response.status(Response.Status.BAD_REQUEST).entity(jobRepresentation).build();
            return;
        }

        final CompletableFuture<ConsulRegistryResult> consulRegistryResultCompletableFuture = vcenterService
                .requestConsulRegistration(vCenterCredentials);
        consulRegistryResultCompletableFuture.thenAccept(consulRegistryResult ->
        {
            if (consulRegistryResult.isSuccess()) {
                LOG.info("Consul registration successfully completed");

                job.addVcenterCredentials(vCenterCredentials);

                final NextStep nextStep = workflowService.findNextStep(job.getWorkflow(), thisStep);
                if (nextStep != null) {
                    workflowService.advanceToNextStep(job, thisStep);
                    jobRepresentation
                            .addLink(createNextStepLink(uriInfo, job, nextStep.getNextStep()), findMethodFromStep(nextStep.getNextStep()));
                }
                asyncResponse.resume(Response.ok(jobRepresentation).build());
            } else {
                LOG.info("Consul registration failed {}", consulRegistryResult.getDescription());
                jobRepresentation.addLink(createRetryStepLink(uriInfo, job, thisStep));
                jobRepresentation.setLastResponse(consulRegistryResult.getDescription());
                asyncResponse.resume(Response.status(Response.Status.BAD_REQUEST).entity(jobRepresentation).build());
            }
        });
    }

    @POST
    @Consumes("application/vnd.dellemc.scaleio.endpoint+json")
    @Path("{jobId}/{step}")
    public Response captureScaleIO(@PathParam("jobId") String jobId, @PathParam("step") String step, @Context UriInfo uriInfo,
            EndpointCredentials scaleIOCredentials) {
        final String thisStep = findStepFromPath(uriInfo);
        final Job job = workflowService.findJob(UUID.fromString(jobId));
        final JobRepresentation jobRepresentation = new JobRepresentation(job);

        final URL url;
        try {
            url = new URL(scaleIOCredentials.getEndpointUrl());
        } catch (MalformedURLException e) {
            jobRepresentation.addLink(createRetryStepLink(uriInfo, job, thisStep));
            return Response.status(Response.Status.BAD_REQUEST).entity(jobRepresentation).build();
        }

        job.addScaleIOCredentials(scaleIOCredentials);

        final NextStep nextStep = workflowService.findNextStep(job.getWorkflow(), thisStep);
        if (nextStep != null) {
            workflowService.advanceToNextStep(job, thisStep);
            jobRepresentation.addLink(createNextStepLink(uriInfo, job, nextStep.getNextStep()), findMethodFromStep(nextStep.getNextStep()));
        }

        return Response.ok(jobRepresentation).build();
    }

    @POST
    @Path("{jobId}/setup-symphony")
    public void setupSymphony(@Suspended final AsyncResponse asyncResponse, @PathParam("jobId") String jobId, @Context UriInfo uriInfo) {
        asyncResponse.setTimeoutHandler(asyncResponse1 -> asyncResponse1
                .resume(Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("{\"status\":\"timeout\"}").build()));
        asyncResponse.setTimeout(10, TimeUnit.SECONDS);

        //
        final String thisStep = findStepFromPath(uriInfo);
        final Job job = workflowService.findJob(UUID.fromString(jobId));
        final JobRepresentation jobRepresentation = new JobRepresentation(job);

        final NextStep nextStep = workflowService.findNextStep(job.getWorkflow(), thisStep);
        if (nextStep != null) {
            workflowService.advanceToNextStep(job, thisStep);
            jobRepresentation.addLink(createNextStepLink(uriInfo, job, nextStep.getNextStep()), findMethodFromStep(nextStep.getNextStep()));
        }

        LOG.info("Completing response");
        asyncResponse.resume(Response.ok(jobRepresentation).build());
        LOG.debug("Completed response");
    }

    @POST
    @Path("{jobId}/start-scaleio-data-collection")
    public void discoverScaleIO(@Suspended final AsyncResponse asyncResponse, @PathParam("jobId") String jobId, @Context UriInfo uriInfo) {
        asyncResponse.setTimeoutHandler(asyncResponse1 -> asyncResponse1
                .resume(Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("{\"status\":\"timeout\"}").build()));
        asyncResponse.setTimeout(10, TimeUnit.SECONDS);

        //
        final String thisStep = findStepFromPath(uriInfo);
        final Job job = workflowService.findJob(UUID.fromString(jobId));
        final JobRepresentation jobRepresentation = new JobRepresentation(job);

        jobRepresentation.addLink(createRetryStepLink(uriInfo, job, thisStep));
        //

        final CompletableFuture<ScaleIOSystemDataRestRep> systemRestCompletableFuture = scaleIOService
                .listStorage(job.getScaleIOCredentials());
        systemRestCompletableFuture.thenAccept(scaleIOSystemDataRestRep ->
        {
            dataService.saveScaleioData(UUID.fromString(jobId), scaleIOSystemDataRestRep);

            final NextStep nextStep = workflowService.findNextStep(job.getWorkflow(), thisStep);
            if (nextStep != null) {
                workflowService.advanceToNextStep(job, thisStep);
                jobRepresentation
                        .addLink(createNextStepLink(uriInfo, job, nextStep.getNextStep()), findMethodFromStep(nextStep.getNextStep()));
            }

            LOG.info("Completing response");
            asyncResponse.resume(Response.ok(jobRepresentation).build());
            LOG.debug("Completed response");
        });
    }

    @POST
    @Path("{jobId}/start-vcenter-data-collection")
    public void discovervCenter(@Suspended final AsyncResponse asyncResponse, @PathParam("jobId") String jobId, @Context UriInfo uriInfo) {
        asyncResponse.setTimeoutHandler(asyncResponse1 -> asyncResponse1
                .resume(Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("{\"status\":\"timeout\"}").build()));
        asyncResponse.setTimeout(60, TimeUnit.SECONDS);

        final String thisStep = findStepFromPath(uriInfo);
        final Job job = workflowService.findJob(UUID.fromString(jobId));
        final JobRepresentation jobRepresentation = new JobRepresentation(job);

        jobRepresentation.addLink(createRetryStepLink(uriInfo, job, thisStep));

        final CompletableFuture<vCenterSystemProperties> vcenterSystemCompletableFuture = vcenterService
                .showSystem(job.getVcenterCredentials());
        vcenterSystemCompletableFuture.thenAccept(vCenterSystemProperties ->
        {
            // TODO: Enable the raw storage object with correlation
            dataService.saveVcenterData(UUID.fromString(jobId), vCenterSystemProperties);

            final NextStep nextStep = workflowService.findNextStep(job.getWorkflow(), thisStep);
            if (nextStep != null) {
                workflowService.advanceToNextStep(job, thisStep);
                jobRepresentation
                        .addLink(createNextStepLink(uriInfo, job, nextStep.getNextStep()), findMethodFromStep(nextStep.getNextStep()));
            }

            LOG.info("Completing response");
            asyncResponse.resume(Response.ok(jobRepresentation).build());
            LOG.debug("Completed response");
        });
    }

    @POST
    @Path("{jobId}/present-system-list-remove")
    public void presentSystemListForRemoval(@Suspended final AsyncResponse asyncResponse, @PathParam("jobId") String jobId,
                                            @Context UriInfo uriInfo) {
        asyncResponse.setTimeoutHandler(asyncResponse1 -> asyncResponse1
                .resume(Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("{\"status\":\"timeout\"}").build()));
        asyncResponse.setTimeout(10, TimeUnit.SECONDS);

        //
        final String thisStep = findStepFromPath(uriInfo);
        final Job job = workflowService.findJob(UUID.fromString(jobId));
        final JobRepresentation jobRepresentation = new JobRepresentation(job);

        final NextStep nextStep = workflowService.findNextStep(job.getWorkflow(), thisStep);
        if (nextStep != null) {
            workflowService.advanceToNextStep(job, thisStep);
            jobRepresentation.addLink(createNextStepLink(uriInfo, job, nextStep.getNextStep()), findMethodFromStep(nextStep.getNextStep()));
        }

        LOG.info("Completing response");
        asyncResponse.resume(Response.ok(jobRepresentation).build());
        LOG.debug("Completed response");
    }

    @POST
    @Path("{jobId}/start-scaleio-remove-workflow")
    public void scaleioRemoveWorkflow(@Suspended final AsyncResponse asyncResponse, @PathParam("jobId") String jobId,
                                      @Context UriInfo uriInfo) {
        asyncResponse.setTimeoutHandler(asyncResponse1 -> asyncResponse1
                .resume(Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("{\"status\":\"timeout\"}").build()));
        asyncResponse.setTimeout(10, TimeUnit.SECONDS);

        //
        final String thisStep = findStepFromPath(uriInfo);
        final Job job = workflowService.findJob(UUID.fromString(jobId));
        final JobRepresentation jobRepresentation = new JobRepresentation(job);

        final NextStep nextStep = workflowService.findNextStep(job.getWorkflow(), thisStep);
        if (nextStep != null) {
            workflowService.advanceToNextStep(job, thisStep);
            jobRepresentation.addLink(createNextStepLink(uriInfo, job, nextStep.getNextStep()), findMethodFromStep(nextStep.getNextStep()));
        }

        LOG.info("Completing response");
        asyncResponse.resume(Response.ok(jobRepresentation).build());
        LOG.debug("Completed response");
    }

    @POST
    @Path("{jobId}/wait-for-scaleio-workflow")
    public void waitForScaleIOWorkflow(@Suspended final AsyncResponse asyncResponse, @PathParam("jobId") String jobId,
                                       @Context UriInfo uriInfo) {
        asyncResponse.setTimeoutHandler(asyncResponse1 -> asyncResponse1
                .resume(Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("{\"status\":\"timeout\"}").build()));
        asyncResponse.setTimeout(10, TimeUnit.SECONDS);

        //
        final String thisStep = findStepFromPath(uriInfo);
        final Job job = workflowService.findJob(UUID.fromString(jobId));
        final JobRepresentation jobRepresentation = new JobRepresentation(job);

        final NextStep nextStep = workflowService.findNextStep(job.getWorkflow(), thisStep);
        if (nextStep != null) {
            workflowService.advanceToNextStep(job, thisStep);
            jobRepresentation.addLink(createNextStepLink(uriInfo, job, nextStep.getNextStep()), findMethodFromStep(nextStep.getNextStep()));
        }

        LOG.info("Completing response");
        asyncResponse.resume(Response.ok(jobRepresentation).build());
        LOG.debug("Completed response");
    }

    @POST
    @Path("{jobId}/destroy-scaleio-vm")
    public void destroyScaleIOVM(@Suspended final AsyncResponse asyncResponse, @PathParam("jobId") String jobId, @Context UriInfo uriInfo) {
        asyncResponse.setTimeoutHandler(asyncResponse1 -> asyncResponse1
                .resume(Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("{\"status\":\"timeout\"}").build()));
        asyncResponse.setTimeout(10, TimeUnit.SECONDS);

        //
        final String thisStep = findStepFromPath(uriInfo);
        final Job job = workflowService.findJob(UUID.fromString(jobId));
        final JobRepresentation jobRepresentation = new JobRepresentation(job);

        //TODO find out where to get the uuid from
        //TODO Update ScaleIO Host status
        final String uuid = "";

        final CompletableFuture<DestroyVmResponse> vmDeletionResponse = vcenterService.requestVmDeletion(job.getVcenterCredentials(), uuid);
        vmDeletionResponse.thenAccept(destroyVmResponse ->
        {
            LOG.info("Destroy VM Response Message Status {}", destroyVmResponse);
            if (DestroyVMResponseMessage.Status.SUCCESS.value().equals(destroyVmResponse.getStatus())) {
                final NextStep nextStep = workflowService.findNextStep(job.getWorkflow(), thisStep);
                if (nextStep != null) {
                    workflowService.advanceToNextStep(job, thisStep);
                    jobRepresentation
                            .addLink(createNextStepLink(uriInfo, job, nextStep.getNextStep()), findMethodFromStep(nextStep.getNextStep()));
                }


                asyncResponse.resume(Response.ok(jobRepresentation).build());
            } else {
                jobRepresentation.addLink(createRetryStepLink(uriInfo, job, thisStep));
                jobRepresentation.setLastResponse(destroyVmResponse.getStatus());
                asyncResponse.resume(Response.status(Response.Status.BAD_REQUEST).build());
            }

            LOG.info("Completing response");
            asyncResponse.resume(Response.ok(jobRepresentation).build());
            LOG.debug("Completed response");
        });
    }

    @POST
    @Path("{jobId}/remove-host-from-vcenter")
    public void removeHostFromVCenter(@Suspended final AsyncResponse asyncResponse, @PathParam("jobId") String jobId,
                                      @Context UriInfo uriInfo) {
        asyncResponse.setTimeoutHandler(asyncResponse1 -> asyncResponse1
                .resume(Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("{\"status\":\"timeout\"}").build()));
        asyncResponse.setTimeout(10, TimeUnit.SECONDS);

        //
        final String thisStep = findStepFromPath(uriInfo);
        final Job job = workflowService.findJob(UUID.fromString(jobId));
        final JobRepresentation jobRepresentation = new JobRepresentation(job);

        //TODO find out where to get the hostname from
        final String hostname = "";
        //TODO find out where to get the cluster id from
        final String clusterId = "";
        final CompletableFuture<ClusterOperationResponse> clusterOperationResponseCompletableFuture = vcenterService
                .requestHostRemoval(job.getVcenterCredentials(), hostname, clusterId);
        clusterOperationResponseCompletableFuture.thenAccept(clusterOperationResponse ->
        {
            LOG.info("Host Removal from VCenter Cluster Response: [{}]", clusterOperationResponse);

            if (ClusterOperationResponseMessage.Status.SUCCESS.value().equals(clusterOperationResponse.getStatus())) {
                final NextStep nextStep = workflowService.findNextStep(job.getWorkflow(), thisStep);
                if (nextStep != null) {
                    workflowService.advanceToNextStep(job, thisStep);
                    jobRepresentation
                            .addLink(createNextStepLink(uriInfo, job, nextStep.getNextStep()), findMethodFromStep(nextStep.getNextStep()));
                }

                asyncResponse.resume(Response.ok(jobRepresentation).build());
            } else {
                jobRepresentation.addLink(createRetryStepLink(uriInfo, job, thisStep));
                jobRepresentation.setLastResponse(clusterOperationResponse.getStatus());
                asyncResponse.resume(Response.status(Response.Status.BAD_REQUEST).build());
            }

            LOG.info("Completing response");
            asyncResponse.resume(Response.ok(jobRepresentation).build());
            LOG.debug("Completed response");
        });
    }

    @POST
    @Path("{jobId}/enter-maintanence-mode")
    public void enterMaintenanceMode(@Suspended final AsyncResponse asyncResponse, @PathParam("jobId") String jobId,
                                     @Context UriInfo uriInfo) {
        asyncResponse.setTimeoutHandler(asyncResponse1 -> asyncResponse1
                .resume(Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("{\"status\":\"timeout\"}").build()));
        asyncResponse.setTimeout(10, TimeUnit.SECONDS);

        final String thisStep = findStepFromPath(uriInfo);
        final Job job = workflowService.findJob(UUID.fromString(jobId));
        final JobRepresentation jobRepresentation = new JobRepresentation(job);

        //TODO find out where to get the hostname from
        final String hostname = "";

        final CompletableFuture<HostMaintenanceModeResponse> hostMaintenanceModeCompletableFuture = vcenterService
                .requestHostMaintenanceModeEnable(job.getVcenterCredentials(), hostname);
        hostMaintenanceModeCompletableFuture.thenAccept(hostMaintenanceModeResponse ->
        {
            LOG.info("Host Maintenance Mode Response: [{}]", hostMaintenanceModeResponse);

            if (HostMaintenanceModeResponseMessage.Status.SUCCESS.value().equals(hostMaintenanceModeResponse.getStatus())) {
                final NextStep nextStep = workflowService.findNextStep(job.getWorkflow(), thisStep);
                if (nextStep != null) {
                    workflowService.advanceToNextStep(job, thisStep);
                    jobRepresentation
                            .addLink(createNextStepLink(uriInfo, job, nextStep.getNextStep()), findMethodFromStep(nextStep.getNextStep()));
                }

                asyncResponse.resume(Response.ok(jobRepresentation).build());
            } else {
                jobRepresentation.addLink(createRetryStepLink(uriInfo, job, thisStep));
                jobRepresentation.setLastResponse(hostMaintenanceModeResponse.getStatus());
                asyncResponse.resume(Response.status(Response.Status.BAD_REQUEST).build());
            }

            LOG.info("Completing response");
            asyncResponse.resume(Response.ok(jobRepresentation).build());
            LOG.debug("Completed response");
        });
    }

    @POST
    @Path("{jobId}/reboot-host-for-discovery")
    public void rebootHostForDiscovery(@Suspended final AsyncResponse asyncResponse, @PathParam("jobId") String jobId,
                                       @Context UriInfo uriInfo) {
        asyncResponse.setTimeoutHandler(asyncResponse1 -> asyncResponse1
                .resume(Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("{\"status\":\"timeout\"}").build()));
        asyncResponse.setTimeout(10, TimeUnit.SECONDS);

        //
        final String thisStep = findStepFromPath(uriInfo);
        final Job job = workflowService.findJob(UUID.fromString(jobId));
        final JobRepresentation jobRepresentation = new JobRepresentation(job);

        final NextStep nextStep = workflowService.findNextStep(job.getWorkflow(), thisStep);
        if (nextStep != null) {
            workflowService.advanceToNextStep(job, thisStep);
            jobRepresentation.addLink(createNextStepLink(uriInfo, job, nextStep.getNextStep()), findMethodFromStep(nextStep.getNextStep()));
        }

        LOG.info("Completing response");
        asyncResponse.resume(Response.ok(jobRepresentation).build());
        LOG.debug("Completed response");
    }

    @POST
    @Path("{jobId}/power-off-esxi-host-for-removal")
    public void powerOffEsxiHostForRemoval(@Suspended final AsyncResponse asyncResponse, @PathParam("jobId") String jobId,
                                           @Context UriInfo uriInfo) {
        asyncResponse.setTimeoutHandler(asyncResponse1 -> asyncResponse1
                .resume(Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("{\"status\":\"timeout\"}").build()));
        asyncResponse.setTimeout(10, TimeUnit.SECONDS);

        final String thisStep = findStepFromPath(uriInfo);
        final Job job = workflowService.findJob(UUID.fromString(jobId));
        final JobRepresentation jobRepresentation = new JobRepresentation(job);

        //TODO find out where to get the hostname from
        final String hostname = "";

        final CompletableFuture<VCenterHostPowerOperationStatus> vCenterHostPowerOperationStatusCompletableFuture = vcenterService
                .requestHostPowerOff(job.getVcenterCredentials(), hostname);
        vCenterHostPowerOperationStatusCompletableFuture.thenAccept(hostPowerOperationStatus ->
        {
            LOG.info("Host Power Off Response: [{}]", hostPowerOperationStatus);

            if (HostPowerOperationResponseMessage.Status.SUCCESS.value().equals(hostPowerOperationStatus.getStatus())) {
                final NextStep nextStep = workflowService.findNextStep(job.getWorkflow(), thisStep);
                if (nextStep != null) {
                    workflowService.advanceToNextStep(job, thisStep);
                    jobRepresentation
                            .addLink(createNextStepLink(uriInfo, job, nextStep.getNextStep()), findMethodFromStep(nextStep.getNextStep()));
                }

                asyncResponse.resume(Response.ok(jobRepresentation).build());
            } else {
                jobRepresentation.addLink(createRetryStepLink(uriInfo, job, thisStep));
                jobRepresentation.setLastResponse(hostPowerOperationStatus.getStatus());
                asyncResponse.resume(Response.status(Response.Status.BAD_REQUEST).build());
            }

            LOG.info("Completing response");
            asyncResponse.resume(Response.ok(jobRepresentation).build());
            LOG.debug("Completed response");
        });
    }

    @POST
    @Path("{jobId}/wait-for-rackhd-discovery")
    public void waitForRackHDDiscovery(@Suspended final AsyncResponse asyncResponse, @PathParam("jobId") String jobId,
                                       @Context UriInfo uriInfo) {
        asyncResponse.setTimeoutHandler(asyncResponse1 -> asyncResponse1
                .resume(Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("{\"status\":\"timeout\"}").build()));
        asyncResponse.setTimeout(10, TimeUnit.SECONDS);

        //
        final String thisStep = findStepFromPath(uriInfo);
        final Job job = workflowService.findJob(UUID.fromString(jobId));
        final JobRepresentation jobRepresentation = new JobRepresentation(job);

        final NextStep nextStep = workflowService.findNextStep(job.getWorkflow(), thisStep);
        if (nextStep != null) {
            workflowService.advanceToNextStep(job, thisStep);
            jobRepresentation.addLink(createNextStepLink(uriInfo, job, nextStep.getNextStep()), findMethodFromStep(nextStep.getNextStep()));
        }

        LOG.info("Completing response");
        asyncResponse.resume(Response.ok(jobRepresentation).build());
        LOG.debug("Completed response");
    }

    @POST
    @Path("{jobId}/instruct-physical-removal")
    public void instructPhysicalRemoval(@Suspended final AsyncResponse asyncResponse, @PathParam("jobId") String jobId,
                                        @Context UriInfo uriInfo) {
        asyncResponse.setTimeoutHandler(asyncResponse1 -> asyncResponse1
                .resume(Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("{\"status\":\"timeout\"}").build()));
        asyncResponse.setTimeout(10, TimeUnit.SECONDS);

        //
        final String thisStep = findStepFromPath(uriInfo);
        final Job job = workflowService.findJob(UUID.fromString(jobId));
        final JobRepresentation jobRepresentation = new JobRepresentation(job);

        final NextStep nextStep = workflowService.findNextStep(job.getWorkflow(), thisStep);
        if (nextStep != null) {
            workflowService.advanceToNextStep(job, thisStep);
            jobRepresentation.addLink(createNextStepLink(uriInfo, job, nextStep.getNextStep()), findMethodFromStep(nextStep.getNextStep()));
        }

        LOG.info("Completing response");
        asyncResponse.resume(Response.ok(jobRepresentation).build());
        LOG.debug("Completed response");
    }

    @POST
    @Path("{jobId}/wait-for-rackhd-host-discovery")
    public void waitForRackHDHostDiscovery(@Suspended final AsyncResponse asyncResponse, @PathParam("jobId") String jobId,
                                           @Context UriInfo uriInfo) {
        asyncResponse.setTimeoutHandler(asyncResponse1 -> asyncResponse1
                .resume(Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("{\"status\":\"timeout\"}").build()));
        asyncResponse.setTimeout(10, TimeUnit.SECONDS);

        //
        final String thisStep = findStepFromPath(uriInfo);
        final Job job = workflowService.findJob(UUID.fromString(jobId));
        final JobRepresentation jobRepresentation = new JobRepresentation(job);

        final NextStep nextStep = workflowService.findNextStep(job.getWorkflow(), thisStep);
        if (nextStep != null) {
            workflowService.advanceToNextStep(job, thisStep);
            jobRepresentation.addLink(createNextStepLink(uriInfo, job, nextStep.getNextStep()), findMethodFromStep(nextStep.getNextStep()));
        }

        LOG.info("Completing response");
        asyncResponse.resume(Response.ok(jobRepresentation).build());
        LOG.debug("Completed response");
    }

    @POST
    @Path("{jobId}/present-system-list-add")
    public void presentSystemListForAddition(@Suspended final AsyncResponse asyncResponse, @PathParam("jobId") String jobId,
                                             @Context UriInfo uriInfo) {
        asyncResponse.setTimeoutHandler(asyncResponse1 -> asyncResponse1
                .resume(Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("{\"status\":\"timeout\"}").build()));
        asyncResponse.setTimeout(10, TimeUnit.SECONDS);

        //
        final String thisStep = findStepFromPath(uriInfo);
        final Job job = workflowService.findJob(UUID.fromString(jobId));
        final JobRepresentation jobRepresentation = new JobRepresentation(job);

        final NextStep nextStep = workflowService.findNextStep(job.getWorkflow(), thisStep);
        if (nextStep != null) {
            workflowService.advanceToNextStep(job, thisStep);
            jobRepresentation.addLink(createNextStepLink(uriInfo, job, nextStep.getNextStep()), findMethodFromStep(nextStep.getNextStep()));
        }

        LOG.info("Completing response");
        asyncResponse.resume(Response.ok(jobRepresentation).build());
        LOG.debug("Completed response");
    }

    @POST
    @Path("{jobId}/configure-disks-rackhd")
    public void configureDisksRackHD(@Suspended final AsyncResponse asyncResponse, @PathParam("jobId") String jobId,
                                     @Context UriInfo uriInfo) {
        asyncResponse.setTimeoutHandler(asyncResponse1 -> asyncResponse1
                .resume(Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("{\"status\":\"timeout\"}").build()));
        asyncResponse.setTimeout(10, TimeUnit.SECONDS);

        //
        final String thisStep = findStepFromPath(uriInfo);
        final Job job = workflowService.findJob(UUID.fromString(jobId));
        final JobRepresentation jobRepresentation = new JobRepresentation(job);

        final NextStep nextStep = workflowService.findNextStep(job.getWorkflow(), thisStep);
        if (nextStep != null) {
            workflowService.advanceToNextStep(job, thisStep);
            jobRepresentation.addLink(createNextStepLink(uriInfo, job, nextStep.getNextStep()), findMethodFromStep(nextStep.getNextStep()));
        }

        LOG.info("Completing response");
        asyncResponse.resume(Response.ok(jobRepresentation).build());
        LOG.debug("Completed response");
    }

    @POST
    @Path("{jobId}/install-esxi")
    public void installEsxi(@Suspended final AsyncResponse asyncResponse, @PathParam("jobId") String jobId, @Context UriInfo uriInfo) {
        asyncResponse.setTimeoutHandler(asyncResponse1 -> asyncResponse1
                .resume(Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("{\"status\":\"timeout\"}").build()));
        asyncResponse.setTimeout(10, TimeUnit.SECONDS);

        //
        final String thisStep = findStepFromPath(uriInfo);
        final Job job = workflowService.findJob(UUID.fromString(jobId));
        final JobRepresentation jobRepresentation = new JobRepresentation(job);

        final NextStep nextStep = workflowService.findNextStep(job.getWorkflow(), thisStep);
        if (nextStep != null) {
            workflowService.advanceToNextStep(job, thisStep);
            jobRepresentation.addLink(createNextStepLink(uriInfo, job, nextStep.getNextStep()), findMethodFromStep(nextStep.getNextStep()));
        }

        LOG.info("Completing response");
        asyncResponse.resume(Response.ok(jobRepresentation).build());
        LOG.debug("Completed response");
    }

    @POST
    @Path("{jobId}/add-host-to-vcenter")
    public void addHostTovCenter(@Suspended final AsyncResponse asyncResponse, @PathParam("jobId") String jobId, @Context UriInfo uriInfo) {
        asyncResponse.setTimeoutHandler(asyncResponse1 -> asyncResponse1
                .resume(Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("{\"status\":\"timeout\"}").build()));
        asyncResponse.setTimeout(10, TimeUnit.SECONDS);


        final String thisStep = findStepFromPath(uriInfo);
        final Job job = workflowService.findJob(UUID.fromString(jobId));
        final JobRepresentation jobRepresentation = new JobRepresentation(job);

        //TODO find out where to get the hostname from
        final String hostname = "";
        //TODO find out where to get the cluster id from
        final String clusterId = "";
        final String hostUsername = "";
        final String hostPassword = "";
        final CompletableFuture<ClusterOperationResponse> clusterOperationResponseCompletableFuture = vcenterService
                .requestHostAddition(job.getVcenterCredentials(), hostname, clusterId, hostUsername, hostPassword);
        clusterOperationResponseCompletableFuture.thenAccept(clusterOperationResponse ->
        {
            LOG.info("Host Addition to the VCenter Cluster Response: [{}]", clusterOperationResponse);

            if (ClusterOperationResponseMessage.Status.SUCCESS.value().equals(clusterOperationResponse.getStatus())) {
                final NextStep nextStep = workflowService.findNextStep(job.getWorkflow(), thisStep);
                if (nextStep != null) {
                    workflowService.advanceToNextStep(job, thisStep);
                    jobRepresentation
                            .addLink(createNextStepLink(uriInfo, job, nextStep.getNextStep()), findMethodFromStep(nextStep.getNextStep()));
                }

                asyncResponse.resume(Response.ok(jobRepresentation).build());
            } else {
                jobRepresentation.addLink(createRetryStepLink(uriInfo, job, thisStep));
                jobRepresentation.setLastResponse(clusterOperationResponse.getStatus());
                asyncResponse.resume(Response.status(Response.Status.BAD_REQUEST).build());
            }

            LOG.info("Completing response");
            asyncResponse.resume(Response.ok(jobRepresentation).build());
            LOG.debug("Completed response");
        });
    }

    @POST
    @Path("{jobId}/install-scaleio-vib")
    public void installScaleIOVIB(@Suspended final AsyncResponse asyncResponse, @PathParam("jobId") String jobId, @Context UriInfo uriInfo) {
        asyncResponse.setTimeoutHandler(asyncResponse1 -> asyncResponse1
                .resume(Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("{\"status\":\"timeout\"}").build()));
        asyncResponse.setTimeout(10, TimeUnit.SECONDS);

        //
        final String thisStep = findStepFromPath(uriInfo);
        final Job job = workflowService.findJob(UUID.fromString(jobId));
        final JobRepresentation jobRepresentation = new JobRepresentation(job);

        final NextStep nextStep = workflowService.findNextStep(job.getWorkflow(), thisStep);
        if (nextStep != null) {
            workflowService.advanceToNextStep(job, thisStep);
            jobRepresentation.addLink(createNextStepLink(uriInfo, job, nextStep.getNextStep()), findMethodFromStep(nextStep.getNextStep()));
        }

        LOG.info("Completing response");
        asyncResponse.resume(Response.ok(jobRepresentation).build());
        LOG.debug("Completed response");
    }

    @POST
    @Path("{jobId}/exit-vcenter-maintenance-mode")
    public void exitMaintenanceMode(@Suspended final AsyncResponse asyncResponse, @PathParam("jobId") String jobId,
                                    @Context UriInfo uriInfo) {
        asyncResponse.setTimeoutHandler(asyncResponse1 -> asyncResponse1
                .resume(Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("{\"status\":\"timeout\"}").build()));
        asyncResponse.setTimeout(10, TimeUnit.SECONDS);

        //
        final String thisStep = findStepFromPath(uriInfo);
        final Job job = workflowService.findJob(UUID.fromString(jobId));
        final JobRepresentation jobRepresentation = new JobRepresentation(job);

        final NextStep nextStep = workflowService.findNextStep(job.getWorkflow(), thisStep);
        if (nextStep != null) {
            workflowService.advanceToNextStep(job, thisStep);
            jobRepresentation.addLink(createNextStepLink(uriInfo, job, nextStep.getNextStep()), findMethodFromStep(nextStep.getNextStep()));
        }

        LOG.info("Completing response");
        asyncResponse.resume(Response.ok(jobRepresentation).build());
        LOG.debug("Completed response");
    }

    @POST
    @Path("{jobId}/deploy-svm")
    public void deploySVM(@Suspended final AsyncResponse asyncResponse, @PathParam("jobId") String jobId, @Context UriInfo uriInfo) {
        asyncResponse.setTimeoutHandler(asyncResponse1 -> asyncResponse1
                .resume(Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("{\"status\":\"timeout\"}").build()));
        asyncResponse.setTimeout(10, TimeUnit.SECONDS);

        //
        final String thisStep = findStepFromPath(uriInfo);
        final Job job = workflowService.findJob(UUID.fromString(jobId));
        final JobRepresentation jobRepresentation = new JobRepresentation(job);

        final NextStep nextStep = workflowService.findNextStep(job.getWorkflow(), thisStep);
        if (nextStep != null) {
            workflowService.advanceToNextStep(job, thisStep);
            jobRepresentation.addLink(createNextStepLink(uriInfo, job, nextStep.getNextStep()), findMethodFromStep(nextStep.getNextStep()));
        }

        LOG.info("Completing response");
        asyncResponse.resume(Response.ok(jobRepresentation).build());
        LOG.debug("Completed response");
    }

    @POST
    @Path("{jobId}/wait-for-svm-deploy")
    public void waitForSVMDeploy(@Suspended final AsyncResponse asyncResponse, @PathParam("jobId") String jobId, @Context UriInfo uriInfo) {
        asyncResponse.setTimeoutHandler(asyncResponse1 -> asyncResponse1
                .resume(Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("{\"status\":\"timeout\"}").build()));
        asyncResponse.setTimeout(10, TimeUnit.SECONDS);

        //
        final String thisStep = findStepFromPath(uriInfo);
        final Job job = workflowService.findJob(UUID.fromString(jobId));
        final JobRepresentation jobRepresentation = new JobRepresentation(job);

        final NextStep nextStep = workflowService.findNextStep(job.getWorkflow(), thisStep);
        if (nextStep != null) {
            workflowService.advanceToNextStep(job, thisStep);
            jobRepresentation.addLink(createNextStepLink(uriInfo, job, nextStep.getNextStep()), findMethodFromStep(nextStep.getNextStep()));
        }

        LOG.info("Completing response");
        asyncResponse.resume(Response.ok(jobRepresentation).build());
        LOG.debug("Completed response");
    }

    @POST
    @Path("{jobId}/start-scaleio-add-workflow")
    public void startScaleIOAddWorkflow(@Suspended final AsyncResponse asyncResponse, @PathParam("jobId") String jobId,
                                        @Context UriInfo uriInfo) {
        asyncResponse.setTimeoutHandler(asyncResponse1 -> asyncResponse1
                .resume(Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("{\"status\":\"timeout\"}").build()));
        asyncResponse.setTimeout(10, TimeUnit.SECONDS);

        //
        final String thisStep = findStepFromPath(uriInfo);
        final Job job = workflowService.findJob(UUID.fromString(jobId));
        final JobRepresentation jobRepresentation = new JobRepresentation(job);

        final NextStep nextStep = workflowService.findNextStep(job.getWorkflow(), thisStep);
        if (nextStep != null) {
            workflowService.advanceToNextStep(job, thisStep);
            jobRepresentation.addLink(createNextStepLink(uriInfo, job, nextStep.getNextStep()), findMethodFromStep(nextStep.getNextStep()));
        }

        LOG.info("Completing response");
        asyncResponse.resume(Response.ok(jobRepresentation).build());
        LOG.debug("Completed response");
    }

    @POST
    @Path("{jobId}/wait-for-scaleio-add-complete")
    public void waitForScaleIOAddComplete(@Suspended final AsyncResponse asyncResponse, @PathParam("jobId") String jobId,
                                          @Context UriInfo uriInfo) {
        asyncResponse.setTimeoutHandler(asyncResponse1 -> asyncResponse1
                .resume(Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("{\"status\":\"timeout\"}").build()));
        asyncResponse.setTimeout(10, TimeUnit.SECONDS);

        //
        final String thisStep = findStepFromPath(uriInfo);
        final Job job = workflowService.findJob(UUID.fromString(jobId));
        final JobRepresentation jobRepresentation = new JobRepresentation(job);

        final NextStep nextStep = workflowService.findNextStep(job.getWorkflow(), thisStep);
        if (nextStep != null) {
            workflowService.advanceToNextStep(job, thisStep);
            jobRepresentation.addLink(createNextStepLink(uriInfo, job, nextStep.getNextStep()), findMethodFromStep(nextStep.getNextStep()));
        }

        LOG.info("Completing response");
        asyncResponse.resume(Response.ok(jobRepresentation).build());
        LOG.debug("Completed response");
    }

    @POST
    @Path("{jobId}/map-scaleio-volumes-to-host")
    public void mapScaleIOVolumesToHost(@Suspended final AsyncResponse asyncResponse, @PathParam("jobId") String jobId,
                                        @Context UriInfo uriInfo) {
        asyncResponse.setTimeoutHandler(asyncResponse1 -> asyncResponse1
                .resume(Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("{\"status\":\"timeout\"}").build()));
        asyncResponse.setTimeout(10, TimeUnit.SECONDS);

        //
        final String thisStep = findStepFromPath(uriInfo);
        final Job job = workflowService.findJob(UUID.fromString(jobId));
        final JobRepresentation jobRepresentation = new JobRepresentation(job);

        final NextStep nextStep = workflowService.findNextStep(job.getWorkflow(), thisStep);
        if (nextStep != null) {
            workflowService.advanceToNextStep(job, thisStep);
            jobRepresentation.addLink(createNextStepLink(uriInfo, job, nextStep.getNextStep()), findMethodFromStep(nextStep.getNextStep()));
        }

        LOG.info("Completing response");
        asyncResponse.resume(Response.ok(jobRepresentation).build());
        LOG.debug("Completed response");
    }

    private Link createNextStepLink(final UriInfo uriInfo, final Job job, final String nextStep) {
        final String path = findPathFromStep(nextStep);
        final String type = findTypeFromStep(nextStep);

        return Link.fromUriBuilder(uriInfo.getBaseUriBuilder().path("workflow").path(job.getId().toString()).path(path)).type(type)
                .rel("step-next").build();
    }

    private Link createRetryStepLink(final UriInfo uriInfo, final Job job, final String step) {
        final String path = findPathFromStep(step);
        final String type = findTypeFromStep(step);

        return Link.fromUriBuilder(uriInfo.getBaseUriBuilder().path("workflow").path(job.getId().toString()).path(path)).type(type)
                .rel("step-retry").build();
    }

    private Link createSelfLink(final UriInfo uriInfo, final Job job) {
        return Link.fromUriBuilder(uriInfo.getBaseUriBuilder().path("workflow").path(job.getId().toString())).rel("self").build();
    }

    private String findPathFromStep(String step) {
        final Map<String, String> stepToPath = new HashMap<>();
        stepToPath.put("captureRackHDEndpoint", "rackhd-endpoint");
        stepToPath.put("captureCoprHDEndpoint", "coprhd-endpoint");
        stepToPath.put("capturevCenterEndpoint", "vcenter-endpoint");
        stepToPath.put("captureScaleIOEndpoint", "scaleio-endpoint");
        stepToPath.put("startScaleIODataCollection", "start-scaleio-data-collection");
        stepToPath.put("startvCenterDataCollection", "start-vcenter-data-collection");
        stepToPath.put("presentSystemListForRemoval", "present-system-list-remove");
        stepToPath.put("startSIORemoveWorkflow", "start-scaleio-remove-workflow");
        stepToPath.put("waitForSIORemoveComplete", "wait-for-scaleio-workflow");
        stepToPath.put("destroyScaleIOVM", "destroy-scaleio-vm");
        //TODO: Change enterMaintanenceMode to enterMaintenanceMode
        stepToPath.put("enterMaintanenceMode", "enter-maintanence-mode");
        stepToPath.put("removeHostFromVCenter", "remove-host-from-vcenter");
        stepToPath.put("rebootHostForDiscovery", "reboot-host-for-discovery");
        stepToPath.put("waitRackHDHostDiscovery", "wait-for-rackhd-discovery");
        stepToPath.put("powerOffEsxiHostForRemoval", "power-off-esxi-host-for-removal");
        stepToPath.put("instructPhysicalRemoval", "instruct-physical-removal");
        stepToPath.put("waitRackHDHostDiscovery", "wait-for-rackhd-host-discovery");
        stepToPath.put("presentSystemListForAddition", "present-system-list-add");
        stepToPath.put("configureDisksRackHD", "configure-disks-rackhd");
        stepToPath.put("installEsxi", "install-esxi");
        stepToPath.put("addHostTovCenter", "add-host-to-vcenter");
        stepToPath.put("installSIOVib", "install-scaleio-vib");
        stepToPath.put("exitVCenterMaintenanceMode", "exit-vcenter-maintenance-mode");
        stepToPath.put("deploySVM", "deploy-svm");
        stepToPath.put("waitForSVMDeploy", "wait-for-svm-deploy");
        stepToPath.put("startSIOAddWorkflow", "start-scaleio-add-workflow");
        stepToPath.put("waitForSIOAddComplete", "wait-for-scaleio-add-complete");
        stepToPath.put("mapSIOVolumesToHost", "map-scaleio-volumes-to-host");
        stepToPath.put("completed", "");

        return stepToPath.get(step);
    }

    private String findStepFromPath(final UriInfo uriInfo) {
        final Map<String, String> stepToPath = new HashMap<>();
        stepToPath.put("rackhd-endpoint", "captureRackHDEndpoint");
        stepToPath.put("coprhd-endpoint", "captureCoprHDEndpoint");
        stepToPath.put("vcenter-endpoint", "capturevCenterEndpoint");
        stepToPath.put("scaleio-endpoint", "captureScaleIOEndpoint");
        stepToPath.put("start-scaleio-data-collection", "startScaleIODataCollection");
        stepToPath.put("start-vcenter-data-collection", "startvCenterDataCollection");
        stepToPath.put("present-system-list-remove", "presentSystemListForRemoval");
        stepToPath.put("start-scaleio-remove-workflow", "startSIORemoveWorkflow");
        stepToPath.put("wait-for-scaleio-workflow", "waitForSIORemoveComplete");
        stepToPath.put("destroy-scaleio-vm", "destroyScaleIOVM");
        //TODO: Change enterMaintanenceMode to enterMaintenanceMode
        stepToPath.put("enter-maintanence-mode", "enterMaintanenceMode");
        stepToPath.put("remove-host-from-vcenter", "removeHostFromVCenter");
        stepToPath.put("reboot-host-for-discovery", "rebootHostForDiscovery");
        stepToPath.put("wait-for-rackhd-discovery", "waitRackHDHostDiscovery");
        stepToPath.put("power-off-esxi-host-for-removal", "powerOffEsxiHostForRemoval");
        stepToPath.put("instruct-physical-removal", "instructPhysicalRemoval");
        stepToPath.put("wait-for-rackhd-host-discovery", "waitRackHDHostDiscovery");
        stepToPath.put("present-system-list-add", "presentSystemListForAddition");
        stepToPath.put("configure-disks-rackhd", "configureDisksRackHD");
        stepToPath.put("install-esxi", "installEsxi");
        stepToPath.put("add-host-to-vcenter", "addHostTovCenter");
        stepToPath.put("install-scaleio-vib", "installSIOVib");
        stepToPath.put("exit-vcenter-maintenance-mode", "exitVCenterMaintenanceMode");
        stepToPath.put("deploy-svm", "deploySVM");
        stepToPath.put("wait-for-svm-deploy", "waitForSVMDeploy");
        stepToPath.put("start-scaleio-add-workflow", "startSIOAddWorkflow");
        stepToPath.put("wait-for-scaleio-add-complete", "waitForSIOAddComplete");
        stepToPath.put("map-scaleio-volumes-to-host", "mapSIOVolumesToHost");

        final List<PathSegment> pathSegments = uriInfo.getPathSegments();
        final PathSegment pathSegment = pathSegments.get(pathSegments.size() - 1);
        return stepToPath.get(pathSegment.getPath());
    }

    private String findTypeFromStep(String step) {
        final Map<String, String> stepToType = new HashMap<>();
        stepToType.put("captureRackHDEndpoint", "application/vnd.dellemc.rackhd.endpoint+json");
        stepToType.put("captureCoprHDEndpoint", "application/vnd.dellemc.coprhd.endpoint+json");
        stepToType.put("capturevCenterEndpoint", "application/vnd.dellemc.vcenter.endpoint+json");
        stepToType.put("captureScaleIOEndpoint", "application/vnd.dellemc.scaleio.endpoint+json");
        return stepToType.getOrDefault(step, "application/json");
    }

    private String findMethodFromStep(String step) {
        final Map<String, String> stepToMethod = new HashMap<>();
        stepToMethod.put("completed", "GET");
        return stepToMethod.getOrDefault(step, "POST");
    }
}