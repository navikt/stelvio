package no.nav.maven.plugins;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import no.nav.maven.plugins.freemarker.Freemarker;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import freemarker.template.TemplateException;

/**
 * Goal which generates a DataPower configuration.
 *
 * @goal generate
 * @phase compile
 * 
 * @author Øystein Gisnås, Accenture
 */
public class GenerateConfigMojo extends AbstractMojo {

	private static final String MAIN_TEMPLATE = "main.ftl";

	private static final String CONFIG_DIRECTORY = "datapower-config";

	/**
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

	/**
	 * The mojo method doing the actual work when goal is invoked
	 */
	public void execute() throws MojoExecutionException, MojoFailureException {
		// Welcome
		getLog().info("Generating Datapower config");
		File outputDirectory = new File(project.getBuild().getDirectory(), CONFIG_DIRECTORY);
		getLog().debug("OutputDirectory=" + outputDirectory);
		File configFile = new File(outputDirectory, "configuration.xcfg");
		getLog().debug("ConfigFile=" + configFile);
		
		// Merge templates with properties and output to config file
		try {
			processFreemarkerTemplates(configFile);
		} catch (IOException e) {
			throw new IllegalStateException("Caught IOException while generating DataPower configuration", e);
		} catch (TemplateException e) {
			throw new IllegalStateException("Caught IllegalStateException while generating DataPower configuration", e);
		}
	}

	private void processFreemarkerTemplates(File configFile) throws IOException, TemplateException {
		// Prepare for fremarker template processing
		Freemarker freemarker = new Freemarker();
		// Open file for writing
		FileWriter writer = new FileWriter(configFile);
		// Process main template
		Properties properties = new Properties();
		freemarker.processTemplate(MAIN_TEMPLATE, properties, writer);
		// Flush and close file writer
		writer.flush();
		writer.close();
	}
}
