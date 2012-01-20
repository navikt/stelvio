package no.nav.maven.plugins;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

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

		/**
		 * @parameter expression="${project.build.directory}"
		 * @required
		 */
		protected String deployDir;
		
		private Commandline commandLine;
		
		/**
		 * The mojo method that executes the script, and then reads the properties from 
		 * a temporary file before exposing the properties (version numbers).  
		 */
		public void execute() throws MojoExecutionException, MojoFailureException {

			String varNames = versions[0].getVariableName();
			
			for (int i=1;i<versions.length;i++){
				varNames=varNames + "," + versions[i].getVariableName();
			}

			File script;
			try {
				script = getPythonScriptFromClassPath("GetVersions.py");
			} catch (IOException e1) {
				throw new MojoExecutionException("[ERROR]: " + "Script not found: " + e1);
			}
			String outputDir = deployDir;
			commandLine = new Commandline();
			prepareCommand();

			getLog().debug("Executing GetVersions.py");
			Commandline.Argument arg = new Commandline.Argument();
			arg.setLine(" -f " + script + " " + varNames);

			commandLine.addArg(arg);
			//System.out.println("RUNNING " + commandLine.toString());
			//For å få med logging fra scriptet:  
			StreamConsumer systemOut = new StreamConsumer() {
				public void consumeLine(String line) {
					getLog().info(line);
				}
			};
			StreamConsumer systemErr = new StreamConsumer() {
				public void consumeLine(String line) {
					getLog().error(line);
				}
			};
			ErrorCheckingStreamConsumer errorChecker = new ErrorCheckingStreamConsumer();

			try {
				
				CommandLineUtils.executeCommandLine(commandLine, new StreamConsumerChain(systemOut).add(errorChecker),
						new StreamConsumerChain(systemErr).add(errorChecker));
			} catch (CommandLineException e) {
				throw new MojoExecutionException("[ERROR]: " + e);
			}

			if (errorChecker.isError()) {
				throw new RuntimeException("An error occured during deploy. Stopping deployment. Consult the logs.");
			}
			Properties props = new Properties();
			FileInputStream is;
			// forventer fil på formen NAV_MODULES_VERSION=7.3.0.22,ESB_MODULES_VERSION=7.3.0.21 med 
			//linjeskift i stedet for komma
			try {
				is = new FileInputStream(outputDir + "/temp.txt");
				props.load(is);
				List<String> failed = new ArrayList<String>();
				if (!props.isEmpty()){
					Enumeration<Object> em = props.keys();
					while(em.hasMoreElements()){
						String propKey = (String) em.nextElement();
						for (ModuleVersion mv : versions) {
							if (propKey.equals(mv.getVariableName())){
								String propValue = props.getProperty(propKey);
								
								if (propValue.contains("$")){//flyttes til etterpå for å få med alle som feiler?
									failed.add(propKey);
								}
								getLog().info("Exposing: " + mv.getExposedAs() + " = " + propValue);
								project.getProperties().put(mv.getExposedAs(), propValue);
							}
						}
					}
				} else {
					throw new MojoExecutionException("[ERROR]: " + "No versions found. ");
				}
				if (failed.size()>0){
					String error = null;
					for (String s : failed) {
						error = error + ", " + s;
					}
					throw new MojoExecutionException("[ERROR]: Variables " + error + " was not set correctly. ");
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
				properties.load(new FileInputStream(project.getBasedir() + "/target/dependency/busconfiguration-jar/environments/" + env.toUpperCase() + ".properties"));
			} catch (IOException e) {
				throw new MojoExecutionException("[ERROR]: " + "No properties found: " + e);
			}
			
			if (properties == null){
				throw new MojoExecutionException("[ERROR]: " + "Missing necessary WPS properties");
			}

			getLog().debug("Found properties.");
			
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
			arg1.setLine(" -host " + dmgrHostname);
			commandLine.addArg(arg1);

			Commandline.Argument arg2 = new Commandline.Argument();
			arg2.setLine(" -port " + dmgrSOAPPort);
			commandLine.addArg(arg2);

			Commandline.Argument arg3 = new Commandline.Argument();
			arg3.setLine(" -user " + dmgrUsername);
			commandLine.addArg(arg3);

			Commandline.Argument arg4 = new Commandline.Argument();
			arg4.setLine(" -password " + dmgrPassword);
			commandLine.addArg(arg4);
		}
		
		/**
		 * Retrives files from the classpath and writes it to the deploy directory
		 * 
		 * @param filename
		 * @return
		 * @throws IOException
		 */
		private File getPythonScriptFromClassPath(String filename) throws IOException {

			File outFile = new File(deployDir + "/" + filename);

			getLog().debug("Extracting " + filename + " from classpath into " + outFile);

			InputStream in = getClass().getResourceAsStream("/scripts/" + filename);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			OutputStream out = new FileOutputStream(outFile);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
			String line;

			while ((line = br.readLine()) != null) {
				bw.write(line);
				bw.newLine();
			}

			br.close();
			bw.close();

			return outFile;
		}
		
		private static class StreamConsumerChain implements StreamConsumer {
			private final Collection<StreamConsumer> chain = new ArrayList<StreamConsumer>();

			public StreamConsumerChain() {
			}

			public StreamConsumerChain(StreamConsumer streamConsumer) {
				add(streamConsumer);
			}

			public StreamConsumerChain add(StreamConsumer streamConsumer) {
				chain.add(streamConsumer);
				return this;
			}

			public void consumeLine(String line) {
				for (StreamConsumer streamConsumer : chain) {
					streamConsumer.consumeLine(line);
				}
			}
		}

		private static class ErrorCheckingStreamConsumer implements StreamConsumer {
			private boolean error;

			public void consumeLine(String line) {
				if (line.toLowerCase().contains("error")) {
					error = true;
				}
			}

			public boolean isError() {
				return error;
			}
		}

}
