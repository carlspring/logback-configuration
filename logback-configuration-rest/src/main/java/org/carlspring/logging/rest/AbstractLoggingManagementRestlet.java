package org.carlspring.logging.rest;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.carlspring.logging.exceptions.AppenderNotFoundException;
import org.carlspring.logging.exceptions.LoggerNotFoundException;
import org.carlspring.logging.exceptions.LoggingConfigurationException;
import org.carlspring.logging.services.LoggingManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This restlet provides a simple wrapper over REST API for the LoggingManagementService.
 *
 * This class does not have a @Path annotation, so that it could be
 * sub-classed and be much more easily configured.
 *
 * @author Martin Todorov
 * @author Yougeshwar Khatri
 */
@Component
public class AbstractLoggingManagementRestlet
{

    private static final Logger logger = LoggerFactory.getLogger(AbstractLoggingManagementRestlet.class);

    @Autowired
    private LoggingManagementService loggingManagementService;


    @PUT
    @Path("/logger")
    @Produces(MediaType.TEXT_PLAIN)
    public Response addLogger(@QueryParam("logger") String loggerPackage,
                              @QueryParam("level") String level,
                              @QueryParam("appenderName") String appenderName)
    {
        try
        {
            loggingManagementService.addLogger(loggerPackage, level, appenderName);

            return Response.ok("The logger was added successfully.").build();
        }
        catch (LoggingConfigurationException | AppenderNotFoundException e)
        {
            logger.trace(e.getMessage(), e);

            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Failed to add logger!")
                           .build();
        }
    }

    @POST
    @Path("/logger")
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateLogger(@QueryParam("logger") String loggerPackage,
                                 @QueryParam("level") String level)
    {
        try
        {
            loggingManagementService.updateLogger(loggerPackage, level);

            return Response.ok("The logger was updated successfully.").build();
        }
        catch (LoggingConfigurationException e)
        {
            logger.trace(e.getMessage(), e);

            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Failed to update logger!")
                           .build();
        }
        catch (LoggerNotFoundException e)
        {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Logger '" + loggerPackage + "' not found!")
                           .build();
        }
    }

    @DELETE
    @Path("/logger")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteLogger(@QueryParam("logger") String loggerPackage)
            throws IOException
    {
        try
        {
            loggingManagementService.deleteLogger(loggerPackage);

            return Response.ok("The logger was deleted successfully.").build();
        }
        catch (LoggingConfigurationException e)
        {
            logger.trace(e.getMessage(), e);

            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Failed to delete the logger!")
                           .build();
        }
        catch (LoggerNotFoundException e)
        {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Logger '" + loggerPackage + "' not found!")
                           .build();
        }
    }
    
    @GET
    @Path("/log/{path:.*}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response downloadLog(@PathParam("path") String path)
    {
        try
        {
            System.out.println("Received a request to retrieve log file " + path + ".");

            InputStream is = loggingManagementService.downloadLog(path);

            System.out.println("Received a request to retrieve log file " + path + ".");

            return Response.ok(is).build();
        }
        catch (LoggingConfigurationException e)
        {
            logger.trace(e.getMessage(), e);

            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Failed to resolve the log!")
                           .build();
        }
    }
    
    @GET
    @Path("/logback")
    @Produces(MediaType.APPLICATION_XML)
    public Response downloadLogbackConfiguration()
    {
        try
        {
            InputStream is = loggingManagementService.downloadLogbackConfiguration();
            return Response.ok(is).build();
        }
        catch (LoggingConfigurationException e)
        {
            logger.trace(e.getMessage(), e);

            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Failed to resolve the logging configuration!")
                           .build();
        }
    }
    
    @POST
    @Path("/logback")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.TEXT_PLAIN)
    public Response uploadLogbackConfiguration(InputStream is)
    {
        try
        {
            loggingManagementService.uploadLogbackConfiguration(is);

            return Response.ok("Logback configuration uploaded successfully.").build();
        }
        catch (LoggingConfigurationException e)
        {
            logger.trace(e.getMessage(), e);

            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Failed to resolve the logging configuration!")
                           .build();
        }
    }

}
