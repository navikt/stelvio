package no.stelvio.common.log;

import java.io.File;
import java.net.URL;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;


public class ReloadLog4JProperties implements Runnable {
	private Logger log = Logger.getLogger(this.getClass());
	private long lastModified;
	private static final String LOG4J_PROPERTIES = "log4j.properties"; 
	
	public ReloadLog4JProperties(){		
		File f = getLog4JFile();
		lastModified = f.lastModified();
	}
		
	public void refreshLog4JProperties(){
		log.info("Attempting to reload Log4J properties");	
		URL log4jprops = getLog4JUrl();
		if (log4jprops != null){
			LogManager.resetConfiguration();
			PropertyConfigurator.configure(log4jprops);
			log.info("Log4J properties reloaded");
		}	
	}	
	
	public void run(){
		if(isModified()){
			log.info("Log4J properties file is modified - reload properties.");
			refreshLog4JProperties();
			long timeModified = getLog4JFile() != null ? getLog4JFile().lastModified() : lastModified;
			this.lastModified = timeModified;
		} 
	}
	
	private File getLog4JFile(){
		 try {
			 if(ResourceUtils.isJarURL(getLog4JUrl()))
			 {
				URL url = ResourceUtils.extractJarFileURL(getLog4JUrl());
				return ResourceUtils.getFile(url); 
			 }
			 else
			 {
				 return new ClassPathResource(LOG4J_PROPERTIES).getFile(); 
			 }
			 
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
	
	private URL getLog4JUrl(){
		return this.getClass().getClassLoader().getResource(LOG4J_PROPERTIES);
	}
	
	private boolean isModified(){
		return getLog4JFile() != null ? getLog4JFile().lastModified() > lastModified : false;
	}
	
}
