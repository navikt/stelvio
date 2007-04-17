/**
 * 
 */
package org.apache.maven.plugin.ejb;

import java.util.ArrayList;
import java.util.List;

/**
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 */
public class ArtifactItem {
	private String DEFAULT_INCLUDES = "**";
	
	/**
	 * @parameter
	 * @required
	 */
	private String groupId;
	
	/**
	 * @parameter
	 * @required
	 */
	private String artifactId;
	
	/**
	 * @parameter
	 */
	private List<String> includes;
	
	/**
	 * @return the artifactId
	 */
	public String getArtifactId() {
		return artifactId;
	}
	
	/**
	 * @param artifactId the artifactId to set
	 */
	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}
	
	/**
	 * @return the groupId
	 */
	public String getGroupId() {
		return groupId;
	}
	
	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
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
}