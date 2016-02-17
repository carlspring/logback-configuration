package org.carlspring.logging.rest.app;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingApplication extends ResourceConfig
{

    private static final Logger logger = LoggerFactory.getLogger(LoggingApplication.class);


    public LoggingApplication()
    {
        if (logger.isDebugEnabled())
        {
            register(new LoggingFilter());
        }
    }

}
