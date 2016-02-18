package org.carlspring.logging.rest;

import org.carlspring.logging.services.LoggingManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * @author Martin Todorov
 */
@Component
@Path("/logging")
public class LoggingRestlet
{

    private static final Logger logger = LoggerFactory.getLogger(LoggingRestlet.class);

    @Autowired
    private LoggingManagementService loggingManagementService;


    @PUT
    @Path("/add")
    public Response addLogger(@QueryParam("logger") String loggerPackage,
                              @QueryParam("level") String level)
    {
        logger.debug("DELETE: " + logger);

        // TODO: Implement
        loggingManagementService.addLogger(loggerPackage, level);

        return Response.ok(Response.status(loggingManagementService.getStatus())).build();
    }

    @POST
    @Path("/update")
    public Response updateLogger(@QueryParam("logger") String loggerPackage,
                                 @QueryParam("level") String level)
    {
        logger.debug("Updating logger: " + logger);

        // TODO: Implement
        loggingManagementService.updateLogger(loggerPackage, level);
        
        return Response.ok(Response.status(loggingManagementService.getStatus())).build();
    }

    @DELETE
    @Path("/delete")
    public Response delete(@QueryParam("logger") String loggerPackage)
            throws IOException
    {
        logger.debug("Deleting logger: " + logger);

        // TODO: Implement
        loggingManagementService.deleteLogger(loggerPackage);
        
        return Response.ok(Response.status(loggingManagementService.getStatus())).build();
    }

}
