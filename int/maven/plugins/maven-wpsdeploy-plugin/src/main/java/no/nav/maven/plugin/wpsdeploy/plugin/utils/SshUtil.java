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

/**
 * @author test@example.com
 *
 */
public class SshUtil {
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
			System.out.println("[ERROR] Execution of command failed!");
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
			System.out.println("[ERROR] The authentication failed for user "+username+" on "+hostname+".");
			return null;
		}
		
		return ssh.openSessionChannel();
	}
	
	public static boolean backupConfig(String hostname, String username, String password) throws IOException {
		SessionChannelClient session = openSshSession(hostname, username, password);
		if (session != null) {
			String cmd = "/opt/IBM/WebSphere/ProcServer/profiles/Dmgr01/bin/backupConfig.sh " +
			"/opt/IBM/WebSphere/ProcServer/profiles/Dmgr01/bin/WebSphereConfig_`date +%d%m%Y%H%M%S`.zip -nostop";
			boolean result = executeCommand(cmd, session);
			session.close();
			return result;
		} else {
			System.out.println("[ERROR] Could not create ssh session! Backup config failed.");
			return false;
		}
	}
	
	public static boolean stopDeploymentManager(String hostname, String username, String password, String dmgrUsername, String dmgrPassword) throws IOException {
		SessionChannelClient session = openSshSession(hostname, username, password);
		//session.startShell();
		if (session != null) {
			String cmd = "/opt/IBM/WebSphere/ProcServer/profiles/Dmgr01/bin/stopManager.sh -username "+dmgrUsername+" -password "+dmgrPassword+" -trace -timeout 300";
			boolean result = executeCommand(cmd, session);
			session.close();
			return result;
		} else {
			System.out.println("[ERROR] Could not create ssh session! Stop manager failed.");
			return false;
		}
	}
	
	public static boolean startDeploymentManager(String hostname, String username, String password) throws IOException {
		SessionChannelClient session = openSshSession(hostname, username, password);
		if (session != null) {
			String cmd = "/opt/IBM/WebSphere/ProcServer/profiles/Dmgr01/bin/startManager.sh";
			boolean result = executeCommand(cmd, session);
			session.close();
			return result;
		} else {
			System.out.println("[ERROR] Could not create ssh session! Stop manager failed.");
			return false;
		}
	}
	
	public static boolean bounceDeploymentManager(String hostname, String username, String password, String dmgrUsername, String dmgrPassword) throws IOException {
		boolean stop = stopDeploymentManager(hostname, username, password, dmgrUsername, dmgrPassword);
		if (stop) {
			System.out.println("[INFO] Deployment manager stopped successfully.");
			boolean start = startDeploymentManager(hostname, username, password);
			if (start) {
				System.out.println("[INFO] Deployment manager started successfully.");
				return start;
			} else {
				return start;
			}
		} else {
			return stop;
		}
	}

	/**
	 * Checks if there is enough disk space available
	 * @param hostname
	 * @param username
	 * @param password
	 * @return true if there is enough space, false otherwise
	 * @throws IOException
	 */
	public static boolean checkDiskSpace(String hostname, String username, String password) throws IOException{
		SessionChannelClient session = openSshSession(hostname, username, password);
		if (session != null) {
			String cmd = "df /opt/";
			boolean result = false;
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
				result = compareSizeStrings(space_available, "1000000") > 0; // size_available > 500M
			} else {
				System.out.println("[ERROR] Execution of command failed!");
			}
			return result;
		} else {
			System.out.println("[ERROR] Could not create ssh session! Disk space check failed.");
			return false;
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
			System.out.println("[ERROR] Size is in incorrect format");
			return -1;
		}
	}
}

