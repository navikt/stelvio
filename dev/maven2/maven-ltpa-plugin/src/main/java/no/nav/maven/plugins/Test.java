/*
 * Created on 29-Oct-2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package no.nav.maven.plugins;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * @author srvmooseadmin
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Test {

	public static void main(String[] args) throws MojoExecutionException, MojoFailureException {
		SetupLTPA ltpa = new SetupLTPA();
		ltpa.execute();
	}
}
