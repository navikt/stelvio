package no.nav.maven.plugin.bounce.plugin.utils;

import java.io.IOException;
import java.util.HashMap;

import no.nav.maven.plugin.bounce.plugin.mojo.MainBounceMojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;

public class DmgrNaBouncer {
	// these parameters should be the same for both dmgr and node agents.
	private String linux_user;
	private String was_linux_password;
	private String wps_linux_password;
	private String ws_admin_user;
	private String ws_admin_password;
	private String ws_is_admin_user;
	private String ws_is_admin_password;
	// the rest is just different hostnames

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
	
	// Which servers shall be restarted. Can be "was", "wps" or "all".
	private String server_type;
	// Can be start/stop/restart
	private String action;
	private MainBounceMojo mainBounceMojo;
	
	/**
	 * Constructor
	 * @param linux_user
	 * @param was_linux_password
	 * @param wps_linux_password
	 * @param ws_admin_user
	 * @param ws_admin_password
	 * @param ws_is_admin_user
	 * @param ws_is_admin_password
	 * @param was_dmgr_hostname
	 * @param was_node1_hostname
	 * @param was_node2_hostname
	 * @param was_node3_hostname
	 * @param was_is_dmgr_hostname
	 * @param was_is_node1_hostname
	 * @param was_is_node2_hostname
	 * @param wps_dmgr_hostname
	 * @param wps_node1_hostname
	 * @param wps_node2_hostname
	 * @param server_type
	 * @param action
	 * @param mainBounceMojo
	 */
	public DmgrNaBouncer(
			String linux_user,
			String was_linux_password,
			String wps_linux_password,
			String ws_admin_user,
			String ws_admin_password,
			String ws_is_admin_user,
			String ws_is_admin_password,
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
			String server_type,
			String action,
			MainBounceMojo mainBounceMojo
	){
		this.linux_user = linux_user;
		this.was_linux_password = was_linux_password;
		this.wps_linux_password = wps_linux_password;
		this.ws_admin_user = ws_admin_user;
		this.ws_admin_password = ws_admin_password;
		this.ws_is_admin_user = ws_is_admin_user;
		this.ws_is_admin_password = ws_is_admin_password;
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
		this.action = action;
		this.mainBounceMojo = mainBounceMojo;
	}
	
	public void performOperation() throws MojoExecutionException, MojoFailureException {
		try {
			if (action.equalsIgnoreCase("restart"))
				this.performRestart(this.server_type);
			else if (action.equalsIgnoreCase("start"))
				this.performStart(this.server_type);
			else if (action.equalsIgnoreCase("stop"))
				this.performStop(this.server_type);
		} catch (IOException e) {
				getLog().error("IO Exception while performing " + action);
		}
	}
	

	private void performStart(String server) throws IOException{
		if (server.equalsIgnoreCase("none")) {
			getLog().info("[INFO] Start operation was not requested for any server.");
			return;
		}
		getLog().info("");
		getLog().info("#########################################");
		getLog().info("### STARTING DMGR AND NODE AGENTS ... ###");
		getLog().info("#########################################");
		getLog().info("");
		if (server.equalsIgnoreCase("was") || server.equalsIgnoreCase("all")){
			// sensitiv sone
			start("SS_AppServer","dmgr", this.was_dmgr_hostname);
			start("SS_AppServer","node", this.was_node1_hostname);
			start("SS_AppServer","node", this.was_node2_hostname);
			start("SS_AppServer","node", this.was_node3_hostname);
		
			// intern sone
			start("IS_AppServer","dmgr", this.was_is_dmgr_hostname);
			start("IS_AppServer","node", this.was_is_node1_hostname);
			start("IS_AppServer","node", this.was_is_node2_hostname);
		}
			
		if (server.equalsIgnoreCase("wps") || server.equalsIgnoreCase("all")){
			start("SS_ProcServer","dmgr", this.wps_dmgr_hostname);
			start("SS_ProcServer","node", this.wps_node1_hostname);
			start("SS_ProcServer","node", this.wps_node2_hostname);
		}
	}
	
