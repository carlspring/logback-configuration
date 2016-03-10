package org.carlspring.logging.exceptions;

/**
 * 
 * @author Yougeshwar
 */
public class LoggerNotFoundException
        extends Exception
{
	public LoggerNotFoundException() 
	{
    }

    public LoggerNotFoundException(String msg) 
    {
        super(msg);
    }
    
    public LoggerNotFoundException(String msg, Throwable cause) 
    {
        super(msg, cause);
    }
    
    public LoggerNotFoundException(Throwable cause) 
    {
        super(cause);
    }
}
