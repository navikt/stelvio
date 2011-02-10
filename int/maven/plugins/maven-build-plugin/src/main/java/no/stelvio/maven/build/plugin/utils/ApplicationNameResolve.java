package no.stelvio.maven.build.plugin.utils;

/**
 * Class that maps project name with the application name
 * 
 * @author test@example.com
 */
public class ApplicationNameResolve {
	/**
	 * Gets the application name from the project name
	 * @param projectName - project name as &lt;APPLICATION&gt;_&lt;FASE&gt; e.g. PEN_HR
	 * @return e.g. PEN from PEN_HR
	 */
	public static String ApplicationFromProject(String projectName){
		if (projectName.contains("BUILD")) return "POPP"; // skal fjernes
		return projectName.substring(0,projectName.indexOf("_"));
	}
	
}
