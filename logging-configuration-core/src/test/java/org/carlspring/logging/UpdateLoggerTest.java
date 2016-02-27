package org.carlspring.logging;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

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
public class UpdateLoggerTest
{

    public static final String PACKAGE_NAME = "org.carlspring.logging.test";

    @Autowired
    private LoggingManagementService loggingManagementService;

    @Rule
    public final ExpectedLogs infoLogs = new ExpectedLogs()
    {{
        captureFor(LogGenerator.class, LogLevel.INFO);
    }};


    @Before
    public void setUp() throws Exception
    {
        loggingManagementService.addLogger(PACKAGE_NAME, "debug", "CONSOLE");
        loggingManagementService.updateLogger(PACKAGE_NAME, "info");
    }

    @Test
    public void testUpdateLogger() throws LoggingConfigurationException, LoggerNotFoundException
    {
        LogGenerator lg = new LogGenerator();
        lg.infoLog();

        assertThat(infoLogs.contains("info log"), is(true));

        // Getting logger from file, if its not in file it will throw exception
        Logger logger = LogBackXMLUtils.getLogger(PACKAGE_NAME);
        assertNotNull(logger);
    }

}
