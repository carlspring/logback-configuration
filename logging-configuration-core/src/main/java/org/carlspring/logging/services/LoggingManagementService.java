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

    void addLogger(String loggerPackage, String level, String appenderName)
            throws LoggingConfigurationException, AppenderNotFoundException;

    void updateLogger(String loggerPackage, String level)
            throws LoggingConfigurationException, LoggerNotFoundException;

    void deleteLogger(String loggerPackage)
            throws LoggingConfigurationException, LoggerNotFoundException;
    
    InputStream downloadLog() 
            throws LoggingConfigurationException;
    
    InputStream downloadLogbackConfiguration() 
            throws LoggingConfigurationException;
    
    void uploadLogbackConfiguration(String content) 
            throws LoggingConfigurationException;
    
    public String getPathToXml();
    
    public void setPathToXml(String pathToXml);
    
}
