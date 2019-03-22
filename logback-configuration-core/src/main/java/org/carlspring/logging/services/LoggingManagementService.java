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
     * @param loggerPackage     The logger package
     * @param level             The logging level
     * @param appenderName      The name of the appender
     * @throws LoggingConfigurationException
     * @throws AppenderNotFoundException
     */
    void addLogger(String loggerPackage, String level, String appenderName)
            throws LoggingConfigurationException, AppenderNotFoundException;

    /**
     * Updates an existing logger.
     *
     * @param loggerPackage     The logger package
     * @param level             The logging level
     * @throws LoggingConfigurationException
     * @throws LoggerNotFoundException
     */
    void updateLogger(String loggerPackage, String level)
            throws LoggingConfigurationException, LoggerNotFoundException;

    /**
     * Deletes a logger.
     *
     * @param loggerPackage     The logger package
     * @throws LoggingConfigurationException
     * @throws LoggerNotFoundException
     */
    void deleteLogger(String loggerPackage)
            throws LoggingConfigurationException, LoggerNotFoundException;

    /**
     * This method resolves an InputStream to a log file.
     *
     * @param logFilePath       The path to the log file.
     * @return
     * @throws LoggingConfigurationException
     */
    InputStream downloadLog(String logFilePath)
            throws LoggingConfigurationException;

    /**
     * This method resolves an InputStream to the logback configuration file.
     *
     * @return                  Returns the contents of the configuration file.
     * @throws LoggingConfigurationException
     */
    InputStream downloadLogbackConfiguration() 
            throws LoggingConfigurationException;

    /**
     * This method overwrites the existing logback configuration file with a specified one.
     *
     * @param inputStream       The input stream of the configuration file to be uploqaded.
     * @throws LoggingConfigurationException
     */
    void uploadLogbackConfiguration(InputStream inputStream)
            throws LoggingConfigurationException;
    
    String getPathToXml();

    String getPathToLogsDir();

}
