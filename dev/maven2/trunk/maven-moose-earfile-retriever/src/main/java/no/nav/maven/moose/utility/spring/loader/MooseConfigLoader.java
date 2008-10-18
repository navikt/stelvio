package no.nav.maven.moose.utility.spring.loader;

import no.nav.maven.moose.utility.EarFileRetriever;

public interface MooseConfigLoader<T> {
	
	public EarFileRetriever getEarFileRetriever();
	
	public T getConfigContext();
	
	public T createConfigContext(String fileName);
}
