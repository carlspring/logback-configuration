package org.carlspring.logging.services;

import java.io.InputStream;

import org.carlspring.logging.exceptions.AppenderNotFoundException;
import org.carlspring.logging.exceptions.LoggerNotFoundException;
import org.carlspring.logging.exceptions.LoggingConfigurationException;

/**
 * @author mtodorov
 * @author Yougeshwar
 */
public interface LoggingManagementService
{

    /**
     * Adds a logger with the specified level and appender.
     *
     * @param loggerPackage
     * @param level
     * @param appenderName
     * @throws LoggingConfigurationException
     * @throws AppenderNotFoundException
     */
    void addLogger(String loggerPackage, String level, String appenderName)
            throws LoggingConfigurationException, AppenderNotFoundException;

    /**
     * Updates an existing logger.
     *
     * @param loggerPackage
     * @param level
     * @throws LoggingConfigurationException
     * @throws LoggerNotFoundException
     */
    void updateLogger(String loggerPackage, String level)
            throws LoggingConfigurationException, LoggerNotFoundException;

    /**
     * Deletes a logger.
     *
     * @param loggerPackage
     * @throws LoggingConfigurationException
     * @throws LoggerNotFoundException
     */
    void deleteLogger(String loggerPackage)
            throws LoggingConfigurationException, LoggerNotFoundException;

    /**
     * This method resolves an InputStream to a log file.
     *
     * @param logFilePath
     * @return
     * @throws LoggingConfigurationException
     */
    InputStream downloadLog(String logFilePath)
            throws LoggingConfigurationException;

    /**
     * This method resolves an InputStream to the logback configuration file.
     *
     * @return
     * @throws LoggingConfigurationException
     */
    InputStream downloadLogbackConfiguration() 
            throws LoggingConfigurationException;

    /**
     * This method overwrites the existing logback configuration file with a specified one.
     *
     * @param inputStream
     * @throws LoggingConfigurationException
     */
    void uploadLogbackConfiguration(InputStream inputStream)
            throws LoggingConfigurationException;
    
    public String getPathToXml();

    public String getPathToLogsDir();

}
