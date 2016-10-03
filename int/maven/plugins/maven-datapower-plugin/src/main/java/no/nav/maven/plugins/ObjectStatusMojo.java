package no.nav.maven.plugins;

import no.nav.datapower.xmlmgmt.XMLMgmtException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * 
 * Goal which checks the status of all objects in the specified domain, 
 * and returns error if objects have OpState "down" and is enabled. It also checks if log objects is down and retries until timeout or log is up.  
 * 
 * @goal testObjects
 * 
 * @author person4fdbf4cece95, Petter Solberg, Christian Askeland, personab5e855575df
 *
 */
public class ObjectStatusMojo extends AbstractDeviceMgmtMojo{

	private final static String ADMIN_STATE_ENABLED = "enabled";
	//private final static String ADMIN_STATE_DISABLED = "disabled";
	
	private final static long WAIT_TIME_MAXIMUM = 90000; // 1,5min
	private final static long WAIT_TIME = 10000; // 10s
	
	private final static String OBJECT_LIST_SPLIT_PATTERN = "<ObjectStatus xmlns:env=\"http://www.w3.org/2003/05/soap-envelope\">";
	private final static String OBJECT_LIST_DOWN_PATTERN = "<OpState>down</OpState>";
	
	private final static String OBJECT_LOG_NAME_POSTFIX = "-log";
	private final static String OBJECT_GW_NAME_PREFIX_SINGLE_UNDERSCORE = "_gp_";
    private final static String OBJECT_GW_NAME_PREFIX_DOUBLE_UNDERSCORE = "__gp_";
	private final static String OBJECT_QMGR_NAME_PREFIX = "QMGR";
	
	protected void doExecute() throws MojoExecutionException, MojoFailureException {
		getLog().info("Executing ObjectStatusMojo");
		try {
						
			int numberOfMaximunRetries = (int) (WAIT_TIME_MAXIMUM/WAIT_TIME)+1;
			
			for (int i = 0; i < numberOfMaximunRetries; i++) {
								
				String objectList = getXMLMgmtSession().getStatus("ObjectStatus");
				String[] objects = objectList.split(OBJECT_LIST_SPLIT_PATTERN);		
				
				int objectsDown = 0;
				boolean shouldStop = false;
				boolean logDown = false;
				boolean mqDown = false;
				String waitError = "";
				
				for (String object : objects){
					if (object.contains(OBJECT_LIST_DOWN_PATTERN)){
						objectsDown++;
						
						if(!logDown && getName(object).endsWith(OBJECT_LOG_NAME_POSTFIX)){
							logDown = true;
							waitError += "At least one log object is down, indicating that NFS is down. ";
						}
						if(!mqDown && getName(object).startsWith(OBJECT_QMGR_NAME_PREFIX)){
							mqDown = true;
							waitError += "At least one MQ Queue Manager object is down, indicating that the connection with the MQ is not up. ";
						}
						String adminState = getAdminState(object);
						// If object is down and is enabled, set stop flag
						if(adminState.toLowerCase().contains(ADMIN_STATE_ENABLED)){
							if(!getName(object).startsWith(OBJECT_GW_NAME_PREFIX_SINGLE_UNDERSCORE) && !getName(object).startsWith(OBJECT_GW_NAME_PREFIX_DOUBLE_UNDERSCORE)){
							    getLog().warn("\tObject is DOWN: " + getName(object) + ", object admin state is " + adminState);
							    shouldStop = true;
							} else {
							    getLog().info("\t\tObject is DOWN: " + getName(object) + ", object admin state is " + adminState);
							}
						}
						else{
							getLog().info("\t\tObject is DOWN: " + getName(object) + ", object admin state is " + adminState);
						}
					}
				}
				if(!shouldStop){
					getLog().info("All enabled objects is up");
					break;
				}
				// SISTE GANG
				if((numberOfMaximunRetries-1)==i){
					throwObjectDownError();
				}
				
				getLog().info("Total number of objects down: " + objectsDown);
				if(logDown || mqDown){
					getLog().info(waitError);
					getLog().info("Waiting for " + WAIT_TIME/1000 + " seconds. Retry " + (i + 1) + " of " + (numberOfMaximunRetries - 1));
					Thread.sleep(WAIT_TIME);
				}
				else{
					throwObjectDownError();
				}				
			}
			
		} catch (XMLMgmtException e) {
			throw new MojoExecutionException("Failed to retrieve object status",e);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	private void throwObjectDownError() throws MojoExecutionException{
		getLog().error("========= An object is down even if it's enabled! =========");
		getLog().error("NOTE! The configuration has been deployed to the DataPower instance.");
		getLog().error("Please revert deploy or fix error and redeploy.");
		getLog().error("=========== =========== =========== =========== ===========");
		throw new MojoExecutionException("An object is down even it is enabled.");
	}
}
