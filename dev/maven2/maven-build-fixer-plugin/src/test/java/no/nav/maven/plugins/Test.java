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
		fixer.setEarDirectory(new File("E:/ControllerScript/kjempen/target/classes/builds/ear"));
		Set<String> modules = new HashSet<String>();
		Set<String> exceptionModules = new HashSet<String>();
		
		exceptionModules.add("nav-cons-frg-tps");
		exceptionModules.add("nav-cons-oko-avg");
		exceptionModules.add("nav-cons-sak-infot");
		exceptionModules.add("nav-cons-sak-infot-opptjening");
		exceptionModules.add("nav-cons-sak-infot-pensjonssak");
		exceptionModules.add("nav-cons-sak-infot-tjenestepensjon");
		exceptionModules.add("nav-cons-sak-infot-vedtak");
		exceptionModules.add("nav-cons-sak-infot-pinst001");
		exceptionModules.add("nav-cons-eks-e500");
		exceptionModules.add("nav-cons-oko-os-oppdrag");
		exceptionModules.add("nav-cons-sak-bisys-bisys001");
	
		modules.add("nav-prod-sak-arena");
		
		fixer.setModules(modules);
		fixer.setExceptionModules(exceptionModules);
		fixer.execute();
		
	}

}
