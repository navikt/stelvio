package no.nav.maven.plugins.descriptor.config.loader;

import org.springframework.context.support.FileSystemXmlApplicationContext;

public class FileSystemSpringConfigLoader extends SpringConfigLoader<FileSystemXmlApplicationContext> {
	
	public FileSystemSpringConfigLoader(String fileName){
		super(fileName);
	}
	public FileSystemXmlApplicationContext createConfigContext(String fileName){
		return new FileSystemXmlApplicationContext(fileName);
	}
}
