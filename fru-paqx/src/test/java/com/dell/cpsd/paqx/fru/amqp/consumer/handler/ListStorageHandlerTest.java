/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.amqp.consumer.handler;


import com.dell.cpsd.storage.capabilities.api.ListStorageResponseMessage;
import com.dell.cpsd.storage.capabilities.api.MessageProperties;
import com.dell.cpsd.storage.capabilities.api.ScaleIOSystemDataRestRep;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 *
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 *
 */
public class ListStorageHandlerTest
{
    @Test
    public void isCompletedWhenReceivingResponse() throws Exception
    {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        ListStorageResponseHandler handler = new ListStorageResponseHandler(null);

        final CompletableFuture<ScaleIOSystemDataRestRep> future = handler.register("test-001");
        future.whenComplete((systemRest, throwable) -> countDownLatch.countDown());

        ListStorageResponseMessage listResponse = new ListStorageResponseMessage();
        ScaleIOSystemDataRestRep scaleIOSystemDataRestRep = new ScaleIOSystemDataRestRep();
        scaleIOSystemDataRestRep.setSystemVersionName("some.test");
        listResponse.getScaleIOSystemDataRestRep();
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setCorrelationId("test-001");
        listResponse.setMessageProperties(messageProperties);
        handler.executeOperation(listResponse);

        Assert.assertTrue("Timeout before completion",  countDownLatch.await(1, TimeUnit.SECONDS));
    }
}
