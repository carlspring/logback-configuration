package org.carlspring.logging;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.carlspring.logging.exceptions.AppenderNotFoundException;
import org.carlspring.logging.exceptions.LoggerNotFoundException;
import org.carlspring.logging.exceptions.LoggingConfigurationException;
import org.carlspring.logging.services.LoggingManagementService;
import org.carlspring.logging.test.LogGenerator;
import org.carlspring.logging.utils.LogBackXMLUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.carmatechnologies.commons.testing.logging.ExpectedLogs;
import com.carmatechnologies.commons.testing.logging.api.LogLevel;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/META-INF/spring/logging-*-context.xml",
        "classpath*:/META-INF/spring/logging-*-context.xml" })
public class AddLoggerTest
{

    public static final String PACKAGE_NAME = "org.carlspring.logging.test";


    @Autowired
    private LoggingManagementService loggingManagementService;
    
    @Rule
    public final ExpectedLogs debugLogs = new ExpectedLogs()
    {{
        captureFor(LogGenerator.class, LogLevel.DEBUG);
    }};


    @Before
    public void setUp() throws Exception
    {
        loggingManagementService.addLogger(PACKAGE_NAME, "debug", "CONSOLE");
    }

    @Test
    public void testAddLogger()
            throws LoggingConfigurationException,
                   AppenderNotFoundException,
                   LoggerNotFoundException
    {
        LogGenerator lg = new LogGenerator();
        lg.debugLog();
        
        assertThat(debugLogs.contains("debug log"), is(true));
        
        // Getting logger from file, if its not in file it will throw exception
        Logger logger = LogBackXMLUtils.getLogger(PACKAGE_NAME);
        assertNotNull(logger);
    }

}
