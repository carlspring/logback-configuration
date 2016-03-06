package org.carlspring.logging;

import static org.junit.Assert.assertNull;

import org.carlspring.logging.exceptions.LoggerNotFoundException;
import org.carlspring.logging.exceptions.LoggingConfigurationException;
import org.carlspring.logging.services.LoggingManagementService;
import org.carlspring.logging.test.LogGenerator;
import org.carlspring.logging.utils.LogBackXmlConfiguration;
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
public class DeleteLoggerTest
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
        loggingManagementService.updateLogger(PACKAGE_NAME, "info");
    }

    @Test
    public void testDeleteLogger()
            throws LoggingConfigurationException,
                   LoggerNotFoundException
    {
        loggingManagementService.deleteLogger(PACKAGE_NAME);

        LogGenerator lg = new LogGenerator();
        lg.debugLog();

        // Getting logger from file, if its not in file it will throw exception
        LogBackXmlConfiguration obj = new LogBackXmlConfiguration(null);
        Logger logger = obj.getLogger(PACKAGE_NAME);

        assertNull("Failed to delete logger!", logger);
    }

}