	private void performStop(String server) throws IOException{
		if (server.equalsIgnoreCase("none")) {
			getLog().info("[INFO] Stop operation was not requested for any server.");
			return;
		}
		getLog().info("");
		getLog().info("#########################################");
		getLog().info("### STOPPING DMGR AND NODE AGENTS ... ###");
		getLog().info("#########################################");
		getLog().info("");
		if (server.equalsIgnoreCase("was") || server.equalsIgnoreCase("all")){
			// sensitiv sone
			stop("SS_AppServer","node", this.was_node1_hostname);
			stop("SS_AppServer","node", this.was_node2_hostname);
			stop("SS_AppServer","node", this.was_node3_hostname);
			stop("SS_AppServer","dmgr", this.was_dmgr_hostname);
		
			// intern sone
			stop("IS_AppServer","node", this.was_is_node1_hostname);
			stop("IS_AppServer","node", this.was_is_node2_hostname);
			stop("IS_AppServer","dmgr", this.was_is_dmgr_hostname);
		}
			
		if (server.equalsIgnoreCase("wps") || server.equalsIgnoreCase("all")){
			stop("SS_ProcServer","node", this.wps_node1_hostname);
			stop("SS_ProcServer","node", this.wps_node2_hostname);
			stop("SS_ProcServer","dmgr", this.wps_dmgr_hostname);
		}
	}
	
	private void performRestart(String server) throws IOException {
		this.performStop(server);
		this.performStart(server);
	}

	private void stop(String server_type,String profile, String hostname) throws IOException{
		if (hostname == null) return;
		String linux_password = (server_type.endsWith("AppServer")) ? this.was_linux_password : this.wps_linux_password;
		String wsAdmin_password = (server_type.startsWith("SS")) ? this.ws_admin_password : this.ws_is_admin_password;
		String wsAdmin_user = (server_type.startsWith("SS")) ? this.ws_admin_user : this.ws_is_admin_user;
		//get all profiles that are found in profiles/ folder
		HashMap<String, String> profiles = SshUtil.getProfileNames(hostname, this.linux_user, linux_password, server_type.substring(3)); 
		if (profile.equalsIgnoreCase("dmgr")){
			String profileName = profiles.get(profile);
			getLog().info("###############################################");
			getLog().info("Stopping " + profileName + " on " + hostname + "...");
			getLog().info("###############################################");
			getLog().info(profile.toUpperCase() +" on "+ hostname + " is stopped:  [ " + 
					SshUtil.stopWebSphereProcess(server_type.substring(3), profileName, hostname, this.linux_user, linux_password, wsAdmin_user, wsAdmin_password)
			+ " ]");
		}else{
			// potentially several nodes on one host
			String profileName = "";
			for (String prof_name : profiles.keySet())
				if (prof_name.contains("node")){
					profileName = profiles.get(prof_name);
					getLog().info("###############################################");
					getLog().info("Stopping " + profileName + " on " + hostname + "...");
					getLog().info("###############################################");
					getLog().info(profile.toUpperCase() +" on "+ hostname + " is stopped:  [ " + 
							SshUtil.stopWebSphereProcess(server_type.substring(3), profileName, hostname, this.linux_user, linux_password, wsAdmin_user, wsAdmin_password)
					+ " ]");
				}
		}
	}
	
	private void start(String server_type, String profile, String hostname) throws IOException{
		if (hostname == null) return;
		String linux_password = (server_type.endsWith("AppServer")) ? this.was_linux_password : this.wps_linux_password;
		//get all profiles that are found in profiles/ folder
		HashMap<String, String> profiles = SshUtil.getProfileNames(hostname, this.linux_user, linux_password, server_type.substring(3)); 
		if (profile.equalsIgnoreCase("dmgr")){
			String profileName = profiles.get(profile);
			getLog().info("###############################################");
			getLog().info("Starting " + profileName + " on " + hostname + "...");
			getLog().info("###############################################");
			getLog().info(profile.toUpperCase() + " on "+ hostname + " is started:  [ " + 
					SshUtil.startWebSphereProcess(server_type.substring(3), profileName, hostname, this.linux_user, linux_password)
			+ " ]");
		}else{
			// potentially several nodes on one host
			String profileName = "";
			for (String prof_name : profiles.keySet())
				if (prof_name.contains("node")){
					profileName = profiles.get(prof_name);
					getLog().info("###############################################");
					getLog().info("Starting " + profileName + " on " + hostname + "...");
					getLog().info("###############################################");
					getLog().info(profile.toUpperCase() + " on "+ hostname + " is started:  [ " + 
							SshUtil.startWebSphereProcess(server_type.substring(3), profileName, hostname, this.linux_user, linux_password)
					+ " ]");
				}
		}
	}
	
	private Log getLog(){
		return mainBounceMojo.getLog();
	}
}