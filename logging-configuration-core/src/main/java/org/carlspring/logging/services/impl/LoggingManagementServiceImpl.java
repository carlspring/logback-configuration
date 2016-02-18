package org.carlspring.logging.services.impl;

import org.carlspring.logging.services.LoggingManagementService;
import java.util.*;
import org.slf4j.LoggerFactory;

/**
 * @author carlspring
 */
public class LoggingManagementServiceImpl
        implements LoggingManagementService
{
	private int status;
	
	public int getStatus() 
	{
		return status;
	}
	
    public void addLogger(String loggerPackage, String level) 
    {
    	if(!isValidPackage(loggerPackage)) 
    	{
    		status = 400;
    	} else if(!isValidLevel(level)) 
    	{
    		status = 400;
    	} else 
    	{
        	ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
            ch.qos.logback.core.Appender<ch.qos.logback.classic.spi.ILoggingEvent> appender = root.getAppender("CONSOLE");
            
            ch.qos.logback.classic.Logger log = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(loggerPackage);
            log.setLevel(ch.qos.logback.classic.Level.toLevel(level.toUpperCase()));
            log.setAdditive(false); /* set to true if root should log too */
            log.addAppender(appender);
    		status = 200;
    	}
    }
    
    public void updateLogger(String loggerPackage, String level) 
    {
    	if(!isValidPackage(loggerPackage)) 
    	{
    		status = 400;
    	} 
    	else if(!isValidLevel(level)) 
    	{
    		status = 400;
    	} 
    	else if(!isPackageLoggerExists(loggerPackage)) 
    	{
    		status = 404;
    	} 
    	else 
    	{
	    	ch.qos.logback.classic.Logger log = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(loggerPackage);
	        log.setLevel(ch.qos.logback.classic.Level.toLevel(level.toUpperCase()));
    		status = 200;
    	}
    }
    
    public void deleteLogger(String loggerPackage) 
    {
    	if(!isValidPackage(loggerPackage)) 
    	{
    		status = 400;
    	} 
    	else if(!isPackageLoggerExists(loggerPackage)) 
    	{
    		status = 404;
    	}
    	else 
    	{
	    	ch.qos.logback.classic.Logger log = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(loggerPackage);
	        log.setLevel(ch.qos.logback.classic.Level.toLevel("off".toUpperCase()));
    		status = 200;
    	}
    }
	
    private boolean isPackageLoggerExists(String s) 
    {
    	ch.qos.logback.classic.LoggerContext lc = (ch.qos.logback.classic.LoggerContext) LoggerFactory.getILoggerFactory();
        List<ch.qos.logback.classic.Logger> loggerList = lc.getLoggerList();
        for (ch.qos.logback.classic.Logger logger : loggerList) {
            if (logger.getName().equals(s)) {
            	return true;
            }
        }
        return false;
    }
	
    private boolean isValidLevel(String s) 
    {
    	java.util.List<String> asList = java.util.Arrays.asList("ALL", "DEBUG", "INFO", "WARN", "ERROR", "FATAL", "OFF", "TRACE");
        return asList.contains(s.toUpperCase());
    }
    
    private boolean isValidPackage(String s) 
    {
    	java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("^[a-zA-Z_\\$][\\w\\$]*(?:\\.[a-zA-Z_\\$][\\w\\$]*)*$");
        return pattern.matcher(s).matches();
    }
}
