package org.carlspring.logging.exceptions;

/**
 * @author carlspring
 */
public class NoLoggerFoundException
        extends Exception
{
	public NoLoggerFoundException() 
	{
    }

    public NoLoggerFoundException(String msg) 
    {
        super(msg);
    }
    
    public NoLoggerFoundException(String msg, Throwable cause) 
    {
        super(msg, cause);
    }
    
    public NoLoggerFoundException(Throwable cause) 
    {
        super(cause);
    }
}
