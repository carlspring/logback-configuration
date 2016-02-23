package org.carlspring.logging.services;

import org.carlspring.logging.services.LoggingManagementService;
import org.carlspring.logging.exceptions.LoggingConfigurationException;
import org.carlspring.logging.exceptions.NoLoggerFoundException;

/**
 * 
 * @author mtodorov
 * @author Yougeshwar
 */
public interface LoggingManagementService 
{

	void addLogger(String loggerPackage, String level) 
			throws LoggingConfigurationException;

	void updateLogger(String loggerPackage, String level) 
			throws LoggingConfigurationException, NoLoggerFoundException;

	void deleteLogger(String loggerPackage) 
			throws LoggingConfigurationException, NoLoggerFoundException;
}
