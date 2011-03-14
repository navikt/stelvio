package no.nav.maven.plugin.wpsdeploy.plugin.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

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
	
	public String getProperty(String key) throws FileNotFoundException, IOException{
		
		String fetchedProperty = properties.getProperty(key);
		
		if (fetchedProperty != null){
			return fetchedProperty;
		} else {
			System.out.println("[ERROR] Unable to find property, " + key + " in file " + fileLocation);
			throw new IOException();
		}
	}
	
	public void exposeProperty(String key, String value, boolean password){
		
		project.getProperties().put(key, value);
		
		if (password)
			System.out.println("[INFO] Exposed property [" + key + ",*****]");
		else
			System.out.println("[INFO] Exposed property [" + key + "," + value + "]");
	}
}
