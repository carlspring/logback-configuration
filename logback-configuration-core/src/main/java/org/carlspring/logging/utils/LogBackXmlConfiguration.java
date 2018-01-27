package org.carlspring.logging.utils;

import org.carlspring.logging.*;
import org.carlspring.logging.exceptions.AppenderNotFoundException;
import org.carlspring.logging.exceptions.LoggerNotFoundException;
import org.carlspring.logging.exceptions.LoggingConfigurationException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.LoggerFactory;

/**
 * @author Yougeshwar
 * @author Pablo Tirado
 */
public class LogBackXmlConfiguration
{

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(LogBackXmlConfiguration.class);
    
    private String pathToXml;
    
    public LogBackXmlConfiguration(String pathToXml)
    {
        this.pathToXml = pathToXml;
        this.pathToXml = pathToXml != null ? pathToXml : "logback.xml";
    }

    public void addLogger(String packageName, String levelName, String appenderName)
            throws LoggingConfigurationException
    {
        try
        {
            checkAppender(appenderName);

            ObjectFactory of = new ObjectFactory();

            Logger logger = of.createLogger();
            logger.setName(packageName);
            logger.setLevel(levelName);

            AppenderRef rf = of.createAppenderRef();
            rf.setRef(appenderName);
            logger.getAppenderRefOrAny().add(of.createLoggerAppenderRef(rf));

            of.createConfigurationLogger(logger);

            Configuration configuration = unmarshalLogbackXML();
            configuration.getStatusListenerOrContextListenerOrInclude().add(of.createConfigurationLogger(logger));

            marshalLogbackXML(configuration);
        }
        catch (Exception ex)
        {
            throw new LoggingConfigurationException(ex);
        }
    }

    public void updateLogger(String packageName, String levelName)
            throws LoggingConfigurationException
    {
        try
        {
            Configuration configuration = unmarshalLogbackXML();

            Logger logger = getLogger(packageName, configuration);
            logger.setLevel(levelName);

            marshalLogbackXML(configuration);
        }
        catch (Exception ex)
        {
            throw new LoggingConfigurationException(ex);
        }
    }

    public void deleteLogger(String packageName)
            throws LoggingConfigurationException
    {
        try
        {
            Configuration configuration = unmarshalLogbackXML();

            JAXBElement<Logger> logger = getLoggerJAXBElement(packageName, configuration);

            configuration.getStatusListenerOrContextListenerOrInclude().remove(logger);

            marshalLogbackXML(configuration);
        }
        catch (Exception ex)
        {
            throw new LoggingConfigurationException(ex);
        }
    }

    public void checkAppender(String appenderName)
            throws AppenderNotFoundException,
                   LoggingConfigurationException
    {
        Configuration configuration = unmarshalLogbackXML();
        List<Object> list = configuration.getStatusListenerOrContextListenerOrInclude();
        for (Object obj : list)
        {
            JAXBElement<?> el = (JAXBElement<?>) obj;
            if (!(el.getValue() instanceof Appender))
            {
                continue;
            }

            Appender appender = (Appender) el.getValue();
            if (appender.getName().equals(appenderName))
            {
                return;
            }
        }

        throw new AppenderNotFoundException("Appender not found!");
    }

    public Logger getLogger(String packageName)
            throws LoggingConfigurationException
    {
        return getLogger(packageName, unmarshalLogbackXML());
    }

    public Logger getLogger(String packageName, Configuration configuration)
            throws LoggingConfigurationException
    {
        List<Object> list = configuration.getStatusListenerOrContextListenerOrInclude();
        for (Object obj : list)
        {
            JAXBElement<?> el = (JAXBElement<?>) obj;
            if (!(el.getValue() instanceof Logger))
            {
                continue;
            }

            Logger logger = (Logger) el.getValue();
            if (logger.getName().equals(packageName))
            {
                return logger;
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private JAXBElement<Logger> getLoggerJAXBElement(String packageName,
                                                            Configuration configuration)
            throws LoggerNotFoundException,
                   LoggingConfigurationException
    {
        List<Object> list = configuration.getStatusListenerOrContextListenerOrInclude();
        for (Object obj : list)
        {
            JAXBElement<?> el = (JAXBElement<?>) obj;
            if (!(el.getValue() instanceof Logger))
            {
                continue;
            }

            Logger logger = (Logger) el.getValue();
            if (logger.getName().equals(packageName))
            {
                return (JAXBElement<Logger>) el;
            }
        }

        throw new LoggerNotFoundException("Logger '" + packageName + "' not found!");
    }

    public void marshalLogbackXML(Configuration configuration)
            throws LoggingConfigurationException
    {
        try
        {
            File file = resolveLogbackConfigurationFile(this.pathToXml);

            JAXBContext jaxbContext = JAXBContext.newInstance(Configuration.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            ObjectFactory of = new ObjectFactory();

            JAXBElement<Configuration> el = of.createConfiguration(configuration);
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(el, file);
            jaxbMarshaller.marshal(el, System.out);
        }
        catch (Exception ex)
        {
            throw new LoggingConfigurationException(ex);
        }
    }

    @SuppressWarnings("unchecked")
    public Configuration unmarshalLogbackXML() throws LoggingConfigurationException
    {
        try
        {
            File file = resolveLogbackConfigurationFile(this.pathToXml);

            JAXBContext jaxbContext = JAXBContext.newInstance(Configuration.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            JAXBElement<Configuration> el = (JAXBElement<Configuration>) jaxbUnmarshaller.unmarshal(file);

            return el.getValue();
        }
        catch (Exception ex)
        {
            throw new LoggingConfigurationException(ex);
        }
    }

    public static File resolveLogbackConfigurationFile(String pathToXml)
            throws URISyntaxException, FileNotFoundException
    {
        Path path;
        URL url = LogBackXmlConfiguration.class.getClassLoader().getResource(pathToXml);
        if (url != null)
        {
            logger.debug("Resolved the Logback configuration class from the classpath ({}).", url.toURI());

            path = Paths.get(url.toURI());
        }
        else
        {
            path = Paths.get(pathToXml);
            if (!path.toFile().exists())
            {
                throw new FileNotFoundException("Failed to locate the Logback configuration file!");
            }

            logger.debug("Resolved the Logback configuration class from the file system ({}).", path.toAbsolutePath());
        }
        
        return path.toFile();
    }
    
}
