/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.service;

import com.dell.cpsd.hdp.capability.registry.api.Capability;
import com.dell.cpsd.hdp.capability.registry.client.CapabilityRegistryException;
import com.dell.cpsd.paqx.fru.amqp.consumer.handler.AsyncAcknowledgement;
import com.dell.cpsd.paqx.fru.dto.ConsulRegistryResult;
import com.dell.cpsd.paqx.fru.rest.dto.EndpointCredentials;
import com.dell.cpsd.paqx.fru.rest.dto.VCenterHostPowerOperationStatus;
import com.dell.cpsd.paqx.fru.rest.dto.vCenterSystemProperties;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.ClusterOperationResponse;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.DestroyVmResponse;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.HostMaintenanceModeResponse;
import com.dell.cpsd.service.common.client.exception.ServiceTimeoutException;
import com.dell.cpsd.virtualization.capabilities.api.ClusterOperationRequest;
import com.dell.cpsd.virtualization.capabilities.api.ClusterOperationRequestMessage;
import com.dell.cpsd.virtualization.capabilities.api.ConsulRegisterRequestMessage;
import com.dell.cpsd.virtualization.capabilities.api.Credentials;
import com.dell.cpsd.virtualization.capabilities.api.DestroyVMRequestMessage;
import com.dell.cpsd.virtualization.capabilities.api.DiscoveryRequestInfoMessage;
import com.dell.cpsd.virtualization.capabilities.api.HostMaintenanceModeRequestMessage;
import com.dell.cpsd.virtualization.capabilities.api.HostPowerOperationRequestMessage;
import com.dell.cpsd.virtualization.capabilities.api.MaintenanceModeRequest;
import com.dell.cpsd.virtualization.capabilities.api.MessageProperties;
import com.dell.cpsd.virtualization.capabilities.api.PowerOperationRequest;
import com.dell.cpsd.virtualization.capabilities.api.RegistrationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
@Service
public class vCenterServiceImpl implements vCenterService
{
    private static final Logger LOG = LoggerFactory.getLogger(vCenterServiceImpl.class);

    private final AsyncAcknowledgement asyncAcknowledgement;
    private final AsyncAcknowledgement consulRegisterAsyncAcknowledgement;
    private final AsyncAcknowledgement vmDeletionAsyncAcknowledgement;
    private final AsyncAcknowledgement vCenterHostPowerAsyncAcknowledgement;
    private final AsyncAcknowledgement hostMaintenanceModeAsyncAcknowledgement;
    private final AsyncAcknowledgement vcenterClusterOperationAsyncAcknowledgement;
    private final String               replyTo;
    private final FruService           fruService;

    @Autowired
    public vCenterServiceImpl(@Qualifier(value = "vCenterDiscoverResponseHandler") final AsyncAcknowledgement asyncAcknowledgement,
            @Qualifier(value = "vCenterConsulRegisterResponseHandler") final AsyncAcknowledgement consulRegisterAsyncAcknowledgement,
            @Qualifier(value = "vCenterDestroyVmResponseHandler") final AsyncAcknowledgement vmDeletionAsyncAcknowledgement,
            @Qualifier(value = "vCenterHostPowerOperationResponseHandler") final AsyncAcknowledgement vCenterHostPowerAsyncAcknowledgement,
            @Qualifier(value = "hostMaintenanceModeResponseHandler") final AsyncAcknowledgement hostMaintenanceModeAsyncAcknowledgement,
            @Qualifier(value = "vCenterClusterOperationsResponseHandler") final AsyncAcknowledgement vcenterClusterOperationAsyncAcknowledgement,
            @Qualifier(value = "replyTo") final String replyTo, final FruService fruService)
    {

        this.asyncAcknowledgement = asyncAcknowledgement;
        this.consulRegisterAsyncAcknowledgement = consulRegisterAsyncAcknowledgement;
        this.vmDeletionAsyncAcknowledgement = vmDeletionAsyncAcknowledgement;
        this.vCenterHostPowerAsyncAcknowledgement = vCenterHostPowerAsyncAcknowledgement;
        this.hostMaintenanceModeAsyncAcknowledgement = hostMaintenanceModeAsyncAcknowledgement;
        this.vcenterClusterOperationAsyncAcknowledgement = vcenterClusterOperationAsyncAcknowledgement;
        this.replyTo = replyTo;
        this.fruService = fruService;
    }

