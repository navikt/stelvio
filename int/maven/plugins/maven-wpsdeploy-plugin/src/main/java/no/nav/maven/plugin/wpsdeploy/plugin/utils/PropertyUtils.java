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
	
	public void exposeProperty(String key, boolean password){
		Properties projectProperties = project.getProperties();
		String value = getProperty(key);
		

		if(value == null){
			throw new ConfigurationException("The "+ key +" property can't be \"null\"!");
		} 
		else if (value.contains("$")){
			throw new ConfigurationException("The "+ key +" property can't contain \"$\"!\nWhen this happens it is most likely that there is a property file that can't be found.");
		}
		
		projectProperties.put(key, value);
				
		if (password)
			System.out.println("[INFO] Exposed property [" + key + ",*****]");
		else
			System.out.println("[INFO] Exposed property [" + key + "," + value + "]");
	}
	
	public void loadFile(String fileLocation) throws FileNotFoundException, IOException{
		properties.load(new FileInputStream(fileLocation));
		this.fileLocations.add(fileLocation);
	}
}
