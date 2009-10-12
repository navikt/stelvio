package no.nav.datapower.xmlmgmt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.Parent;
import org.jdom.filter.ElementFilter;
import org.jdom.filter.Filter;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;

public class XMLMgmtDeploymentPolicy {
	
	private static final String DP_NAMESPACE = "http://www.datapower.com/schemas/management";
	//private static final String XPATH_EXPR = "//*[local-name()='ConfigDeploymentPolicy']";
	private Element policy;
	
	public XMLMgmtDeploymentPolicy(File configFile) throws IOException, JDOMException {
		policy = loadDeploymentPolicyXML(configFile);	
	}
	
	public XMLMgmtDeploymentPolicy(File configFile, Properties overrides) throws IOException, JDOMException {
		policy = loadDeploymentPolicyXML(configFile);
		overrideProperties(overrides);
	}
	
	public Element getDeploymentPolicy() {
		return policy;
	}
	
	public String getDeploymentPolicyXML(){
		return getXML(policy);
	}
	
//	public Element applyTransformToPolicy(String xsltFile) throws FileNotFoundException, TransformerConfigurationException{	
//		StreamSource xsltStream = new StreamSource(new FileInputStream(xsltFile));
//		TransformerFactory factory = TransformerFactory.newInstance();
//		Transformer transformer = factory.newTransformer(null);
//		Result s 
//		transformer.transform(xsltStream, )
//		return null;
//	}
	
	@Override
	public String toString(){
		return getXML(policy);
	}
	
	private Element loadDeploymentPolicyXML(File file) throws IOException, JDOMException {
		SAXBuilder sax = new SAXBuilder();
		Document config = sax.build(file);
		return config.getRootElement();
		
		
//		XPath xPath = XPath.newInstance(XPATH_EXPR);
//		xPath.addNamespace(Namespace.getNamespace(DP_NAMESPACE));
//		Object list = xPath.selectNodes(config);
//		
//		ArrayList nodes = (ArrayList)list;
//		Element element = (Element)nodes.get(0);
//		List clonedNodes = element.cloneContent();
//		
//		Element parent = new Element("deployment-policy", Namespace.getNamespace("dp",DP_NAMESPACE));
//		parent.setContent(clonedNodes);
		//cloned.setNamespace(Namespace.getNamespace("dp", DP_NAMESPACE));
//		element.setName("deployment-policy");
//		element.setNamespace( Namespace.getNamespace(DP_NAMESPACE));
		
		
		
		
//		Element parent = new Element("deployment-policy", Namespace.getNamespace(DP_NAMESPACE));
//		ElementFilter s = new ElementFilter();
//		element.getDescendants(Filter sad)
		
		
//		parent.setContent(clonedNodes);
//		for (Object object : nodes) {
//			Element element = (Element)(object);
//			element.detach();
//			element.setNamespace(Namespace.getNamespace(DP_NAMESPACE));
//			parent.addContent(element);	
//		}	
		
		//element.detach();
		//Document doc = new Document(cloned);
		//doc.setRootElement(element);
	
		//return parent;//doc.getRootElement();
	}
	public void overrideProperties(Properties overrides) throws JDOMException {
		if(overrides != null){
			Set<Entry<Object,Object>> entrySet = overrides.entrySet();
			for (Entry<Object, Object> entry : entrySet) {
				String key = (String)entry.getKey();
				int index= key.indexOf(".");
				String name= key.substring(0, index -1);
				String property =  key.substring(index +1, key.length());
				String transformed = property.replace(".", "/");
				setChangeValue(name, transformed, (String)entry.getValue());
			}
		}	
	}
	
	public void setChangeValue(String name, String property, String value) throws JDOMException {
		
		/**
		 * signature-gw-q1/crypto/key?Name=signature-gw-CryptoKey&amp;Property=Password&amp;Value=.
		 * 
		 */
		//String matchExpr
		/*String xpathExpr = 	"//*[local-name() = 'ConfigDeploymentPolicy']" +
							"/*[local-name() = 'ModifiedConfig']" +
							"[child::*[local-name()='Match']" +
							"[contains(.,'" +
							matchExpr +
							"')]]";*/
		
		String xpathExpr = 	"//*[local-name() = 'ConfigDeploymentPolicy']" +
		"/*[local-name() = 'ModifiedConfig']" +
		"[child::*[local-name()='Match']" +
		"[contains(.,'Name=" +
		name +
		"') and contains(.,'Property=" +
		property +
		"')]]";
		
		
		XPath xPath = XPath.newInstance(xpathExpr);
		Object node = xPath.selectSingleNode(policy);
		if(node != null){
			Element element = (Element)node;
			Element valueElement = element.getChild("Value", element.getNamespace());
			valueElement.setText(value);
		}	
	}

	/***
	 * 
	 * 
	 * USE output METHODS INSTEAD OF outputString FOR BEST PERFORMANCE
	 * 
	 * 
	 * @param element
	 * @return
	 */
	public String getXML(Parent element){
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
