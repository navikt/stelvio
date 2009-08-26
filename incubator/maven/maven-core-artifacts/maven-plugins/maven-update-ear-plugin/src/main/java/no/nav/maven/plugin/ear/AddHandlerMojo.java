package no.nav.maven.plugin.ear;

import java.io.IOException;

import no.nav.maven.plugins.descriptor.jee.WebServicesEditor;
import no.nav.maven.plugins.descriptor.utils.EarFile;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.eclipse.jst.j2ee.commonarchivecore.internal.Archive;
import org.eclipse.jst.j2ee.commonarchivecore.internal.EARFile;

/**
 * Goal which updates the ejb with a handler in webservices.xml
 * 
 * @author test@example.com
 * 
 * @goal add-handler
 */
public class AddHandlerMojo extends AbstractMojo {
	/**
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

	/**
	 * @parameter expression="${handlername}"
	 * @required
	 */
	private String handlerName;

	/**
	 * @parameter expression="${handlerclass}"
	 * @required
	 */
	private String handlerClass;

	public void execute() throws MojoExecutionException, MojoFailureException {
		if (!"pom".equals(project.getPackaging())) {
			EARFile earFile = EarFile.openEarFile(project.getArtifact().getFile().getAbsolutePath());
			addHandler(earFile);
			EarFile.closeEarFile(earFile);
		}
	}

	private final void addHandler(final EARFile earFile) {
		Archive ejb = (Archive) earFile.getEJBJarFiles().get(0);
		WebServicesEditor wse = new WebServicesEditor(ejb);
		if (wse.exists()) {
			wse.addHandler(handlerName, handlerClass);
			try {
				wse.save();
			} catch (IOException e) {
				throw new RuntimeException("An error occured saving the ejb.", e);
			}
		}
	}
}
