package no.nav.maven.plugin.sca;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
 * @author test@example.com
 * 
 * @goal build-manifest-classpath
 * @requiresDependencyResolution
 */
public class BuildManifestClasspathMojo extends AbstractMojo {
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

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public void execute() throws MojoExecutionException {
		StringBuilder classPath = new StringBuilder();

		// Sort artifacts to get consistent behavior
		List<Artifact> dependencyArtifacts = new ArrayList<Artifact>(project.getDependencyArtifacts());
		Collections.sort(dependencyArtifacts, new Comparator<Artifact>() {
			public int compare(Artifact o1, Artifact o2) {
				return o1.getArtifactId().compareTo(o2.getArtifactId());
			}
		});

		for (Artifact dependencyArtifact : dependencyArtifacts) {
			String scope = dependencyArtifact.getScope();
			if (Artifact.SCOPE_COMPILE.equals(scope) || Artifact.SCOPE_RUNTIME.equals(scope)) {
				if (classPath.length() > 0) {
					classPath.append(" ").append("\r\n");
				}
				String dependencyArtifactExtension = dependencyArtifact.getArtifactHandler().getExtension();
				classPath.append(dependencyArtifact.getArtifactId()).append(".").append(dependencyArtifactExtension);
			}
		}
		getLog().debug("Setting property <" + propertyName + "> to value <" + classPath + ">");
		project.getProperties().put(propertyName, classPath.toString());
	}
}
