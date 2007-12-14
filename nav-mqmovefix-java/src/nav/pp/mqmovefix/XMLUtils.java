package nav.pp.mqmovefix;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XMLUtils {
	
	/**
	 * Parse string as XML document
	 * @param xmlString
	 * @return Document
	 * @throws Exception
	 */
	public static Document parseXML(String xmlString ) throws Exception {
		DocumentBuilderFactory buildFact = DocumentBuilderFactory.newInstance();
		buildFact.setCoalescing(true);
		buildFact.setValidating(false);
		
		DocumentBuilder builder = buildFact.newDocumentBuilder();
		Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
		return doc;
	}
	
	/**
	 * @param currentNode
	 * @param name
	 * @return
	 */
	public static Element findNamedNode(Node currentNode, String name){
		if( currentNode instanceof Element && currentNode.getNodeName().equals(name)) {
			return (Element)currentNode;
		} else {
			NodeList childNodes = currentNode.getChildNodes();
			for( int num=0;num<childNodes.getLength(); num++ ) {
				Element result = findNamedNode(childNodes.item(num), name);
				if( result != null ) return result;
			}
			return null;
		}
	}
	
	/**
	 * @param currentNode
	 * @param name
	 * @return
	 */
	public static String findNamedNodeValue(Node currentNode, String name ) {
		Element elem = findNamedNode(currentNode, name);
		if( elem != null ) {
			return elem.getFirstChild().getNodeValue();
		} else {
			return null;
		}
	}
	
	/**
	 * @param doc
	 * @return
	 * @throws Exception
	 */
	public static String encodeXML(Document doc ) throws Exception {
		StringWriter stringWriter = new StringWriter();
		OutputFormat format = new OutputFormat();
		format.setIndenting(false);
		XMLSerializer xmlSerializer = new XMLSerializer(stringWriter, format);
		xmlSerializer.serialize(doc);
		return stringWriter.toString();
	}
	
}
