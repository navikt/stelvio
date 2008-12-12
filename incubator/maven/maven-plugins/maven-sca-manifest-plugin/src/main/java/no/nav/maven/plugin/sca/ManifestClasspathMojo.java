package no.nav.maven.plugin.sca;

import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

/**
 * This is plugin is used as a workaround for a bug in Maven when generating a
 * manifest classpath based on artifacts from the repository. See Maven JAR JIRA
 * for more details (MJAR-61).
 * 
 * The plugin computes a string that can be added explicitly to the manifest.
 * The classpath is based on runtime (and compile-time) dependencies using the
 * following customized pattern (for each applicable artifact):
 * ${artifactId}.${type} (i.e. myLibrary.jar)
 * 
 * @goal manifest-classpath
 * @requiresDependencyResolution
 */
public class ManifestClasspathMojo extends AbstractMojo {
	/**
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

	/**
	 * The (project) property that will be set to the value of the computed
	 * string.
	 * 
	 * @parameter default-value="manifest.classpath"
	 * @required
	 */
	private String propertyName;

	@SuppressWarnings("unchecked")
	public void execute() throws MojoExecutionException {
		StringBuilder classPath = new StringBuilder();
		List<Artifact> runtimeArtifacts = project.getRuntimeArtifacts();
		for (Artifact runtimeArtifact : runtimeArtifacts) {
			if (classPath.length() > 0) {
				classPath.append(" ");
			}
			classPath.append(runtimeArtifact.getArtifactId()).append(".").append(runtimeArtifact.getType());
		}
		getLog().info("Setting property <" + propertyName + "> to value <" + classPath + ">");
		project.getProperties().put(propertyName, classPath.toString());
	}
}
