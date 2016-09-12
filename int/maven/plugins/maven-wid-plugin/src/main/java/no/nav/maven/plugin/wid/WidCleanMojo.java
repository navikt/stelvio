package no.nav.maven.plugin.wid;

import java.io.File;
import java.io.FilenameFilter;

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

		// This is just a temporary solution - deleting jar-files that might be
		// copied in to the project folder.
		String[] files = getBasedir().list(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".jar");
			}
		});
		for (String file : files) {
			delete(new File(getBasedir(), file));
		}

		delete(new File(getBasedir(), "sca.library.attributes"));
		delete(new File(getBasedir(), "sca.module.attributes"));
		
		// PK-31605: Rollemapping skal nå være en del av modul-bygget, dvs ibm-deploy.scaj2ee kan ikke lenger automatisk slettes
		//delete(new File(getBasedir(), "ibm-deploy.scaj2ee"));
	}
}
