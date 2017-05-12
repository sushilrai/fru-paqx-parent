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
import com.dell.cpsd.service.common.client.exception.ServiceTimeoutException;
import com.dell.cpsd.storage.capabilities.api.ConsulRegisterRequestMessage;
import com.dell.cpsd.storage.capabilities.api.ListStorageRequestMessage;
import com.dell.cpsd.storage.capabilities.api.MessageProperties;
import com.dell.cpsd.storage.capabilities.api.RegistrationInfo;
import com.dell.cpsd.storage.capabilities.api.ScaleIOSystemDataRestRep;
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
public class ScaleIOServiceImpl implements ScaleIOService
{
    private static final Logger LOG = LoggerFactory.getLogger(ScaleIOServiceImpl.class);

    private final AsyncAcknowledgement asyncAcknowledgement;
    private final String               replyTo;
    private final FruService           fruService;

    private final AsyncAcknowledgement consulRegisterAsyncAcknowledgement;

    @Autowired
    public ScaleIOServiceImpl(@Qualifier(value = "listStorageResponseHandler") final AsyncAcknowledgement asyncAcknowledgement,
            @Qualifier(value = "scaleIOConsulRegisterResponseHandler") final AsyncAcknowledgement consulRegisterAsyncAcknowledgement,
            final String replyTo, final FruService fruService)
    {
        this.asyncAcknowledgement = asyncAcknowledgement;
        this.consulRegisterAsyncAcknowledgement = consulRegisterAsyncAcknowledgement;
        this.replyTo = replyTo;
        this.fruService = fruService;
    }

    public CompletableFuture<ScaleIOSystemDataRestRep> listStorage(final EndpointCredentials scaleIOCredentials)
    {
        final String requiredCapability = "coprhd-list-storage";

        try
        {
            new URL(scaleIOCredentials.getEndpointUrl());
        }
        catch (MalformedURLException e)
        {
            final CompletableFuture<ScaleIOSystemDataRestRep> promise = new CompletableFuture<>();
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
            final ListStorageRequestMessage requestMessage = new ListStorageRequestMessage();
            requestMessage.setEndpointURL(scaleIOCredentials.getEndpointUrl());
            requestMessage.setUserName(scaleIOCredentials.getUsername());
            requestMessage.setPassword(scaleIOCredentials.getPassword());
            requestMessage.setMessageProperties(new MessageProperties(new Date(), correlationId, replyTo));
            return fruService.registerPromiseAndSendRequestMessage(asyncAcknowledgement, correlationId, requestMessage, amqpProperties);
        }
        catch (CapabilityRegistryException | ServiceTimeoutException e)
        {
            return CompletableFuture.completedFuture(null);
        }
    }

    public CompletableFuture<ConsulRegistryResult> requestConsulRegistration(final EndpointCredentials scaleIOCredentials)
    {
        final String requiredCapability = "coprhd-consul-register";

        try
        {
            new URL(scaleIOCredentials.getEndpointUrl());
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
            final RegistrationInfo registrationInfo = new RegistrationInfo(scaleIOCredentials.getEndpointUrl(),
                    scaleIOCredentials.getPassword(), scaleIOCredentials.getUsername());
            requestMessage.setRegistrationInfo(registrationInfo);
            requestMessage.setMessageProperties(new MessageProperties(new Date(), correlationId, replyTo));
            return fruService.registerPromiseAndSendRequestMessage(consulRegisterAsyncAcknowledgement, correlationId, requestMessage,
                    amqpProperties);
        }
        catch (CapabilityRegistryException | ServiceTimeoutException e)
        {
            return CompletableFuture.completedFuture(null);
        }
    }
}
