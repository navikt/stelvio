package no.nav.maven.plugins;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.UnArchiver;

public class Archiver {

	// Extracts the file (artifact) to the given directory and returns the destination directory
	public static File extractArchive(File file, File stagingDirectory, UnArchiver ua) throws MojoExecutionException {
		try {

			String formattedDirName;

			// Remove the extension from the destination directory name
			if (file.getName().endsWith(".war")) {
				formattedDirName = file.getName().replace(".war", "");
			} else if (file.getName().endsWith(".ear")) {
				formattedDirName = file.getName().replace(".ear", "");
			} else if (file.getName().endsWith(".jar")) {
				formattedDirName = file.getName().replace(".jar", "");
			} else if (file.getName().endsWith(".zip")) {
				formattedDirName = file.getName().replace(".zip", "");
			} else {
				throw new MojoExecutionException("[ERROR] Unsupported file format. Only 'ear', 'war', 'jar' and 'zip'");
			}

			File destinationDir = new File(stagingDirectory, formattedDirName);
			destinationDir.delete();
			destinationDir.mkdir();

			ua.setSourceFile(file);
			ua.setDestDirectory(destinationDir);
			ua.extract();
			
			return destinationDir;

		} catch (ArchiverException e) {
			throw new MojoExecutionException("[ERROR] Error extracting archive", e);
		} catch (IOException e) {
			throw new MojoExecutionException("[ERROR] Error extracting archive", e);
		} catch (Exception e) {
			throw new MojoExecutionException("[ERROR] Error extracting archive", e);
		}
	}
}
