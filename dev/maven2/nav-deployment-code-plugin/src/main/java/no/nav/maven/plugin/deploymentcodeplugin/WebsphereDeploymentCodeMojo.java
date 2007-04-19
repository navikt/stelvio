package no.nav.maven.plugin.deploymentcodeplugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.FileUtils;

/**
 * Mojo that generates EJB deployment code using IBM Websphere 6.1 deploy tool.
 * Some variables use default values that should be defined in "settings.xml".
 * 
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 * @goal websphere-deployment-code
 */
public class WebsphereDeploymentCodeMojo extends AbstractDeploymentCodeMojo {
	/**
	 * @parameter
	 */
	private File inputJar;
	
	/**
	 * @parameter
	 */
	private File outputJar;
	
	/**
	 * @parameter
	 */
	private File workingDir;
	
	/**
	 * 
	 * @parameter expression="${was.home}"
	 */
	private File was61Home;
	
	/**
	 * @parameter expression="${itp.loc}"
	 */
	private File itpLoc;
	
	/**
	 * @parameter expression="${was.java.home}"
	 */
	private File javaHome;
	
	/**
	 * @parameter expression="${boot.path}"
	 */
	private String bootPath;
	
	/**
	 * @parameter expression="${ejbd.cp}"
	 */
	private String ejbdCp;
	
	/**
	 * @parameter expression="${was.ext.dirs}"
	 */
	private String wasExtDirs;
	
	/**
	 * @parameter
	 */
	private boolean keepEjbDeployDir;
	
	/**
	 * @parameter
	 */
	private boolean unpackCode;
	
	/**
	 * Method that is executed when plugin is started.
	 */
	public void execute() throws MojoExecutionException, MojoFailureException {
		init();
		
		try {
			// Execute the ejb deploy tool in a separate JVM
			Process process = Runtime.getRuntime().exec(getJavaExecutable());
			
			// Print ejb deploy tool log
			logStreams(process.getInputStream(), process.getErrorStream());

			// Make final preperations to the resulting artifacts
			finalizeArtifacts();
		}
		catch (DependencyResolutionRequiredException e) {
			throw new MojoExecutionException("An error occured while setting ejbdeploy arguments.", e);
		}
		catch (IOException e) {
			throw new MojoExecutionException("An error occured while executing process", e);
		}
	}
	
	/**
	 * Initiates variables.
	 */
	private void init() {
		if (inputJar == null) {
			inputJar = new File(getProject().getBuild().getDirectory()+
								File.separatorChar+
								getProject().getArtifactId()+"-"+getProject().getVersion()+".jar");
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
	}
	
	/**
	 * Make final preperations to the resulting artifacts.
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
			// TODO: Unpack files here
		}
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
	 * Dumps ejb deploy tool to maven log.
	 * 
	 * @param stdOut
	 * @param stdErr
	 * @throws IOException
	 */
	private void logStreams(InputStream stdOut, InputStream stdErr) throws IOException {
		BufferedReader bro = new BufferedReader(new InputStreamReader(stdOut));
		String line;
		while ((line = bro.readLine()) != null) {
			getLog().info(line);
		}
		
		BufferedReader bre = new BufferedReader(new InputStreamReader(stdErr));
		while ((line = bre.readLine()) != null) {
			getLog().info(line);
		}
	}
	
	/**
	 * Returns a String containing the java executables with all its arguments.
	 * 
	 * @return String
	 * @throws DependencyResolutionRequiredException
	 */
	private String getJavaExecutable() throws DependencyResolutionRequiredException {
		return 	javaHome.toString()+File.separatorChar+
				"bin"+File.separatorChar+
				"java "+getJvmArgs();
	}
	
	/**
	 * Returns a String containing the Java VM argument line
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
				" -Ditp.loc=\""+itpLoc+"\""+
				" -Dwas.install.root=\""+was61Home+"\""+
				" -Dwebsphere.lib.dir=\""+was61Home+File.separatorChar+"lib\""+
				" -Dws.ext.dirs=\""+was61Home+File.separatorChar+"eclipse"+File.separatorChar+"plugins"+File.separatorChar+"j2ee.javax_1.4.0\";\""+was61Home+File.separatorChar+"eclipse"+File.separatorChar+"plugins"+File.separatorChar+"com.ibm.ws.runtime.eclipse_1.0.0\";\""+wasExtDirs+"\""+
				" -Dcom.ibm.sse.model.structuredbuilder=\"off\""+
				" com.ibm.etools.ejbdeploy.EJBDeploy "+getEjbDeployArgs();
	}
	
	/**
	 * Returns a String containing the ejb deploy tool argument line. 
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