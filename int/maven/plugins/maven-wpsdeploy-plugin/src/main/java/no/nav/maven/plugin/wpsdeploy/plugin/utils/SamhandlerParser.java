package no.nav.maven.plugin.wpsdeploy.plugin.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class SamhandlerParser {
	public static Map<String, String> parseXml(File xmlFile){
		Map<String, String> output = new HashMap<String, String>();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		
		try {
			db = dbf.newDocumentBuilder();
			Element docElement = db.parse(xmlFile).getDocumentElement();
			docElement.normalize();
			
			List<Element> nameSpaceBindings = findAll(docElement,"nameSpaceBinding");
			for (Element nameSpaceBinding : nameSpaceBindings){
				List<Element> resources = findAll(nameSpaceBinding, "resource");
				
				for (Element resource : resources){
					String resourceName = get(resource);
					List<String> resourceValues = new ArrayList<String>();
					List<Element> samhandlers = findAll(nameSpaceBinding, "samhandler");
					
					for(Element samhandler : samhandlers){
						String id = samhandler.getAttribute("tpnr");
						String version = get(find(samhandler, "version"));
						String endpoint = get(find(samhandler, "endpoint"));
						
						resourceValues.add(version +"^"+ id +"^"+ endpoint);
					}
					output.put(resourceName, join("|", resourceValues.toArray()));
				}
			}
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return output;
	}
	
	public static Map<String, String> parseTilkoblingslisteXml(File xmlFile){
		Map<String, String> output = new HashMap<String, String>();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		
		try {
			db = dbf.newDocumentBuilder();
			Element docElement = db.parse(xmlFile).getDocumentElement();
			docElement.normalize();

			List<Element> nameSpaceBindings = findAll(docElement,"nameSpaceBinding");
			for (Element nameSpaceBinding : nameSpaceBindings){
				List<Element> resources = findAll(nameSpaceBinding, "resource");
				
				for (Element resource : resources){
					String resourceName = get(resource);
					List<String> resourceValues = new ArrayList<String>();
					List<Element> samhandlers = findAll(nameSpaceBinding, "samhandler");
					Element commonProperties = find(nameSpaceBinding, "commonProperties");
					
					for(Element samhandler : samhandlers){
						String id = samhandler.getAttribute("eksternTSSId");
						String tjenesteKode = get(find(commonProperties, "tjenesteKode"));
						String tjenesteId = get(find(commonProperties, "tjenesteId"));
						String tjenesteNavn = get(find(commonProperties, "tjenesteNavn"));
						String statusKode = get(find(commonProperties, "statusKode"));
						String aktivertFom = get(find(samhandler, "aktivertFom"));
						
						resourceValues.add(id +"^"+ tjenesteKode +"^"+ tjenesteId +"^"+ tjenesteNavn +"^"+ statusKode +"^"+ aktivertFom);
					}
					output.put(resourceName, join("|", resourceValues.toArray()));
				}
			}
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return output;
		
	}
	
	private static Element find(Element e, String tag){
		List<Element> tags = getElementsFromNodeList(e.getElementsByTagName(tag));
		return tags.get(0);
	}
	
	private static List<Element> findAll(Element e, String tag){
		List<Element> tags = getElementsFromNodeList(e.getElementsByTagName(tag));
		return tags;
	}
	
	private static String get(Element e){
		return e.getChildNodes().item(0).getNodeValue();
	}
	
	private static List<Element> getElementsFromNodeList(NodeList nodeList){
		List<Element> output = new ArrayList<Element>();
		for (int i=0;i<nodeList.getLength();i++){
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE){
				output.add((Element) node);
			}
			
		}
		return output;
	}
	
	private static String join(String delimitor, Object[] s){
	  int k=s.length;
	  if (k==0) return null;
	  StringBuilder out=new StringBuilder();
	  out.append(s[0]);
	  for (int x=1;x<k;++x)
	    out.append(delimitor).append(s[x]);
	  return out.toString();
	}
}
