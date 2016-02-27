package org.carlspring.logging.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogGenerator
{

    private Logger logger = LoggerFactory.getLogger(LogGenerator.class);

    public void infoLog()
    {
        logger.info("info log");
    }

    public void debugLog()
    {
        logger.debug("debug log");
    }
}
