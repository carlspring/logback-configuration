package org.carlspring.logging.exceptions;

/**
 * @author carlspring
 */
public class LoggingConfigurationException
        extends Exception
{
	public LoggingConfigurationException() 
	{
    }

    public LoggingConfigurationException(String msg) 
    {
        super(msg);
    }
    
    public LoggingConfigurationException(String msg, Throwable cause) 
    {
        super(msg, cause);
    }
    
    public LoggingConfigurationException(Throwable cause) 
    {
        super(cause);
    }
}
