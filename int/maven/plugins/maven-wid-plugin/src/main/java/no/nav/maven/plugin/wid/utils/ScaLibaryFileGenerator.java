package no.nav.maven.plugin.wid.utils;

import java.io.File;
import java.io.IOException;

import no.nav.maven.utilities.sca.ScaLibraryBuilder;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

public class ScaLibaryFileGenerator {
	/**
	 * IBM Integration Designer 8.5 (ID) requires a sca.library file for library projects. 
	 * If the file is not provided, ID will run the Migration Wizard to create the file. 
	 * This method generates the file in the workspace it does not already exists. 
	 * 
	 * Generally, the sca.library should be a part of development files that are under version control. 
	 * However, since most of the libraries are old and are not often changed, auto-generation of sca.library  
	 * will make a little easier for the ID developer by not constantly having to run the Migration Wizard after 
	 * running e.g. wid:create-project goal. 
	 * 
	 * Also, for service specifications, the ID library modules and projects files are generated by the 
	 * wid:create-projet goal. In this case, the sca.library will never be created, committed and released. 
	 * This means that each time a service-specification project is imported into ID, it will always require 
	 * migration, because sca.library cannot be commited.  
	 * @throws MojoExecutionException 
	 */
	public static void createScaLibraryFile(String libName, File destinationDirectory, Log log ) throws MojoExecutionException {
		ScaLibraryBuilder builder = new ScaLibraryBuilder(libName);
		try {
			if (!ScaLibraryBuilder.scaLibraryFileExistsIn(destinationDirectory)) { 
				log.info("Creating SCA library file for "+libName+" in "+destinationDirectory.getAbsolutePath());
				builder.writeToDirectory(destinationDirectory);
			}else log.info("SCA library file already exists in directory "+destinationDirectory.getAbsolutePath()+". Skipping file creation");
				
		} catch (IOException e) {
			throw new MojoExecutionException("Unable to write SCA library file", e);
		}
	}

}