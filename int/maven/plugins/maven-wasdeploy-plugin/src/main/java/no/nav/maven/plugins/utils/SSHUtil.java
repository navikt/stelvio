package no.nav.maven.plugins.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.codehaus.plexus.util.FileUtils;

import com.sshtools.j2ssh.FileTransferProgress;
import com.sshtools.j2ssh.ScpClient;
import com.sshtools.j2ssh.SftpClient;
import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.authentication.AuthenticationProtocolState;
import com.sshtools.j2ssh.authentication.PasswordAuthenticationClient;
import com.sshtools.j2ssh.session.SessionChannelClient;
import com.sshtools.j2ssh.transport.HostKeyVerification;
import com.sshtools.j2ssh.transport.TransportProtocolException;
import com.sshtools.j2ssh.transport.publickey.SshPublicKey;

/**
 * Util class for handling SSH/SCP operations
 * 
 * @author test@example.com
 */
public class SSHUtil {

	/*
	 * Uploads a local directory to a folder on the remote host
	 */
	public static void uploadDir(String hostname, String username, String password, String localDir, String remoteDir, String application) throws IOException {

		System.out.println("[INFO] Uploading " + localDir + " to " + username + ":" + password + "@" + hostname + ":" + remoteDir + "/" + application);

		File localDirectory = new File(localDir);
		String tempString = localDirectory.getParentFile().getAbsolutePath() + "/" + "temp" + new Date().getTime() + "/" + application;
		File tempDirectory = new File(tempString);

		FileUtils.copyDirectoryStructure(localDirectory, tempDirectory);
		SshClient ssh = getSshClient(hostname, username, password);
		
		ScpClient client = ssh.openScpClient();
		client.put(tempDirectory.getAbsolutePath(), remoteDir, true);
		ssh.disconnect();

		System.out.println("[INFO] Deleting temp folder: " + tempDirectory.getParentFile().getAbsolutePath());
		FileUtils.deleteDirectory(tempDirectory.getParentFile());

		System.out.println("[INFO] Successfully uploaded the directory: " + localDir + " => " + hostname + ":" + remoteDir);
	}
	
	private static SshClient getSshClient(String hostname, String username, String password) throws IOException{
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
			throw new IOException("[ERROR] Unable to connect to host: " + hostname + " with username/pw:" + username + "/" + password + ".");
		}
		return ssh;
	}
	
	private static boolean executeCommand(String cmd, SessionChannelClient session) throws IOException {
		
		System.out.println("[INFO] Executing command " + cmd);
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

	/**
	 * Delete folder on remote computer
	 * @param hostname
	 * @param username
	 * @param password
	 * @param folder - absolute path to delete
	 * @throws IOException
	 */
	public static void deleteRemoteDir(String hostname, String username, String password, String folder) throws IOException{
		System.out.println("[INFO] Deleting " + folder + " on " + hostname + " as " + username);
		SshClient ssh = getSshClient(hostname, username, password);
		executeCommand("rm -rf "+folder, ssh.openSessionChannel());
		System.out.println("[INFO] Successfully deleted " + folder + " on " + hostname + " as " + username);
	}
}
