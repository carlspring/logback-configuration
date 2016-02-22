package org.carlspring.logging.utils;

import org.carlspring.logging.exceptions.LoggingConfigurationException;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Yougeshwar
 */
public class LogBackXMLUtils
{

    public static void addLogger(String packageName, String level) 
            throws LoggingConfigurationException
    {
        try
        {
            URL url = LogBackXMLUtils.class.getClassLoader().getResource(
                    "logback.xml");
            File file = new File(url.toURI());

            DocumentBuilderFactory docFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(file);

            // Get the root element
            Element root = doc.getDocumentElement();

            Element loggerElement = doc.createElement("logger");
            loggerElement.setAttribute("name", packageName);

            root.appendChild(loggerElement);

            Element levelElement = doc.createElement("level");
            levelElement.setAttribute("value", level);

            loggerElement.appendChild(levelElement);

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory
                    .newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(url.toURI()));
            transformer.transform(source, result);
        } 
        catch (URISyntaxException ex)
        {
            throw new LoggingConfigurationException(ex);
        } 
        catch (ParserConfigurationException ex)
        {
            throw new LoggingConfigurationException(ex);
        } 
        catch (SAXException ex)
        {
            throw new LoggingConfigurationException(ex);
        } 
        catch (IOException ex)
        {
            throw new LoggingConfigurationException(ex);
        } 
        catch (TransformerConfigurationException ex)
        {
            throw new LoggingConfigurationException(ex);
        } 
        catch (TransformerException ex)
        {
            throw new LoggingConfigurationException(ex);
        }
    }

    public static void updateLogger(String packageName, String level)
            throws LoggingConfigurationException
    {
        try
        {
            URL url = LogBackXMLUtils.class.getClassLoader().getResource(
                    "logback.xml");
            File file = new File(url.toURI());

            DocumentBuilderFactory docFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(file);

            // Get the root element
            Element root = doc.getDocumentElement();
            Element loggerElement = getElement(packageName,
                    root.getElementsByTagName("logger"));
            Element levelElement = (Element) loggerElement
                    .getElementsByTagName("level").item(0);
            levelElement.setAttribute("value", level);

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory
                    .newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(url.toURI()));
            transformer.transform(source, result);
        } 
        catch (URISyntaxException ex)
        {
            throw new LoggingConfigurationException(ex);
        } 
        catch (ParserConfigurationException ex)
        {
            throw new LoggingConfigurationException(ex);
        } 
        catch (SAXException ex)
        {
            throw new LoggingConfigurationException(ex);
        } 
        catch (IOException ex)
        {
            throw new LoggingConfigurationException(ex);
        } 
        catch (TransformerConfigurationException ex)
        {
            throw new LoggingConfigurationException(ex);
        } 
        catch (TransformerException ex)
        {
            throw new LoggingConfigurationException(ex);
        }
    }

    public static void deleteLogger(String packageName)
            throws LoggingConfigurationException
    {
        try
        {
            URL url = LogBackXMLUtils.class.getClassLoader().getResource(
                    "logback.xml");
            File file = new File(url.toURI());

            DocumentBuilderFactory docFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(file);

            // Get the root element
            Element root = doc.getDocumentElement();
            Element loggerElement = getElement(packageName,
                    root.getElementsByTagName("logger"));
            loggerElement.getParentNode().removeChild(loggerElement);

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory
                    .newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(url.toURI()));
            transformer.transform(source, result);
        } 
        catch (URISyntaxException ex)
        {
            throw new LoggingConfigurationException(ex);
        } 
        catch (ParserConfigurationException ex)
        {
            throw new LoggingConfigurationException(ex);
        } 
        catch (SAXException ex)
        {
            throw new LoggingConfigurationException(ex);
        } 
        catch (IOException ex)
        {
            throw new LoggingConfigurationException(ex);
        } 
        catch (TransformerConfigurationException ex)
        {
            throw new LoggingConfigurationException(ex);
        } 
        catch (TransformerException ex)
        {
            throw new LoggingConfigurationException(ex);
        }
    }

    private static Element getElement(String name, NodeList nodeList)
    {
        if (nodeList == null)
            return null;

        for (int i = 0; i < nodeList.getLength(); i++)
        {
            Element el = (Element) nodeList.item(i);
            if (el.hasAttribute("name") && el.getAttribute("name").equals(name))
            {
                return el;
            }
        }

        return null;
    }
}