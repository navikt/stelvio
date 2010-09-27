package no.nav.maven.plugin.deploymentcodeplugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.FileUtils;

/**
 * Mojo that generates EJB deployment code using IBM Websphere 6.1 deploy tool.
 * The following variables use default values that should be defined in "settings.xml":
 * <table>
 * 	<tr>
 *   <td style="font-weight: bold">Maven Pom property</td><td style="font-weight: bold">settings.xml property</td>
 *  </tr>
 *  <tr>
 *   <td>wasHome</td></td>was.home</td>
 *   <td>itpLoc</td></td>itp.loc</td>
 *   <td>wasJavaHome</td></td>was.java.home</td>
 *   <td>bootPath</td></td>boot.path</td>
 *   <td>ejbdCp</td></td>ejbd.cp</td>
 *   <td>wasExtDirs</td></td>was.ext.dirs</td>
 *  </tr>
 * </table>
 * 
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 * @goal websphere-deployment-code
 * @phase package
 */
public class WebsphereDeploymentCodeMojo extends AbstractDeploymentCodeMojo {
	/**
	 * Source jar needed to generate deployment code.
	 * 
	 * @parameter
	 */
	private File inputJar;
	
	/**
	 * Temporary output jar file with deployment code.
	 * 
	 * @parameter
	 */
	private File outputJar;
	
	/**
	 * Folder the ejb deploy tool will generate work files under.
	 * 
	 * @parameter
	 */
	private File workingDir;
	
	/**
	 * Default value should be specified as a property ('was.home') in settings.xml.
	 * 
	 * @parameter expression="${was.home}"
	 */
	private File wasHome;
	
	/**
	 * Default value should be specified as a property ('itp.loc') in settings.xml.
	 * 
	 * @parameter expression="${itp.loc}"
	 */
	private File itpLoc;
	
	/**
	 * Default value should be specified as a property ('was.java.home') in settings.xml.
	 * 
	 * @parameter expression="${was.java.home}"
	 */
	private File wasJavaHome;
	
	/**
	 * Default value should be specified as a property ('boot.path') in settings.xml.
	 * 
	 * @parameter expression="${boot.path}"
	 */
	private String bootPath;
	
	/**
	 * Default value should be specified as a property ('ejbd.cp') in settings.xml.
	 * 
	 * @parameter expression="${ejbd.cp}"
	 */
	private String ejbdCp;
	
	/**
	 * Default value should be specified as a property ('was.ext.dirs') in settings.xml.
	 * 
	 * @parameter expression="${was.ext.dirs}"
	 */
	private String wasExtDirs;
	
	/**
	 * Set to true if the ejb deploy working folder shouldn't be deleted after finish.
	 *  
	 * @parameter
	 */
	private boolean keepEjbDeployDir;
	
	/**
	 * Set to true if the resulting artifact should be unpacked after finsish.
	 * 
	 * @parameter
	 */
	private boolean unpackCode;
	
	/**
	 * Directory to unpack the finished jar file.
	 * 
	 * @parameter expression="${project.build.directory}/classes"
	 */
	private File unpackDir;
	
	/**
	 * Method that is executed when plugin is started.
	 * 
	 * @throws MojoExecutionException
	 * @throws MojoFailureException
	 * @throws  
	 */
	public void execute() throws MojoExecutionException, MojoFailureException {
		boolean skip = init();
		
		if (!skip) {
		
			try {
				// Execute the ejb deploy tool in a separate JVM
				Process process = Runtime.getRuntime().exec(getJavaExecutable());
				
                // any output?
                StreamGobbler errorGobbler = new
                    StreamGobbler(process.getErrorStream(), getLog());
                StreamGobbler outputGobbler = new
                    StreamGobbler(process.getInputStream(), getLog());

                // kick them off
                errorGobbler.start();
                outputGobbler.start();

                //wait for process to finish, ignoring the return code
                process.waitFor();

				// Make final preparations to the resulting artifacts
				finalizeArtifacts();
			}
			catch (DependencyResolutionRequiredException e) {
				throw new MojoExecutionException("An error occurred while setting ejbdeploy arguments.", e);
			}
			catch (IOException e) {
				throw new MojoExecutionException("An error occurred while executing process", e);
			} catch (InterruptedException e) {
                throw new MojoExecutionException("An error occurred while executing process", e);
            }
        }
	}
	
