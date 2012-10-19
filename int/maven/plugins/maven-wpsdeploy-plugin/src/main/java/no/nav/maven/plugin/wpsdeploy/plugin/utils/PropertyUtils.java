package no.nav.maven.plugin.wpsdeploy.plugin.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import no.nav.maven.plugin.wpsdeploy.plugin.exceptions.ConfigurationException;

import org.apache.maven.project.MavenProject;

/**
 * Utility class for handling properties during the execution
 * 
 */

public class PropertyUtils {
	
	private MavenProject project;
	private Properties properties; 
	private List<String> fileLocations;
	
	public PropertyUtils(MavenProject project) throws FileNotFoundException, IOException{
		this.project = project;
		this.fileLocations = new ArrayList<String>();
		
		properties = new Properties();
	}
	
	public String getProperty(String key){
		
		String fetchedProperty = properties.getProperty(key);
		
		if (fetchedProperty != null){
			return fetchedProperty;
		} else {
			throw new ConfigurationException("[ERROR] Unable to find property, " + key);
		}
	}
	
	public void exposeProperty(String key, boolean password, boolean lowercase){
		Properties projectProperties = project.getProperties();
		String value;
		if(lowercase){
			value = getProperty(key).toLowerCase();
		} else {
			value = getProperty(key);
		}
		

		if(value == null){
			throw new ConfigurationException("The "+ key +" property can't be \"null\"!");
		} 
		else if (value.startsWith("${") && value.endsWith("}")){
			throw new ConfigurationException("The "+ key +" property can't start with \"${\" and end with \"}\"!\nWhen this happens it is most likely that there is a property file that can't be found. ("+ value +")");
		} 
		else if (value.startsWith("@") && value.endsWith("@")){
			throw new ConfigurationException("The "+ key +" property can't start and end with \"@\"!\nWhen this happens it is most likely that there is a property file that can't be found. ("+ value +")");
		}
		
		projectProperties.put(key, value);
		
		String lowerMsg = ""; 
		if (password) value = "*****";
		if (lowercase) lowerMsg = " (forced lower case)";
		System.out.println("[INFO] Exposed property [" + key + "," + value + "]"+ lowerMsg);
	}
	
	public void exposeProperty(String key, boolean password){
		exposeProperty(key, password, false);
	}
	
	public void loadFile(String fileLocation) throws FileNotFoundException, IOException{
		properties.load(new FileInputStream(fileLocation));
		this.fileLocations.add(fileLocation);
	}
}
