package no.nav.maven.plugins;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import no.nav.datapower.config.Policy;
import no.nav.datapower.config.freemarker.Freemarker;
import no.nav.datapower.util.DPCollectionUtils;
import no.nav.datapower.util.DPPropertiesUtils;
import no.nav.datapower.util.PropertiesBuilder;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import freemarker.template.TemplateException;

/**
 * Goal which generates a DataPower configuration for the sikkerhetslytter-domain (MQ).
 * 
 * @goal generateSikkerhetslytterConfig
 * @phase compile
 * 
 * @author person4fdbf4cece95, Accenture
 */

public class GenerateSikkerhetslytterConfigMojo extends AbstractMojo {

	private static final String MAIN_TEMPLATE = "main.ftl";

	/**
	 * The maven project
	 * 
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

	/**
	 * The mojo method doing the actual work when goal is invoked
	 */
	
	/**
	 * sikkerhetslytter
	 * 
	 * @parameter expression="${gateway-name}"
	 * @readonly
	 * @required
	 */
	private String gatewayName;	
	
	/**
	 * @parameter expression="${envclass}"
	 * @required
	 */
	protected String envclass;
	
	// Properties for template interpolation
	private Properties properties;
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		
		File outputDirectory = new File(project.getBuild().getOutputDirectory());
		File localFilesDirectory = new File(outputDirectory, "files/local");
		
		File certFile = new File(project.getBasedir(), "target/dependency/" + gatewayName + "-nonenvironment-configuration-jar/filters/main.properties");

		File tempDir = new File(project.getBasedir(), "target/temp");
		tempDir.mkdir();
		File configFile = new File(tempDir, "configuration.xml");

		properties = loadProperties(certFile.getPath());
		addTrustCerts(properties);
		
		// Create MPG, frontsidehandlers and policy, and map to queue
		// TODO
		// for hver policy og alle artifakter pr policy (lag egne objekter og kanskje nytt policy-objekt):  
		// generer mpg, frontsidehandler, knytt mot kø, lag policy av aktuell type
		
		// Merge templates with properties and output to config file
		try {
			processFreemarkerTemplates(configFile);
		} catch (IOException e) {
			throw new IllegalStateException("Caught IOException while generating DataPower configuration", e);
		} catch (TemplateException e) {
			throw new IllegalStateException("Caught IllegalStateException while generating DataPower configuration", e);
		}

		
		
	}
	
	/*
	 * Load properties from property file
	 */
	private Properties loadProperties(String propertiesFile) {
		Properties properties = DPPropertiesUtils.load(propertiesFile);
		PropertiesBuilder builder = new PropertiesBuilder();
		builder.putAll(properties);
		builder.interpolate();
		return builder.buildProperties();
	}
	
	/*
	 * Expand trust certificate property and inject new expanded property TODO
	 * Move out of the generic configuration generator
	 */
	private void addTrustCerts(Properties props) {
		if (props.getProperty("partnerTrustCerts") != null) {
			List<String> trustCertList = DPCollectionUtils.listFromString(props.getProperty("partnerTrustCerts"));
			List<Map<String, String>> trustCertMapList = DPCollectionUtils.newArrayList();
			for (String trustCert : trustCertList) {
				Map<String, String> cert = DPCollectionUtils.newHashMap();
				cert.put("name", trustCert.substring(trustCert.lastIndexOf("/") + 1));
				cert.put("file", trustCert);
				trustCertMapList.add(cert);
			}
			props.put("partnerTrustedCerts", trustCertMapList);
		}
	}
	
	private void processFreemarkerTemplates(File configFile) throws IOException, TemplateException {
		// Prepare for fremarker template processing
		Freemarker freemarker = new Freemarker(new File(project.getBasedir(), "target/dependency/" + gatewayName + "-nonenvironment-configuration-jar/freemarker-templates"));
		// Open file for writing
		FileWriter writer = new FileWriter(configFile);
		// Process main template
		freemarker.processTemplate(MAIN_TEMPLATE, properties, writer);
		// Flush and close file writer
		writer.flush();
		writer.close();
	}

}
