package no.nav.maven.plugins;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Goal which scans through all files in a directory, prints warning for unresolved properties and fails at the end if
 * one or more properties are unresolved
 * 
 * Delimiters on the form ${propertyname} and @propertyname@ are recognized.
 * 
 * @goal detect-unresolved-properties-aaa
 * 
 * @author person4fdbf4cece95, Accenture
 */
public class DetectUnresolvedPropertiesInDirMojo extends AbstractMojo {

	private static final Delimter[] DELIMITERS = new Delimter[] { new Delimter("${", "}"), new Delimter("@", "@")};

	/**
	 * Folder containing files to scan
	 * 
	 * @parameter
	 * @required
	 */
	private File verifyDir;
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			if (!verifyDir.isDirectory())
				throw new IllegalArgumentException("Specified path '" + verifyDir + "'is not a directory");
			
			File[] files = verifyDir.listFiles((FileFilter) FileFilterUtils.directoryFileFilter());
			int numUnresolvedProperties = 0;
			
			for(File file : files){
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line;

				while ((line = reader.readLine()) != null) {
					int index = 0;
					for (Delimter delimiter : DELIMITERS) {
						int startIndex = line.indexOf(delimiter.getBegin(), index);
						if (startIndex > -1) {
							index = startIndex + 1;
							int endIndex = line.indexOf(delimiter.getEnd(), index);
							if (endIndex > -1) {
								index = endIndex + 1;
								numUnresolvedProperties++;
								getLog().warn("Found unresolved property '" + line.substring(startIndex + 1, endIndex) + "'");
								getLog().info("Context: '" + line + "'");
							}
						}
					}
				}
				reader.close();
			}
			
			if (numUnresolvedProperties > 0) {
				throw new MojoFailureException("Found " + numUnresolvedProperties + " unresolved properties");
			}
		} catch (FileNotFoundException e) {
			throw new MojoExecutionException("Directory not found: " + verifyDir.getAbsolutePath(), e);
		} catch (IOException e) {
			throw new MojoExecutionException("An error occured while reading from directory: " + verifyDir.getAbsolutePath(), e);
		}
	}
}
