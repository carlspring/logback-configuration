package org.carlspring.logging;

import org.carlspring.logging.exceptions.AppenderNotFoundException;
import org.carlspring.logging.exceptions.LoggerNotFoundException;
import org.carlspring.logging.exceptions.LoggingConfigurationException;
import org.carlspring.logging.services.LoggingManagementService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "/META-INF/spring/spring-*-context.xml",
        "classpath*:/META-INF/spring/spring-*-context.xml" })
public class LoggerCoreTest
{
    @Autowired
    private LoggingManagementService loggingManagementService;

    @Test
    public void testLogger() 
    {
        testAddLogger();
        testUpdateLogger();
        testDeleteLogger();
    }
    
    public void testAddLogger()
    {
        try
        {
            loggingManagementService.addLogger("com.yog.test", "debug", "console");
            Assert.assertTrue(true);
        } 
        catch (LoggingConfigurationException e)
        {
            Assert.assertTrue(false);
        } 
        catch (AppenderNotFoundException e)
        {
            Assert.assertTrue(false);
        }
    }

    public void testUpdateLogger()
    {
        try
        {
            loggingManagementService.updateLogger("com.yog.test", "info");
            Assert.assertTrue(true);
        } 
        catch (LoggingConfigurationException e)
        {
            Assert.assertTrue(false);
        } 
        catch (LoggerNotFoundException e)
        {
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    public void testDeleteLogger()
    {
        try
        {
            loggingManagementService.deleteLogger("com.yog.test");
            Assert.assertTrue(true);
        } 
        catch (LoggingConfigurationException e)
        {
            Assert.assertTrue(false);
        } 
        catch (LoggerNotFoundException e)
        {
            Assert.assertTrue(false);
        }
    }

}
