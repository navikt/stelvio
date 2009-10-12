package no.nav.datapower.xmlmgmt;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.jdom.Attribute;
import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Parent;
import org.jdom.filter.ElementFilter;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;

public class XMLMgmtDocument {

	protected Element document;
	
	public static Element findDescendantNode(Element startNode, String elementName){
		String name = startNode.getName();
		if(name.equals(elementName)){
			return startNode;
		} else {
			Iterator decendants = startNode.getDescendants(new ElementFilter(elementName));
			Object node = decendants.hasNext() ? decendants.next() : null;
			return node != null ? (Element) node : null;
		}	
	}
	
	public static Element getRootNode(Element startNode){	
		return startNode.isRootElement() ? startNode : getRootNode(startNode.getParentElement());
	}
	
	public Element getNodeTree(){
		return (Element)document.clone();
	}
	
	@Override
	public String toString(){
		return getXML(getRootNode(document));
	}
	
	protected Element loadXML(String file) throws IOException, JDOMException {
		SAXBuilder sax = new SAXBuilder();
		URL url = Thread.currentThread().getContextClassLoader().getResource(file);
		Document config = sax.build(url);
		return config.getRootElement();
	}
	

	/***
	 * 
	 * @param element
	 * @return
	 */
	public static String getXML(Parent element){
		XMLOutputter output = new XMLOutputter();
		output.setFormat(Format.getPrettyFormat());		
		if(element instanceof Document)
			return output.outputString((Document)element);
		else if(element instanceof Element) {
			return output.outputString((Element)element);
		} else {
			return "";
		}
	}
	
	
}
