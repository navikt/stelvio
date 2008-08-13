package no.nav.maven.plugins;

import org.apache.maven.plugin.AbstractMojo;

public abstract class AbstractDataPowerMojo extends AbstractMojo {

    /**
     * The DataPower configuration domain
     * 
     * @parameter expression="${domain}"
     * @required
     */
    private String domain;
    
    
    public String getDomain() {
    	return domain;
    }
}
