package no.nav.maven.plugins.descriptor.config.loader;

import no.nav.maven.plugins.descriptor.config.DeploymentDescriptorMojoConfig;

import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public abstract class SpringConfigLoader<T extends AbstractXmlApplicationContext> implements ConfigLoader{

	private T springContext;
	private static final String CONFIG_BEAN = "deploymentDescriptorMojoConfig";
	private DeploymentDescriptorMojoConfig deploymentDescriptorMojoConfig;
	
	public SpringConfigLoader(String fileName){
		this.springContext = createConfigContext(fileName);
		//springContext = new T(fileName);
			
		/*FileSystemXmlApplicationContext fileContext = new FileSystemXmlApplicationContext(fileName);
		fileContext.getBean(CONFIG_BEAN);*/
		//	new ClassPathXmlApplicationContext()
		deploymentDescriptorMojoConfig = 
			(DeploymentDescriptorMojoConfig)springContext.getBean(CONFIG_BEAN);
	}

	public DeploymentDescriptorMojoConfig getDeploymentDescriptorMojoConfig() {
		return deploymentDescriptorMojoConfig;
	}

	public T getConfigContext(){
		return springContext;
	}
	
	public abstract T createConfigContext(String fileName);
		
}
