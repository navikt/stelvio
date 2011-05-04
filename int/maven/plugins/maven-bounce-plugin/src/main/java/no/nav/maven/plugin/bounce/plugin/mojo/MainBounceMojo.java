package no.nav.maven.plugin.bounce.plugin.mojo;

import no.nav.maven.plugin.bounce.plugin.utils.AppServerBouncer;
import no.nav.maven.plugin.bounce.plugin.utils.DmgrNaBouncer;
import no.nav.maven.plugin.bounce.plugin.utils.IFixInstaller;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Goal that decides what should be restarted and in which order
 * 
 * @goal bounce
 * 
 * @author test@example.com
 *
 */
public class MainBounceMojo extends AbstractMojo {
	
	/**
	 * @parameter expression="${action}" default-value="restart"
	 */
	protected String action;
	/**
	 * @parameter expression="${bounce_app_servers}" default-value=true
	 */
	private boolean bounce_servers;
	/**
	 * @parameter expression="${bounce_dmgr_na}" default-value=false
	 */
	private boolean bounce_dmgr_na;
	
	/**
	 * @parameter expression="${install_ifix}" 
	 */
	private boolean install_ifixes;
	
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
	 * @parameter expression="${wasSs}"
	 */
	private boolean wasSs;
	/**
	 * @parameter expression="${wasIs}"
	 */
	private boolean wasIs;
	/**
	 * @parameter expression="${wps}"
	 */
	private boolean wps;
	/**
	 * @parameter expression="${onlyAppTarget}"
	 */
	private boolean onlyAppTarget;
	/**
	 * @parameter expression="${wid.runtime}"
	 * @required
	 */
	private String widRuntime;
	/**
	 * @parameter expression="${excludeBus}"
	 */
	private boolean excludeBus;
	/**
	 * @parameter expression="${environmentFilesDir}"
	 * @required
	 */
	private String envFilesDir;
	// ==== parameters to bounce app servers ====
	
	// ==== parameters to boucne dmgr and node agents ====
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
	// ==== parameters to boucne dmgr and node agents ====
	

//	@Override
//	public void execute() throws MojoExecutionException, MojoFailureException {
//		boolean dmgr_stopped = false;
//		IFixInstaller ifixInstaller = this.getIFixInstaller();
//		getLog().info("server type: " + this.getServerType());
//		if (action.equalsIgnoreCase("stop") || action.equalsIgnoreCase("restart")){
//			if (bounce_servers){ 
//				this.bounceAppServers("stop");
//				if (ifixInstaller.isInstallNeeded()){
//					getLog().info("IFix installation is going to be performed");
//					this.bounceDmgrAndNA("stop");
//					dmgr_stopped = true;
//					ifixInstaller.installIFixes();
//					this.bounce_dmgr_na = true;
//				}else{
//					getLog().info("IFix installation is not needed");
//				}
//			}
//			if (bounce_dmgr_na && !dmgr_stopped)
//				this.bounceDmgrAndNA("stop");
//			else getLog().info("Skipping stop of dmgr and na");
//		}
//		if (action.equalsIgnoreCase("start") || action.equalsIgnoreCase("restart")){
//			dmgr_stopped = true;
//			if (ifixInstaller.isInstallNeeded()){
//				getLog().info("IFix installation is going to be performed");
//				this.bounceDmgrAndNA("stop");
//				ifixInstaller.installIFixes();
//				this.bounceDmgrAndNA("start");
//				dmgr_stopped = false;
//				this.bounce_dmgr_na = false;
//			}else{
//				getLog().info("IFix installation is not needed");
//			}
//			if (bounce_dmgr_na && dmgr_stopped)
//				this.bounceDmgrAndNA("start");
//			else getLog().info("Skipping start of dmgr and na");
//			if (bounce_servers)
//				this.bounceAppServers("start");
//			else getLog().info("Skipping start of appservers");
//		}
//	}
	
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException{
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
				getServerType() // was/wps/all
		);
	}
	
	private AppServerBouncer getAppServerBouncer(String action){
		if (this.apps != null) action = null; // let AppServerBouncer find out what should be restarted
		return new AppServerBouncer(
				this, 
				this.widRuntime, 
				this.env, 
				this.envFilesDir, 
				this.restartCfgFile, 
				action, 
				this.buildDir, 
				this.apps, 
				this.wasSs, 
				this.wasIs, 
				this.wps, 
				this.onlyAppTarget, 
				this.includeJoark, 
				this.excludeBus
		);
	}
	
	private String getServerType(){
		if ((this.wasSs || this.wasIs) && this.wps) return "all";
		else if (this.wasSs || this.wasIs) return "was";
		else if (this.wps) return "wps";
		return "all";
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
