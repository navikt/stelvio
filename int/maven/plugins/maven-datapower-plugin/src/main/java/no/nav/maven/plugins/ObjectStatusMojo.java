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

	protected void doExecute() throws MojoExecutionException, MojoFailureException {
		getLog().info("Executing ObjectStatusMojo");
		try {
			String objectList = getXMLMgmtSession().getStatus("ObjectStatus");
			
			String[] objects = objectList.split("<ObjectStatus xmlns:env=\"http://www.w3.org/2003/05/soap-envelope\">");		
			String pattern = "<OpState>down</OpState>";
			int objectsDown = 0;
			
			for (String object : objects){
				if (object.contains(pattern)){
					getLog().warn("Object is DOWN: " + getName(object) + ", object admin state is " + getAdminState(object));
					objectsDown++;
				}
			}
			getLog().info("Total number of objects down: " + objectsDown);
			
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
