package no.nav.maven.plugins;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.filtering.PropertyUtils;

/**
 * Goal which loads properties into project scode from a list of files
 * 
 * The properties are interpolated with existing project properties and other properties from the file.
 * Only the format ${propertyname} is recognized for cross-property references.
 * 
 * Existing properties will not be overwritten. The existing property will be kept.
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
					Properties properties = PropertyUtils.loadPropertyFile(file, project.getProperties());
					for (Entry<Object, Object> property : properties.entrySet()) {
						if (!project.getProperties().containsKey(property.getKey())) {
							project.getProperties().put(property.getKey(), property.getValue());
						}
					}
				} catch (IOException e) {
					throw new MojoExecutionException("An error occured while loading properties from " + file, e);
				}
			} else {
				getLog().info("Ignoring missing properties file: " + file.getAbsolutePath());
			}
		}
	}
}