    public CompletableFuture<vCenterSystemProperties> showSystem(final EndpointCredentials vcenterCredentials)
    {
        final String requiredCapability = "vcenter-discover";

        try
        {
            new URL(vcenterCredentials.getEndpointUrl());
        }
        catch (MalformedURLException e)
        {
            final CompletableFuture<vCenterSystemProperties> promise = new CompletableFuture<>();
            return fruService.malformedUrlException(e, promise);
        }

        try
        {
            final List<Capability> matchedCapabilities = fruService.findMatchingCapabilities(requiredCapability);
            if (matchedCapabilities.isEmpty())
            {
                LOG.info("No matching capability found for capability [{}]", requiredCapability);
                return CompletableFuture.completedFuture(null);
            }

            final Capability matchedCapability = matchedCapabilities.stream().findFirst().get();
            LOG.debug("Found capability {}", matchedCapability.getProfile());

            final Map<String, String> amqpProperties = fruService.declareBinding(matchedCapability, replyTo);

            final String correlationId = UUID.randomUUID().toString();
            final DiscoveryRequestInfoMessage requestMessage = new DiscoveryRequestInfoMessage();
            requestMessage.setCredentials(new Credentials(vcenterCredentials.getEndpointUrl(), vcenterCredentials.getPassword(),
                    vcenterCredentials.getUsername()));
            requestMessage.setMessageProperties(new MessageProperties(new Date(), correlationId, replyTo));
            return fruService.registerPromiseAndSendRequestMessage(asyncAcknowledgement, correlationId, requestMessage, amqpProperties);
        }
        catch (CapabilityRegistryException | ServiceTimeoutException e)
        {
            return CompletableFuture.completedFuture(null);
        }
    }

    public CompletableFuture<ConsulRegistryResult> requestConsulRegistration(final EndpointCredentials vcenterCredentials)
    {
        final String requiredCapability = "vcenter-consul-register";

        try
        {
            new URL(vcenterCredentials.getEndpointUrl());
        }
        catch (MalformedURLException e)
        {
            final CompletableFuture<ConsulRegistryResult> promise = new CompletableFuture<>();
            return fruService.malformedUrlException(e, promise);
        }

        try
        {
            final List<Capability> matchedCapabilities = fruService.findMatchingCapabilities(requiredCapability);
            if (matchedCapabilities.isEmpty())
            {
                LOG.info("No matching capability found for capability [{}]", requiredCapability);
                return CompletableFuture.completedFuture(null);
            }

            final Capability matchedCapability = matchedCapabilities.stream().findFirst().get();
            LOG.debug("Found capability {}", matchedCapability.getProfile());

            final Map<String, String> amqpProperties = fruService.declareBinding(matchedCapability, replyTo);

            final String correlationId = UUID.randomUUID().toString();
            final ConsulRegisterRequestMessage requestMessage = new ConsulRegisterRequestMessage();
            requestMessage.setRegistrationInfo(new RegistrationInfo(vcenterCredentials.getEndpointUrl(), vcenterCredentials.getPassword(),
                    vcenterCredentials.getUsername()));
            requestMessage.setMessageProperties(new MessageProperties(new Date(), correlationId, replyTo));
            return fruService.registerPromiseAndSendRequestMessage(consulRegisterAsyncAcknowledgement, correlationId, requestMessage,
                    amqpProperties);
        }
        catch (CapabilityRegistryException | ServiceTimeoutException e)
        {
            return CompletableFuture.completedFuture(null);
        }
    }

    @Override
    public CompletableFuture<DestroyVmResponse> requestVmDeletion(final EndpointCredentials vcenterCredentials, final String uuid)
    {
        final String requiredCapability = "vcenter-destroy-virtualMachine";

        try
        {
            new URL(vcenterCredentials.getEndpointUrl());
        }
        catch (MalformedURLException e)
        {
            final CompletableFuture<DestroyVmResponse> promise = new CompletableFuture<>();
            return fruService.malformedUrlException(e, promise);
        }

        try
        {
            final List<Capability> matchedCapabilities = fruService.findMatchingCapabilities(requiredCapability);
            if (matchedCapabilities.isEmpty())
            {
                LOG.info("No matching capability found for capability [{}]", requiredCapability);
                return CompletableFuture.completedFuture(null);
            }

            final Capability matchedCapability = matchedCapabilities.stream().findFirst().get();
            LOG.debug("Found capability {}", matchedCapability.getProfile());

            final Map<String, String> amqpProperties = fruService.declareBinding(matchedCapability, replyTo);

            final String correlationId = UUID.randomUUID().toString();
            final DestroyVMRequestMessage requestMessage = new DestroyVMRequestMessage();
            requestMessage.setUuid(uuid);
            requestMessage.setCredentials(new Credentials(vcenterCredentials.getEndpointUrl(), vcenterCredentials.getPassword(),
                    vcenterCredentials.getUsername()));
            requestMessage.setMessageProperties(new MessageProperties(new Date(), correlationId, replyTo));
            return fruService
                    .registerPromiseAndSendRequestMessage(vmDeletionAsyncAcknowledgement, correlationId, requestMessage, amqpProperties);
        }
        catch (CapabilityRegistryException | ServiceTimeoutException e)
        {
            return CompletableFuture.completedFuture(null);
        }
    }

