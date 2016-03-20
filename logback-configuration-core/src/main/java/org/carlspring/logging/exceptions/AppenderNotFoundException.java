package org.carlspring.logging.exceptions;

/**
 * 
 * @author Yougeshwar
 */
public class AppenderNotFoundException
        extends Exception
{
	public AppenderNotFoundException() 
	{
    }

    public AppenderNotFoundException(String msg) 
    {
        super(msg);
    }
    
    public AppenderNotFoundException(String msg, Throwable cause) 
    {
        super(msg, cause);
    }
    
    public AppenderNotFoundException(Throwable cause) 
    {
        super(cause);
    }
}
