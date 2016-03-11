package org.carlspring.logging.services.impl;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import org.carlspring.logging.exceptions.AppenderNotFoundException;
import org.carlspring.logging.exceptions.LoggerNotFoundException;
import org.carlspring.logging.exceptions.LoggingConfigurationException;
import org.carlspring.logging.services.LoggingManagementService;
import org.carlspring.logging.utils.LogBackXmlConfiguration;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import static org.slf4j.Logger.ROOT_LOGGER_NAME;

/**
 * @author mtodorov
 * @author Yougeshwar
 */
@Service
public class LoggingManagementServiceImpl
        implements LoggingManagementService
{

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(LoggingManagementServiceImpl.class);

    private Object lock = new Object();

    private List<String> asList = Arrays.asList("ALL",
                                                "DEBUG",
                                                "INFO",
                                                "WARN",
                                                "ERROR",
                                                "FATAL",
                                                "OFF",
                                                "TRACE");

    @Value("${logging.config.file:logback.xml}")
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
            checkPackageValidity(loggerPackage);
            checkIsValidLevel(level);

            Logger root = (Logger) LoggerFactory.getLogger(ROOT_LOGGER_NAME);
            Appender<ILoggingEvent> appender = root.getAppender(appenderName.toUpperCase());

            if (appender == null)
            {
                logger.error("Appender '" + appenderName + "' not found!");

                throw new AppenderNotFoundException("Appender '" + appenderName + "' not found!");
            }

            Logger log = (Logger) LoggerFactory.getLogger(loggerPackage);
            log.setLevel(Level.toLevel(level.toUpperCase()));
            log.setAdditive(false); /* set to true if root should log too */
            log.addAppender(appender);

            LogBackXmlConfiguration xmlCnfig = new LogBackXmlConfiguration(pathToXml);
            xmlCnfig.addLogger(loggerPackage, level.toUpperCase(), appenderName);

            logger.debug("Added logger '" + loggerPackage +
                         "' with level '" + level.toLowerCase() +
                         "' and appender '" + appenderName + "'.");
        }
    }

    @Override
    public void updateLogger(String loggerPackage, String level)
            throws LoggingConfigurationException,
                   LoggerNotFoundException
    {
        synchronized (lock)
        {
            checkPackageValidity(loggerPackage);
            checkPackageLoggerIsValid(loggerPackage);
            checkIsValidLevel(level);

            Logger log = (Logger) LoggerFactory.getLogger(loggerPackage);
            log.setLevel(Level.toLevel(level.toUpperCase()));

            LogBackXmlConfiguration xmlCnfig = new LogBackXmlConfiguration(pathToXml);
            xmlCnfig.updateLogger(loggerPackage, level.toUpperCase());

            logger.debug("Updated logger '" + loggerPackage +
                         "' to use level '" + level.toLowerCase() + "'.");
        }
    }

    @Override
    public void deleteLogger(String loggerPackage)
            throws LoggingConfigurationException,
                   LoggerNotFoundException
    {
        synchronized (lock)
        {
            checkPackageValidity(loggerPackage);
            checkPackageLoggerIsValid(loggerPackage);

            logger.debug("Deleting logger '" + loggerPackage + "...");

            Logger log = (Logger) LoggerFactory.getLogger(loggerPackage);
            log.setLevel(Level.OFF);

            LogBackXmlConfiguration xmlCnfig = new LogBackXmlConfiguration(pathToXml);
            xmlCnfig.deleteLogger(loggerPackage);

            logger.debug("Deleted logger '" + loggerPackage + ".");
        }
    }
    
    @Override
    public InputStream downloadLog(String logFilePath)
            throws LoggingConfigurationException
    {
        try
        {
            File file = new File(pathToLogsDir, logFilePath);

            logger.debug("Requested log " + file.getAbsolutePath() + "...");

            dumpLoggingProperties();

            FileInputStream fis = new FileInputStream(file);

            logger.debug("Downloading log " + file.getAbsolutePath() + "...");

            return fis;
        }
        catch (FileNotFoundException e)
        {
            logger.error(e.getMessage(), e);

            throw new LoggingConfigurationException(e);
        }
    }

    @Override
    public InputStream downloadLogbackConfiguration()
            throws LoggingConfigurationException
    {
        try
        {
            String path = pathToXml != null ? pathToXml : "logback.xml";

            logger.debug("Resolving Logback configuration file " + path + "...");

            dumpLoggingProperties();

            File file;
            URL url = LoggingManagementServiceImpl.class.getClassLoader().getResource(path);
            if (url != null)
            {
                logger.debug("Resolved the Logback configuration class from the classpath (" + url.toURI() + ").");

                file = new File(url.toURI());
            }
            else
            {
                file = new File(path);
                if (!file.exists())
                {
                    throw new FileNotFoundException("Failed to locate the Logback configuration file!");
                }

                logger.debug("Resolved the Logback configuration class from the file system (" + file.getAbsolutePath() + ").");
            }

            logger.debug("Downloading configuration file " + file.getAbsolutePath() + "...");

            return new FileInputStream(file);
        }
        catch (URISyntaxException | FileNotFoundException e)
        {
            logger.error(e.getMessage(), e);

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
            logger.debug("Received request to update the Logback configuration...");

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

            logger.debug("Logback configuration updated successfully!");
        }
        catch (URISyntaxException | IOException e)
        {
            logger.error("Failed to update the Logback configuration!");
            logger.error(e.getMessage(), e);

            throw new LoggingConfigurationException(e);
        }
        finally
        {
            close(is);
            close(fos);
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

    private void checkPackageValidity(String loggerPackage)
            throws LoggingConfigurationException
    {
        if (!isValidPackage(loggerPackage))
        {
            logger.error("Invalid package '" + loggerPackage + "'!");

            throw new LoggingConfigurationException("Invalid package '" + loggerPackage + "'!");
        }
    }

    private void checkIsValidLevel(String level)
            throws LoggingConfigurationException
    {
        if (!isValidLevel(level))
        {
            logger.error("Invalid level '" + level + "'!");

            throw new LoggingConfigurationException("Invalid level '" + level + "'!");
        }
    }

    private void checkPackageLoggerIsValid(String loggerPackage)
            throws LoggerNotFoundException
    {
        if (!packageLoggerExists(loggerPackage))
        {
            throw new LoggerNotFoundException("Logger '" + loggerPackage + "' not found!");
        }
    }

    private void close(Closeable resource)
    {
        if (resource != null)
        {
            try
            {
                resource.close();
            }
            catch (IOException e)
            {
                logger.error(e.getMessage(), e);
            }
        }
    }

    private void dumpLoggingProperties()
    {
        Properties properties = System.getProperties();

        for (Map.Entry<Object, Object> entry : properties.entrySet())
        {
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();

            if (key.startsWith("logging"))
            {
                System.out.println(key + " = " + value);
            }
        }
    }

}
