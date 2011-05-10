package no.nav.maven.plugin.bounce.plugin.mojo;

import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import no.nav.devarch.utils.Application;
import no.nav.maven.plugin.bounce.plugin.utils.AppServerBouncer;
import no.nav.maven.plugin.bounce.plugin.utils.DmgrNaBouncer;
import no.nav.maven.plugin.bounce.plugin.utils.IFixInstaller;
import no.nav.maven.plugin.bounce.plugin.utils.XMLParser;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.xml.sax.SAXException;

/**
 * Goal that decides what should be restarted and in which order
 * 
 * @goal bounce
 * 
 * @author test@example.com
 *
 */
public class MainBounceMojo extends AbstractMojo {
	
	private boolean bounce_servers;
	
	/**
	 * @parameter expression="${action}" default-value="restart"
	 */
	protected String action;
	/**
	 * @parameter expression="${bounce_dmgr_na}" default-value=false
	 */
	private boolean bounce_dmgr_na;
	
	/**
	 * @parameter expression="${install_ifix}" 
	 */
	private boolean install_ifixes;
	
	
	private boolean wasSs;
	
	private boolean wasIs;

	private boolean wps;
	
	private boolean onlyAppTarget;
	
	/**
	 * @parameter expression="${ss_pensjons_cluster}"
	 */
	private boolean ss_pensjonsCluster;
	/**
	 * @parameter expression="${is_pensjons_cluster}"
	 */
	private boolean is_pensjonsCluster;
	/**
	 * @parameter expression="${apptarget}"
	 */
	private boolean wps_AppTarget;
	/**
	 * @parameter expression="${msg_sup}"
	 */
	private boolean wps_MessagingAndSupport;
	
	// ==== parameters to bounce app servers ====
	/**
	 * @parameter expression="${env}"
	 * @required
	 */
	protected String env;
	/**
	 * @parameter expression="${restartConfigFile}"
	 * @required
	 */
	private String restartCfgFile;
	/**
	 * @parameter expression="${project.build.directory}"
	 */
	private String buildDir;
	/**
	 * @parameter expression="${includeJoark}" default-value=false
	 */
	private boolean includeJoark;
	/**
	 * @parameter expression="${apps}"
	 */
	private String apps;
	/**
	 * @parameter expression="${excludeBus}"
	 */
	private boolean excludeBus;
	/**
	 * @parameter expression="${wid.runtime}"
	 * @required
	 */
	private String widRuntime;
	
	/**
	 * @parameter expression="${environmentFilesDir}"
	 * @required
	 */
	private String envFilesDir;
	// ==== parameters to bounce app servers ====
	
