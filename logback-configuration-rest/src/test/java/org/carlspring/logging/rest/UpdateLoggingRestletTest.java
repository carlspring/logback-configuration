package org.carlspring.logging.rest;

import org.carlspring.logging.test.LogGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author carlspring
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/META-INF/spring/logging-*-context.xml",
                                    "classpath*:/META-INF/spring/logging-*-context.xml" })
public class UpdateLoggingRestletTest
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
        String path = "/logging/logger?logger=" + PACKAGE_NAME;

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
                     "logger=" + PACKAGE_NAME + "&" +
                     "level=INFO";

        // 1) Update the logger
        WebTarget target = client.getClientInstance().target(url);
        Response response = target.request(MediaType.TEXT_PLAIN)
                                  .post(Entity.entity("Update", MediaType.TEXT_PLAIN));

        int status = response.getStatus();

        assertEquals("Failed to update logger!", Response.ok().build().getStatus(), status);

        System.out.println(response.getStatus());
        System.out.println(response.readEntity(String.class));

        // 2) Generate an info message
        LogGenerator generator = new LogGenerator();
        String message = "This is an info message test!";
        generator.info(message);

        resetClient();

        // 3) Check that the logback.xml contains the new logger.
        url = client.getContextBaseUrl() + "/logging/logback";

        target = client.getClientInstance().target(url);
        response = target.request(MediaType.APPLICATION_XML).get();

        status = response.getStatus();
        assertEquals("Failed to get logback config file!", Response.ok().build().getStatus(), status);

        resetClient();

        // 4) Get the log file and check that it's not empty
        System.out.println("Retrieving log file...");

        url = client.getContextBaseUrl() + "/logging/log/test.log";

        target = client.getClientInstance().target(url);
        response = target.request(/*MediaType.TEXT_PLAIN*/).get();

        status = response.getStatus();

        System.out.println("Retrieved log file. Status: " + response.getStatus());

        assertEquals("Failed to retrieve log file!", Response.ok().build().getStatus(), status);

        InputStream bais = (InputStream) response.getEntity();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int readLength;
        byte[] bytes = new byte[4096];
        while ((readLength = bais.read(bytes, 0, bytes.length)) != -1)
        {
            // Write the artifact
            baos.write(bytes, 0, readLength);
            baos.flush();
        }

        String log = new String(baos.toByteArray());

        System.out.println("Retrieved log file:");
        System.out.println(log);

        assertTrue(log.contains(message));
    }

}
