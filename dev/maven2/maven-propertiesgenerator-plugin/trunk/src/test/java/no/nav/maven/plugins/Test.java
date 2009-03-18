
package no.nav.maven.plugins;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

public class Test {
	public static void main(String[] args) throws MojoExecutionException, MojoFailureException{
		PropertiesGeneratorMojo propGen = new PropertiesGeneratorMojo();
		propGen.setEnvironmentDir("E:/test/src/main/resources/scripts/environments");
		propGen.setEnvironmentName("T6");
		propGen.setOutputDir("E:/test/target/classes/scripts/app_props");
		propGen.setTemplateDir("E:/test/src/main/resources/scripts/templates");
		
		propGen.execute();
		System.out.println("execute done");
	}
}
