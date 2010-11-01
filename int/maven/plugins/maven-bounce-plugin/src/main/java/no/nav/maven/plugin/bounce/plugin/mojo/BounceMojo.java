package no.nav.maven.plugin.bounce.plugin.mojo;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import no.nav.maven.plugin.bounce.plugin.utils.InvalideNodeValueException;
import no.nav.maven.plugin.bounce.plugin.utils.RestartConfig;
import no.nav.maven.plugin.bounce.plugin.utils.XMLParser;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

//import org.codehaus.plexus.components.interactivity.PrompterException; 
import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;
import org.xml.sax.SAXException;

/**
 * Bounce script that restarts WPS/App target-cluster on WPS if this is
 * specified.
 * 
 * @goal bounce
 * 
 * @author test@example.com
 */
public class BounceMojo extends AbstractMojo {

	/**
	 * Operation that will be sent to the script.
	 * Can be start, stop or restart.
	 */
	private enum Operation {
		START("start"), STOP("stop"), RESTART("restart");
		String action;

		Operation(String s) {
			this.action = s;
		}

		public String toString() {
			return action.toLowerCase();
		}
	}
	
	/**
	 * @parameter expression="${wid.runtime}"
	 * @required
	 */
	protected String widRuntime;

	/**
	 * Indicates whether bus deploy is needed.
	 * 
	 * @parameter expression="${excludeBus}" default-value=false
	 * @required
	 */
	private boolean excludeBus;

	/**
	 * @parameter expression="${environmentFile}"
	 * @required
	 */
	private String envFile;
	
	/**
	 * @parameter expression="${env}"
	 * @required
	 */
	private String env;
	
	/**
	 * @parameter expression="${restartConfigFile}"
	 * @required
	 */
	private String restartCfgFile;

	private String scriptCommand = " -lang jython -f ";
	
	private String scriptName = "ClusterStartStop.py";
	
	/**
	 * @parameter expression="${project.build.directory}"
	 */
	private String buildDir;
	
	
	/**
	 * @parameter expression="${restartJoark}" default-value=false
	 */
	private boolean isJoarkRestartNeeded;

	/**
	 * @parameter expression="${apps}"
	 */
	private String apps;
	
	/**
	 * @parameter expression="${wasSs}"
	 */
	private boolean wasSs;
	
	/**
	 * @parameter expression="${wasIs}"
	 */
	private boolean wasIs;
	
	/**
	 * @parameter expression="${wps}"
	 */
	private boolean wps;
	
	/**
	 * @parameter expression="${action}"
	 * can be start/stop/restart
	 */
	private String action;
	
	private boolean was_ss_operation = false;
	private boolean was_is_operation = false;
	private boolean wps_operation = false;
	
	/**
	 * This method will parse the apps string  together with the restart_config.xml 
	 * and decide which modules should be restarted. 
	 * The appropriate variables are set to true.
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	private void ResolveRestart() throws SAXException, IOException, ParserConfigurationException{
		// apps-string:
		String [] apps_names = apps.split(",");
		
		// restart_config.xml
		File restartConfigFile = new File(this.restartCfgFile);
		HashMap<String, RestartConfig> restart_config = XMLParser.parseRestartConfigFile(restartConfigFile);
		// set variables
		for (String name : apps_names){
			was_ss_operation |= restart_config.get(name).hasWas_ss();
			was_is_operation |= restart_config.get(name).hasWas_is();
			wps_operation |= restart_config.get(name).hasWps();
			if (name.equalsIgnoreCase("joark")) this.isJoarkRestartNeeded = true; 
		}
		if (this.excludeBus) wps_operation = false;
		

	}
	
	public void execute() throws MojoExecutionException {
		try {
			Operation operationMode = Operation.RESTART;;
			if (action != null){
				was_ss_operation = wasSs;
				was_is_operation = wasIs;
				wps_operation = wps;
				if (action.equalsIgnoreCase("start")) operationMode = Operation.START;
				else if (action.equalsIgnoreCase("stop")) operationMode = Operation.STOP;
			} else {
				getLog().info("");
				getLog().info("Parsing restart configuration file ...");
				getLog().info("");
				this.ResolveRestart();
			}
			
			getLog().info("Parsing environment file for parameters: \n " + this.envFile);
			
			File xml = new File(this.envFile);
			String parse_result = XMLParser.parseEnvironmentFile(xml);
			/* parse_result:
			 * was.sensitiv
			 * hostname:d10apvl022.test.internsone.local; 
			 * soap-port:8879; 
			 * ws-username:username; 
			 * ws-password:password;
			 * [was.intern
			 * hostname:d10apvl022.test.internsone.local; 
			 * soap-port:8879; 
			 * ws-username:username; 
			 * ws-password:password;]
			 * wps
			 * hostname:d10apvl022.test.internsone.local; 
			 * soap-port:8879; 
			 * ws-username:username; 
			 * ws-password:password;
			 * */
			
