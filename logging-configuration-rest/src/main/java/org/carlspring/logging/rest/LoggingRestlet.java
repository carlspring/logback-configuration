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
        
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        Appender<ILoggingEvent> appender = root.getAppender("CONSOLE");
        
        ch.qos.logback.classic.Logger log = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(loggerPackage);
        log.setLevel(ch.qos.logback.classic.Level.toLevel(level.toUpperCase()));
        log.setAdditive(false); /* set to true if root should log too */
        log.addAppender(appender);

        return Response.ok().build();
    }

    @POST
    @Path("/update")
    public Response updateLogger(@QueryParam("logger") String loggerPackage,
                                 @QueryParam("level") String level)
    {
        logger.debug("Updating logger: " + logger);

        // TODO: Implement
        ch.qos.logback.classic.Logger log = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(loggerPackage);
        log.setLevel(ch.qos.logback.classic.Level.toLevel(level.toUpperCase()));

        return Response.ok().build();
    }

    @DELETE
    @Path("/delete")
    public Response delete(@QueryParam("logger") String loggerPackage)
            throws IOException
    {
        logger.debug("Deleting logger: " + logger);

        // TODO: Implement
        ch.qos.logback.classic.Logger log = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(loggerPackage);
        log.setLevel(ch.qos.logback.classic.Level.toLevel("off".toUpperCase()));

        return Response.ok().build();
    }

}
