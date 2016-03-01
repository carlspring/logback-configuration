package org.carlspring.logging.rest;

import static org.junit.Assert.assertEquals;

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
public class UpdateLoggingRestletTest
{

    private TestClient client;


    @Before
    public void setUp() throws Exception
    {
        client = TestClient.getTestInstance();
        String url = client.getContextBaseUrl() +
                "/logging/logger?" +
                "logger=org.carlspring.logging.test&" +
                "level=DEBUG&" +
                "appenderName=CONSOLE";

	   WebTarget resource = client.getClientInstance().target(url);
	
	   Response response = resource.request(MediaType.TEXT_PLAIN)
	                               .put(Entity.entity("Add", MediaType.TEXT_PLAIN));
	
	   int status = response.getStatus();
	
	   assertEquals("Failed to add logger!", Response.ok().build().getStatus(), status);
	}

    @After
    public void tearDown()
            throws Exception
    {
        String path = "/logging/logger?" +
                "logger=org.carlspring.logging.test";

		Response response = client.delete(path);
		
		assertEquals("Failed to delete logger!", Response.ok().build().getStatus(), response.getStatus());
		
        if (client != null)
        {
            client.close();
        }
    }
    
    @Test
    public void testUpdateLogger() throws Exception
    {
        String url = client.getContextBaseUrl() +
                     "/logging/logger?" +
                     "logger=org.carlspring.logging.test&" +
                     "level=INFO";

        WebTarget resource = client.getClientInstance().target(url);

        Response response = resource.request(MediaType.TEXT_PLAIN)
                                    .post(Entity.entity("Update", MediaType.TEXT_PLAIN));

        int status = response.getStatus();

        assertEquals("Failed to update logger!", Response.ok().build().getStatus(), status);

//        // Checking that the logback.xml contains the new logger.
//        url = client.getContextBaseUrl() + 
//		           "/logging/logger/logback";
//
//        resource = client.getClientInstance().target(url);
//
//        response = resource.request(MediaType.TEXT_PLAIN)
//		                    .get();
//
//        status = response.getStatus();
//        assertEquals("Failed to get log file!", Response.ok().build().getStatus(), status);
//
//        assertTrue(response.toString().contains("org.carlspring.logging.test"));
        
        // TODO: 2) Intercept the logging and check that the output is
        // TODO:    really being printed to the respective level
    }
}
