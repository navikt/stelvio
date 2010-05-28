package no.nav.maven.plugin.wid;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.UnArchiver;

/**
 * Generates the wid configuration files.
 * 
 * @author <a href="mailto:test@example.com">Erik Godding Boye</a>
 * @goal create-projects
 * @requiresDependencyResolution compile
 * @execute goal="wid"
 * @aggregator
 */
public class CreateProjectsMojo extends AbstractMojo {
	private static final String PACKAGING_WPS_MODULE_EAR = "wps-module-ear";
	private static final String PACKAGING_WPS_LIBRARY_JAR = "wps-library-jar";

	/**
	 * @parameter expression="${reactorProjects}"
	 * @required
	 * @readonly
	 */
	private Collection<MavenProject> reactorProjects;

	/**
	 * @parameter default-value="${project.basedir}/libraries"
	 * @required
	 */
	private File dependencyProjectsDirectory;

	/**
	 * @component roleHint="zip"
	 */
	private UnArchiver unarchiver;

	/**
	 * @component
	 */
	private Invoker invoker;

	public void execute() throws MojoExecutionException, MojoFailureException {
		Collection<String> reactorProjectIds = new HashSet<String>(reactorProjects.size());
		for (MavenProject reactorProject : reactorProjects) {
			reactorProjectIds.add(reactorProject.getId());
		}

		// Using a HashSet to eliminate any duplicate artifacts (relying on Artifact.hashCode)
		Collection<Artifact> dependencyArtifacts = new HashSet<Artifact>();
		for (MavenProject reactorProject : reactorProjects) {
			String packaging = reactorProject.getPackaging();
			if (PACKAGING_WPS_MODULE_EAR.equals(packaging) || PACKAGING_WPS_LIBRARY_JAR.equals(packaging)) {
				for (Artifact dependencyArtifact : (Collection<Artifact>) reactorProject.getArtifacts()) {
					// TODO: For now we only support dependencies of type wps-library-jar
					if (PACKAGING_WPS_LIBRARY_JAR.equals(dependencyArtifact.getType())) {
						String dependencyArtifactId = dependencyArtifact.getId();
						// Only add dependency artifacts that are not part of the reactor
						if (!reactorProjectIds.contains(dependencyArtifactId)) {
							dependencyArtifacts.add(dependencyArtifact);
						}
					}
				}
			}
		}

		for (Artifact dependencyArtifact : dependencyArtifacts) {
			createProject(dependencyArtifact);
		}
	}

	private void createProject(Artifact artifact) throws MojoExecutionException {
		File extractDirectory = extractArtifact(artifact);
		copyPomFileToRoot(extractDirectory);
		invokeWidPlugin(extractDirectory);
	}

	private void invokeWidPlugin(File baseDirectory) throws MojoExecutionException {
		try {
			InvocationRequest request = new DefaultInvocationRequest();
			request.setBaseDirectory(baseDirectory);
			request.setGoals(Collections.singletonList("wid:wid"));

			invoker.execute(request);
		} catch (MavenInvocationException e) {
			throw new MojoExecutionException("Error invoking Maven", e);
		}
	}

	private void copyPomFileToRoot(File directory) throws MojoExecutionException {
		try {
			Collection filesFound = FileUtils.listFiles(new File(directory, "META-INF/maven"), new NameFileFilter("pom.xml"),
					TrueFileFilter.INSTANCE);
			// Expect exactly one file found
			FileUtils.copyFileToDirectory((File) filesFound.iterator().next(), directory);
		} catch (IOException e) {
			throw new MojoExecutionException("Error copying file", e);
		}
	}

	private File extractArtifact(Artifact artifact) throws MojoExecutionException {
		try {
			File extractDirectory = new File(dependencyProjectsDirectory, getExtractDirectoryPath(artifact));
			extractDirectory.mkdirs();
			unarchiver.setDestDirectory(extractDirectory);
			unarchiver.setSourceFile(artifact.getFile());
			unarchiver.extract();
			return extractDirectory;
		} catch (ArchiverException e) {
			throw new MojoExecutionException("Error extracting archive", e);
		} catch (IOException e) {
			throw new MojoExecutionException("Error extracting archive", e);
		}
	}

	private String getExtractDirectoryPath(Artifact artifact) {
		StringBuilder extractDirectoryPath = new StringBuilder();
		extractDirectoryPath.append(artifact.getGroupId()).append(File.separator);
		extractDirectoryPath.append(artifact.getArtifactId()).append(File.separator);
		extractDirectoryPath.append(artifact.getVersion());
		return extractDirectoryPath.toString();
	}
}
