package no.nav.maven.plugins.descriptor.config.loader;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ClasspathSpringConfigLoader extends SpringConfigLoader<ClassPathXmlApplicationContext> {

	public ClasspathSpringConfigLoader(String fileName){
		super(fileName);
	}
	@Override
	public ClassPathXmlApplicationContext createConfigContext(String fileName) {
		return new ClassPathXmlApplicationContext(fileName);
	}

}
