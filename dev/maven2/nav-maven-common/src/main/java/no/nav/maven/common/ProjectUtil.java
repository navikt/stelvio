/**
 * 
 */
package no.nav.maven.common;

import java.util.Iterator;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.apache.maven.project.MavenProject;

/**
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 */
public class ProjectUtil {
	private static Log log = new SystemStreamLog();;
	
    /**
     * Method to get Artifact-object based on artifactitems groupId and artifactId.
     * Searches to all artifacts in a project and returns a match.
     * 
     * @param project
     * @param groupId
     * @param artifactId
     * @return artifact
     */
    public static Artifact getArtifact(MavenProject project, String groupId, String artifactId) {
    	Iterator iterator = project.getArtifacts().iterator();
    	while (iterator.hasNext()) {
    		Artifact artifact = (Artifact) iterator.next();
    		if (artifact.getGroupId().equals(groupId) && artifact.getArtifactId().equals(artifactId)) {
    			return artifact;
    		}
    	}

    	return null;
    }
}