package org.carlspring.logging.rest;

import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author mtodorov
 */
public class TestClient implements Closeable
{

    private static final Logger logger = LoggerFactory.getLogger(TestClient.class);
    
    private String protocol = "http";

    private String host = System.getProperty("logging.host") != null ?
                          System.getProperty("logging.host") :
                          "localhost";

    private int port = System.getProperty("logging.port") != null ?
                       Integer.parseInt(System.getProperty("logging.port")) :
                       48080;

    private String contextBaseUrl = "/logging";

    private Client client;


    public TestClient()
    {
    }

    public static TestClient getTestInstance()
    {
        String host = System.getProperty("logging.host") != null ?
                      System.getProperty("logging.host") :
                      "localhost";

        int port = System.getProperty("logging.port") != null ?
                   Integer.parseInt(System.getProperty("logging.port")) :
                   48080;

        TestClient client = new TestClient();
        client.setPort(port);
        client.setContextBaseUrl("http://" + host + ":" + client.getPort());

        return client;
    }

    public Client getClientInstance()
    {
        if (client == null)
        {
            ClientConfig config = getClientConfig();
            client = ClientBuilder.newClient(config);

            return client;
        }
        else
        {
            return client;
        }
    }

    private ClientConfig getClientConfig()
    {
        ClientConfig config = new ClientConfig();
        config.connectorProvider(new ApacheConnectorProvider());

        return config;
    }

    @Override
    public void close()
    {
        if (client != null)
        {
            client.close();
        }
    }

    public InputStream getResource(String path)
            throws IOException
    {
        return getResource(path, 0);
    }

    public InputStream getResource(String path, long offset)
            throws IOException
    {
        String url = getContextBaseUrl() + (!path.startsWith("/") ? "/" : "") + path;

        logger.debug("Getting " + url + "...");

        WebTarget resource = getClientInstance().target(url);

        Invocation.Builder request = resource.request();
        Response response;

        if (offset > 0)
        {
            response = request.header("Range", "bytes=" + offset + "-").get();
        }
        else
        {
            response = request.get();
        }

        return response.readEntity(InputStream.class);
    }

    public Response getResourceWithResponse(String path)
            throws IOException
    {
        String url = getContextBaseUrl() + (!path.startsWith("/") ? "/" : "") + path;

        logger.debug("Getting " + url + "...");

        WebTarget resource = getClientInstance().target(url);

        return resource.request(MediaType.TEXT_PLAIN).get();
    }

    public Response delete(String path)
    {
        @SuppressWarnings("ConstantConditions")
        String url = getContextBaseUrl() + (path.endsWith("/") ? "" : "/") + path;

        WebTarget resource = getClientInstance().target(url);

        Response response = resource.request().delete();

        handleFailures(response, "Failed to delete artifact!");

        return response;
    }

    public boolean pathExists(String path)
    {
        String url = getContextBaseUrl() + (path.startsWith("/") ? path : '/' + path);

        logger.debug("Path to artifact: " + url);

        WebTarget resource = getClientInstance().target(url);

        Response response = resource.request(MediaType.TEXT_PLAIN).get();

        return response.getStatus() == 200;
    }

    private void handleFailures(Response response, String message)
    {
        int status = response.getStatus();
        if (status != 200)
        {
            Object entity = response.getEntity();

            if (entity != null && entity instanceof String)
            {
                logger.error(message);
                logger.error((String) entity);
            }
        }
    }

    public String getProtocol()
    {
        return protocol;
    }

    public void setProtocol(String protocol)
    {
        this.protocol = protocol;
    }

    public String getHost()
    {
        return host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public String getContextBaseUrl()
    {
        if (contextBaseUrl == null)
        {
            contextBaseUrl = protocol + "://" + host + ":" + port;
        }

        return contextBaseUrl;
    }

    public void setContextBaseUrl(String contextBaseUrl)
    {
        this.contextBaseUrl = contextBaseUrl;
    }

}
