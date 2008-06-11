package no.nav.maven.plugins;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

public class Test {
	public static void main(String[] args) throws MojoExecutionException, MojoFailureException{
		ConfigFixer fixer = new ConfigFixer();
		fixer.setAddUsernameToken(true);
		fixer.setDoRoleBinding(false);
		fixer.setEarFolder(new File("E:/tmp2/target/classes/builds/ear"));
		fixer.setFlattenedFolder(new File("E:/tmp2/target/classes/builds/eardist"));
		fixer.setEnvFile(new File("E:/tmp2/src/main/resources/scripts/environments/i2.properties"));
		
		fixer.execute(); 
	}
}
