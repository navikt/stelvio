package no.nav.maven.plugins;

import no.nav.datapower.xmlmgmt.XMLMgmtException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * 
 * Goal which checks the status of all objects in the specified domain, 
 * and returns warnings if objects have OpState "down".  
 * 
 * @goal testObjects
 * 
 * @author person4fdbf4cece95, Accenture
 *
 */
public class ObjectStatusMojo extends AbstractDeviceMgmtMojo{

	private final static String ADMIN_STATE_ENABLED = "enabled";
	private final static String ADMIN_STATE_DISABLED = "disabled";
	
	protected void doExecute() throws MojoExecutionException, MojoFailureException {
		getLog().info("Executing ObjectStatusMojo");
		try {
			String objectList = getXMLMgmtSession().getStatus("ObjectStatus");
			
			String[] objects = objectList.split("<ObjectStatus xmlns:env=\"http://www.w3.org/2003/05/soap-envelope\">");		
			String pattern = "<OpState>down</OpState>";
			int objectsDown = 0;
			
			boolean shouldStop = false;
			
			for (String object : objects){
				if (object.contains(pattern)){
					
					String adminState = getAdminState(object);
					
					getLog().warn("Object is DOWN: " + getName(object) + ", object admin state is " + adminState);
					objectsDown++;
					
					// If object is down and is enabled, set stop flag
					if(adminState.toLowerCase().contains(ADMIN_STATE_ENABLED)){
						shouldStop = true;
					}
				}
			}
			getLog().info("Total number of objects down: " + objectsDown + "\n");
			
			if(shouldStop){
				getLog().error("========= An object is down even if it's enabled! =========");
				getLog().error("NOTE! The configuration has been deployed to the DataPower instance.");
				getLog().error("Please revert deploy or fix error and redeploy.");
				getLog().error("=========== =========== =========== =========== ===========\n\n");
				
				throw new MojoExecutionException("An object is down even it is enabled.");
			}
			
		} catch (XMLMgmtException e) {
			throw new MojoExecutionException("Failed to retrieve object status",e);
		}
	}
	
	private String getName(String object){
		if (!object.contains("<Name>")){
			return "Unknown object";
		}
		String name = object.split("<Name>")[1].split("</Name>")[0];
		
		return name;
	}
	
	private String getAdminState(String object){
		if (!object.contains("<AdminState>")){
			return "Unknown state";
		}
		String state = object.split("<AdminState>")[1].split("</AdminState>")[0];
		
		return state;
	}
}
