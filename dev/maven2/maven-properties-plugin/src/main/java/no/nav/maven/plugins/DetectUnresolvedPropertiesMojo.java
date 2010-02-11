package no.nav.maven.plugins;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Goal which scans through a properties file, prints warning for unresolved properties and fails at the end if
 * one or more properties are unresolved
 * 
 * Delimiters on the form ${propertyname} and @propertyname@ are recognized.
 * 
 * @goal detect-unresolved-properties
 * 
 * @author Øystein Gisnås, Accenture
 */
public class DetectUnresolvedPropertiesMojo extends AbstractMojo {

	private static final Delimter[] DELIMITERS = new Delimter[] { new Delimter("${", "}"), new Delimter("@", "@")};

	/**
	 * Property file to scan
	 * 
	 * @parameter
	 * @required
	 */
	private File file;
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			int numUnresolvedProperties = 0;
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
							getLog().info("Context: '" + line);
						}
					}
				}
			}
			reader.close();
			
			if (numUnresolvedProperties > 0) {
				throw new MojoFailureException("Found " + numUnresolvedProperties + " unresolved properties");
			}
		} catch (FileNotFoundException e) {
			throw new MojoExecutionException("File not found: " + file.getAbsolutePath(), e);
		} catch (IOException e) {
			throw new MojoExecutionException("An error occured while reading from file: " + file.getAbsolutePath(), e);
		}
	}
}
