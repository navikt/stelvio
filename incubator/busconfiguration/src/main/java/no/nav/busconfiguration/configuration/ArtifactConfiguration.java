package no.nav.busconfiguration.configuration;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.xmlbeans.XmlException;

import no.nav.pensjonsprogrammet.wpsconfiguration.ConfigurationDocument;
import no.nav.pensjonsprogrammet.wpsconfiguration.ConfigurationType;

public class ArtifactConfiguration {
	
	private static final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private static final Lock read  = readWriteLock.readLock();   
	private static final Lock write = readWriteLock.writeLock();
	private static final HashMap<String,ConfigurationType> configuration  = new HashMap<String, ConfigurationType>();

	
	public static final boolean isConfigurationLoaded() {
			
		read.lock();	
		try {
			return !configuration.isEmpty();
		} finally {
			read.unlock();
		}
	}
	public static void loadConfiguration(final File directory) {
		write.lock();
		try {
			if(configuration.isEmpty() == false) {
				return;
			}
			loadXML(directory);
		} finally {
			write.unlock();
		}
	}

	public static ConfigurationType getConfiguration(final String artifactId) {
		read.lock();
		try {
			return configuration.get(artifactId);
		} finally {
			read.unlock();
		}
	}
	
	private final static void loadXML(final File directory) {
		File[] files = directory.listFiles(new FileFilter() {public boolean accept(File file){return file.isFile() && file.getName().endsWith(".xml");}}); 
		
		if(files == null)  {
			return;
		}
		
		for(File f : files) {
				ConfigurationType config = null;
				try {
					config = ConfigurationDocument.Factory.parse(f).getConfiguration();
				} catch (XmlException e) {
					throw new RuntimeException("An XML exception occured reading file ", e);
				} catch (IOException e) {
					throw new RuntimeException("An IO exception occured reading file", e);
				}
				
				configuration.put(f.getName().replace(".xml", ""), config);
		}
	}
}
