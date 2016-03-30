package org.carlspring.logging.rest;

import org.carlspring.logging.test.LogGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author carlspring
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/META-INF/spring/logging-*-context.xml",
                                    "classpath*:/META-INF/spring/logging-*-context.xml" })
public class TailFileLoggingRestletTest
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
    public void testTailFileLogger() throws Exception
    {
        // Generating log 1st time to get only new log
        LogGenerator generator = new LogGenerator();
        String message = "This is an log message test!";
        generator.info(message);

        // Checking that the logback.xml contains the new logger.
        String url = client.getContextBaseUrl() + "/logging/partial-log/test.log?offset=0";

        Object[] objs = getLogOnRequest(url);

        String log = (String) objs[0];
        byte[] data = (byte[]) objs[1];

        System.out.println("Bytes-Length: " + data.length);
        System.out.println("Retrieved log file:");
        System.out.println(log);

        assertTrue(log.contains(message));

        // Generating log 2nd time to get only new log
        url = client.getContextBaseUrl() + "/logging/partial-log/test.log?offset=" + data.length;
        message = "This is an new log message test!";
        generator.info(message);

        objs = getLogOnRequest(url);
        log = (String) objs[0];
        data = (byte[]) objs[1];

        System.out.println("Bytes-Length: " + data.length);
        System.out.println("2nd Retrieved log file:");
        System.out.println(log);

        assertTrue(log.contains(message));
    }

    private Object[] getLogOnRequest(String url) throws IOException {
        WebTarget resource = client.getClientInstance().target(url);
        Response response = resource.request(MediaType.TEXT_PLAIN).get();

        int status = response.getStatus();

        assertEquals("Failed to retrieve log file!", Response.ok().build().getStatus(), status);

        InputStream bais = (InputStream) response.getEntity();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int readLength;
        byte[] bytes = new byte[bais.available()];
        while ((readLength = bais.read(bytes, 0, bytes.length)) != -1)
        {
            // Write the stream
            baos.write(bytes, 0, readLength);
            baos.flush();
        }

        String log = new String(baos.toByteArray());

        return new Object[]{log, baos.toByteArray()};
    }

}
