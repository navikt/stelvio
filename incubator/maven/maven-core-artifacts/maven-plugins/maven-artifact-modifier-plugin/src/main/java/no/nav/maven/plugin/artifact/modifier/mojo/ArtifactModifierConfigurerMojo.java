package no.nav.maven.plugin.artifact.modifier.mojo;

import java.io.File;
import java.util.Set;
import java.util.StringTokenizer;

import no.nav.maven.commons.configuration.ArtifactConfiguration;
import no.nav.maven.commons.constants.Constants;
import no.nav.maven.commons.managers.ArchiveManager;
import no.nav.maven.commons.managers.IArchiveManager;
import no.nav.pensjonsprogrammet.wpsconfiguration.ConfigurationType;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.UnArchiver;;


/**
 * Abstract class using the template pattern for child mojos doing real configuration work.
 * 
 * @author test@example.com 
 */
public abstract class ArtifactModifierConfigurerMojo extends ArtifactModifierMojo {
	
	protected abstract void applyConfiguration(Artifact artifact, ConfigurationType configuration) throws MojoExecutionException, MojoFailureException;
	
	public void doExecute() throws MojoExecutionException, MojoFailureException {
		for(Artifact a : artifacts) {
			if(a.getType().equals(Constants.EAR_ARTIFACT_TYPE)) {
				StringTokenizer tokenizer = new StringTokenizer(a.getArtifactId(), Constants.ARTIFACT_MODIFIER_SEPARATOR);
	
				ConfigurationType configuration = null;
				while(tokenizer.hasMoreTokens()) {
					String tok=tokenizer.nextToken();
					configuration = ArtifactConfiguration.getConfiguration(tok);
					if(configuration != null) {
						applyConfiguration(a, configuration);
					}
				}
	
				configuration = ArtifactConfiguration.getConfiguration(a.getArtifactId());
				applyConfiguration(a, configuration);
			}
		}
	}
}