			boolean hasIntern = parse_result.contains("was.intern");
			was_is_operation &= hasIntern;
			getLog().info("Performing " + operationMode.toString() + " on the following servers in " + env );
			if (was_ss_operation){
				if (this.isJoarkRestartNeeded)
					getLog().info(" - WAS SS ( + JOARK )");
				else {
					getLog().info(" - WAS SS");
				}
			}
			if (was_is_operation)
				getLog().info(" - WAS IS");
			if (wps_operation)
				getLog().info(" - WPS");
			
			getLog().info("");

			/*
			 * [0]: was.intern 
			 * [1]: hostname:d10apvl022.test.internsone.local; 
			 * [2]: soap-port:8879; 
			 * [3]: ws-username:username; 
			 * [4]: ws-password:password;
			 */
			String[] was_ss_env_info = null; // was sensitiv
			String[] was_is_env_info = null; // was intern
			String[] wps_env_info = null; // wps
			// It is possible that the env. file does not have was intern properties
			was_ss_env_info = (hasIntern) ? parse_result.substring(0,
					parse_result.indexOf("was.intern")).split("\n")	: 
					parse_result.substring(0,parse_result.indexOf("wps")).split("\n");
			
			if (hasIntern) {
				was_is_env_info = parse_result.substring(
						parse_result.indexOf("was.intern"),
						parse_result.indexOf("wps")).split("\n") ;
			}
			
			wps_env_info = parse_result.substring(parse_result.indexOf("wps")).split("\n");

			if ((operationMode == Operation.STOP || operationMode == Operation.RESTART)) {
				this.modulesStop(was_ss_env_info, was_is_env_info, wps_env_info);
			}
			
