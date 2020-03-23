package no.stelvio.common.config;

import no.stelvio.common.ejb.config.StelvioRemoteStatelessSessionBeanDefinitionParser;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Stelvio namespace handler.
 * 
 */
public class StelvioNamespaceHandler extends NamespaceHandlerSupport {

	/**
	 * Initializes the namespace handler with "stelvio-remote-slsb" and StelvioRemoteStatelessSessionBeanDefinitionParser.
	 */
	public void init() {
		registerBeanDefinitionParser("stelvio-remote-slsb", new StelvioRemoteStatelessSessionBeanDefinitionParser());
	}

}
