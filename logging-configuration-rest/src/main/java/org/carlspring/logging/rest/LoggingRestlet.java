package org.carlspring.logging.rest;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.carlspring.logging.exceptions.AppenderNotFoundException;
import org.carlspring.logging.exceptions.LoggerNotFoundException;
import org.carlspring.logging.exceptions.LoggingConfigurationException;
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
    @Path("/logger")
    public Response addLogger(@QueryParam("logger") String loggerPackage,
                              @QueryParam("level") String level,
                              @QueryParam("appenderName") String appenderName)
    {
        try
        {
            loggingManagementService.addLogger(loggerPackage, level, appenderName);
        }
        catch (LoggingConfigurationException ex)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
        catch (AppenderNotFoundException ex)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }

        return Response.ok().build();
    }

    @POST
    @Path("/logger")
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
    @Path("/logger")
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
    
    @GET
    @Path("/log")
    public Response downloadLog()
    {
        try
        {
            InputStream is = loggingManagementService.downloadLog();
            return Response.ok(is).build();
        }
        catch (LoggingConfigurationException ex)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
    
    @GET
    @Path("/logback")
    public Response downloadLogbackConfiguration()
    {
        try
        {
            InputStream is = loggingManagementService.downloadLogbackConfiguration();
            return Response.ok(is).build();
        }
        catch (LoggingConfigurationException ex)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
    
    @POST
    @Path("/logback")
    public Response uploadLogbackConfiguration(InputStream is)
    {
        try
        {
            loggingManagementService.uploadLogbackConfiguration(is);
            return Response.ok().build();
        }
        catch (LoggingConfigurationException ex)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

//    responseBuilder = Response.ok(is);
//    InputStream is = new FileInputStream(file...);
//    return responseBuilder.build();
    // TODO: 1) Add a method for downloading the config file:
    // TODO:      This should simply return the current logback XML configuration.
    // TODO: 2) Add a method for uploading a config file:
    // TODO:      This should overwrite the existing logback file and reload it.
    // TODO: 3) Add a method for downloading the current log file.
    // TODO:
    // TODO: The code for all of the above should be implemented in the service class and just be called here.

}
