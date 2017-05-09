/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.rest;

import com.dell.cpsd.paqx.fru.dto.FRUSystemData;
import com.dell.cpsd.paqx.fru.service.DataService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.UUID;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
@Api
@Component
@Path("/data")
@Produces(MediaType.APPLICATION_JSON)
public class DataResource {
    private final DataService dataService;

    @Autowired
    public DataResource(final DataService dataService) {
        this.dataService = dataService;
    }

    @GET
    @Path("{taskID}")
    public Response getData(@PathParam("taskID") String taskID, @Context UriInfo uriInfo) {
        final FRUSystemData data = dataService.getData(UUID.fromString(taskID));
        return Response.ok(data).build();
    }
}
