package no.nav.maven.plugins;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import junit.framework.TestCase;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;


public class PropertiesGeneratorMojoTest extends TestCase {
	public void testSlow() throws MojoExecutionException, MojoFailureException {
		PropertiesGeneratorMojo propGen = new PropertiesGeneratorMojo();
		String env = "src/main/resources/properties";
		String output = "target/app_props";
		String templ = "src/main/resources/templates";
		
		propGen.setEnvironmentDir(env);
		propGen.setOutputDir(output);
		propGen.setTemplateDir(templ);

		propGen.execute();


		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(output + "/nav-cons-oko-mot-batch-oppdrag-04.properties"));
		} catch (IOException e) {
			e.printStackTrace();
			assertTrue(false);
		}

		assertEquals("SS_T5.Messaging", prop.getProperty("CLUSTER"));
		assertEquals("MRT1", prop.getProperty("FOREIGN_BUS"));
	}
}
