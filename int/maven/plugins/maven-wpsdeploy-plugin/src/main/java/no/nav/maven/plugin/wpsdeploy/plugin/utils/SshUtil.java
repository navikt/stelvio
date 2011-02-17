package no.nav.maven.plugin.wpsdeploy.plugin.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.omg.CORBA.SystemException;

import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.authentication.AuthenticationProtocolState;
import com.sshtools.j2ssh.authentication.PasswordAuthenticationClient;
import com.sshtools.j2ssh.session.SessionChannelClient;
import com.sshtools.j2ssh.transport.HostKeyVerification;
import com.sshtools.j2ssh.transport.TransportProtocolException;
import com.sshtools.j2ssh.transport.publickey.SshPublicKey;

/**
 * @author utvikler
 *
 */
public class SshUtil {
	private static boolean executeCommand(String cmd, SessionChannelClient session) throws IOException {
		
		System.out.println("[INFO] Executing: "+cmd);
		boolean result = session.executeCommand(cmd);
		
		if (result) {
			InputStream in = session.getInputStream();
			byte buffer[] = new byte[255];
			int read;
			int dots = 0;
						
			while ((read = in.read(buffer)) > 0) {
				String output = new String(buffer, 0, read);
				if (output.equals(".")) {
					dots++;
					if (dots % 1000 == 0) {
						System.out.println("           " + dots + " files backed up.");
					} 
				} else if (output != " " && output != "\n") {
					System.out.print(output);
				}
			}	
		} else {
			System.out.println("[ERROR] Execution of command "+cmd+" failed!");
		}
		
		return result;
		
	}
	private static SessionChannelClient openSshSession(String hostname, String username, String password) throws IOException {
		HostKeyVerification hkv = new HostKeyVerification() {
			public boolean verifyHost(String arg0, SshPublicKey arg1) throws TransportProtocolException {
				return true;
			}
		};

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
		if (session != null) {
			String cmd = "/opt/IBM/WebSphere/ProcServer/profiles/Dmgr01/bin/stopManager.sh -username "+dmgrUsername+" -password "+dmgrPassword;
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
		SessionChannelClient session = openSshSession(hostname, username, password);
		if (session != null) {
			boolean stop = stopDeploymentManager(hostname, username, password, dmgrUsername, dmgrPassword);
			if (stop) {
				System.out.println("[INFO] Deployment manager stopped successfully.");
				boolean start = startDeploymentManager(hostname, username, password);
				if (start) {
					System.out.println("[INFO] Deployment manager started successfully.");
					session.close();
					return start;
				} else {
					session.close();
					return start;
				}
			} else {
				session.close();
				return stop;
			}
			
		} else {
			System.out.println("[ERROR] Could not create ssh session! Stop manager failed.");
			return false;
		}
	}
	
	public static void main (String args[]) throws IOException {
			String hostname = "e11apvl127.utv.internsone.local";
			String username = "wasadm";
			String password = "W43Kopiaq";
			String dmgrUsername = "srvpensjon";
			String dmgrPassword = "Ash5SoxP";
			backupConfig(hostname, username, password);
			bounceDeploymentManager(hostname, username, password, dmgrUsername, dmgrPassword);
//			startDeploymentManager(hostname, username, password);
	}
	
}
