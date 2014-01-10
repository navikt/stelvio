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

/**
 * @author test@example.com
 *
 */
public class SshUtil {
	private static String BASE_PATH = "/opt/IBM/BPM/profiles/Dmgr01/bin/";
	private static String OLD_BASE_PATH = "/opt/IBM/WebSphere/ProcServer/profiles/Dmgr01/bin/"; //TODO: @karl.gustav: Denne kan slettes så snart BPM8.5 går i prod
	private static String currentBasePath;
	private static Integer ServerAlreadyStartedExitCode = 255;
	private static Integer ServerAlreadyStoppedExitCode = 246;

	/**
	 * @throws NonZeroSshExitCode
	 */
	private static void executeSingleCommand(SshUser sshUser, String cmd) {
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
					String output = new String(buffer, 0, read);
					System.out.print(output);
				}
			} else {
				throw new RuntimeException("[ERROR] Execution of command failed!");
			}
			session.close();
			Integer exitCode =  session.getExitCode();
			if ( exitCode == null || exitCode != 0 ){
				NonZeroSshExitCode nonZeroSshExitCodeException = new NonZeroSshExitCode("[ERROR] The ssh command had an exitcode different that zero (was "+ exitCode +")!");
				nonZeroSshExitCodeException.setExitCode(exitCode);
				System.out.println("[INFO] Throwing non zero exit code: " + exitCode);
				throw nonZeroSshExitCodeException;
			}
		} catch (IOException e){
			throw new RuntimeException("[ERROR] Execution of command failed!");
		}
		System.out.println("[INFO] Finished executing command.");
	}

	private static boolean executeCommand(String cmd, SessionChannelClient session) throws IOException {

		System.out.println("[INFO] Executing command.");
		boolean result = session.executeCommand(cmd);
		if (result) {
			InputStream in = session.getInputStream();
			byte buffer[] = new byte[255];
			int read;

			while ((read = in.read(buffer)) > 0) {
				String output = new String(buffer, 0, read);
				System.out.print(output);
			}
		} else {
			throw new RuntimeException("[ERROR] Execution of command failed!");
		}

		return result;

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

	/**
	 * @throws NonZeroSshExitCode
	 */
	public static void backupConfig(SshUser sshUser) {
		String baseDir = getBaseDir(sshUser); //TODO: getBaseDir() kan slettes etter at BPM8.5 går i prod
		String cmd = baseDir + "backupConfig.sh " + baseDir + "ConfigBackup_`date +%Y.%m.%d-%H.%M.%S`.zip -nostop";
		executeSingleCommand(sshUser, cmd);
	}

	private static String getBaseDir(SshUser sshUser){
		if(currentBasePath != null){
			return currentBasePath;
		} else {
			try{
				checkIfDirExists(sshUser, BASE_PATH);
				currentBasePath = BASE_PATH;
			} catch (NonZeroSshExitCode e){
				if (e.getExitCode() == null){
					throw new RuntimeException("An error occurred while checking if the target server was a BPM server!");
				}
				currentBasePath = OLD_BASE_PATH;
			}
			System.out.println("[INFO] Setting ssh basepath to: " + currentBasePath);
			return currentBasePath;
		}
	}

	private static void checkIfDirExists(SshUser sshUser, String dir) {
		executeSingleCommand(sshUser, "/usr/bin/test -d " + dir);
	}

	private static void stopDeploymentManager(SshUser sshUser, String dmgrUsername, String dmgrPassword) {
		String cmd = getBaseDir(sshUser) + "stopManager.sh -username "+dmgrUsername+" -password "+dmgrPassword+" -timeout 600"; //TODO: getBaseDir() kan slettes etter at BPM8.5 går i prod
		try {
			executeSingleCommand(sshUser, cmd);
		} catch (NonZeroSshExitCode e){
			if (e.getExitCode() == ServerAlreadyStartedExitCode) {
				System.out.println("[INFO] Server was already stopped! (exitcode " + ServerAlreadyStoppedExitCode + ")");
			} else {
				throw e;
			}
		}
	}
	private static void startDeploymentManager(SshUser sshUser) {
		String cmd = getBaseDir(sshUser) + "startManager.sh"; //TODO: getBaseDir() kan slettes etter at BPM8.5 går i prod
		try {
			executeSingleCommand(sshUser, cmd);
		} catch (NonZeroSshExitCode e){
			if (e.getExitCode() == ServerAlreadyStartedExitCode) {
				System.out.println("[INFO] Server was already running! (exitcode " + ServerAlreadyStartedExitCode + ")");
			} else {
				throw e;
			}
		}
	}

	/**
	 * @throws NonZeroSshExitCode
	 */
	public static void bounceDeploymentManager(SshUser sshUser, String dmgrUsername, String dmgrPassword) {
		stopDeploymentManager(sshUser, dmgrUsername, dmgrPassword);
		startDeploymentManager(sshUser);
	}

	/**
	 * Checks if there is enough disk space available
	 * @return true if there is enough space, false otherwise
	 * @throws IOException
	 */
	public static boolean checkDiskSpace(SshUser sshUser) {
		try {
			SessionChannelClient session = openSshSession(sshUser.getHostname(), sshUser.getUsername(), sshUser.getPassword());
			if (session != null) {
				String cmd = "df /opt/";
				boolean result;
				System.out.println("[INFO] Checking disk space usage in /opt");
				if (session.executeCommand(cmd)) {
					InputStream in = session.getInputStream();
					byte buffer[] = new byte[255];
					int read;

					String output = "";
					while ((read = in.read(buffer)) > 0) {
						output += new String(buffer, 0, read);
						System.out.print(output);
					}
					String space_available = extractSize(output.split("\n")[2].trim());
					result = compareSizeStrings(space_available, "1000000") > 0;
				} else {
					throw new IOException();
				}
				session.close();
				return result;
			} else {
				throw new RuntimeException("[ERROR] Could not create ssh session! Disk space check failed.");
			}
		} catch (IOException e){
			throw new RuntimeException("[ERROR] Execution of checkDiskSpace() command failed!");
		}
	}

	private static String extractSize(String sizeString){
		StringBuilder output = new StringBuilder(sizeString.trim());
		int index;
		while ((index = output.indexOf(" ")) >=0 )
			output.setCharAt(index, '_');
		for (int i=0; i<2; i++){
			output.replace(0, output.indexOf("_"), "");
			while (output.charAt(0) == '_')
				output.deleteCharAt(0);
		}

		return output.substring(0,output.indexOf("_"));
	}

	/**
	 * Compares numbers from strings
	 * @param a
	 * @param b
	 * @return positiv if a>b, negativ if a&ltb or error (message is written out), 0 if either a=b
	 */
	private static int compareSizeStrings(String a, String b){
		int number_a = 0;
		int number_b = 0;
		try{
			number_a = Integer.parseInt(a);
			number_b = Integer.parseInt(b);
			return number_a - number_b;
		}catch (NumberFormatException e){
			throw new RuntimeException("[ERROR] Size is in incorrect format");
		}
	}
}

