package org.carlspring.logging.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author carlspring
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/META-INF/spring/logging-*-context.xml",
                                    "classpath*:/META-INF/spring/logging-*-context.xml" })
public class DeleteLoggingRestletTest
        extends AbstractLoggingRestletTestCase
{


    @Before
    public void setUp() throws Exception
    {
        addLogger();
    }

    @After
    public void tearDown()
            throws Exception
    {
        if (client != null)
        {
            client.close();
        }
    }
    
    @Test
    public void testDeleteLogger() throws Exception
    {
        String path = "/logging/logger?" +
                      "logger=" + PACKAGE_NAME + "&" +
                      "level=INFO";

        Response response = client.delete(path);

        assertEquals("Failed to delete logger!", Response.ok().build().getStatus(), response.getStatus());

        resetClient();

        // Checking that the logback.xml contains the new logger.
        String url = client.getContextBaseUrl() + "/logging/logback";

        WebTarget resource = client.getClientInstance().target(url);

        response = resource.request(MediaType.APPLICATION_XML).get();

        int status = response.getStatus();
        assertEquals("Failed to get log file!", Response.ok().build().getStatus(), status);

        assertFalse(response.toString().contains(PACKAGE_NAME));
    }

}
