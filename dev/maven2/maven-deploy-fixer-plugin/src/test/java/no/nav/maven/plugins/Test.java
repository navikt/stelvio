package no.nav.maven.plugins;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

public class Test {
	public static void main(String[] args) throws MojoExecutionException, MojoFailureException{
		ConfigFixer fixer = new ConfigFixer();
		fixer.setAddUsernameToken(true);
		fixer.setDoRoleBinding(true);
		fixer.setEarFolder(new File("E:/tmp2/target/classes/builds/ear"));
		fixer.setFlattenedFolder(new File("E:/tmp2/target/classes/builds/eardist"));
		fixer.setEnvFile(new File("E:/tmp2/src/main/resources/scripts/environments/i2.properties"));
		Set excluded = new HashSet();
		excluded.add("nav-cons-pen-psak-arenasak");
		fixer.setExcludedModules(excluded);
		fixer.execute(); 
	}
}
