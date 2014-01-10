package no.nav.maven.plugin.wpsdeploy.plugin.utils;

import java.io.IOException;
import java.io.InputStream;

import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.authentication.AuthenticationProtocolState;
import com.sshtools.j2ssh.authentication.PasswordAuthenticationClient;
import com.sshtools.j2ssh.session.SessionChannelClient;
import com.sshtools.j2ssh.transport.HostKeyVerification;
import com.sshtools.j2ssh.transport.TransportProtocolException;
import com.sshtools.j2ssh.transport.publickey.SshPublicKey;
import no.nav.maven.plugin.wpsdeploy.plugin.exceptions.NonZeroSshExitCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author test@example.com
 *
 */
public class SshUtil {
	private static String BASE_PATH = "/opt/IBM/BPM/profiles/Dmgr01/bin/";
	private static String OLD_BASE_PATH = "/opt/IBM/WebSphere/ProcServer/profiles/Dmgr01/bin/"; //TODO: @karl.gustav: Denne kan slettes så snart BPM8.5 går i prod
	private static String currentBasePath;

	private final static Logger logger = LoggerFactory.getLogger(SshUtil.class);

	/**
	 * @throws NonZeroSshExitCode
	 */
	public static String executeCommand(SshUser sshUser, String cmd) {
		StringBuffer output = new StringBuffer();


		try {
			SessionChannelClient session = openSshSession(sshUser.getHostname(), sshUser.getUsername(), sshUser.getPassword());
			if (session == null){
				throw new RuntimeException("[ERROR] Could not create ssh session! Backup config failed.");
			}
			if (session.executeCommand(cmd)) {
				InputStream in = session.getInputStream();
				byte buffer[] = new byte[255];
				int read;

				while ((read = in.read(buffer)) > 0) {
					output.append(new String(buffer, 0, read));
				}
			} else {
				throw new RuntimeException("[ERROR] Execution of command failed!");
			}

			session.close();
			Thread.sleep(20);
			while (! session.isClosed()){
				int furtherWaitTime = 1000;
				logger.info("Waiting " + furtherWaitTime + " milliseconds for the SSH channel to properly close...");
				Thread.sleep(furtherWaitTime);
			}

			Integer exitCode =  session.getExitCode();
			if ( exitCode == null || exitCode != 0 ){
				NonZeroSshExitCode nonZeroSshExitCodeException = new NonZeroSshExitCode("[ERROR] The ssh command had an exitcode different that zero (was "+ exitCode +")!");
				nonZeroSshExitCodeException.setExitCode(exitCode);
				logger.info("Throwing non zero exit code: " + exitCode);
				throw nonZeroSshExitCodeException;
			}
		} catch (IOException e){
			throw new RuntimeException("[ERROR] Execution of command failed!");
		} catch (InterruptedException e){
			throw new RuntimeException("[ERROR] Waiting for ssh channel to close was interrupted!");
		}
		logger.info("Finished executing command.");
		return output.toString();
	}

	private static SessionChannelClient openSshSession(String hostname, String username, String password) throws IOException {
		HostKeyVerification hkv = new HostKeyVerification() {
			public boolean verifyHost(String arg0, SshPublicKey arg1) throws TransportProtocolException {
				return true;
			}
		};

		java.util.logging.Logger.getLogger("com.sshtools").setLevel(java.util.logging.Level.OFF);

		SshClient ssh = new SshClient();
		ssh.connect(hostname, 22, hkv);

		PasswordAuthenticationClient pac = new PasswordAuthenticationClient();

		pac.setUsername(username);
		pac.setPassword(password);

		int result = ssh.authenticate(pac);

		if (result != AuthenticationProtocolState.COMPLETE) {
			throw new RuntimeException("[ERROR] The authentication failed for user "+username+" on "+hostname+".");
		}

		return ssh.openSessionChannel();
	}


	public static String getBaseDir(SshUser sshUser){
		if(currentBasePath != null){
			return currentBasePath;
		} else {
			try{
				SshCommands.checkIfDirExists(sshUser, BASE_PATH);
				currentBasePath = BASE_PATH;
			} catch (NonZeroSshExitCode e){
				if (e.getExitCode() == null){
					throw new RuntimeException("An error occurred while checking if the target server was a BPM server!");
				}
				currentBasePath = OLD_BASE_PATH;
			}
			logger.info("Setting ssh basepath to: " + currentBasePath);
			return currentBasePath;
		}
	}

}

