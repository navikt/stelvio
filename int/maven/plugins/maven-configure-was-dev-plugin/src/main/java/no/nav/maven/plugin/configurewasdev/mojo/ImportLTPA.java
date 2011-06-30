package no.nav.maven.plugin.configurewasdev.mojo;

import java.net.MalformedURLException;
import java.net.URL;

import no.nav.maven.plugin.configurewasdev.utils.Utils;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * 
 * @goal import-ltpa
 * 
 * @phase process-sources
 */
public class ImportLTPA extends AbstractWSAdminMojo {
	/**
	 * @parameter expression="${ltpaurl}"
	 * @required
	 */
	protected String ltpaurl;
	
	@Override
	protected void runWSAdmin(Commandline commandLine) throws MojoExecutionException, MojoFailureException {
		
		URL ltpaURL;
		try {
			ltpaURL = new URL(ltpaurl);
			Utils.downloadURL(ltpaURL, targetFolder);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Commandline.Argument arg0 = new Commandline.Argument();
		arg0.setLine(scriptsHome + "\\importLTPA.py");
		commandLine.addArg(arg0);
		Commandline.Argument arg1 = new Commandline.Argument();
		arg1.setLine(scriptsHome);
		commandLine.addArg(arg1);
		Commandline.Argument arg2 = new Commandline.Argument();
		arg2.setLine(targetFolder.toString());
		commandLine.addArg(arg2);
		executeCommand(commandLine);

	}

}
