package org.carlspring.logging.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogGenerator
{

    private Logger logger = LoggerFactory.getLogger(LogGenerator.class);

    public void info(String message)
    {
        logger.info(message);
    }

    public void infoLog()
    {
        logger.info("info log");
    }

    public void debug(String message)
    {
        logger.debug(message);
    }

    public void debugLog()
    {
        logger.debug("debug log");
    }

}
