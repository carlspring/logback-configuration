package org.carlspring.logging.services.impl;

import static org.slf4j.Logger.ROOT_LOGGER_NAME;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.carlspring.logging.exceptions.AppenderNotFoundException;
import org.carlspring.logging.exceptions.LoggerNotFoundException;
import org.carlspring.logging.exceptions.LoggingConfigurationException;
import org.carlspring.logging.services.LoggingManagementService;
import org.carlspring.logging.utils.LogBackXmlConfiguration;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;

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
    
    private String pathToXml;

    @Value("${logging.dir:logs}")
    public String pathToLogsDir;


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

                LogBackXmlConfiguration xmlCnfig = new LogBackXmlConfiguration(pathToXml);
                xmlCnfig.addLogger(loggerPackage, level.toUpperCase(), appenderName);
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

                LogBackXmlConfiguration xmlCnfig = new LogBackXmlConfiguration(pathToXml);
                xmlCnfig.updateLogger(loggerPackage, level.toUpperCase());
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
                
                LogBackXmlConfiguration xmlCnfig = new LogBackXmlConfiguration(pathToXml);
                xmlCnfig.deleteLogger(loggerPackage);
            }
        }
    }
    
    @Override
    public InputStream downloadLog(String logFilePath)
            throws LoggingConfigurationException
    {
        try
        {
            File file = new File(pathToLogsDir, logFilePath);

            System.out.println("Retrieving log from " + file.getAbsolutePath());

            return new FileInputStream(file);
        }
        catch (FileNotFoundException e)
        {
            throw new LoggingConfigurationException(e);
        }
        
    }

    @Override
    public InputStream downloadLogbackConfiguration() throws LoggingConfigurationException
    {
        try
        {
            String path = pathToXml != null ? pathToXml : "logback.xml";

            URL url = LoggingManagementServiceImpl.class.getClassLoader().getResource(path);
            File file = new File(url.toURI());

            return new FileInputStream(file);
        }
        catch (URISyntaxException | FileNotFoundException e)
        {
            throw new LoggingConfigurationException(e);
        }
    }

    @Override
    public void uploadLogbackConfiguration(InputStream is)
            throws LoggingConfigurationException
    {
        OutputStream fos = null;

        try
        {
            String path = pathToXml != null ? pathToXml : "logback.xml";

            URL url = LoggingManagementServiceImpl.class.getClassLoader().getResource(path);
            File file = new File(url.toURI());

            fos = new FileOutputStream(file);
            int readLength;
            byte[] bytes = new byte[4096];

            while ((readLength = is.read(bytes, 0, bytes.length)) != -1)
            {
                fos.write(bytes, 0, readLength);
                fos.flush();
            }
        }
        catch (URISyntaxException | IOException e)
        {
            throw new LoggingConfigurationException(e);
        }
        finally
        {
            if (is != null)
            {
                try
                {
                    is.close();
                }
                catch (IOException e)
                {
                    throw new LoggingConfigurationException(e);
                }
            }
            if (fos != null)
            {
                try
                {
                    fos.close();
                }
                catch (IOException e)
                {
                    throw new LoggingConfigurationException(e);
                }
            }
        }
    }

    @Override
    public String getPathToXml()
    {
        return pathToXml;
    }
    
    public void setPathToXml(String pathToXml)
    {
        this.pathToXml = pathToXml;
    }

    public String getPathToLogsDir()
    {
        return pathToLogsDir;
    }

    public void setPathToLogsDir(String pathToLogsDir)
    {
        this.pathToLogsDir = pathToLogsDir;
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
