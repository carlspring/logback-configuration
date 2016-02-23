package org.carlspring.logging.services.impl;

import org.carlspring.logging.services.LoggingManagementService;
import org.carlspring.logging.exceptions.LoggingConfigurationException;
import org.carlspring.logging.exceptions.NoLoggerFoundException;
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
	
	private List<String> asList = 
			Arrays.asList("ALL", "DEBUG", "INFO", "WARN", "ERROR", "FATAL", "OFF", "TRACE");
	
    public void addLogger(String loggerPackage, String level) 
    		throws LoggingConfigurationException
    {
    	synchronized(lock) 
    	{
        	if(!isValidPackage(loggerPackage)) 
        	{
        		throw new LoggingConfigurationException("Invalid package exception");
        	} 
        	else if(!isValidLevel(level)) 
        	{
        		throw new LoggingConfigurationException("Invalid level exception");
        	} 
        	else 
        	{
            	Logger root = (Logger) 
            			LoggerFactory.getLogger(ROOT_LOGGER_NAME);
                Appender<ILoggingEvent> appender = root.getAppender("CONSOLE");
                
                Logger log = (Logger) 
                		LoggerFactory.getLogger(loggerPackage);
                log.setLevel(Level.toLevel(level.toUpperCase()));
                log.setAdditive(false); /* set to true if root should log too */
                log.addAppender(appender);

                LogBackXMLUtils.addLogger(loggerPackage, level);
        	}
    	}
    }
    
    public void updateLogger(String loggerPackage, String level) 
    		throws LoggingConfigurationException, NoLoggerFoundException
    {
    	synchronized(lock) 
    	{
    		if(!isValidPackage(loggerPackage)) 
        	{
        		throw new LoggingConfigurationException("Invalid package exception");
        	} 
        	else if(!isValidLevel(level)) 
        	{
        		throw new LoggingConfigurationException("Invalid level exception");
        	} 
	    	else if(!isPackageLoggerExists(loggerPackage)) 
	    	{
	    		throw new NoLoggerFoundException("Logger not found exception");
	        } 
	    	else 
	    	{
		    	Logger log = (Logger) 
		    			LoggerFactory.getLogger(loggerPackage);
		        log.setLevel(Level.toLevel(level.toUpperCase()));
		        
	            LogBackXMLUtils.updateLogger(loggerPackage, level);
	    	}
    	}
    }
    
    public void deleteLogger(String loggerPackage) 
    		throws LoggingConfigurationException, NoLoggerFoundException 
    {
    	synchronized(lock) 
    	{
	    	if(!isValidPackage(loggerPackage)) 
	    	{
	    		throw new LoggingConfigurationException("Invalid package exception");
	    	} 
	    	else if(!isPackageLoggerExists(loggerPackage)) 
	    	{
	    		throw new NoLoggerFoundException("Logger not found exception");
	    	}
	    	else 
	    	{
		    	Logger log = (Logger) 
		    			LoggerFactory.getLogger(loggerPackage);
		        log.setLevel(Level.toLevel("off".toUpperCase()));

                LogBackXMLUtils.deleteLogger(loggerPackage);
	    	}
    	}
    }
	
    private boolean isPackageLoggerExists(String packageLogger) 
    {
    	LoggerContext lc = (LoggerContext) 
    			LoggerFactory.getILoggerFactory();
    	
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
