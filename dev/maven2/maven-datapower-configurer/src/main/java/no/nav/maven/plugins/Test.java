package no.nav.maven.plugins;

import org.apache.maven.plugin.MojoExecutionException;


/**
 * @author utvikler
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Test {

	public static void main(String[] args) {
		Configurer config = new Configurer();
		try {
			config.execute();
		} catch (MojoExecutionException e) {
			e.printStackTrace();
		}
	}
}
