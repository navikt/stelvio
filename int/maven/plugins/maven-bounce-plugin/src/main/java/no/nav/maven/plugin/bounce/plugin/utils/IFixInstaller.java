package no.nav.maven.plugin.bounce.plugin.utils;

import java.io.IOException;

/**
 * This class shall check if installation is needed (.pak files are found in pending_fixpacks folder),
 * then stop dmgr and na if necessary, call installation script and start dmgr and na again.
 * 
 * @author test@example.com
 */
public class IFixInstaller {
	
	private String linux_user;
	private String was_linux_password;
	private String wps_linux_password;
	private String was_dmgr_hostname;
	private String was_node1_hostname;
	private String was_node2_hostname;
	private String was_node3_hostname;
	private String was_is_dmgr_hostname;
	private String was_is_node1_hostname;
	private String was_is_node2_hostname;
	private String wps_dmgr_hostname;
	private String wps_node1_hostname;
	private String wps_node2_hostname;
	private String server_type; // was/wps/all
	
	public IFixInstaller(
			String linux_user,
			String was_linux_password,
			String wps_linux_password,
			String was_dmgr_hostname,
			String was_node1_hostname,
			String was_node2_hostname,
			String was_node3_hostname,
			String was_is_dmgr_hostname,
			String was_is_node1_hostname,
			String was_is_node2_hostname,
			String wps_dmgr_hostname,
			String wps_node1_hostname,
			String wps_node2_hostname,
			String server_type		
	){
		this.linux_user = linux_user;
		this.was_linux_password = was_linux_password;
		this.wps_linux_password = wps_linux_password;
		this.was_dmgr_hostname = was_dmgr_hostname;
		this.was_node1_hostname = was_node1_hostname;
		this.was_node2_hostname = was_node2_hostname;
		this.was_node3_hostname = was_node3_hostname;
		this.was_is_dmgr_hostname = was_is_dmgr_hostname;
		this.was_is_node1_hostname = was_is_node1_hostname;
		this.was_is_node2_hostname = was_is_node2_hostname;
		this.wps_dmgr_hostname = wps_dmgr_hostname;
		this.wps_node1_hostname = wps_node1_hostname;
		this.wps_node2_hostname = wps_node2_hostname;
		this.server_type = server_type;
	}

	/**
	 * Check if installation is needed (.pak files are found in pending_fixpacks folder)
	 * @return true
	 * @throws IOException 
	 */
	private boolean isInstallNeeded(String hostname,String  username,String  password) throws IOException{
		return (hostname != null) ? SshUtil.isIFixInstallationNeeded(hostname, username, password) : false;
	}
	
	public boolean isInstallNeeded(){
		boolean result = false;
		try{
		if (server_type.equalsIgnoreCase("was") || server_type.equalsIgnoreCase("all")){
			// was ss
			result |= isInstallNeeded(was_dmgr_hostname, linux_user, was_linux_password);
			result |= isInstallNeeded(was_node1_hostname, linux_user, was_linux_password);
			result |= isInstallNeeded(was_node2_hostname, linux_user, was_linux_password);
			result |= isInstallNeeded(was_node3_hostname, linux_user, was_linux_password);
			//was is
			result |= isInstallNeeded(was_is_dmgr_hostname, linux_user, was_linux_password);
			result |= isInstallNeeded(was_is_node1_hostname, linux_user, was_linux_password);
			result |= isInstallNeeded(was_is_node2_hostname, linux_user, was_linux_password);
		}
		if (server_type.equalsIgnoreCase("wps") || server_type.equalsIgnoreCase("all")){
			//wps
			result |= isInstallNeeded(wps_dmgr_hostname, linux_user, wps_linux_password);
			result |= isInstallNeeded(wps_node1_hostname, linux_user, wps_linux_password);
			result |= isInstallNeeded(wps_node2_hostname, linux_user, wps_linux_password);
		}
		}catch(IOException ioe){
			System.out.println("IO exception during check if ifix installation is needed. Probably ssh connection could not be opened.");
			return result;
		}
		return result;
		
	}
	
	private void installIFixes(String hostname, String username, String password) throws IOException{
		if (hostname != null) SshUtil.installIFixes(hostname, username, password);
	}
	
	/**
	 * Run script to install all ifixes
	 */
	public void installIFixes(){
		try{
			if (server_type.equalsIgnoreCase("was") || server_type.equalsIgnoreCase("all")){
				// was ss
				installIFixes(was_dmgr_hostname, linux_user, was_linux_password);
				installIFixes(was_node1_hostname, linux_user, was_linux_password);
				installIFixes(was_node2_hostname, linux_user, was_linux_password);
				installIFixes(was_node3_hostname, linux_user, was_linux_password);
				//was is
				installIFixes(was_is_dmgr_hostname, linux_user, was_linux_password);
				installIFixes(was_is_node1_hostname, linux_user, was_linux_password);
				installIFixes(was_is_node2_hostname, linux_user, was_linux_password);
			}
			if (server_type.equalsIgnoreCase("wps") || server_type.equalsIgnoreCase("all")){
				//wps
				installIFixes(wps_dmgr_hostname, linux_user, wps_linux_password);
				installIFixes(wps_node1_hostname, linux_user, wps_linux_password);
				installIFixes(wps_node2_hostname, linux_user, wps_linux_password);
			}
			}catch(IOException ioe){
				System.out.println("IO exception during ifix installation. Probably ssh connection could not be opened.");
			}
	}
}