	// ==== parameters to bounce dmgr and node agents ====
	// these parameters should be the same for both dmgr and node agents.
	/**
	 * @parameter expression="${environment/servers/was/node1/linux-app-username}"
	 */
	private String linux_user;
	/**
	 * @parameter expression="${environment/servers/was/node1/linux-app-password}"
	 */
	private String was_linux_password;
	/**
	 * @parameter expression="${wps-linux-password}"
	 */
	private String wps_linux_password;
	/**
	 * @parameter expression="${environment/servers/was/dmgr/ws-username}"
	 */
	private String ws_admin_user;
	/**
	 * @parameter expression="${environment/servers/was/dmgr/ws-password}"
	 */
	private String ws_admin_password;
	/**
	 * @parameter expression="${environment/servers/was/intern/dmgr/ws-username}"
	 */
	private String ws_is_admin_user;
	/**
	 * @parameter expression="${environment/servers/was/intern/dmgr/ws-password}"
	 */
	private String ws_is_admin_password;
	// the rest is just different hostnames
	/**
	 * @parameter expression="${environment/servers/was/dmgr/hostname}"
	 */
	private String was_dmgr_hostname;
	/**
	 * @parameter expression="${environment/servers/was/node1/hostname}"
	 */
	private String was_node1_hostname;
	/**
	 * @parameter expression="${environment/servers/was/node2/hostname}"
	 */
	private String was_node2_hostname;
	/**
	 * @parameter expression="${environment/servers/was/node3/hostname}"
	 */
	private String was_node3_hostname;
	/**
	 * @parameter expression="${environment/servers/was/intern/dmgr/hostname}"
	 */
	private String was_is_dmgr_hostname;
	/**
	 * @parameter expression="${environment/servers/was/intern/node1/hostname}"
	 */
	private String was_is_node1_hostname;
	/**
	 * @parameter expression="${environment/servers/was/intern/node2/hostname}"
	 */
	private String was_is_node2_hostname;
	/**
	 * @parameter expression="${environment/servers/wps/dmgr/hostname}"
	 */
	private String wps_dmgr_hostname;
	/**
	 * @parameter expression="${environment/servers/wps/node1/hostname}"
	 */
	private String wps_node1_hostname;
	/**
	 * @parameter expression="${environment/servers/wps/node2/hostname}"
	 */
	private String wps_node2_hostname;
	// ==== parameters to bounce dmgr and node agents ====

	
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException{
		
		try {
			this.ResolveRestart();
		} catch (SAXException e) {
			getLog().error(e.getMessage());
		} catch (IOException e) {
			getLog().error(e.getMessage());
		} catch (ParserConfigurationException e) {
			getLog().error(e.getMessage());
		} 
		getLog().info("bouce_servers: " + this.bounce_servers);
		getLog().info("bouce_dmgr: " + this.bounce_dmgr_na);
		getLog().info("ifix: " + this.install_ifixes);
		printSummary();
		if (!this.install_ifixes){
			getLog().info("IFix installation is not requested. Performing " + action);
			if (action.equalsIgnoreCase("stop") || action.equalsIgnoreCase("restart")){
				if (bounce_servers) this.bounceAppServers("stop");
				if (bounce_dmgr_na) this.bounceDmgrAndNA("stop");
			}
			if (action.equalsIgnoreCase("start") || action.equalsIgnoreCase("restart")){
				if (bounce_dmgr_na) this.bounceDmgrAndNA("start");
				if (bounce_servers) this.bounceAppServers("start");
			}
		} else {
			IFixInstaller ifixInstaller = this.getIFixInstaller();
			getLog().info("Checking if there is anything to install...");
			if (ifixInstaller.isInstallNeeded()){
				getLog().info("Detected .pak files. Ensuring everything is stopped...");
				this.bounceAppServers("stop");
				this.bounceDmgrAndNA("stop");
				getLog().info("Running installation script...");
				ifixInstaller.installIFixes();
				if (action.equalsIgnoreCase("start") || action.equalsIgnoreCase("restart")){
					getLog().info("Starting up after installation...");
					if (bounce_dmgr_na) this.bounceDmgrAndNA("start");
					if (bounce_servers) this.bounceAppServers("start");
				}
			} else {
				getLog().info("IFix installation is not needed. Performing " + action);
				if (action.equalsIgnoreCase("stop") || action.equalsIgnoreCase("restart")){
					if (bounce_servers) this.bounceAppServers("stop");
					if (bounce_dmgr_na) this.bounceDmgrAndNA("stop");
				}
				if (action.equalsIgnoreCase("start") || action.equalsIgnoreCase("restart")){
					if (bounce_dmgr_na) this.bounceDmgrAndNA("start");
					if (bounce_servers) this.bounceAppServers("start");
				}
			}
		}
	}
	
	private void printSummary(){
		getLog().info("***********************************************************************************");
		getLog().info("*");
		getLog().info("* Summary:");
		getLog().info("*");
		getLog().info("* " + action.toString().toUpperCase() + " is requested on the following servers in " + env);
		if (bounce_servers){
			if (wasSs) {
				if (this.includeJoark)
					getLog().info("* - WAS SS (Pensjon and JOARK cluster)");
				else
					getLog().info("* - WAS SS (Only Pensjons cluster)");
	
			}
			if (wasIs)
				getLog().info("* - WAS IS");
			if (wps) {
				if (onlyAppTarget)
					getLog().info("* - WPS (Only AppTarget cluster)");
				else
					getLog().info("* - WPS (All clusters)");
			}
		}
		getLog().info("*");
		if (bounce_dmgr_na){
			if (wasSs)
				getLog().info("* - Deployment Manager and Node Agents for WAS SS");
			if (wasIs)
				getLog().info("* - Deployment Manager and Node Agents for WAS IS");
			if (wps)
				getLog().info("* - Deployment Manager and Node Agents for WPS");
		}
		if (install_ifixes)
			getLog().info("* - Installation of Ifixes");
		getLog().info("*");
		getLog().info("***********************************************************************************");
	}
	
