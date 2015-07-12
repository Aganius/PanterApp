package com.panter.panterapp.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Util {

    public static Element[] getHeader() {
        Element header = new Element().createElement(AppConstants.NAMESPACE, "AuthHeader");
        Element username = new Element().createElement(AppConstants.NAMESPACE, "UserName");
        username.addChild(Node.TEXT, "usuario");
        header.addChild(Node.ELEMENT, username);
        Element password = new Element().createElement(AppConstants.NAMESPACE, "Password");
        password.addChild(Node.TEXT, "hola");
        header.addChild(Node.ELEMENT, password);
        return new Element[] { header };
    }

    static public String format(String unformattedXml) {
        StringWriter writer = new StringWriter();
        try {
            Document doc = parseXml(unformattedXml);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            writer.close();
        }
        catch (Exception ex) {
            return unformattedXml;
        }

        return writer.toString();
    }

    private static Document parseXml(String xml) throws IOException, ParserConfigurationException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return db.parse(is);
    }
}
