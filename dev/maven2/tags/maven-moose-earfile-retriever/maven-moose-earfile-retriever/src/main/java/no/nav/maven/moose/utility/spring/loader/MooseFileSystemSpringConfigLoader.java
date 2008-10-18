package no.nav.maven.moose.utility.spring.loader;

import org.springframework.context.support.FileSystemXmlApplicationContext;

public class MooseFileSystemSpringConfigLoader extends MooseSpringConfigLoader<FileSystemXmlApplicationContext> {
	
	public MooseFileSystemSpringConfigLoader(String fileName){
		super(fileName);
	}
	public FileSystemXmlApplicationContext createConfigContext(String fileName){
		return new FileSystemXmlApplicationContext(fileName);
	}
}
