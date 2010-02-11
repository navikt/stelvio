package no.nav.maven.plugins;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.filtering.PropertyUtils;

/**
 * Goal which scans through a properties file and fails if it finds an unresolved property
 * 
 * The properties are interpolated with existing project properties and other properties from the file.
 * Only the format ${propertyname} is recognized for cross-property references.
 * 
 * Missing files are ignored with an info message.
 * 
 * @goal load-properties
 * 
 * @author Øystein Gisnås, Accenture
 */
public class LoadPropertiesMojo extends AbstractMojo {

	/**
	 * The maven project
	 * 
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;
	
	/**
	 * Property files to load properties from
	 * 
	 * @parameter
	 * @required
	 */
	private File[] files;
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		for (File file : files) {
			if (file.exists()) {
				getLog().debug("Loading property file: " + file);
				
				try {
					project.getProperties().putAll(PropertyUtils.loadPropertyFile(file, project.getProperties()));
				} catch (IOException e) {
					throw new MojoExecutionException("An error occured while loading properties from " + file, e);
				}
			} else {
				getLog().info("Ignoring missing properties file: " + file.getAbsolutePath());
			}
		}
	}
}
