package org.carlspring.logging.rest;

import java.io.IOException;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.carlspring.logging.exceptions.AppenderNotFoundException;
import org.carlspring.logging.exceptions.LoggingConfigurationException;
import org.carlspring.logging.exceptions.LoggerNotFoundException;
import org.carlspring.logging.services.LoggingManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
            @QueryParam("level") String level,
            @QueryParam("appender_name") String appenderName)
    {
        try
        {
            loggingManagementService.addLogger(loggerPackage, level, appenderName);
        }
        catch (LoggingConfigurationException ex) 
        {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (AppenderNotFoundException ex)
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
        catch (LoggerNotFoundException ex) 
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
        catch (LoggerNotFoundException ex) 
        {
            return Response.status(Response.Status.NOT_FOUND).entity(ex.getMessage()).build();
        }

        return Response.ok().build();
    }

}
