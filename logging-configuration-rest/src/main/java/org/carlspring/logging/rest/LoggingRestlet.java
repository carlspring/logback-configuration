package org.carlspring.logging.rest;

import org.carlspring.logging.services.LoggingManagementService;
import org.carlspring.logging.exceptions.LoggingConfigurationException;
import org.carlspring.logging.exceptions.NoLoggerFoundException;
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
        try
        {
            loggingManagementService.addLogger(loggerPackage, level);
        }
        catch (LoggingConfigurationException ex) 
        {
            return Response.ok(Response.status(400)).build();
        }

        return Response.ok(Response.status(200)).build();
    }

    @POST
    @Path("/update")
    public Response updateLogger(@QueryParam("logger") String loggerPackage,
                                 @QueryParam("level") String level)
    {
        logger.debug("Updating logger: " + logger);

        // TODO: Implement
        try
        {
            loggingManagementService.updateLogger(loggerPackage, level);
        }
        catch (LoggingConfigurationException ex) 
        {
            return Response.ok(Response.status(400)).build();
        }
        catch (NoLoggerFoundException ex) 
        {
            return Response.ok(Response.status(404)).build();
        }

        return Response.ok(Response.status(200)).build();
    }

    @DELETE
    @Path("/delete")
    public Response delete(@QueryParam("logger") String loggerPackage)
            throws IOException
    {
        logger.debug("Deleting logger: " + logger);

        // TODO: Implement
        try
        {
            loggingManagementService.deleteLogger(loggerPackage);
        }
        catch (LoggingConfigurationException ex) 
        {
            return Response.ok(Response.status(400)).build();
        }
        catch (NoLoggerFoundException ex) 
        {
            return Response.ok(Response.status(404)).build();
        }

        return Response.ok(Response.status(200)).build();
    }

}
