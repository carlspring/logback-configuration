package org.carlspring.logging.services.impl;

import org.carlspring.logging.services.LoggingManagementService;
import org.carlspring.logging.exceptions.AppenderNotFoundException;
import org.carlspring.logging.exceptions.LoggingConfigurationException;
import org.carlspring.logging.exceptions.LoggerNotFoundException;
import org.carlspring.logging.utils.LogBackXMLUtils;

import java.util.List;
import java.util.Arrays;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.slf4j.LoggerFactory;

import static org.slf4j.Logger.ROOT_LOGGER_NAME;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.core.Appender;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * @author mtodorov
 * @author Yougeshwar
 */
@Service
public class LoggingManagementServiceImpl
        implements LoggingManagementService
{
    private Object lock = new Object();

    private List<String> asList = Arrays.asList("ALL",
                                                "DEBUG",
                                                "INFO",
                                                "WARN",
                                                "ERROR",
                                                "FATAL",
                                                "OFF",
                                                "TRACE");

    @Override
    public void addLogger(String loggerPackage,
                          String level,
                          String appenderName)
            throws LoggingConfigurationException,
                   AppenderNotFoundException
    {
        synchronized (lock)
        {
            if (!isValidPackage(loggerPackage))
            {
                throw new LoggingConfigurationException("Invalid package '" + loggerPackage + "'!");
            }
            else if (!isValidLevel(level))
            {
                throw new LoggingConfigurationException("Invalid level '" + level + "'!");
            }
            else
            {
                Logger root = (Logger) LoggerFactory.getLogger(ROOT_LOGGER_NAME);
                Appender<ILoggingEvent> appender = root.getAppender(appenderName.toUpperCase());

                if (appender == null)
                {
                    throw new AppenderNotFoundException("Appender '" + appenderName + "' not found!");
                }

                Logger log = (Logger) LoggerFactory.getLogger(loggerPackage);
                log.setLevel(Level.toLevel(level.toUpperCase()));
                log.setAdditive(false); /* set to true if root should log too */
                log.addAppender(appender);

                LogBackXMLUtils.addLogger(loggerPackage, level.toUpperCase(), appenderName);
            }
        }
    }

    @Override
    public void updateLogger(String loggerPackage, String level)
            throws LoggingConfigurationException,
                   LoggerNotFoundException
    {
        synchronized (lock)
        {
            if (!isValidPackage(loggerPackage))
            {
                throw new LoggingConfigurationException("Invalid package '" + loggerPackage + "'!");
            }
            else if (!isValidLevel(level))
            {
                throw new LoggingConfigurationException("Invalid level '" + level + "'!");
            }
            else if (!packageLoggerExists(loggerPackage))
            {
                throw new LoggerNotFoundException("Logger '" + loggerPackage + "' not found!");
            }
            else
            {
                Logger log = (Logger) LoggerFactory.getLogger(loggerPackage);
                log.setLevel(Level.toLevel(level.toUpperCase()));

                LogBackXMLUtils.updateLogger(loggerPackage, level.toUpperCase());
            }
        }
    }

    @Override
    public void deleteLogger(String loggerPackage)
            throws LoggingConfigurationException,
                   LoggerNotFoundException
    {
        synchronized (lock)
        {
            if (!isValidPackage(loggerPackage))
            {
                throw new LoggingConfigurationException("Invalid package '" + loggerPackage + "'!");
            }
            else if (!packageLoggerExists(loggerPackage))
            {
                throw new LoggerNotFoundException("Logger '" + loggerPackage + "' not found!");
            }
            else
            {
                Logger log = (Logger) LoggerFactory.getLogger(loggerPackage);
                log.setLevel(Level.OFF);
                LogBackXMLUtils.deleteLogger(loggerPackage);
            }
        }
    }

    private boolean packageLoggerExists(String packageLogger)
    {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

        List<Logger> loggerList = lc.getLoggerList();

        for (Logger logger : loggerList)
        {
            if (logger.getName().equals(packageLogger))
            {
                return true;
            }
        }

        return false;
    }

    private boolean isValidLevel(String level)
    {
        return asList.contains(level.toUpperCase());
    }

    private boolean isValidPackage(String pkg)
    {
        Pattern pattern = Pattern.compile("^[a-zA-Z_\\$][\\w\\$]*(?:\\.[a-zA-Z_\\$][\\w\\$]*)*$");
        return pattern.matcher(pkg).matches();
    }

}