			if ((operationMode == Operation.START || operationMode == Operation.RESTART)) {
				this.modulesStart(was_ss_env_info, was_is_env_info, wps_env_info);
			}
		} catch (SAXException e) {
			getLog().error(e.getMessage());
		} catch (IOException e) {
			getLog().error(e.getMessage());
		} catch (ParserConfigurationException e) {
			getLog().error(e.getMessage());
		} catch (InvalideNodeValueException e) {
			getLog().error(e.getMessage());
		}
	}
	
	/**
	 * This method will perform the START operation on the modules based on operation flags
	 * @param was_ss_info - array with was_ss related info: host, port, username, password
	 * @param was_is_info - array with was_is related info: host, port, username, password
	 * @param wps_info - array with wps related info: host, port, username, password
	 */
	private void modulesStart(String [] was_ss_info, String [] was_is_info, String [] wps_info){
		Commandline was_sen_cl = new Commandline(); // was sensitive command line
		Commandline was_int_cl = new Commandline(); // was intern command line
		Commandline wps_cl = new Commandline(); // wps command line
		if (was_ss_operation)
			was_sen_cl = this.prepareCommandline(was_ss_info, Operation.START);
		if (was_is_operation)
			was_int_cl = this.prepareCommandline(was_is_info, Operation.START);
		if (wps_operation)
			wps_cl = this.prepareCommandline(wps_info, Operation.START);

		if (this.was_ss_operation) {
			getLog().info("");
			getLog().info("###########################");
			getLog().info("### STARTING WAS SS ... ###");
			getLog().info("###########################");
			getLog().info("");
			executeCommand(was_sen_cl);
		}
		if (was_is_operation) {
			getLog().info("");
			getLog().info("###########################");
			getLog().info("### STARTING WAS IS ... ###");
			getLog().info("###########################");
			getLog().info("");
			executeCommand(was_int_cl);
		}
		if (wps_operation) {
			getLog().info("");
			getLog().info("########################");
			getLog().info("### STARTING WPS ... ###");
			getLog().info("########################");
			getLog().info("");
			executeCommand(wps_cl);
		}
	}
	
	/**
	 * This method will perform the STOP operation on the modules based on operation flags
	 * @param was_ss_info - array with was_ss related info: host, port, username, password
	 * @param was_is_info - array with was_is related info: host, port, username, password
	 * @param wps_info - array with wps related info: host, port, username, password
	 */
	private void modulesStop(String [] was_ss_info, String [] was_is_info, String [] wps_info){
		Commandline was_sen_cl = new Commandline(); // was sensitive command line
		Commandline was_int_cl = new Commandline(); // was intern command line
		Commandline wps_cl = new Commandline(); // wps command line
		if (was_ss_operation)
			was_sen_cl = this.prepareCommandline(was_ss_info, Operation.STOP);
		if (was_is_operation)
			was_int_cl = this.prepareCommandline(was_is_info, Operation.STOP);
		if (wps_operation)
			wps_cl = this.prepareCommandline(wps_info, Operation.STOP);

		if (this.was_ss_operation) {
			getLog().info("");
			getLog().info("###########################");
			getLog().info("### STOPPING WAS SS ... ###");
			getLog().info("###########################");
			getLog().info("");
			executeCommand(was_sen_cl);
		}
		if (was_is_operation) {
			getLog().info("");
			getLog().info("###########################");
			getLog().info("### STOPPING WAS IS ... ###");
			getLog().info("###########################");
			getLog().info("");
			executeCommand(was_int_cl);
		}
		if (wps_operation) {
			getLog().info("");
			getLog().info("########################");
			getLog().info("### STOPPING WPS ... ###");
			getLog().info("########################");
			getLog().info("");
			executeCommand(wps_cl);
		}
	}
	/**
	 * This method extracts the value from the parameter
	 * 
	 * @param parameter
	 *            A string with the needed value in format: parameter:value;
	 * @return value
	 */
	private String getParameterValue(String parameter) {
		return parameter.substring(parameter.indexOf(':') + 1, parameter.length() - 1);
	}
	
	/**
	 * This method injects all the necessary parameters into the command line
	 * @param params array with parameters (each in format: parameter:value, except for params[0])
	 * @param op operation: start/stop
	 * @return prepared command line
	 */
	private Commandline prepareCommandline(String [] params, Operation op){
		Commandline cmd = new Commandline();
		
		scriptCommand = " -lang jython -f ";
		
		if (Os.isFamily("windows") == true) {
			cmd.setExecutable(widRuntime + "\\bin\\wsadmin.bat");
			scriptCommand += this.buildDir + "\\da-config\\misc\\scripts\\" + 
							 this.scriptName + " " + this.buildDir + "\\da-config\\misc " + op;
 		} else {
			cmd.setExecutable(widRuntime + "/bin/wsadmin.sh");
			scriptCommand += this.buildDir + "/da-config/misc/scripts/" + 
			     			 this.scriptName + " " + this.buildDir + "/da-config/misc " + op;
		}
		if (this.isJoarkRestartNeeded) scriptCommand += " JOARK_RESTART";
		
		
		Commandline.Argument lang = new Commandline.Argument();
		Commandline.Argument host = new Commandline.Argument();
		Commandline.Argument port = new Commandline.Argument();
		Commandline.Argument user = new Commandline.Argument();
		Commandline.Argument pswd = new Commandline.Argument();
		
		// === set parameters === //
		lang.setLine(scriptCommand); // -lang jython -f ClusterStartStop.py ...
		host.setLine("-host " + this.getParameterValue(params[1])); // hostname
		port.setLine("-port " + this.getParameterValue(params[2])); // soap-port
		user.setLine("-user " + this.getParameterValue(params[3])); // username
		pswd.setLine("-password " + this.getParameterValue(params[4])); // password
		
		cmd.addArg(host);
		cmd.addArg(port);
		cmd.addArg(user);
		cmd.addArg(pswd);
		cmd.addArg(lang);
		
		return cmd;
	}
	
	protected final void executeCommand(Commandline command) {
		try {
			
			// If a password is sent as a parameter, we hide it from the output
			if (command.toString().contains("-password")) {
				String cmd = command.toString().replaceFirst("-password\\s[\\w]+", "-password *****");
				getLog().info("Executing the following command: " + cmd);
			}
			else {
				getLog().info("Executing the following command: " + command.toString());
			}
			

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

			CommandLineUtils.executeCommandLine(command, new StreamConsumerChain(systemOut).add(errorChecker),
					new StreamConsumerChain(systemErr).add(errorChecker));

		} catch (CommandLineException e) {
			throw new RuntimeException("An error occured executing: " + command, e);
		}
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
