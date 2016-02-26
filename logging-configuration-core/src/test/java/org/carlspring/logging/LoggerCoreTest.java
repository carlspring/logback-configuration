package org.carlspring.logging;

import org.carlspring.logging.exceptions.AppenderNotFoundException;
import org.carlspring.logging.exceptions.LoggerNotFoundException;
import org.carlspring.logging.exceptions.LoggingConfigurationException;
import org.carlspring.logging.services.LoggingManagementService;
import org.carlspring.logging.utils.LogBackXMLUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/META-INF/spring/logging-*-context.xml",
        "classpath*:/META-INF/spring/logging-*-context.xml" })
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
            String packageName = "com.yog.test";
            loggingManagementService.addLogger(packageName, "debug", "CONSOLE");
            
            LogBackXMLUtils.getLogger(packageName);
            Assert.assertTrue(true);
        }
        catch (LoggingConfigurationException e)
        {
            e.printStackTrace();
            Assert.assertTrue(false);
        }
        catch (AppenderNotFoundException e)
        {
            e.printStackTrace();
            Assert.assertTrue(false);
        }
        catch (LoggerNotFoundException e)
        {
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    public void testUpdateLogger()
    {
        try
        {
            String packageName = "com.yog.test";
            loggingManagementService.updateLogger(packageName, "info");
            LogBackXMLUtils.getLogger(packageName);
            Assert.assertTrue(true);
        }
        catch (LoggingConfigurationException e)
        {
            e.printStackTrace();
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
            String packageName = "com.yog.test";
            loggingManagementService.deleteLogger(packageName);
            LogBackXMLUtils.getLogger(packageName);
            Assert.assertTrue(false);
        }
        catch (LoggingConfigurationException e)
        {
            e.printStackTrace();
            Assert.assertTrue(false);
        }
        catch (LoggerNotFoundException e)
        {
            Assert.assertTrue(true);
        }
    }

}
