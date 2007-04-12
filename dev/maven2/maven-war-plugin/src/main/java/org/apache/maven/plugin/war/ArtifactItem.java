/**
 * 
 */
package org.apache.maven.plugin.war;

/**
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 */
public class ArtifactItem {
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
}