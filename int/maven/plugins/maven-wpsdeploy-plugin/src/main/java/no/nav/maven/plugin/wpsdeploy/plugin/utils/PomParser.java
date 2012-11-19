package no.nav.maven.plugin.wpsdeploy.plugin.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.maven.plugin.MojoExecutionException;
import org.sonatype.aether.util.artifact.DefaultArtifact;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PomParser {

	public static List<DefaultArtifact> getDependencies(File f) throws MojoExecutionException{
		List<DefaultArtifact> output = new ArrayList<DefaultArtifact>();
		try {
			Element xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(f).getDocumentElement();
			Element dependencies = find(xml, "dependencies");
			for (Element dependency : findAll(dependencies, "dependency")) {
				String groupId = get(find(dependency, "groupId"));
				String artifactId = get(find(dependency, "artifactId"));
				String version = get(find(dependency, "version"));
				String extension = get(find(dependency, "type"));
				
				DefaultArtifact a = new DefaultArtifact(groupId, artifactId, extension, version);
				
				output.add(a);
			}
			
		} catch (Exception e) {
			throw new MojoExecutionException("[ERROR]: " + e);
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
	
	
}
