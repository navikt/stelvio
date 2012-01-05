package no.nav.maven.plugins;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * Goal which fetches the versions of the modules deployed on the WPS in the given environment 
 * by using a python script.
 * 
 * @goal getVersions
 * @phase initialize
 * 
 * @author person4fdbf4cece95, Accenture
 */

public class GetVersionsMojo extends AbstractMojo {

		/**
		 * The maven project
		 * 
		 * @parameter expression="${project}"
		 * @required
		 * @readonly
		 */
		private MavenProject project;
		
		/**
		 * @parameter expression="${wid.runtime}"
		 * @required
		 */
		protected String widRuntime;
		
		/**
		 * @parameter
		 * @required
		 */
		private ModuleVersion[] versions;
		
		/**
		 * @parameter expression="${env}"
		 * @required
		 */
		protected String env;
		
		private Commandline commandLine;
		
		/**
		 * The mojo method that executes the script, and then reads the properties from 
		 * a temporary file before exposing the properties (version numbers).  
		 */
		public void execute() throws MojoExecutionException, MojoFailureException {

			String varNames = "";
			
			for (ModuleVersion mv : versions){
				varNames=varNames + "," + mv.getVariableName();
			}
			
			String script = project.getBasedir() + "/target/src/main/resources/scripts/GetVersions.py";
			commandLine = new Commandline();
			prepareCommand();
			
			getLog().debug("Executing GetVersions.py");
			Commandline.Argument arg = new Commandline.Argument();
			arg.setLine("-f " + script + " " + varNames);
			commandLine.addArg(arg);
						
			Properties props = new Properties();
			FileInputStream is;
			// forventer fil på formen NAV_MODULES_VERSION=7.3.0.22,ESB_MODULES_VERSION=7.3.0.21 med 
			//linjeskift i stedet for komma
			try {
				is = new FileInputStream(project.getBuild().getOutputDirectory() + "/temp.txt");
				props.load(is);
				
				if (!props.isEmpty()){
					while(props.keys().hasMoreElements()){
						String fetchedProps = (String) props.keys().nextElement();
						getLog().debug("Fetched version: " + fetchedProps);
						String[] versionsProp = fetchedProps.split("=");
						project.getProperties().put(versionsProp[0], versionsProp[1]);
					}
				} else {
					throw new MojoExecutionException("[ERROR]: " + "No versions found. ");
				}
				
			} catch (IOException e) {
				throw new MojoExecutionException("[ERROR]: " + e);
			}
			
		}
		
		/**
		 * Method for preparing the script execution
		 */
		private void prepareCommand() throws MojoExecutionException{
			
			//Retrieving necessary properties for running wsadmin command on wps dmgr from busconfiguration:  
			Properties properties = new Properties();
			try {
				properties.load(new FileInputStream(project.getBuild().getOutputDirectory() + "/bus-config/environments/" + env.toUpperCase() + ".properties"));
			} catch (IOException e) {
				throw new MojoExecutionException("[ERROR]: " + "No properties found: " + e);
			}
			
			if (properties == null){
				throw new MojoExecutionException("[ERROR]: " + "Missing necessary WPS properties");
			}

			String dmgrHostname = properties.getProperty("dmgrHostname");
			String dmgrSOAPPort = properties.getProperty("dmgrSOAPPort");
			String dmgrUsername = properties.getProperty("dmgrUsername");
			String dmgrPassword = properties.getProperty("dmgrPassword");
			
			/* Given that the variable wid.runtime is set correctly in settings.xml */
			if (Os.isFamily("windows") == true) {
				commandLine.setExecutable(widRuntime + "/bin/wsadmin.bat");
			} else {
				commandLine.setExecutable(widRuntime + "/bin/wsadmin.sh");
			}
			
			Commandline.Argument arg1 = new Commandline.Argument();
			arg1.setLine("-host " + dmgrHostname);
			commandLine.addArg(arg1);

			Commandline.Argument arg2 = new Commandline.Argument();
			arg2.setLine("-port " + dmgrSOAPPort);
			commandLine.addArg(arg2);

			Commandline.Argument arg3 = new Commandline.Argument();
			arg3.setLine("-user " + dmgrUsername);
			commandLine.addArg(arg3);

			Commandline.Argument arg4 = new Commandline.Argument();
			arg4.setLine("-password " + dmgrPassword);
			commandLine.addArg(arg4);
		}

}
