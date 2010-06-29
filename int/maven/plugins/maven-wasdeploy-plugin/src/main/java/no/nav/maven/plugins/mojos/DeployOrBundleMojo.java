package no.nav.maven.plugins.mojos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import no.nav.maven.plugins.utils.ApplicationArtifactDependency;
import no.nav.maven.plugins.utils.Archiver;
import no.nav.maven.plugins.utils.NativeOps;
import no.nav.maven.plugins.utils.SSHUtil;

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
 * Goal bundles everything or uploads application/batch config and deploys the earfile
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
	 * @parameter expression="${project}"
	 * @readonly
	 * @required
	 */
	private MavenProject project;

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
	 * @parameter expression="${environment/server/was-node1-hostname}"
	 */
	private String n1Hostname;

	/**
	 * @parameter expression="${environment/server/was-node1-username}"
	 */
	private String n1Username;

	/**
	 * @parameter expression="${environment/server/was-node1-password}"
	 */
	private String n1Password;

	/**
	 * @parameter expression="${environment/server/was-node2-hostname}"
	 */
	private String n2Hostname;

	/**
	 * @parameter expression="${environment/server/was-node2-username}"
	 */
	private String n2Username;

	/**
	 * @parameter expression="${environment/server/was-node2-password}"
	 */
	private String n2Password;
	
	/**
	 * @parameter expression="${environment/server/node2-config-upload}"
	 */
	private boolean node2Upload;

	/**
	 * @parameter expression="${environment/server/was-dmgr-hostname}"
	 */
	private String hostname;
	
	/**
	 * @parameter expression="${environment/server/was-dmgr-soap-port}"
	 */
	private String soapPort;
	
	/**
	 * @parameter expression="${environment/server/was-dmgr-username}"
	 */
	private String username;
	
	/**
	 * @parameter expression="${environment/server/was-dmgr-password}"
	 */
	private String password;
	
	/**
	 * @parameter expression="${environment/server/was-server-name}"
	 */
	private String serverName;
	
	/**
	 * @parameter expression="${environment/server/was-node-name}"
	 */
	private String nodeName;
	
	/**
	 * @parameter expression="${environment/server/was-cluster-name}"
	 */
	private String clusterName;
	
	/**
	 * @parameter expression="${wsadminLocation}"
	 */
	private String wsadminLocation;

	public void execute() throws MojoExecutionException, MojoFailureException {
		try {

			boolean isBatch = ApplicationArtifactDependency.isBatch(application);

			if (bundleEnvironment.equals("true")) {
				bundle(isBatch);

			} else {
				uploadAndDeploy(isBatch);
			}

		} catch (IOException e) {
			throw new MojoFailureException("[ERROR]: " + e);
		} catch (CommandLineException e) {
			throw new MojoFailureException("[ERROR]: " + e);
		}
	}

	private void bundle(boolean isBatch) throws MojoFailureException, IOException {

		getLog().info("###########################################################################");
		getLog().info("### BUNDLING SELECTED - Creating archives and moving to bundle area ... ###");
		getLog().info("###########################################################################");

		File bundleArea = new File(this.bundleArea);
		File earArea = new File(bundleArea + "/ear");
		earArea.mkdirs();
		File configArea = new File(bundleArea + "/config");
		configArea.mkdirs();

		if (isBatch) {
			File batchArea = new File(bundleArea + "/batchklienter");
			batchArea.mkdirs();
			Archiver.createArchive(new File(batchDir), batchArea, "jar");
		}

		NativeOps.copy(new File(earPath), new File(earArea + "/" + earName));
		Archiver.createArchive(new File(configDir), configArea, "jar");
	}

	private void uploadAndDeploy(boolean isBatch) throws IOException, CommandLineException, MojoFailureException {

		getLog().info("###########################################################################");
		getLog().info("### DEPLOYMENT SELECTED - Uploading files and deploying application ... ###");
		getLog().info("###########################################################################");

		if (node2Upload) {
			// Upload application config
			SSHUtil.uploadDir(n1Hostname, n1Username, n1Password, configDir, "/was_app/config", application);
			SSHUtil.uploadDir(n2Hostname, n2Username, n2Password, configDir, "/was_app/config", application);

			// Upload batch config
			if (isBatch) {
				SSHUtil.uploadDir(n1Hostname, n1Username, n1Password, batchDir, "/was_app/batch", application);
				SSHUtil.uploadDir(n2Hostname, n2Username, n2Password, batchDir, "/was_app/batch", application);
			}

		} else {
			SSHUtil.uploadDir(n1Hostname, n1Username, n1Password, configDir, "/was_app/config", application);

			if (isBatch)
				SSHUtil.uploadDir(n1Hostname, n1Username, n1Password, batchDir, "/was_app/batch", application);
		}

		// Deploy the application
		deploy();
	}

	private void deploy() throws CommandLineException, MojoFailureException {
		
		String expectedLocation = project.getBasedir() + "/scripts/InstallApp.py";
		File scriptFile = new File(expectedLocation);
		earName = earName.replace(".ear", "");
		earPath = earPath.replace("\\", "/");
		
		if (!scriptFile.exists()) {
			throw new MojoFailureException("[ERROR] The Jython script file is not at the expected location, " + expectedLocation + " - Please make sure the file is present, and rerun the deployment.");
		}
		
		String wsAdmin;
		boolean security = true;

		if (password == null || password.equals("")) {
			System.out.println("[WARN] ### DEPLOY ### The environment configuration does not specify a password. This is ok if the environment doesn't have security enabled.");
			security = false;
		}

		Commandline commandLine = new Commandline();
		Commandline.Argument arg1 = new Commandline.Argument();

		if (Os.isFamily("windows") == true) {
			wsAdmin = "/wsadmin.bat";
		} else {
			wsAdmin = "/wsadmin.sh";
		}

		StringBuilder s = new StringBuilder();

		s.append(wsadminLocation + wsAdmin + " -lang jython");
		s.append(" -host " + hostname);
		s.append(" -port " + soapPort);

		if (security) {
			s.append(" -username " + username);
			s.append(" -password " + password);
		}

		s.append(" -f " + scriptFile.getAbsolutePath() + " ");
		s.append(earName + " ");
		s.append(earPath + " ");
		s.append(serverName + " ");
		s.append(nodeName + " ");
		s.append(clusterName + " ");
		s.append(application);

		arg1.setLine(s.toString());
		commandLine.addArg(arg1);

		System.out.println("[INFO] ### DEPLOY ### Executing the following commandline: " + commandLine.toString());

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

		CommandLineUtils.executeCommandLine(commandLine, new StreamConsumerChain(systemOut), new StreamConsumerChain(systemErr));
		
		System.out.println("[INFO] ### DEPLOY ### The application: " + application + " has been successfully deployed.");
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
