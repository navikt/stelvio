package no.nav.maven.plugins.pi;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.artifact.handler.manager.ArtifactHandlerManager;
import org.apache.maven.model.Build;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.util.FileUtils;

/**
 * Mojo that generates a project interchange zip-file for the current project
 * 
 * @author test@example.com
 * 
 * @goal project-interchange
 */
public class ProjectInterchangeMojo extends AbstractMojo {
	private static final String PROJECT_INTERCHANGE_ARTIFACT_TYPE = "project-interchange";

	private static final String[] ALL_FILES_PATTERN = { "**/*" };
	private static final String[] ECLIPSE_PROJECT_FILES_PATTERN = { ".project", ".classpath", ".settings/**" };
	private static final String[] JAVA_FILES_PATTERN = { "**/*.java" };

	/**
	 * @component
	 */
	private MavenProjectHelper projectHelper;

	/**
	 * @component
	 */
	private ArtifactHandlerManager artifactHandlerManager;

	/**
	 * @component roleHint="project-interchange"
	 */
	private ArtifactHandler projectInterchangeArtifactHandler;

	/**
	 * @component roleHint="zip"
	 * @required
	 * @readonly
	 */
	private Archiver archiver;

	/**
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

	public void execute() throws MojoExecutionException {
		// TODO: The following must be done because of one (or more) bug(s) in
		// Maven. See Maven bugs MNG-3506 and MNG-2426 for more info.
		if (!projectInterchangeArtifactHandler.equals(artifactHandlerManager
				.getArtifactHandler(PROJECT_INTERCHANGE_ARTIFACT_TYPE))) {
			getLog().debug("Adding project interchange artifact handler to artifact handler manager");
			artifactHandlerManager.addHandlers(Collections.singletonMap(PROJECT_INTERCHANGE_ARTIFACT_TYPE,
					projectInterchangeArtifactHandler));
		}

		try {
			new ProjectInterchangeBuilder(archiver).build();

			Build build = project.getBuild();
			File outputFile = new File(build.getDirectory(), build.getFinalName() + "-"
					+ projectInterchangeArtifactHandler.getClassifier() + ".zip");
			archiver.setDestFile(outputFile);
			archiver.createArchive();

			projectHelper.attachArtifact(project, PROJECT_INTERCHANGE_ARTIFACT_TYPE, projectInterchangeArtifactHandler
					.getClassifier(), outputFile);
		} catch (ArchiverException e) {
			throw new MojoExecutionException("Error creating project interchange", e);
		} catch (IOException e) {
			throw new MojoExecutionException("Error creating project interchange", e);
		}
	}

	private class ProjectInterchangeBuilder {
		private String baseDirectory = project.getArtifactId();

		private Archiver archiver;

		public ProjectInterchangeBuilder(Archiver archiver) {
			this.archiver = archiver;
		}

		public void build() throws ArchiverException, MojoExecutionException {
			addEclipseProjectFiles();
			addSources();
			addResources();
		}

		private void addEclipseProjectFiles() throws ArchiverException {
			archiver.addDirectory(project.getBasedir(), baseDirectory + "\\", ECLIPSE_PROJECT_FILES_PATTERN, FileUtils
					.getDefaultExcludes());
		}

		@SuppressWarnings("unchecked")
		private void addSources() throws ArchiverException, MojoExecutionException {
			File sourceDirectory = new File(project.getBuild().getSourceDirectory());
			if (!sourceDirectory.exists()) {
				getLog().warn("Skipping non-existing source directory (" + sourceDirectory + ").");
				return;
			}
			String relativePath = getRelativePathToBaseDir(sourceDirectory);

			Collection<String> excludes = new HashSet<String>(FileUtils.getDefaultExcludesAsList());
			// Loop through resources and see if any resource directory is the
			// same as the source directory.
			Collection<Resource> resources = project.getBuild().getResources();
			for (Resource resource : resources) {
				String resourceRelativePath = getRelativePathToBaseDir(resource.getDirectory());
				if (resourceRelativePath.equals(relativePath)) {
					// Add excludes from resources if resource directory equals
					// source directory. I wish Maven had support for source
					// excludes.......
					excludes.addAll(resource.getExcludes());
				}
			}

			archiver.addDirectory(sourceDirectory, baseDirectory + relativePath + "\\", JAVA_FILES_PATTERN, excludes
					.toArray(new String[excludes.size()]));
		}

		@SuppressWarnings("unchecked")
		private void addResources() throws MojoExecutionException, ArchiverException {
			Collection<Resource> resources = project.getBuild().getResources();
			for (Resource resource : resources) {
				addResource(resource);
			}
		}

		@SuppressWarnings("unchecked")
		private void addResource(Resource resource) throws MojoExecutionException, ArchiverException {
			File resourceDirectory = new File(resource.getDirectory());
			if (!resourceDirectory.exists()) {
				getLog().warn("Skipping non-existing resource directory (" + resourceDirectory + ").");
				return;
			}
			String relativePath = getRelativePathToBaseDir(resourceDirectory);

			Collection<String> includes = new HashSet<String>(resource.getIncludes());
			if (includes.isEmpty()) {
				includes.addAll(Arrays.asList(ALL_FILES_PATTERN));
			}

			Collection<String> excludes = new HashSet<String>(FileUtils.getDefaultExcludesAsList());
			// Exclude java files - just in case resource directory is the same
			// as source directory
			excludes.addAll(Arrays.asList(JAVA_FILES_PATTERN));
			excludes.addAll(resource.getExcludes());

			archiver.addDirectory(resourceDirectory, baseDirectory + relativePath + "\\", includes.toArray(new String[includes
					.size()]), excludes.toArray(new String[excludes.size()]));
		}

		private String getRelativePathToBaseDir(File dir) throws MojoExecutionException {
			return getRelativePathToBaseDir(dir.getAbsolutePath());
		}

		private String getRelativePathToBaseDir(String dir) throws MojoExecutionException {
			String baseDir = project.getBasedir().getAbsolutePath();
			if (!dir.startsWith(baseDir)) {
				throw new MojoExecutionException("Project interchange only supports folders within baseDir");
			}
			return dir.substring(baseDir.length(), dir.length());
		}
	}
}
