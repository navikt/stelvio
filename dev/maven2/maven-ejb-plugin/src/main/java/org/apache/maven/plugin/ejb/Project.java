/**
 * 
 */
package org.apache.maven.plugin.ejb;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 */
public class Project {
	private String DEFAULT_INCLUDES = "**";
	
	/**
	 * @parameter
	 */
	private String projectName;
	
	/**
	 * @parameter
	 * @required
	 */
	private File basedir;
	
	/**
	 * @parameter
	 */
	private List<String> includes;

	/**
	 * @return the basedir
	 */
	public File getBasedir() {
		return basedir;
	}

	/**
	 * @param basedir the basedir to set
	 */
	public void setBasedir(File basedir) {
		this.basedir = basedir;
	}

	/**
	 * @return the includes
	 */
	public List<String> getIncludes() {
		if (includes == null) {
			includes = new ArrayList<String>();
			includes.add(DEFAULT_INCLUDES);
		}
		
		return includes;
	}

	/**
	 * @param includes the includes to set
	 */
	public void setIncludes(List<String> includes) {
		this.includes = includes;
	}
	
	@Override
	public String toString() {
		return basedir+":"+includes;
	}

	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName == null ? basedir.toString() : projectName;
	}

	/**
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
}