package no.stelvio.maven.build.plugin.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesFile {
	
	private static Properties props;	
	
	public static Properties getProperties(String location, String application) throws FileNotFoundException, IOException{
		if (!location.endsWith("/")) location += "/";
		File props_file = new File(location+application+"_build.properties");
		props = new Properties();
		if (props_file.exists()) props.load(new FileInputStream(props_file));
		else props_file.createNewFile();
		return props;
	}

	public static void setProperties(String location, String application, Properties properties) throws IOException{
		props = properties;
		File props_file = new File(location+application+"_build.properties");
		if (props_file.exists()) props_file.delete();
		saveProperties(location, application);
	}
	
	public static void appendProperties(String location, String application, Properties properties) throws IOException {
		props = getProperties(location, application);
		for (String name : properties.stringPropertyNames())
			props.setProperty(name, properties.getProperty(name));
		saveProperties(location, application);
	}

	private static void saveProperties(String location, String application) throws IOException{
		File props_file = new File(location+application+"_build.properties");
		props.store(new FileOutputStream(props_file), "Properties from maven created by maven-build-plugin");
		System.out.println("[INFO] Saving properties to: " + props_file.getAbsolutePath());
	}

	public String getProperty(String application, String name){
		return props.getProperty(name);
	}
	
	public static void main(String[] args) {
		try {
			saveProperties("E:/", "POPP");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
