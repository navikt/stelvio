package no.nav.maven.plugin.wpsdeploy.plugin.utils;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import no.nav.maven.plugin.wpsdeploy.plugin.exceptions.ConfigurationException;
import org.w3c.dom.Document;

public class XpathDocumentParser {
	private XPath xpathInstance;
	private Document document;
	public XpathDocumentParser(String xml){
		this.xpathInstance = this.getXpathInstance();
		this.document = XmlUtil.getDocument(xml);
	}
	public String evaluate(String xpathString) {
		try {
			return this.xpathInstance.evaluate(xpathString, this.document);
		} catch (XPathExpressionException e) {
			throw new ConfigurationException("There was a problem parsing your XPATH expression: "+ e);
		}
	}

	public static XPath getXpathInstance(){
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();

		return xpath;
	}
}
