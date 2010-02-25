package no.nav.maven.plugin.ear;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.xmlbeans.XmlException;
import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.util.FileUtils;

import com.sun.java.xml.ns.j2Ee.ApplicationDocument;

/**
 * Goal which updates the ear file generated by service deploy.
 * 
 * @author test@example.com
 * 
 * @goal update-ear
 */
public class UpdateEarMojo extends AbstractMojo {
	/**
	 * @component roleHint="zip"
	 * @required
	 * @readonly
	 */
	private UnArchiver unArchiver;

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

	/**
	 * Defines the assembly type to use. Valid values are zip [default] and pi
	 * (project interchange).
	 * 
	 * @parameter default-value="${project.build.finalName}"
	 */
	private String displayName;

	public void execute() throws MojoExecutionException, MojoFailureException {
		// This "stupid" if test is here because I want to configure the plugin
		// execution element i parent POMs
		if (!"pom".equals(project.getPackaging())) {
			new EarUpdater(project.getArtifact().getFile()).update();
		}
	}

	private class EarUpdater {
		private File earFile;

		public EarUpdater(File earFile) {
			this.earFile = earFile;
		}

		public void update() throws MojoExecutionException {
			File workingDir = null;
			try {
				workingDir = createWorkingDir(project);

				extractArchive(workingDir);
				updateArchive(workingDir);
				createArchive(workingDir);
			} finally {
				if (workingDir != null) {
					workingDir.delete();
				}
			}
		}

		private File createWorkingDir(MavenProject project) throws MojoExecutionException {
			try {
				File workingDir = new File(project.getBuild().getDirectory(), "update-ear");
				if (workingDir.exists()) {
					if (workingDir.isDirectory()) {
						FileUtils.deleteDirectory(workingDir);
					} else {
						workingDir.delete();
					}
				}
				workingDir.mkdir();
				return workingDir;
			} catch (IOException e) {
				throw new MojoExecutionException("An error occured when creating working directory", e);
			}
		}

		private void extractArchive(File dir) throws MojoExecutionException {
			try {
				unArchiver.setDestDirectory(dir);
				unArchiver.setSourceFile(earFile);
				unArchiver.extract();
			} catch (ArchiverException e) {
				throw new MojoExecutionException("An error occured when extracting archive", e);
			} catch (IOException e) {
				throw new MojoExecutionException("An error occured when extracting archive", e);
			}
		}

		private void createArchive(File dir) throws MojoExecutionException {
			try {
				archiver.addDirectory(dir);
				archiver.setDestFile(earFile);
				archiver.createArchive();
			} catch (ArchiverException e) {
				throw new MojoExecutionException("An error occured when creating archive", e);
			} catch (IOException e) {
				throw new MojoExecutionException("An error occured when creating archive", e);
			}
		}

		private void updateArchive(File dir) throws MojoExecutionException {
			try {
				File applicationXmlFile = new File(new File(dir, "META-INF"), "application.xml");
				ApplicationDocument applicationDocument = ApplicationDocument.Factory.parse(applicationXmlFile);

				applicationDocument.getApplication().getDisplayNameArray(0).setStringValue(displayName);

				applicationDocument.save(applicationXmlFile);
			} catch (XmlException e) {
				throw new MojoExecutionException("An error occured when updating archive", e);
			} catch (IOException e) {
				throw new MojoExecutionException("An error occured when updating archive", e);
			}
		}
	}
}
