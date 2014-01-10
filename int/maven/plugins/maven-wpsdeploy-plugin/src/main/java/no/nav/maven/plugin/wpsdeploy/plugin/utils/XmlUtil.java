package no.nav.maven.plugin.wpsdeploy.plugin.utils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import no.nav.maven.plugin.wpsdeploy.plugin.exceptions.ConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.io.StringReader;

public class XmlUtil {
	public static Document getDocument(String xml){
		String exeptionString = "Error parsing the XML in XmlUtil.getDocument()";
		Document document;
		DocumentBuilder db;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		StringReader sr = new StringReader(xml);
		InputSource src = new InputSource(sr);

		try {
			db = dbf.newDocumentBuilder();
			document = db.parse(src);
		} catch (ParserConfigurationException e) {
			throw new ConfigurationException(exeptionString);
		} catch (SAXException e) {
			throw new ConfigurationException(exeptionString);
		} catch (IOException e) {
			throw new ConfigurationException(exeptionString);
		}
		return document;
	}
}


