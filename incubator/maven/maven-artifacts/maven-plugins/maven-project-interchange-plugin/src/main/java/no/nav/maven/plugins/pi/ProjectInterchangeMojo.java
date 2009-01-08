package no.nav.maven.plugins.pi;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.artifact.handler.manager.ArtifactHandlerManager;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.zip.ZipArchiver;
import org.codehaus.plexus.util.FileUtils;

/**
 * Goal that generates a project interchange zip-file for the current project
 * 
 * @author test@example.com
 * 
 * @goal project-interchange
 */
public class ProjectInterchangeMojo extends AbstractMojo {
	private static final String PROJECT_INTERCHANGE_ARTIFACT_TYPE = "project-interchange";

	private static final String[] DEFAULT_INCLUDES = { "**/*" };
	private static final String[] ECLIPSE_PROJECT_INCLUDES = { ".project", ".classpath", ".settings/**" };

	/**
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

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
	 * @parameter expression="${project.build.finalName}"
	 * @required
	 * @readonly
	 */
	private String finalName;

	/**
	 * @parameter expression="${baseDirectory}"
	 *            default-value="${project.artifactId}"
	 * @required
	 */
	private String baseDirectory;

	/**
	 * @parameter expression="${project.build.directory}"
	 * @required
	 */
	private File outputDirectory;

	/**
	 * @parameter expression="${classifier}" default-value="pi"
	 */
	private String classifier;

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
			Archiver archiver = new ZipArchiver();

			new ProjectInterchangeBuilder(archiver).build();

			File outputFile = new File(outputDirectory, finalName + "-" + classifier + ".zip");
			archiver.setDestFile(outputFile);
			archiver.createArchive();

			projectHelper.attachArtifact(project, PROJECT_INTERCHANGE_ARTIFACT_TYPE, classifier, outputFile);
		} catch (ArchiverException e) {
			throw new MojoExecutionException("Error creating project interchange", e);
		} catch (IOException e) {
			throw new MojoExecutionException("Error creating project interchange", e);
		}
	}

	private class ProjectInterchangeBuilder {
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
			archiver.addDirectory(project.getBasedir(), baseDirectory + "\\", ECLIPSE_PROJECT_INCLUDES, FileUtils
					.getDefaultExcludes());
		}

		private void addSources() throws ArchiverException, MojoExecutionException {
			File sourceDirectory = new File(project.getBuild().getSourceDirectory());
			if (!sourceDirectory.exists()) {
				getLog().warn("Skipping non-existing source directory (" + sourceDirectory + ").");
				return;
			}
			String relativePath = getRelativePathToBaseDir(sourceDirectory);
			archiver.addDirectory(sourceDirectory, baseDirectory + relativePath + "\\", new String[] { "**/*.java" }, FileUtils
					.getDefaultExcludes());
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

			String[] includes;
			Collection<String> resourceIncludes = resource.getIncludes();
			if (resourceIncludes == null || resourceIncludes.isEmpty()) {
				includes = DEFAULT_INCLUDES;
			} else {
				includes = resourceIncludes.toArray(new String[resourceIncludes.size()]);
			}

			String[] excludes;
			Collection<String> resourceExcludes = resource.getExcludes();
			if (resourceExcludes == null || resourceExcludes.isEmpty()) {
				excludes = FileUtils.getDefaultExcludes();
			} else {
				Collection<String> allExcludes = new ArrayList<String>(FileUtils.getDefaultExcludesAsList());
				allExcludes.addAll(resourceExcludes);
				excludes = allExcludes.toArray(new String[allExcludes.size()]);
			}

			archiver.addDirectory(resourceDirectory, baseDirectory + relativePath + "\\", includes, excludes);
		}

		private String getRelativePathToBaseDir(File dir) throws MojoExecutionException {
			File baseDir = project.getBasedir();
			String dirStr = dir.getAbsolutePath();
			String baseDirStr = baseDir.getAbsolutePath();
			if (!dirStr.startsWith(baseDirStr)) {
				throw new MojoExecutionException("Project interchange only supports folders within baseDir");
			}
			return dirStr.substring(baseDirStr.length(), dirStr.length());
		}
	}
}
