package no.nav.maven.plugin.wid;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.eclipse.EclipseCleanMojo;

/**
 * Cleans up the wid configuration files.
 * 
 * @author <a href="mailto:test@example.com">Erik Godding Boye</a>
 * @extendsPlugin eclipse
 * @goal clean
 */
public class WidCleanMojo extends EclipseCleanMojo {
	@Override
	protected void cleanExtras() throws MojoExecutionException {
		super.cleanExtras();
		delete(new File(getBasedir(), ".settings"));
	}
}
