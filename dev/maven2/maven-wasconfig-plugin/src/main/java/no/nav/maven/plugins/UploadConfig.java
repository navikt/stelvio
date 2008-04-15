/*
 * Created on Jan 24, 2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package no.nav.maven.plugins;

/**
 * @author bwa2812
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.logging.Log;

import com.sshtools.j2ssh.ScpClient;
import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.authentication.AuthenticationProtocolState;
import com.sshtools.j2ssh.authentication.PasswordAuthenticationClient;
import com.sshtools.j2ssh.session.SessionChannelClient;
import com.sshtools.j2ssh.transport.HostKeyVerification;
import com.sshtools.j2ssh.transport.TransportProtocolException;
import com.sshtools.j2ssh.transport.publickey.SshPublicKey;

public class UploadConfig {
	
	private static final int port = 22 ;
	private static String destPath;
	
	/**
	 * Creates an SSH connection to the provided host IP, and uploads the config files for the module provided
	 * using an SCPClient.
	 * 
	 * @param host the IP adress to the server you want to upload files to.
	 * @param usr the username for the server
	 * @param pwd the password for the server
	 * @param sourcePath the sourcepath for the files you want to upload.
	 * @param module the name of the module you want to upload files from/to.
	 * @param logger the maven.plugin.logging class, to enable logging in this class.
	 * @return boolen
	 * @throws IOException
	 */
	public static boolean uploadConfigFilesToHost(String host, String usr, String pwd, String sourcePath, String module, Log logger) throws IOException{
		
		UploadConfig.destPath = "/was_app/config/"+module;
		
		File tempSource = new File(sourcePath);
		File[] allSourceFiles = tempSource.listFiles();
		
		SshClient ssh = new SshClient();
		SessionChannelClient session = null ;
		ScpClient scp = null ;
		try {
			HostKeyVerification hkv = new HostKeyVerification(){
				public boolean verifyHost(String arg0, SshPublicKey arg1) throws TransportProtocolException {
					// TODO Auto-generated method stub
					return true;
				}
			};
			ssh.connect(host, port, hkv );
			PasswordAuthenticationClient pwdAutClient = new PasswordAuthenticationClient();
			pwdAutClient.setUsername(usr);
			pwdAutClient.setPassword(pwd);
			
			int result = ssh.authenticate(pwdAutClient);

			if(result != AuthenticationProtocolState.COMPLETE){
				throw new IOException("Login to "+host+":"+port+ " "+usr+" " +pwd +" failed");
				
			}else{
				logger.info("Connection complete!\nTransfering files");
				session = ssh.openSessionChannel();
				scp = ssh.openScpClient();
				for(int i=0;i<allSourceFiles.length;i++){
					//System.out.println("Uploading file: "+allSourceFiles[i].getAbsolutePath());
					logger.info("Uploading file: "+allSourceFiles[i].getAbsolutePath());
					scp.put(allSourceFiles[i].getAbsolutePath(), destPath, true);
				}					
				
				
				
				return true ;
				
									
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new IOException("An error occured during filetransfer: " + e.getMessage());
			
		}
	}

}
