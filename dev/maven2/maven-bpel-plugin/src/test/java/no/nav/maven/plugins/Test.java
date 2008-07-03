package no.nav.maven.plugins;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

public class Test {

	/**
	 * @param args
	 * @throws MojoExecutionException 
	 * @throws MojoFailureException 
	 */
	public static void main(String[] args) throws MojoExecutionException, MojoFailureException {
		ConfigFixer fixer = new ConfigFixer();
		fixer.setDmgrProfileName("Dmgr01");
		fixer.setFlattenedFolder(new File("E:/Controller_Scripts/myggen_kontroller_clean/target/classes/builds/eardist"));
		fixer.setPassword("was1234A");
		fixer.setHost("e11apvl029.utv.internsone.local");
		fixer.setPort(22);
		fixer.setUser("wasadm");
		fixer.setWPSHome("/opt/IBM/WebSphere/ProcServer");
		
		HashSet<String> bpels = new HashSet<String>();
		bpels.add("nav-ent-frg-person");
		fixer.setBPELModules(bpels);
		
		fixer.execute();
		
	}

}
