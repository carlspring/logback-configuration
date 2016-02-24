package org.carlspring.logging.services;

import org.carlspring.logging.exceptions.AppenderNotFoundException;
import org.carlspring.logging.exceptions.LoggerNotFoundException;
import org.carlspring.logging.exceptions.LoggingConfigurationException;

/**
 * 
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
}