	private IFixInstaller getIFixInstaller(){
		return new IFixInstaller(
				linux_user,
				was_linux_password,
				wps_linux_password,
				was_dmgr_hostname,
				was_node1_hostname,
				was_node2_hostname,
				was_node3_hostname,
				was_is_dmgr_hostname,
				was_is_node1_hostname,
				was_is_node2_hostname,
				wps_dmgr_hostname,
				wps_node1_hostname,
				wps_node2_hostname,
				getServerType() // was/wps/all/none
		);
	}
	
	/**
	 * This method will parse the apps string together with the restart_config.xml and decide which modules should be restarted. The appropriate variables are set to true.
	 * 
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 */
	private void ResolveRestart() throws SAXException, IOException, ParserConfigurationException {
		if (this.apps != null){ // try to find out what should be restarted
			// apps-string:
			String[] apps_names = apps.split(",");
			getLog().info("[INFO] Starting resolve for " + apps);
			getLog().info("[INFO] Parsing application configuration file ...");
			// restart_config.xml
			HashMap<String, Application> restart_config = XMLParser.parseRestartConfigFile(this.restartCfgFile);
			// set variables
			for (String name : apps_names) {
				wasSs |= restart_config.get(name).isWasSSRestartRequired();
				wasIs |= restart_config.get(name).isWasISRestartRequired();
				wps |= restart_config.get(name).isWpsRestartRequired();
				if (name.equalsIgnoreCase("joark"))
					this.includeJoark = true;
			}
			if (this.excludeBus)
				wps = false;
			this.onlyAppTarget = true;
			this.bounce_servers = true;
		} else {
			wasSs = ss_pensjonsCluster;
			wasIs = is_pensjonsCluster;
			wps = wps_AppTarget & !excludeBus;
			onlyAppTarget = !wps_MessagingAndSupport;
			this.bounce_servers = true;
		}
	}
	
	private AppServerBouncer getAppServerBouncer(String action){
		return new AppServerBouncer(
				this, 
				this.widRuntime, 
				this.env, 
				this.envFilesDir, 
				action, 
				this.buildDir, 
				this.wasSs, 
				this.wasIs, 
				this.wps, 
				this.onlyAppTarget, 
				this.includeJoark 
		);
	}
	
	private String getServerType(){
		if ((this.wasSs || this.wasIs) && this.wps) return "all";
		else if (this.wasSs || this.wasIs) return "was";
		else if (this.wps) return "wps";
		return "none";
	}
	
	private DmgrNaBouncer getDmgrNaBouncer(String action){
		
		return new DmgrNaBouncer(
				linux_user,
				was_linux_password,
				wps_linux_password,
				ws_admin_user,
				ws_admin_password,
				ws_is_admin_user,
				ws_is_admin_password,
				was_dmgr_hostname,
				was_node1_hostname,
				was_node2_hostname,
				was_node3_hostname,
				was_is_dmgr_hostname,
				was_is_node1_hostname,
				was_is_node2_hostname,
				wps_dmgr_hostname,
				wps_node1_hostname,
				wps_node2_hostname,
				getServerType(), // was/wps/all
				action, 
				this
		);
	}
	
	private void bounceAppServers(String action) throws MojoExecutionException, MojoFailureException{
		this.getAppServerBouncer(action).performOperation();
	}
	
	private void bounceDmgrAndNA(String action) throws MojoExecutionException, MojoFailureException{
		this.getDmgrNaBouncer(action).performOperation();
	}
}
