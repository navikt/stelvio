package no.nav.datapower.xmlmgmt;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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

public class XMLMgmtRequest extends XMLMgmtDocument {
	
	//private Element document;
	
	public XMLMgmtRequest(String xmlFile) {	
		try {
			document = loadXML(xmlFile);
		} catch (IOException e) {
			throw new RuntimeException("Failed to load file:" + xmlFile,e);
		} catch (JDOMException e) {
			throw new RuntimeException("Failed to parse file:" + xmlFile,e);
		}
	}
	
	public XMLMgmtRequest(String xmlFile, Properties overrides) {
		this(xmlFile);
		try {
			updateNodeTree(overrides);
		} catch (JDOMException e) {
			throw new RuntimeException("Failed to update nodetree with overrides.",e);
		}
		
	}

	public XMLMgmtRequest(Element document){
		this.document = (Element)document.clone();
	}
	
	public XMLMgmtRequest(Element document, Properties overrides) {
		this(document);
		try {
			updateNodeTree(overrides);
		} catch (JDOMException e) {
			throw new RuntimeException("Failed to update nodetree with overrides.",e);
		}
	}
	
	public XMLMgmtRequest clone(){
		return new XMLMgmtRequest((Element)document.clone());
	}
	
	/**
	 * Must be an xpath string which uses no predicates or //
	 * @param key
	 * @return
	 */
	public String createXPathFromKey(String key){
		String[] nodes = key.split("/");
		//Makes sure that the xPath expression is executed from anywhere in the document (//)
		String xPath = "/"; 
		
		for (String string : nodes) {			
			if(StringUtils.isNotBlank(string)){
				xPath += createLocalNamePredicateOrAttributeExpression(string);		
			}	
		}
		
		return xPath;
	}
	
	private static String createLocalNamePredicateOrAttributeExpression(String expr){
		
		String nodeExpr = expr;
		String xPathExpr = "/";
		if(nodeExpr.startsWith("@")){
			xPathExpr += nodeExpr;
		} else {
			String predicate = StringUtils.substringBetween(nodeExpr, "[", "]");
			if(nodeExpr.contains(":")){
				nodeExpr = StringUtils.substringAfter(nodeExpr, ":");
			} 
			if(nodeExpr.contains("[")){
				nodeExpr = StringUtils.substringBefore(nodeExpr,"[");
			}
			String localNameExpr = "*[local-name()='" + nodeExpr + "']";
			xPathExpr += StringUtils.isEmpty(predicate) ? localNameExpr
							 : localNameExpr + "[" + predicate + "]";
			
		}	
		return xPathExpr;
	}
	
	private Element findClosestNode(String xPathExpr) throws JDOMException{
		XPath xPath = XPath.newInstance(xPathExpr);	
		Object node = xPath.selectSingleNode(document);
		if(node == null && xPathExpr.startsWith("/")){
			String parent = StringUtils.substringBeforeLast(xPathExpr, "/");
			
			return findClosestNode(parent);
			
		} else {
			System.out.println("Found closest node with xPath=" + xPathExpr);
			return (Element)node;
		}	
	}
	
	private List findClosestNodes(String xPathExpr) throws JDOMException{
		XPath xPath = XPath.newInstance(xPathExpr);	
		List nodes = xPath.selectNodes(document);
		if(nodes.size() == 0 && xPathExpr.startsWith("/")){
			String upOneLevel = StringUtils.substringBeforeLast(xPathExpr, "/");
			
			return findClosestNodes(upOneLevel);
			
		} else {
			System.out.println("Found closest node with xPath=" + xPathExpr);
			return nodes;
		}	
	}
	
	
	
	protected void updateWithXPath(Element document, String xPathExpr, String value) throws JDOMException {
		XPath xPath = XPath.newInstance(xPathExpr);
		List nodes = xPath.selectNodes(document);			
		if(nodes != null){
			if(nodes.size() == 0){
				
				String parentExpression = StringUtils.substringBeforeLast(xPathExpr, "/");
				Element closestNode = findClosestNode(parentExpression);
				Element newNode = addNewNodes(closestNode, xPathExpr, value);
			}
			else{
				setNodeValue(nodes, value);
			}
		}
	}
	
	private Element addNewNodes(Element parent, String xPathExpr, String value){
		String parentNodeExpression = "/*[local-name()='" + parent.getName() + "']";
		String newChildNodesExpression = StringUtils.substringAfterLast(xPathExpr, parentNodeExpression);
		String nodeExpression = StringUtils.substringAfter(newChildNodesExpression,"/");
		String newNode = StringUtils.substringBetween(nodeExpression, "local-name()='", "'");
		String newAttribute = StringUtils.substringAfter(nodeExpression, "@");
		
		if(StringUtils.isNotBlank(newNode)){	
			Element child = new Element(newNode);
			parent.addContent(child);
			return addNewNodes(child, newChildNodesExpression, value);
		} else if(StringUtils.isNotBlank(newAttribute)){
			parent.setAttribute(newAttribute, value);
			return parent;
		} else {
			parent.setText(value);
			return parent;
		}	
	}
	
	private void setNodeValue(List nodes, String value){
		for (Object node : nodes) {
			if(node instanceof Element){
				Element e = (Element)node;
				e.setText(value);
			} else if (node instanceof Attribute){
				Attribute a = (Attribute)node;
				a.setValue(value);
			}
		}
	}
	
	public XMLMgmtRequest updateNodeTree(Properties props) throws JDOMException{
		Set<Entry<Object,Object>> entrySet =  props.entrySet();
		for (Entry<Object, Object> entry : entrySet) {
			String key = (String)entry.getKey();	
			
			String xPathExpr = createXPathFromKey(key);
			updateWithXPath(this.document, xPathExpr, (String)entry.getValue());
		}	
		return clone();
	}
	
	public static XMLMgmtRequest merge(XMLMgmtRequest req1, XMLMgmtRequest req2, String mergeElementName)  {
		//String xPathExpr = "/" + createLocalNamePredicateOrAttributeExpression(mergeElementName);
		Element mergeTree = req1.getNodeTree();
		Element parentToMergeInto = findDescendantNode(mergeTree, mergeElementName);
		Element parentToMergeFrom = findDescendantNode(req2.getNodeTree(), mergeElementName); 
		if(parentToMergeInto != null && parentToMergeFrom != null){
			List childrenToCopy = parentToMergeFrom.cloneContent();
			for (Object object : childrenToCopy) {
				Content c = (Content)object;
				parentToMergeInto.addContent(c);
			}
			return new XMLMgmtRequest(mergeTree);
		} else {
			return null;
		}
		
		
	}
	
	/*private static Element findDescendantNode(Element startNode, String elementName){
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
	
	private Element loadXML(String file) throws IOException, JDOMException {
		SAXBuilder sax = new SAXBuilder();
		URL url = Thread.currentThread().getContextClassLoader().getResource(file);
		Document config = sax.build(url);
		return config.getRootElement();
	}
	

	
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
	}*/
	
	
}
