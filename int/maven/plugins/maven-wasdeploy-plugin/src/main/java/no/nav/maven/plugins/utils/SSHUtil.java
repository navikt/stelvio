package no.nav.maven.plugins.utils;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.codehaus.plexus.util.FileUtils;

import com.sshtools.j2ssh.FileTransferProgress;
import com.sshtools.j2ssh.ScpClient;
import com.sshtools.j2ssh.SftpClient;
import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.authentication.AuthenticationProtocolState;
import com.sshtools.j2ssh.authentication.PasswordAuthenticationClient;
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
		
		ScpClient client = ssh.openScpClient();
		client.put(tempDirectory.getAbsolutePath(), remoteDir, true);
		ssh.disconnect();

		System.out.println("[INFO] Deleting temp folder: " + tempDirectory.getParentFile().getAbsolutePath());
		FileUtils.deleteDirectory(tempDirectory.getParentFile());

		System.out.println("[INFO] Successfully uploaded the directory: " + localDir + " => " + hostname + ":" + remoteDir);
	}
}
