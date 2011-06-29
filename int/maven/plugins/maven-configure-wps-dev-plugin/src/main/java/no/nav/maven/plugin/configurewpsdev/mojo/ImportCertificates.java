package no.nav.maven.plugin.configurewpsdev.mojo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import no.nav.maven.plugin.configurewpsdev.utils.Utils;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * 
 * @goal import-certificates
 * 
 * @phase process-sources
 */
public class ImportCertificates extends AbstractWSAdminMojo {
	/**
	 * @parameter
	 * @required
	 */
	protected List<String> certificates;
	
	@Override
	protected void runWSAdmin(Commandline commandLine)
			throws MojoExecutionException, MojoFailureException {
		
		for (String certificate:certificates) {
			URL certificateURL;
			try {
				certificateURL = new URL(certificate);
				Utils.downloadURL(certificateURL, targetFolder);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		Commandline.Argument arg0 = new Commandline.Argument();
		arg0.setLine(scriptsHome + "\\importCertificates.py");
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
