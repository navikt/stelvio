package no.nav.maven.plugin.wpsdeploy.plugin.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import no.nav.maven.plugin.wpsdeploy.plugin.exceptions.MyConfigurationException;

import org.apache.maven.project.MavenProject;

/**
 * Utility class for handling properties during the execution
 * 
 * @author test@example.com
 */

public class PropertyUtils {
	
	private MavenProject project;
	private Properties properties; 
	private String fileLocation;
	
	public PropertyUtils(String fileLocation, MavenProject project) throws FileNotFoundException, IOException{
		
		this.project = project;
		this.fileLocation = fileLocation;
		
		properties = new Properties();
		properties.load(new FileInputStream(fileLocation));
	}
	
	public String getProperty(String key){
		
		String fetchedProperty = properties.getProperty(key);
		
		if (fetchedProperty != null){
			return fetchedProperty;
		} else {
			throw new MyConfigurationException("[ERROR] Unable to find property, " + key + " in file " + fileLocation);
		}
	}
	
	public void exposeProperty(String key, boolean password){
		Properties projectProperties = project.getProperties();
		String value = getProperty(key);
		

		if(value == null){
			throw new MyConfigurationException("The "+ key +" property can't be \"null\"!");
		} 
		else if (value.contains("$")){
			throw new MyConfigurationException("The "+ key +" property can't contain \"$\"!\nWhen this happens it is most likely that there is a property file that can't be found.");
		}
		
		projectProperties.put(key, value);
				
		if (password)
			System.out.println("[INFO] Exposed property [" + key + ",*****]");
		else
			System.out.println("[INFO] Exposed property [" + key + "," + value + "]");
	}
}
