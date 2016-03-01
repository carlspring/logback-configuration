package org.carlspring.logging.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.carlspring.logging.test.LogGenerator;
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
public class AddLoggingRestletTest
{

    public static final String PACKAGE_NAME = "org.carlspring.logging.test";
    
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
        String path = "/logging/logger?" +
                "logger=" + PACKAGE_NAME;

		Response response = client.delete(path);
		
		assertEquals("Failed to delete logger!", Response.ok().build().getStatus(), response.getStatus());
		
        if (client != null)
        {
            client.close();
        }
    }

    @Test
    public void testAddLogger() throws Exception
    {
        String url = client.getContextBaseUrl() +
                     "/logging/logger?" +
                     "logger=" + PACKAGE_NAME +
                     "&" +
                     "level=DEBUG&" +
                     "appenderName=FILE";

        WebTarget resource = client.getClientInstance().target(url);

        Response response = resource.request(MediaType.TEXT_PLAIN)
                                    .put(Entity.entity("Add", MediaType.TEXT_PLAIN));

        int status = response.getStatus();

        assertEquals("Failed to add logger!", Response.ok().build().getStatus(), status);

        /** 
         * Checking that the logback.xml contains the new logger. 
         * */
        url = client.getContextBaseUrl() + 
		           "/logging/logger/logback";

        resource = client.getClientInstance().target(url);

        response = resource.request(MediaType.TEXT_PLAIN)
		                    .get();

        status = response.getStatus();
        assertEquals("Failed to get log file!", Response.ok().build().getStatus(), status);

        assertTrue(response.readEntity(String.class).contains("org.carlspring.logging.test"));
        
        /**
         * Generate log to intercept
         * */
//        LogGenerator logGen = new LogGenerator();
//        logGen.debugLog();
//        
//        url = client.getContextBaseUrl() + 
//		           "/logging/logger/log";
//
//		resource = client.getClientInstance().target(url);
//		
//		response = resource.request(MediaType.TEXT_PLAIN)
//			                    .get();
//		
//		status = response.getStatus();
//		assertEquals("Failed to get log file!", Response.ok().build().getStatus(), status);
//		
//		assertTrue(response.readEntity(String.class).contains("debug log"));
		 
        
        // TODO: 2) Intercept the logging and check that the output is
        // TODO:    really being printed to the respective level
    }

}
