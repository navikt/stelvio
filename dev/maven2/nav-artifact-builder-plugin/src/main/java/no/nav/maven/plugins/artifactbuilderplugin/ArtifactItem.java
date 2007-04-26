/**
 * 
 */
package no.nav.maven.plugins.artifactbuilderplugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.codehaus.plexus.util.DirectoryScanner;

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
	 * @parameter
	 */
	private List<String> includes;
	
	/**
	 * @parameter
	 */
	private List<String> excludes;
	
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
			includes.add(ArtifactBuilderMojo.DEFAULT_INCLUDES);
		}
		
		return includes;
	}

	/**
	 * @param includes the includes to set
	 */
	public void setIncludes(List<String> includes) {
		this.includes = includes;
	}

	/**
	 * @return the excludes
	 */
	public List<String> getExcludes() {
		if (excludes == null) {
			excludes = new ArrayList<String>();
			excludes.addAll(Arrays.asList(DirectoryScanner.DEFAULTEXCLUDES));
		}
		
		return excludes;
	}

	/**
	 * @param excludes the excludes to set
	 */
	public void setExcludes(List<String> excludes) {
		this.excludes = excludes;
	}
}