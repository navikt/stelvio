package no.nav.maven.moose.utility.spring.loader;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MooseClasspathSpringConfigLoader extends MooseSpringConfigLoader<ClassPathXmlApplicationContext> {

	public MooseClasspathSpringConfigLoader(String fileName){
		super(fileName);
	}
	@Override
	public ClassPathXmlApplicationContext createConfigContext(String fileName) {
		return new ClassPathXmlApplicationContext(fileName);
	}

}