	/**
	 * Initiates variables.
	 * 
	 * @throws MojoExecutionException 
	 */
	private boolean init() throws MojoExecutionException {
		// Check that all required argument are populated with standard or custom values.
		checkArguments();
		
		boolean skip = false;
		// Set default values, if noe specified.
		if (inputJar == null) {
			inputJar = new File(getProject().getBuild().getDirectory()+
								File.separatorChar+
								getProject().getArtifactId()+"-"+getProject().getVersion()+".jar");
		}
		
		// clover lager sin egen jarfil som ikke skal pludres med
		if (inputJar.getAbsolutePath().indexOf(File.separatorChar+"clover"+File.separatorChar) != -1) {
			skip=true;
		}
		
		if (outputJar == null) {
			outputJar = new File(getProject().getBuild().getDirectory()+
								File.separatorChar+
								getProject().getArtifactId()+"-"+getProject().getVersion()+"-OUTPUT.jar");
		}
		
		if (workingDir == null) {
			workingDir = new File(getProject().getBuild().getDirectory()+
								File.separatorChar+
								"ejbdeploy-working");
		}
		return skip;
	}
	
	/**
	 * Check that all required arguments are specified.
	 * If any default values retrived from settings.xml are null or invalid, throw an exception.
	 * 
	 * @throws MojoExecutionException 
	 */
	private void checkArguments() throws MojoExecutionException {
		if (wasHome == null || !wasHome.exists()) {
			throw new MojoExecutionException("The argument 'wasHome' is not specified or specified as an invalid value (folder does not exists). "+
							"A possible cause is that the property 'was.home' is missing in your settings.xml file.");
		}
		if (itpLoc == null || !itpLoc.exists()) {
			throw new MojoExecutionException("The argument 'itpLoc' is not specified or specified as an invalid value (folder does not exists). "+
							"A possible cause is that the property 'itp.loc' is missing in your settings.xml file.");
		}
		if (wasJavaHome == null || !wasJavaHome.exists()) {
			throw new MojoExecutionException("The argument 'wasJavaHome' is not specified or specified as an invalid value (folder does not exists). "+
							"A possible cause is that the property 'was.java.home' is missing in your settings.xml file.");
		}
		if (bootPath == null) {
			throw new MojoExecutionException("The argument 'bootPath' is not specified. "+
							"A possible cause is that the property 'boot.path' is missing in your settings.xml file.");
		}
		if (ejbdCp == null) {
			throw new MojoExecutionException("The argument 'ejbdCp' is not specified. "+
							"A possible cause is that the property 'ejbd.cp' is missing in your settings.xml file.");
		}
		if (wasExtDirs == null) {
			throw new MojoExecutionException("The argument 'wasExtDirs' is not specified. "+
							"A possible cause is that the property 'was.ext.dirs' is missing in your settings.xml file.");
		}
	}
	
	/**
	 * Make final preperations to the resulting artifacts.
	 * 
	 * @throws MojoExecutionException 
	 * @throws IOException 
	 */
	private void finalizeArtifacts() throws MojoExecutionException, IOException {
		String generatedJarFilename = 	getProject().getBuild().getDirectory()+File.separatorChar+
										getProject().getArtifactId()+"-"+
										getProject().getVersion()+".jar";
		
		// Delete generated jar
		FileUtils.fileDelete(generatedJarFilename);
		
		// Rename outputJar to final artifact name
		if (outputJar.exists()) {
			outputJar.renameTo(new File(generatedJarFilename));
		}
		else {
			throw new MojoExecutionException("The output jar "+outputJar+" does not exists.");
		}
		
		// Delete ejb deploy folder if specified
		if (!keepEjbDeployDir) {
			if (workingDir.exists()) {
				FileUtils.deleteDirectory(workingDir);
			}
			else {
				throw new MojoExecutionException("The working directory "+workingDir+" does not exists.");
			}
		}
		
		// Unpack jar file if specified
		if (unpackCode) {
			unpack(generatedJarFilename);
		}
	}
	
