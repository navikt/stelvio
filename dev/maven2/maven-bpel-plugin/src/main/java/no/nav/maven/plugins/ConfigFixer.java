package no.nav.maven.plugins;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Set;

import javax.naming.AuthenticationException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;


import com.sshtools.j2ssh.ScpClient;
import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.authentication.AuthenticationProtocolState;
import com.sshtools.j2ssh.authentication.PasswordAuthenticationClient;
import com.sshtools.j2ssh.session.SessionChannelClient;
import com.sshtools.j2ssh.transport.HostKeyVerification;
import com.sshtools.j2ssh.transport.TransportProtocolException;
import com.sshtools.j2ssh.transport.publickey.SshPublicKey;

/**
 * Goal which that adds the Stelvio jaxrpc handler to webservices.xml
 * 
 * @goal fixConfig
 * 
 *  
 */
public class ConfigFixer extends AbstractMojo {
	
	/**
	 * This is the directory of the flatten ear directory.
	 * 
	 * @parameter expression="${host}"
	 * @required
	 */
	protected String host;
	
	/**
	 * 
	 * @parameter expression="${port}"
	 * @required
	 */
	private int port;
	
	/**
	 * This is the directory of the flatten ear directory.
	 * 
	 * @parameter expression="${user}"
	 * @required
	 */
	protected String user;
	
	/**
	 * This is the directory of the flatten ear directory.
	 * 
	 * @parameter expression="${password}"
	 * @required
	 */
	protected String password;
	
	/**
	 * 
	 * @parameter expression="${WPSHome}"
	 * @required
	 */
	private String WPSHome;
	
	/**
	 * 
	 * @parameter expression="${dmgrProfileName}"
	 * @required
	 */
	private String dmgrProfileName;
	
	/**
	 * 
	 * @parameter expression="${BPELModules}"
	 * @required
	 */
	private Set<String> BPELModules;
	
	/**
	 * 
	 * @parameter expression="${flattenedFolder}"
	 * @required
	 */
	private File flattenedFolder;
	
	
	//NON MAVEN PROPERTIES
	private SshClient ssh;
	private SessionChannelClient session;
	private ScpClient scp;
	
	
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("########## Uninstalling modules with BPELS running ############");
		getLog().info("###############################################################");
		
		File[] bpels = getBPELModules();
		
		
		//setting up ssh connection
		HostKeyVerification hkv = new HostKeyVerification(){
			public boolean verifyHost(String arg0, SshPublicKey arg1) throws TransportProtocolException {
				// TODO Auto-generated method stub
				return true;
			}
		};
		try {
			ssh = new SshClient();
			ssh.connect(host, port, hkv );
			
			PasswordAuthenticationClient pwdAutClient = new PasswordAuthenticationClient();
			pwdAutClient.setUsername(user);
			pwdAutClient.setPassword(password);

			if(ssh.authenticate(pwdAutClient) != AuthenticationProtocolState.COMPLETE){
				throw new AuthenticationException("Error while connecting to " + host + "!");
				
			}
			
			session = ssh.openSessionChannel();
			/**
			 * 
			 * 
			 * 
			 * HER MÅ DET LEGGES TIL  LYTTER PÅ SYSTEMOUT, OG HÅNDTERE FEIL!!
			 *
			 *
			 *
			 *
			 */
		} catch (Exception e1) {
			throw new MojoExecutionException("Error stopping BPELS!",e1);
		}
		
		
		for(File bpel : bpels){
			String appName = bpel.getName().replaceAll(".ear", "");
			getLog().info("Uninstalling BPEL module '" + appName + "'...");
			
			String cmd = WPSHome + "/profiles/" + dmgrProfileName + "bin/wsadmin.sh -user " + 
						 user + " -password " + password + " -f " + WPSHome + 
						 "/ProcessChoreographer/admin/bpcTemplates.jacl -uninstall " + appName + "App -force";
			try {
				if(session.executeCommand(cmd)){
					getLog().info(appName + " uninstalled successfully!");
				}
			} catch (IOException e) {
				throw new MojoExecutionException("Error uninstalling BPEL module!",e);
			}
		}
		
		getLog().info("############## Done uninstalling BPEL modules #################");
		getLog().info("###############################################################");
	}
	
	private File[] getBPELModules(){
		return flattenedFolder.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return 	name.startsWith("nav-") && 
						name.endsWith(".ear") &&
						BPELModules.contains(name.replaceAll(".ear", ""));
			}
		
		});
	}

	public String getDmgrProfileName() {
		return dmgrProfileName;
	}

	public void setDmgrProfileName(String dmgrProfileName) {
		this.dmgrProfileName = dmgrProfileName;
	}

	public File getFlattenedFolder() {
		return flattenedFolder;
	}

	public void setFlattenedFolder(File flattenedFolder) {
		this.flattenedFolder = flattenedFolder;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getWPSHome() {
		return WPSHome;
	}

	public void setWPSHome(String home) {
		WPSHome = home;
	}

	public void setBPELModules(Set<String> modules) {
		BPELModules = modules;
	}
	
}
