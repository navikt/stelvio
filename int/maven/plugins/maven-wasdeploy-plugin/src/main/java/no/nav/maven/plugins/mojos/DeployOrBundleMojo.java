package no.nav.maven.plugins.mojos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.parsers.ParserConfigurationException;

import no.nav.maven.plugins.utils.Archiver;
import no.nav.maven.plugins.utils.NativeOps;
import no.nav.maven.plugins.utils.SSHUtil;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;
import org.xml.sax.SAXException;

/**
 * Goal bundles everything or uploads application/batch config and deploys the
 * earfile
 * 
 * @goal deploy-or-bundle
 * 
 * @author test@example.com
 *  
 */
public class DeployOrBundleMojo extends AbstractMojo {

	/**
	 * @parameter expression="${app}"
	 * @required
	 */
	private String application;
	
	/**
	 * @parameter expression="${zone}"
	 * @required
	 */
	private String zone;

	/**
	 * @parameter expression="${environment/bundleEnvironment}"
	 * @required
	 */
	private String bundleEnvironment;

	/**
	 * @parameter expression="${bundleArea}"
	 * @required
	 */
	private String bundleArea;

	/**
	 * @parameter expression="${earPath}"
	 * @required
	 */
	private String earPath;

	/**
	 * @parameter expression="${earName}"
	 * @required
	 */
	private String earName;

	/**
	 * @parameter expression="${configDir}"
	 * @required
	 */
	private String configDir;

	/**
	 * @parameter expression="${batchDir}"
	 */
	private String batchDir;
	
	/**
	 * @parameter expression="${scriptAbsPath}"
	 */
	private String scriptAbsPath;

	/**
	 * @parameter expression="${environment/servers/was/node1/hostname}"
	 */
	private String node1Hostname;

	/**
	 * @parameter expression="${environment/servers/was/node1/linux-app-username}"
	 */
	private String node1LinuxAppUsername;

	/**
	 * @parameter expression="${environment/servers/was/node1/linux-app-password}"
	 */
	private String node1LinuxAppPassword;

	/**
	 * @parameter expression="${environment/servers/was/node1/linux-batch-username}"
	 */
	private String node1LinuxBatchUsername;

	/**
	 * @parameter expression="${environment/servers/was/node1/linux-batch-password}"
	 */
	private String node1LinuxBatchPassword;

	/**
	 * @parameter expression="${environment/servers/was/node2/hostname}"
	 */
	private String node2Hostname;

	/**
	 * @parameter expression="${environment/servers/was/node2/linux-app-username}"
	 */
	private String node2AppUsername;

	/**
	 * @parameter expression="${environment/servers/was/node2/linux-app-password}"
	 */
	private String node2AppPassword;

	/**
	 * @parameter expression="${environment/servers/was/node2/linux-batch-username}"
	 */
	private String node2BatchUsername;

	/**
	 * @parameter expression="${environment/servers/was/node2/linux-batch-password}"
	 */
	private String node2BatchPassword;

	/**
	 * @parameter expression="${environment/servers/was/misc/node2-config-upload}"
	 */
	private boolean node2Upload;

	/**
	 * @parameter expression="${environment/servers/was/dmgr/hostname}"
	 */
	private String dmgrHostname;

	/**
	 * @parameter expression="${environment/servers/was/dmgr/soap-port}"
	 */
	private String dmgrSoapPort;

	/**
	 * @parameter expression="${environment/servers/was/dmgr/ws-username}"
	 */
	private String dmgrUsername;

	/**
	 * @parameter expression="${environment/servers/was/dmgr/ws-password}"
	 */
	private String password;

	/**
	 * @parameter expression="${environment/servers/was/misc/pensjon-server-name}"
	 */
	private String pensjonServerName;

	/**
	 * @parameter expression="${environment/servers/was/misc/pensjon-node-name}"
	 */
	private String pensjonNodeName;

	/**
	 * @parameter expression="${environment/servers/was/misc/pensjon-cluster-name}"
	 */
	private String pensjonClusterName;

	/**
	 * @parameter expression="${environment/servers/was/misc/joark-server-name}"
	 */
	private String joarkServerName;

	/**
	 * @parameter expression="${environment/servers/was/misc/joark-node-name}"
	 */
	private String joarkNodeName;

	/**
	 * @parameter expression="${environment/servers/was/misc/joark-cluster-name}"
	 */
	private String joarkClusterName;

	/**
	 * @parameter expression="${wsadminLocation}"
	 */
	private String wsadminLocation;
	
	/**
	 * @parameter expression="${applicationConfig}"
	 * @required
	 */
	private String applicationConfig;

	public void execute() throws MojoExecutionException, MojoFailureException {
		try {

			boolean isBatch = ProcessDependenciesMojo.isBatch(applicationConfig, application);

			if (bundleEnvironment.equals("true")) {
				bundle(isBatch);

			} else {
				uploadAndDeploy(isBatch);
			}

		} catch (IOException e) {
			throw new MojoFailureException("[ERROR]: " + e);
		} catch (CommandLineException e) {
			throw new MojoFailureException("[ERROR]: " + e);
		} catch (SAXException e) {
			throw new MojoFailureException("[ERROR]: " + e);
		} catch (ParserConfigurationException e) {
			throw new MojoFailureException("[ERROR]: " + e);
		}
	}

