package org.carlspring.logging.utils;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

import org.carlspring.logging.Appender;
import org.carlspring.logging.AppenderRef;
import org.carlspring.logging.Configuration;
import org.carlspring.logging.Logger;
import org.carlspring.logging.ObjectFactory;
import org.carlspring.logging.exceptions.AppenderNotFoundException;
import org.carlspring.logging.exceptions.LoggerNotFoundException;
import org.carlspring.logging.exceptions.LoggingConfigurationException;

/**
 *
 * @author Yougeshwar
 */
public class LogBackXMLUtils
{
    private static Configuration configuration;

    static
    {
        try
        {
            configuration = unmarshalLogbackXML();
        }
        catch (LoggingConfigurationException e)
        {
            e.printStackTrace();
        }
    }

    public static void addLogger(String packageName, String levelName, String appenderName)
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
            logger.getAppenderRefOrAny().add(rf);

            configuration.getStatusListenerOrContextListenerOrInclude().add(
                    new JAXBElement<Logger>(new QName("logger"), Logger.class, logger));

            marshalLogbackXML(configuration);
        }
        catch (Exception ex)
        {
            throw new LoggingConfigurationException(ex);
        }
    }

    public static void updateLogger(String packageName, String levelName) throws LoggingConfigurationException
    {
        try
        {
            Configuration configuration = unmarshalLogbackXML();

            Logger logger = getLogger(packageName);
            logger.setName(packageName);
            logger.setLevel(levelName);

            marshalLogbackXML(configuration);
        }
        catch (Exception ex)
        {
            throw new LoggingConfigurationException(ex);
        }
    }

    public static void deleteLogger(String packageName) throws LoggingConfigurationException
    {
        try
        {
            Configuration configuration = unmarshalLogbackXML();

            JAXBElement<Logger> logger = getLoggerJAXBElement(packageName);

            configuration.getStatusListenerOrContextListenerOrInclude().remove(logger);

            marshalLogbackXML(configuration);
        }
        catch (Exception ex)
        {
            throw new LoggingConfigurationException(ex);
        }
    }

    public static void checkAppender(String appenderName) throws AppenderNotFoundException
    {
        List<Object> list = configuration.getStatusListenerOrContextListenerOrInclude();
        for (Object obj : list)
        {
            JAXBElement<?> el = (JAXBElement<?>) obj;
            if (!(el.getValue() instanceof Appender))
                continue;

            Appender appender = (Appender) el.getValue();
            if (appender.getName().equals(appenderName))
            {
                return;
            }
        }
        throw new AppenderNotFoundException("Appender not found exception");
    }

    public static Logger getLogger(String packageName) throws LoggerNotFoundException
    {
        List<Object> list = configuration.getStatusListenerOrContextListenerOrInclude();
        for (Object obj : list)
        {
            JAXBElement<?> el = (JAXBElement<?>) obj;
            if (!(el.getValue() instanceof Logger))
                continue;

            Logger logger = (Logger) el.getValue();
            if (logger.getName().equals(packageName))
            {
                return logger;
            }
        }
        throw new LoggerNotFoundException("Logger not found exception");
    }

    @SuppressWarnings("unchecked")
    public static JAXBElement<Logger> getLoggerJAXBElement(String packageName) throws LoggerNotFoundException
    {
        List<Object> list = configuration.getStatusListenerOrContextListenerOrInclude();
        for (Object obj : list)
        {
            JAXBElement<?> el = (JAXBElement<?>) obj;
            if (!(el.getValue() instanceof Logger))
                continue;

            Logger logger = (Logger) el.getValue();
            if (logger.getName().equals(packageName))
            {
                return (JAXBElement<Logger>) el;
            }
        }
        throw new LoggerNotFoundException("Logger not found exception");
    }

    public static void marshalLogbackXML(Configuration configuration) throws LoggingConfigurationException
    {
        try
        {
            URL url = LogBackXMLUtils.class.getClassLoader().getResource("logback.xml");
            File file = new File(url.toURI());

            JAXBContext jaxbContext = JAXBContext.newInstance(Configuration.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            // jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
            // true);

            FileWriter fw = null;
            try
            {
                fw = new FileWriter(file, true);
                jaxbMarshaller.marshal(configuration, fw);
            }
            finally
            {
                if (fw != null)
                    fw.close();
            }

            jaxbMarshaller.marshal(configuration, System.out);
        }
        catch (Exception ex)
        {
            throw new LoggingConfigurationException(ex);
        }
    }

    @SuppressWarnings("unchecked")
    public static Configuration unmarshalLogbackXML() throws LoggingConfigurationException
    {
        if (configuration != null)
            return configuration;
        try
        {
            URL url = LogBackXMLUtils.class.getClassLoader().getResource("logback.xml");
            File file = new File(url.toURI());

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
}
