package no.nav.maven.plugins;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;

/**
 * @author utvikler
 *
 */
public abstract class EarExtractor extends AbstractMojo {


	
	/**
	 * This parameter is the temporary directory where the files are unpacked.
	 * @parameter
	 * @required
	 */
	private File workingArea;
	/**
     * The maven project's helper.
     *
     * @parameter expression="${component.org.apache.maven.project.MavenProjectHelper}"
     * @required
     * @readonly
     */
    private MavenProjectHelper projectHelper;
	
	/**
     * The maven project.
     *
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    
    protected abstract boolean isValidFile(String fileName, boolean isFile); 

	protected void doDirectory(File directory) throws MojoExecutionException {
		try {
			File[] files = directory.listFiles();
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				String fileName = file.getName();
				boolean valid = isValidFile(fileName,file.isFile());
				if (valid) {
					String unpackDir = workingArea + "/" + "ear" + "/" + fileName.substring(0, fileName.length() - 4);
					final File destination = new File(unpackDir);
					destination.mkdirs();
					ZipUtils.extract(file, destination);
					getLog().info("\tdone unpacking ear files");
					boolean lagtTil = extraxtEJBJarFiles(unpackDir);
					getLog().info("\tdone unpacking and repacking jar files");
					if (lagtTil) {
						ZipUtils.compress(destination, file);
					}
				}
			}
		} catch (IOException e) {
			throw new MojoExecutionException("Error parsing inputfile", e);
		}
	}

	/*
	 * This method extracts the EJB jar files, and adds the handler element to
	 * webservices.xml
	 */
	private boolean extraxtEJBJarFiles(String unpackDir) throws MojoExecutionException, IOException {
		File dir = new File(unpackDir);
		File[] files = dir.listFiles();
		boolean lagtTil = false;
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if (file.getName().startsWith("nav-") && file.getName().endsWith("EJB.jar")) {
				final String outputDir = workingArea + "/" + "jar" + "/" + file.getName().substring(0, file.getName().length() - 4);
				final File jarDir = new File(outputDir);
				ZipUtils.extract(file, jarDir);
				lagtTil = changeFile(outputDir);
				if (lagtTil) { 
					getLog().info("lagt til handler, pakker filene");
					ZipUtils.compress(jarDir, file);
				}
			}
		}
		return lagtTil;
	}

	protected abstract boolean changeFile(String outputDir) throws MojoExecutionException;
		
	
}
