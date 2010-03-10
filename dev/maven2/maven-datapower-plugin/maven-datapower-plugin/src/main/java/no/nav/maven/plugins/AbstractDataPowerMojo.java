package no.nav.maven.plugins;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.pyx4j.log4j.MavenLogAppender;

public abstract class AbstractDataPowerMojo extends AbstractMojo {

    /**
     * The DataPower configuration domain
     * 
     * @parameter expression="${domain}"
     * @required
     */
    private String domain;    
    
    protected String getDomain() {
    	return domain;
    }
    
    public void execute() throws MojoExecutionException, MojoFailureException {
		MavenLogAppender.startPluginLog(this);		
		try {
			doExecute();
		}
		finally {
			MavenLogAppender.endPluginLog(this);
		}
    }
    
    protected abstract void doExecute() throws MojoExecutionException, MojoFailureException;
}
