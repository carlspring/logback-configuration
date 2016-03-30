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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This restlet provides a simple wrapper over REST API for the LoggingManagementService.
 *
 * This class does not have a @Path annotation, so that it could be
 * sub-classed and be much more easily configured.
 *
 * @author Martin Todorov
 * @author Yougeshwar
 */
@Component
public class AbstractLoggingManagementRestlet
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
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/log/{path:.*}")
    public Response downloadLog(@PathParam("path") String path)
    {
        try
        {
            InputStream is = loggingManagementService.downloadLog(path);
            return Response.ok(is).build();
        }
        catch (LoggingConfigurationException ex)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
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

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/partial-log/{path:.*}")
    public Response partialLogDownload(@PathParam("path") String path, @QueryParam("offset") long offset)
    {
        try
        {
            InputStream is = loggingManagementService.downloadLog(path);
            is.skip(offset);

            return Response.ok(is).build();
//            Response.ResponseBuilder responseBuilder = prepareResponseBuilderForPartialRequest(is, offset);
////            responseBuilder.header("Content-Length", is.available());
//            responseBuilder.status(Response.Status.PARTIAL_CONTENT);
//
//            return responseBuilder.build();
        }
        catch (IOException ex)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
        catch (LoggingConfigurationException ex)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    private Response.ResponseBuilder prepareResponseBuilderForPartialRequest(InputStream ais, long offset)
            throws IOException
    {
        Response.ResponseBuilder responseBuilder = Response.ok(ais).status(Response.Status.PARTIAL_CONTENT);
        responseBuilder.header("Accept-Ranges", "bytes");
//         responseBuilder.header("Content-Length", ais.getLength());
        responseBuilder.header("Content-Range", "bytes " + offset + "-" + (ais.available() - 1) + "/" + ais.available());
        responseBuilder.header("Content-Type", ais.available());
        responseBuilder.header("Pragma", "no-cache");

        return responseBuilder;
    }
}