	/**
	 * Method to unpack generated code to a specified folder.
	 * 
	 * @param inputJar
	 * @throws MojoExecutionException if unpackDir is not specified.
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	private void unpack(String inputJar) throws MojoExecutionException, FileNotFoundException, IOException {
		if (unpackDir == null || !unpackDir.isDirectory()) {
			throw new MojoExecutionException("'unpackDir' needs to be specified as a valid directory.");
		}
		
    	JarInputStream jis = new JarInputStream(new FileInputStream(new File(inputJar)));
		JarEntry je = null;
		while ((je = jis.getNextJarEntry()) != null) {
			if (je.isDirectory()) {
				continue;
			}
			
			int bytes = 0;
			File unzippedFile = new File(unpackDir, je.getName());
			new File(unzippedFile.getParent()).mkdirs();
			FileOutputStream fos = new FileOutputStream(unzippedFile);
			byte[] buffer = new byte[10];
			while ((bytes = jis.read(buffer, 0, buffer.length)) > 0) {
				for (int i = 0; i < bytes; i++) {
					fos.write((byte) buffer[i]);
				}
			}
			fos.close();
		}
		jis.close();
	}
	
	/**
	 * Creates a semi-colon seperated string based on a list of runtime classpath elements.
	 * 
	 * @param runtimeClasspath
	 * @return String
	 */
	private String runtimeClassPathAsString(List runtimeClasspath) {
		StringBuffer buffer = new StringBuffer();
		for (Object object : runtimeClasspath) {
			String classpathElement = (String) object;
			buffer.append(classpathElement);
			buffer.append(";");
		}
		
		return buffer.toString();
	}
	
	/**
	 * Returns a String containing the java executables with all its arguments.
	 * 
	 * @return String
	 * @throws DependencyResolutionRequiredException
	 */
	private String getJavaExecutable() throws DependencyResolutionRequiredException {
		return 	wasJavaHome.toString()+File.separatorChar+
				"bin"+File.separatorChar+
				"java "+getJvmArgs();
	}
	
	/**
	 * Returns a String containing the Java VM argument line
	 * 
	 * @return
	 * @throws DependencyResolutionRequiredException
	 */
    private String getJvmArgs() throws DependencyResolutionRequiredException {
        return 	"-classpath "+ejbdCp+
                " -Xbootclasspath/a:"+bootPath+
                " -Xj9"+
                " -Xquickstart"+
                " -Xverify:none"+
                " -Xms256M"+
                " -Xmx256M"+
                " -Dorg.osgi.framework.bootdelegation=*"+
                " -Ditp.loc=\""+itpLoc+"\""+
                " -Dwas.install.root=\""+ wasHome +"\""+
                " -Dwebsphere.lib.dir=\""+ wasHome +File.separatorChar+"lib\""+
                " -Dws.ext.dirs=\""+ wasHome +File.separatorChar+"eclipse"+File.separatorChar+"plugins"+File.separatorChar+"j2ee.javax_1.4.0\";\""+ wasHome +File.separatorChar+"eclipse"+File.separatorChar+"plugins"+File.separatorChar+"com.ibm.ws.runtime.eclipse_1.0.0\";\""+wasExtDirs+"\""+
                " -Dcom.ibm.sse.model.structuredbuilder=\"off\""+
                " com.ibm.etools.ejbdeploy.EJBDeploy "+getEjbDeployArgs();
    }
	
	/**
	 * Returns a String containing the ejb deploy tool argument line. 
	 * 
	 * @return String
	 * @throws DependencyResolutionRequiredException
	 */
	private String getEjbDeployArgs() throws DependencyResolutionRequiredException {
		return 	inputJar.toString()+" "+
				workingDir.toString()+" "+
				outputJar.toString()+" "+
				"-cp \""+runtimeClassPathAsString(getProject().getRuntimeClasspathElements())+"\"";
	}
}