    @Override
    public CompletableFuture<HostMaintenanceModeResponse> requestHostMaintenanceModeEnable(final EndpointCredentials vcenterCredentials,
            final String hostname)
    {
        final String requiredCapability = "vcenter-enterMaintenance";

        try
        {
            new URL(vcenterCredentials.getEndpointUrl());
        }
        catch (MalformedURLException e)
        {
            final CompletableFuture<HostMaintenanceModeResponse> promise = new CompletableFuture<>();
            return fruService.malformedUrlException(e, promise);
        }

        try
        {
            final List<Capability> matchedCapabilities = fruService.findMatchingCapabilities(requiredCapability);
            if (matchedCapabilities.isEmpty())
            {
                LOG.info("No matching capability found for capability [{}]", requiredCapability);
                return CompletableFuture.completedFuture(null);
            }

            final Capability matchedCapability = matchedCapabilities.stream().findFirst().get();
            LOG.debug("Found capability {}", matchedCapability.getProfile());

            final Map<String, String> amqpProperties = fruService.declareBinding(matchedCapability, replyTo);

            final String correlationId = UUID.randomUUID().toString();
            final HostMaintenanceModeRequestMessage requestMessage = new HostMaintenanceModeRequestMessage();
            requestMessage.setCredentials(new Credentials(vcenterCredentials.getEndpointUrl(), vcenterCredentials.getPassword(),
                    vcenterCredentials.getUsername()));
            requestMessage.setMaintenanceModeRequest(new MaintenanceModeRequest(hostname, Boolean.TRUE));
            requestMessage.setMessageProperties(new MessageProperties(new Date(), correlationId, replyTo));
            return fruService.registerPromiseAndSendRequestMessage(hostMaintenanceModeAsyncAcknowledgement, correlationId, requestMessage,
                    amqpProperties);
        }
        catch (CapabilityRegistryException | ServiceTimeoutException e)
        {
            return CompletableFuture.completedFuture(null);
        }
    }

    @Override
    public CompletableFuture<VCenterHostPowerOperationStatus> requestHostPowerOff(EndpointCredentials vcenterCredentials, String hostname)
    {
        final String requiredCapability = "vcenter-powercommand";

        try
        {
            new URL(vcenterCredentials.getEndpointUrl());
        }
        catch (MalformedURLException e)
        {
            final CompletableFuture<VCenterHostPowerOperationStatus> promise = new CompletableFuture<>();
            return fruService.malformedUrlException(e, promise);
        }

        try
        {
            final List<Capability> matchedCapabilities = fruService.findMatchingCapabilities(requiredCapability);
            if (matchedCapabilities.isEmpty())
            {
                LOG.info("No matching capability found for capability [{}]", requiredCapability);
                return CompletableFuture.completedFuture(null);
            }

            final Capability matchedCapability = matchedCapabilities.stream().findFirst().get();
            LOG.debug("Found capability {}", matchedCapability.getProfile());

            final Map<String, String> amqpProperties = fruService.declareBinding(matchedCapability, replyTo);

            final String correlationId = UUID.randomUUID().toString();
            final HostPowerOperationRequestMessage requestMessage = new HostPowerOperationRequestMessage();
            requestMessage.setCredentials(new Credentials(vcenterCredentials.getEndpointUrl(), vcenterCredentials.getPassword(),
                    vcenterCredentials.getUsername()));
            requestMessage.setPowerOperationRequest(new PowerOperationRequest(hostname, PowerOperationRequest.PowerOperation.POWER_OFF));
            requestMessage.setMessageProperties(new MessageProperties(new Date(), correlationId, replyTo));
            return fruService.registerPromiseAndSendRequestMessage(vCenterHostPowerAsyncAcknowledgement, correlationId, requestMessage,
                    amqpProperties);
        }
        catch (CapabilityRegistryException | ServiceTimeoutException e)
        {
            return CompletableFuture.completedFuture(null);
        }
    }

