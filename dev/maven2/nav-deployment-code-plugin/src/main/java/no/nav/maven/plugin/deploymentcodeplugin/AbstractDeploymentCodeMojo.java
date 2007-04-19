package no.nav.maven.plugin.deploymentcodeplugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;

/**
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 */
public abstract class AbstractDeploymentCodeMojo extends AbstractMojo {
	/**
     * The maven project.
     *
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;
    
    /**
     * The maven project's helper.
     *
     * @parameter expression="${component.org.apache.maven.project.MavenProjectHelper}"
     * @required
     * @readonly
     */
    private MavenProjectHelper projectHelper;
    
	/**
	 * @return the project
	 */
	public MavenProject getProject() {
		return project;
	}

	/**
	 * @param project the project to set
	 */
	public void setProject(MavenProject project) {
		this.project = project;
	}

	/**
	 * @return the projectHelper
	 */
	public MavenProjectHelper getProjectHelper() {
		return projectHelper;
	}

	/**
	 * @param projectHelper the projectHelper to set
	 */
	public void setProjectHelper(MavenProjectHelper projectHelper) {
		this.projectHelper = projectHelper;
	}
}