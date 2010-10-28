/**
 * 
 */
package no.nav.maven.plugin.bounce.plugin.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Class that parses environment configuration file
 * 
 * @author test@example.com
 * 
 */
public class XMLParser {

	/**
	 * Parser xml environment file and gets data for the deploy managers for was
	 * sensitive/intern (if present) and wps (if present)
	 * 
	 * @param file
	 * @return a string with parameters from the xml file in format:<br />
	 * was.sensitiv\nhostname:verdi;\nsoap-port:verdi;\nws-username:verdi;\nws-password:verdi;\n[was.intern\n...][wps\n...]
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public static String parseEnvironmentFile(File file) throws SAXException, IOException,
			ParserConfigurationException, InvalideNodeValueException {
		StringBuilder result = new StringBuilder();

		Document xml = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder().parse(file);
		
		xml.getDocumentElement().normalize();	
				
		try {
		NodeList servers = xml.getElementsByTagName("servers");		
		NodeList serverElements = servers.item(0).getChildNodes(); // servers
		NodeList wasElements = serverElements.item(1).getChildNodes(); // was
		NodeList sensitivElements = wasElements.item(1).getChildNodes(); // sensitiv

		// deploy manager for was sensitive zone
		NodeList dmgr = sensitivElements.item(1).getChildNodes(); // dmgr
		result.append("was.sensitiv\n");
		for (int i = 0; i < dmgr.getLength(); i++) {
			Node n = dmgr.item(i);
			if (n.getNodeName().equals("hostname"))
				result.append(n.getNodeName() + ":"
						+ n.getChildNodes().item(0).getNodeValue() + ";\n");
			if (n.getNodeName().equals("soap-port"))
				result.append(n.getNodeName() + ":"
						+ n.getChildNodes().item(0).getNodeValue() + ";\n");
			if (n.getNodeName().equals("ws-username"))
				result.append(n.getNodeName() + ":"
						+ n.getChildNodes().item(0).getNodeValue() + ";\n");
			if (n.getNodeName().equals("ws-password"))
				result.append(n.getNodeName() + ":"
						+ n.getChildNodes().item(0).getNodeValue() + ";\n");
		}
		// deploy manager for was intern zone
		if (wasElements.getLength()>3) { // check to see if there is an internsone element
			NodeList internElements = wasElements.item(3).getChildNodes(); // intern

			result.append("was.intern\n");
			dmgr = internElements.item(1).getChildNodes(); // dmgr
			for (int i = 0; i < dmgr.getLength(); i++) {
				Node n = dmgr.item(i);
				if (n.getNodeName().equals("hostname"))
					result.append(n.getNodeName() + ":"
							+ n.getChildNodes().item(0).getNodeValue() + ";\n");
				if (n.getNodeName().equals("soap-port"))
					result.append(n.getNodeName() + ":"
							+ n.getChildNodes().item(0).getNodeValue() + ";\n");
				if (n.getNodeName().equals("ws-username"))
					result.append(n.getNodeName() + ":"
							+ n.getChildNodes().item(0).getNodeValue() + ";\n");
				if (n.getNodeName().equals("ws-password"))
					result.append(n.getNodeName() + ":"
							+ n.getChildNodes().item(0).getNodeValue() + ";\n");
			}
		}
		// deploy manager for wps
		if (serverElements.getLength()>3){ // check to see if there is a wps element
			NodeList wpsElements = serverElements.item(3).getChildNodes();
			result.append("wps\n");
			dmgr = wpsElements.item(1).getChildNodes();
			for (int i = 0; i < dmgr.getLength(); i++) {
				Node n = dmgr.item(i);
				if (n.getNodeName().equals("hostname"))
					result.append(n.getNodeName() + ":"
							+ n.getChildNodes().item(0).getNodeValue() + ";\n");
				if (n.getNodeName().equals("soap-port"))
					result.append(n.getNodeName() + ":"
							+ n.getChildNodes().item(0).getNodeValue() + ";\n");
				if (n.getNodeName().equals("ws-username"))
					result.append(n.getNodeName() + ":"
							+ n.getChildNodes().item(0).getNodeValue() + ";\n");
				if (n.getNodeName().equals("ws-password"))
					result.append(n.getNodeName() + ":"
							+ n.getChildNodes().item(0).getNodeValue() + ";\n");
			}
		}

		return result.toString();
		}catch(Exception e){
			throw new InvalideNodeValueException("Failed to parse XML",e);
		}
	}

	/**
	 * Parser restart_config.xml file and gets flags for which modules should be restarted when given applications are deployed
	 * @param file
	 * @return a HashMap with names of applications as keys and RestartConfig objects as values
	 * format: app:was_ss/was_is/wps
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public static HashMap<String, RestartConfig> parseRestartConfigFile(File file) throws SAXException, IOException, ParserConfigurationException{
		HashMap<String, RestartConfig> result = new HashMap<String, RestartConfig>();
		Document xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
		xml.getDocumentElement().normalize();	
				
		NodeList applications = xml.getElementsByTagName("applications").item(0).getChildNodes();	
		
		for (int i=0;i<applications.getLength();i++){
			Node node = applications.item(i);
			if (node.hasChildNodes()){
				NodeList modules = node.getChildNodes();
				RestartConfig rc = new RestartConfig();
				for (int j=0;j<modules.getLength();j++){
					Node n = modules.item(j);
					if (n.hasChildNodes()){
						if (n.getNodeName().equals("name")) rc.setApp(n.getChildNodes().item(0).getNodeValue());
						if (n.getNodeName().equals("was_sensitiv")) rc.setWas_ss((n.getChildNodes().item(0).getNodeValue()));
						if (n.getNodeName().equals("was_intern")) rc.setWas_is((n.getChildNodes().item(0).getNodeValue()));
						if (n.getNodeName().equals("wps")) rc.setWps((n.getChildNodes().item(0).getNodeValue()));
					}
				}
				result.put(rc.getApp(), rc);
			}
		}
		return result;
	}
	
	public static void main(String[] args) {
		File f = new File("E:\\restart_config.xml");
		try {
			HashMap<String, RestartConfig> list = parseRestartConfigFile(f);
			for (RestartConfig rc: list.values()){
				System.out.println(rc);
			}
			String apps = "pen:version,psak:version,pselv:version,";
			String [] tmp = apps.split(",");
			for (String s : tmp) System.out.println("_"+ s);
			System.out.println(tmp[0].substring(0,tmp[0].indexOf(":")));
			boolean a = true;
			a |= true;
			System.out.println(a);
			
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
