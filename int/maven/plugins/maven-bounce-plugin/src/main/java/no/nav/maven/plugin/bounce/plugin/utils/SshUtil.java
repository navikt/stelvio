package no.nav.maven.plugin.bounce.plugin.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

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
		
		System.out.println("[INFO] Executing command via ssh");
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
		System.out.println("Authentication OK with username "+username+" and password ***** on "+hostname);
		return ssh.openSessionChannel();
	}
	
	public static boolean stopWebSphereProcess(String server_type, String profile, String hostname, String username, String password, String wsAdminUsername, String wsAdminPassword) throws IOException {
		SessionChannelClient session = openSshSession(hostname, username, password);
		if (session != null) {
			String cmd;
			if (profile.toLowerCase().contains("dmgr"))
				cmd = "/opt/IBM/WebSphere/"+server_type+"/profiles/"+profile+"/bin/stopManager.sh -username "+wsAdminUsername+" -password "+wsAdminPassword;
			else cmd = "/opt/IBM/WebSphere/"+server_type+"/profiles/"+profile+"/bin/stopNode.sh -username "+wsAdminUsername+" -password "+wsAdminPassword;
			boolean result = executeCommand(cmd, session);
			session.close();
			return result;
		} else {
			System.out.println("[ERROR] Could not create ssh session! Stop operation failed.");
			return false;
		}
	}
	
	public static boolean startWebSphereProcess(String server_type, String profile, String hostname, String username, String password) throws IOException {
		SessionChannelClient session = openSshSession(hostname, username, password);
		if (session != null) {
			String cmd;
			if (profile.toLowerCase().contains("dmgr"))
				cmd = "/opt/IBM/WebSphere/"+server_type+"/profiles/"+profile+"/bin/startManager.sh";
			else cmd = "/opt/IBM/WebSphere/"+server_type+"/profiles/"+profile+"/bin/startNode.sh";
			boolean result = executeCommand(cmd, session);
			session.close();
			return result;
		} else {
			System.out.println("[ERROR] Could not create ssh session! Start operation failed.");
			return false;
		}
	}
	
	public static boolean isIFixInstallationNeeded(String hostname, String username, String password) throws IOException {
		boolean isInstallationNeeded = false;
		
		SessionChannelClient session = openSshSession(hostname, username, password);
		if (session == null) {
			System.out.println("Could not open ssh connection to " + hostname);
			return false;
		}
		System.out.println("[INFO] Checking .../pending_fixpacks/ folder");
		boolean result = session.executeCommand("ls /home/wasadm/pending_fixpacks/ | grep .pak");
		if (result) {
			InputStream in = session.getInputStream();
			byte buffer[] = new byte[255];
			int read;
						
			while ((read = in.read(buffer)) > 0) {
				String output = new String(buffer, 0, read);
				System.out.println(output);
				if (output.contains(".pak")) isInstallationNeeded = true;
			}	
		} else {
			System.out.println("[ERROR] Execution of command failed!");
		}
		session.close();
		return isInstallationNeeded;
	}
	
	public static HashMap<String, String> getProfileNames(String hostname, String username, String password, String server_type) throws IOException{
		HashMap<String, String> profileNames = new HashMap<String, String>(0);
		
		SessionChannelClient session = openSshSession(hostname, username, password);
		if (session == null) {
			System.out.println("Could not open ssh connection to " + hostname);
			return profileNames;
		}
		String profilesFolder = "/opt/IBM/WebSphere/" + server_type + "/profiles/";
		System.out.println("[INFO] Checking content of " + profilesFolder + " folder on " + hostname);
		boolean result = session.executeCommand("ls -1 " + profilesFolder);
		if (result) {
			InputStream in = session.getInputStream();
			byte buffer[] = new byte[255];
			int read;
					
			String output ="";
			while ((read = in.read(buffer)) > 0) {
				output += new String(buffer, 0, read);
			}
			// parse output of ls command and figure out what profiles are called
			if (output.length()>0 && !output.contains("No such file or directory")){
				String [] profiles = output.split("\n");
				for (String profile : profiles){
					if (profile.toLowerCase().contains("dmgr")) profileNames.put("dmgr", profile);
					if (profile.toLowerCase().contains("app") || profile.toLowerCase().contains("wps")){
						if (profile.contains("1"))profileNames.put("node1", profile);
						if (profile.contains("2"))profileNames.put("node2", profile);
						if (profile.contains("3"))profileNames.put("node3", profile);
					}
				}
				System.out.println("[INFO] Found following profiles:");
				for (String key : profileNames.keySet()){
					System.out.println(key + ": " + profileNames.get(key));
				}
			}else System.out.println("[ERROR] Found no profiles on folder. Check if there is a server on " + hostname);
		} else {
			System.out.println("[ERROR] Execution of command failed!");
		}
		session.close();
		return profileNames;
	}
	
	public static boolean installIFixes(String hostname, String username, String password) throws IOException{
		SessionChannelClient session = openSshSession(hostname, username, password);
		if (session != null) {
			String cmd = "sh /home/wasadm/pending_fixpacks/install_fixpacks.sh";
			boolean result = executeCommand(cmd, session);
			session.close();
			return result;
		} else {
			System.out.println("[ERROR] Could not create ssh session! Start operation failed.");
			return false;
		}
	}
}
