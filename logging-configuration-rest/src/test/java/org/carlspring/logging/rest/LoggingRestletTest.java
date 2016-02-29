package org.carlspring.logging.rest;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

/**
 * @author carlspring
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/META-INF/spring/logging-*-context.xml",
                                    "classpath*:/META-INF/spring/logging-*-context.xml" })
public class LoggingRestletTest
{

    private TestClient client;


    @Before
    public void setUp() throws Exception
    {
        client = TestClient.getTestInstance();
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

    @Ignore
    @Test
    public void testAddLogger() throws Exception
    {
        String url = client.getContextBaseUrl() +
                     "/logger?" +
                     "logger=org.carlspring.logging.test&" +
                     "level=DEBUG&" +
                     "appenderName=CONSOLE";

        WebTarget resource = client.getClientInstance().target(url);

        Response response = resource.request(MediaType.TEXT_PLAIN)
                                    .post(Entity.entity("Add", MediaType.TEXT_PLAIN));

        int status = response.getStatus();

        assertEquals("Failed to add logger!", Response.ok(), status);

        url = client.getContextBaseUrl() +
                "/logger?" +
                "logger=org.carlspring.logging.test&" +
                "level=DEBUG&" +
                "appenderName=CONSOLE";
        
        
        // TODO: 1) Check that the logback.xml contains the new logger.
        // TODO: 2) Intercept the logging and check that the output is
        // TODO:    really being printed to the respective level
    }

    @Ignore
    @Test
    public void testUpdateLogger() throws Exception
    {
        String url = client.getContextBaseUrl() +
                     "/logger?" +
                     "logger=org.carlspring.logging.test&" +
                     "level=INFO";

        WebTarget resource = client.getClientInstance().target(url);

        Response response = resource.request(MediaType.TEXT_PLAIN)
                                    .post(Entity.entity("Update", MediaType.TEXT_PLAIN));

        int status = response.getStatus();

        assertEquals("Failed to update logger!", Response.ok(), status);

        // TODO: 1) Check that the logback.xml really was updated as expected.
        // TODO: 2) Intercept the logging and check that the output is
        // TODO:    really being printed to the respective level
    }

    @Ignore
    @Test
    public void testDeleteLogger() throws Exception
    {
        String path = "/logger?" +
                      "logger=org.carlspring.logging.test&" +
                      "level=INFO";

        Response response = client.delete(path);

        assertEquals("Failed to delete logger!", Response.ok(), response.getStatus());


        // TODO: Check that the logback.xml really has the logger removed.
    }

    // TODO: Add test cases for the rest of the things mentioned as things at the end of the

}
