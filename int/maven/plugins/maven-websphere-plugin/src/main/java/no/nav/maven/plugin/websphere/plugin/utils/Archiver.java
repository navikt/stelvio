package no.nav.maven.plugin.websphere.plugin.utils;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.archiver.zip.ZipArchiver;
import org.codehaus.plexus.util.FileUtils;

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
				throw new MojoExecutionException("[ERROR]: Unsupported file format. Only 'ear', 'war', 'jar' and 'zip'");
			}

			File destinationDir = new File(stagingDirectory, formattedDirName);
			destinationDir.delete();
			destinationDir.mkdir();

			ua.setSourceFile(file);
			ua.setDestDirectory(destinationDir);
			ua.extract();
			
			System.out.println("[INFO] ### ARCHIVER ### Successfully extracted: " + file + " => " + destinationDir);

			return destinationDir;

		} catch (ArchiverException e) {
			throw new MojoExecutionException("Error extracting archive", e);
		} catch (IOException e) {
			throw new MojoExecutionException("Error extracting archive", e);
		} catch (Exception e) {
			throw new MojoExecutionException("Error extracting archive", e);
		}
	}

	// Creates a new archive with the source directory in the given parent directory (supported extensions: 'ear', 'war' and
	// 'zip')
	public static File createArchive(File src, File parentDir, String extension) throws MojoFailureException {
		try {

			if (!extension.equals("war") && !extension.equals("ear") && !extension.equals("zip") && !extension.equals("jar")) {
				throw new MojoFailureException("[ERROR]: Unable to compress, " + src + ". Unsupported extension: " + extension + ", please use 'ear', 'jar', 'war' or 'zip'.");
			}

			if (!src.isDirectory()) {
				throw new MojoFailureException("[ERROR]: Unable to compress, " + src + ". The source is not a directory.");
			}

			ZipArchiver za = new ZipArchiver();

			File destination = new File(parentDir + "/" + src.getName() + "." + extension);

			za.addDirectory(src);
			za.setDestFile(destination);
			za.createArchive();
			

			if (!extension.equals("zip")) {
				// counter the silly message!
				System.out.println("[INFO] pfff, ignore that.. we're actually creating a " + extension + "! :)");
			}
			
			System.out.println("[INFO] ### ARCHIVER ### Successfully created: " + destination);
			
			FileUtils.deleteDirectory(src);
			
			return za.getDestFile();

		} catch (ArchiverException e) {
			throw new MojoFailureException("[ERROR]: Unable to compress, " + src + ". " + e.getMessage());
		} catch (IOException e) {
			throw new MojoFailureException("[ERROR]: Unable to compress, " + src + ". " + e.getMessage());
		}
	}
}
