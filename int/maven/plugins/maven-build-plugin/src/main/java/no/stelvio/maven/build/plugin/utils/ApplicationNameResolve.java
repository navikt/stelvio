package no.stelvio.maven.build.plugin.utils;

import java.util.HashMap;

/**
 * Class that maps project name with the application name
 * 
 * @author test@example.com
 */
public class ApplicationNameResolve {
	private static HashMap<String,String> names;
	
	static {
		names = new HashMap<String,String>();
		names.put("BUILD_TEST", "POPP");
		names.put("PEN_HR", "PEN");
	}
	
	/**
	 * Gets the application name from the project name
	 * @param projectName
	 * @return
	 */
	public static String ApplicationFromProject(String projectName){
		return names.get(projectName);
	}
	
}