	private void bundle(boolean isBatch) throws MojoFailureException, IOException {

		getLog().info("###########################################################################");
		getLog().info("### BUNDLING SELECTED - Creating archives and moving to bundle area ... ###");
		getLog().info("###########################################################################");
		File bundleArea = new File(this.bundleArea);
		bundleArea.mkdir();
		File earArea = new File(bundleArea + "/ear");
		earArea.mkdir();
		File configArea = new File(bundleArea + "/config");
		configArea.mkdir();

		if (isBatch) {
			File batchArea = new File(bundleArea + "/batchklienter");
			batchArea.mkdir();
			Archiver.createArchive(new File(batchDir), batchArea, "jar");
		}

		NativeOps.copy(new File(earPath), new File(earArea + "/" + earName));
		Archiver.createArchive(new File(configDir), configArea, "jar");
	}

	private void uploadAndDeploy(boolean isBatch) throws IOException, CommandLineException, MojoFailureException {

		getLog().info("###########################################################################");
		getLog().info("### DEPLOYMENT SELECTED - Uploading files and deploying application ... ###");
		getLog().info("###########################################################################");
		
		if (node2Upload && !zone.equals("intern")) {
			
			// Upload application config
			SSHUtil.uploadDir(node1Hostname, node1LinuxAppUsername, node1LinuxAppPassword, configDir, "/was_app/config", application);
			SSHUtil.uploadDir(node2Hostname, node2AppUsername, node2AppPassword, configDir, "/was_app/config", application);

			// Upload batch config
			if (isBatch) {
				SSHUtil.uploadDir(node1Hostname, node1LinuxBatchUsername, node1LinuxBatchPassword, batchDir, "/was_app/batch", application);
				SSHUtil.uploadDir(node2Hostname, node2BatchUsername, node2BatchPassword, batchDir, "/was_app/batch", application);
			}

		} else {
			
			SSHUtil.uploadDir(node1Hostname, node1LinuxAppUsername, node1LinuxAppPassword, configDir, "/was_app/config", application);

			if (isBatch)
				SSHUtil.uploadDir(node1Hostname, node1LinuxBatchUsername, node1LinuxBatchPassword, batchDir, "/was_app/batch", application);
		}

		// Deploy the application
		deploy();
	}

	private void deploy() throws CommandLineException, MojoFailureException {

		File scriptFile = new File(scriptAbsPath);
		
		if (!scriptFile.exists()) {
			throw new MojoFailureException("[ERROR] The Jython script file is not at the expected location, " + scriptAbsPath + " - Please make sure the file is present, and rerun the deployment.");
		}

		earName = earName.replace(".ear", "");
		earPath = earPath.replace("\\", "/");
		
		String wsAdmin;
		boolean security = true;

		// If the password is not specified, it assumes the environment doesn't
		// have security enabled.
		if (password == null || password.equals("")) {
			getLog().warn("The environment configuration does not specify a password. This is ok if the environment doesn't have security enabled.");
			security = false;
		}

		Commandline commandLine = new Commandline();
		Commandline.Argument arg1 = new Commandline.Argument();

		if (Os.isFamily("windows") == true) {
			wsAdmin = "wsadmin.bat";
		} else {
			wsAdmin = "wsadmin.sh";
		}

		StringBuilder s = new StringBuilder();

		s.append(wsadminLocation + wsAdmin + " -lang jython");
		s.append(" -host " + dmgrHostname);
		s.append(" -port " + dmgrSoapPort);

		if (security) {
			s.append(" -username " + dmgrUsername);
			s.append(" -password " + password);
		}

		s.append(" -f " + scriptFile.getAbsolutePath() + " ");
		s.append(earName + " ");
		s.append(earPath + " ");

		if (!application.equals("joark")) {
			s.append(pensjonServerName + " ");
			s.append(pensjonNodeName + " ");
			s.append(pensjonClusterName + " ");
		} else {
			s.append(joarkServerName + " ");
			s.append(joarkNodeName + " ");
			s.append(joarkClusterName + " ");
		}

		s.append(application);

		arg1.setLine(s.toString());
		commandLine.addArg(arg1);

		getLog().info("Executing the following commandline: " + commandLine.toString());

		StreamConsumer systemOut = new StreamConsumer() {
			public void consumeLine(String line) {
				System.out.println(line);
			}
		};
		StreamConsumer systemErr = new StreamConsumer() {
			public void consumeLine(String line) {
				System.out.println(line);
			}
		};

		int retval = CommandLineUtils.executeCommandLine(commandLine, new StreamConsumerChain(systemOut), new StreamConsumerChain(systemErr));

		getLog().info("Return value from command line execution was: " + retval);

		/*
		 * We get return value 86 because the wsadmin instance we use is
		 * "incompatible, and unsupported" and we get an error message, but the
		 * deployment works. Can be removed once we upgrade WAS instances to
		 * v.7.0
		 */
		if (retval != 0 && retval != 86) {
			throw new MojoFailureException("[ERROR] Deployment failed, check error messages. Tried to execute: " + commandLine + ". Got return value: " + retval);
		}

		getLog().info(application + " has been successfully deployed.");
	}

	// Needed in order to print the output to SystemOut.
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

}