    @Override
    public CompletableFuture<ClusterOperationResponse> requestHostRemoval(final EndpointCredentials vcenterCredentials,
            final String clusterId, final String hostname)
    {
        final String requiredCapability = "vcenter-remove-host";

        try
        {
            new URL(vcenterCredentials.getEndpointUrl());
        }
        catch (MalformedURLException e)
        {
            final CompletableFuture<ClusterOperationResponse> promise = new CompletableFuture<>();
            return fruService.malformedUrlException(e, promise);
        }

        try
        {
            final List<Capability> matchedCapabilities = fruService.findMatchingCapabilities(requiredCapability);
            if (matchedCapabilities.isEmpty())
            {
                LOG.info("No matching capability found for capability [{}]", requiredCapability);
                return CompletableFuture.completedFuture(null);
            }

            final Capability matchedCapability = matchedCapabilities.stream().findFirst().get();
            LOG.debug("Found capability {}", matchedCapability.getProfile());

            final Map<String, String> amqpProperties = fruService.declareBinding(matchedCapability, replyTo);

            final String correlationId = UUID.randomUUID().toString();
            final ClusterOperationRequestMessage requestMessage = new ClusterOperationRequestMessage();
            requestMessage.setCredentials(new Credentials(vcenterCredentials.getEndpointUrl(), vcenterCredentials.getPassword(),
                    vcenterCredentials.getUsername()));
            final ClusterOperationRequest clusterOperationRequest = new ClusterOperationRequest();
            clusterOperationRequest.setHostName(hostname);
            //TODO: If not required remove the cluster id
            clusterOperationRequest.setClusterID(clusterId);
            clusterOperationRequest.setClusterOperation(ClusterOperationRequest.ClusterOperation.REMOVE_HOST);
            requestMessage.setClusterOperationRequest(clusterOperationRequest);
            requestMessage.setMessageProperties(new MessageProperties(new Date(), correlationId, replyTo));
            return fruService
                    .registerPromiseAndSendRequestMessage(vcenterClusterOperationAsyncAcknowledgement, correlationId, requestMessage,
                            amqpProperties);
        }
        catch (CapabilityRegistryException | ServiceTimeoutException e)
        {
            return CompletableFuture.completedFuture(null);
        }
    }

    @Override
    public CompletableFuture<ClusterOperationResponse> requestHostAddition(final EndpointCredentials vcenterCredentials,
            final String hostname, final String clusterId, final String hostUsername, final String hostPassword)
    {
        final String requiredCapability = "vcenter-addhostvcenter";

        try
        {
            new URL(vcenterCredentials.getEndpointUrl());
        }
        catch (MalformedURLException e)
        {
            final CompletableFuture<ClusterOperationResponse> promise = new CompletableFuture<>();
            return fruService.malformedUrlException(e, promise);
        }

        try
        {
            final List<Capability> matchedCapabilities = fruService.findMatchingCapabilities(requiredCapability);
            if (matchedCapabilities.isEmpty())
            {
                LOG.info("No matching capability found for capability [{}]", requiredCapability);
                return CompletableFuture.completedFuture(null);
            }

            final Capability matchedCapability = matchedCapabilities.stream().findFirst().get();
            LOG.debug("Found capability {}", matchedCapability.getProfile());

            final Map<String, String> amqpProperties = fruService.declareBinding(matchedCapability, replyTo);

            final String correlationId = UUID.randomUUID().toString();
            final ClusterOperationRequestMessage requestMessage = new ClusterOperationRequestMessage();
            requestMessage.setCredentials(new Credentials(vcenterCredentials.getEndpointUrl(), vcenterCredentials.getPassword(),
                    vcenterCredentials.getUsername()));
            final ClusterOperationRequest clusterOperationRequest = new ClusterOperationRequest();
            clusterOperationRequest.setHostName(hostname);
            clusterOperationRequest.setClusterID(clusterId);
            clusterOperationRequest.setUserName(hostUsername);
            clusterOperationRequest.setPassword(hostPassword);
            clusterOperationRequest.setClusterOperation(ClusterOperationRequest.ClusterOperation.ADD_HOST);
            requestMessage.setClusterOperationRequest(clusterOperationRequest);
            requestMessage.setMessageProperties(new MessageProperties(new Date(), correlationId, replyTo));
            return fruService
                    .registerPromiseAndSendRequestMessage(vcenterClusterOperationAsyncAcknowledgement, correlationId, requestMessage,
                            amqpProperties);
        }
        catch (CapabilityRegistryException | ServiceTimeoutException e)
        {
            return CompletableFuture.completedFuture(null);
        }
    }
}
