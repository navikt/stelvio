package no.nav.maven.moose.utility.spring.loader;

import no.nav.maven.moose.utility.EarFileRetriever;

import org.springframework.context.support.AbstractXmlApplicationContext;

public abstract class MooseSpringConfigLoader<T extends AbstractXmlApplicationContext> implements MooseConfigLoader{

	private T springContext;
	private static final String CONFIG_BEAN = "earFileRetriever";
	private EarFileRetriever earFileRetriever;
	
	public MooseSpringConfigLoader(String fileName){
		this.springContext = createConfigContext(fileName);

		earFileRetriever = 
			(EarFileRetriever)springContext.getBean(CONFIG_BEAN);
	}

	public EarFileRetriever getEarFileRetriever() {
		return earFileRetriever;
	}

	public T getConfigContext(){
		return springContext;
	}
	
	public abstract T createConfigContext(String fileName);
		
}
