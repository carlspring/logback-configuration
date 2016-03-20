package org.carlspring.logging.rest;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

/**
 * @author carlspring
 */
public abstract class AbstractLoggingRestletTestCase
{

    public static final String PACKAGE_NAME = "org.carlspring.logging.test";

    protected TestClient client;


    public void addLogger()
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

    public TestClient getClient()
    {
        return client;
    }

    public void setClient(TestClient client)
    {
        this.client = client;
    }

}
