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
 * @author Yougeshwar
 */
@Component
@Path("/logging")
public class LoggingRestlet
{

    @Autowired
    private LoggingManagementService loggingManagementService;


    @PUT
    @Path("/add")
    public Response addLogger(@QueryParam("logger") String loggerPackage,
                              @QueryParam("level") String level)
    {
        try
        {
            loggingManagementService.addLogger(loggerPackage, level);
        }
        catch (LoggingConfigurationException ex) 
        {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }

        return Response.ok().build();
    }

    @POST
    @Path("/update")
    public Response updateLogger(@QueryParam("logger") String loggerPackage,
                                 @QueryParam("level") String level)
    {
        try
        {
            loggingManagementService.updateLogger(loggerPackage, level);
        }
        catch (LoggingConfigurationException ex) 
        {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
        catch (NoLoggerFoundException ex) 
        {
            return Response.status(Response.Status.NOT_FOUND).entity(ex.getMessage()).build();
        }

        return Response.ok().build();
    }

    @DELETE
    @Path("/delete")
    public Response delete(@QueryParam("logger") String loggerPackage)
            throws IOException
    {
        try
        {
            loggingManagementService.deleteLogger(loggerPackage);
        }
        catch (LoggingConfigurationException ex) 
        {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
        catch (NoLoggerFoundException ex) 
        {
            return Response.status(Response.Status.NOT_FOUND).entity(ex.getMessage()).build();
        }

        return Response.ok().build();
    }

}
