package no.nav.maven.plugin.batchclientplugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;

/**
 * @author person59d660d1d1a2 Gustavsen, Accenture
 * @version $id$
 * @goal create-scripts
 */
public class BatchClientBuilderMojo extends AbstractMojo {

	/**
	 * The maven project.
	 * 
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

	/**
	 * The maven project's helper.
	 * 
	 * @parameter expression="${component.org.apache.maven.project.MavenProjectHelper}"
	 * @required
	 * @readonly
	 */
	private MavenProjectHelper projectHelper;

	/**
	 * The resulting config file.
	 * 
	 * @parameter
	 * @required
	 */
	private File outputConfigFile;
	
	/**
	 * The resulting startbatch file.
	 * 
	 * @parameter
	 * @required
	 */
	private File outputStartFile;
	
	/**
	 * The WAS_HOME folder for client classpath
	 * 
	 * @parameter
	 * @required
	 */
	private String wasHome;
	
	/**
	 * The CONSOLE_ENCODING parameter. Ex."-Dws.output.encoding=console"
	 * 
	 * @parameter
	 * @required
	 */
	private String consoleEncoding;
	
	/**
	 * The CONFIG_PATH folder for config path
	 * 
	 * @parameter
	 * @required
	 */
	private String configPath;
	
	/**
	 * The client classpath prefix. Used to construct the classpath.
	 * 	 * 
	 * @parameter
	 * @required
	 */
	private String clientClasspathPrefix;
	
	/**
	 * The classpath deilimiter. To use for Windows/Linux
	 * 	 * 
	 * @parameter
	 * @required
	 */
	private String delimiter;
	
	/**
	 * The java class to run.
	 * 	 * 
	 * @parameter
	 * @required
	 */
	private String javaClass;
	

	public void execute() throws MojoFailureException {
		printInput();
		writeConfigFile();
		writeStartBatchFile();
			
	}
	
	private void writeStartBatchFile() throws MojoFailureException {
		
		try{
			PrintWriter pw = new PrintWriter(outputStartFile);
			pw.println("#!/bin/bash");
			pw.println("source " + outputConfigFile.getName());
			pw.println("");
			pw.println("${JAVA_HOME}/bin/java ${CONSOLE_ENCODING} -Dwas.install.root=${WAS_HOME} -Djava.ext.dirs=${WAS_EXT_DIRS} -Xbootclasspath/p" + delimiter + "${WAS_BOOTCLASSPATH} -classpath ${APP_CLASSPATH} " + javaClass + " $1 $2");
			pw.flush();
			pw.close();
		} catch( FileNotFoundException fe){
			fe.printStackTrace();
			throw new MojoFailureException("Could not create StartBatch File:" + outputStartFile);
		}
	}
	
	private void writeConfigFile() throws MojoFailureException {
		try{
			PrintWriter pw = new PrintWriter(outputConfigFile);
			pw.println("#!/bin/bash");
			pw.println("");
			pw.println("WAS_HOME=" + wasHome);
			pw.println("JAVA_JRE=\"${WAS_HOME}/java/jre\"");
			pw.println("JAVA_JDK=\"${WAS_HOME}/java\"");
			pw.println("");
			pw.println("echo \"WAS_HOME: ${WAS_HOME}\"");
			pw.println("echo \"JAVA_JRE: ${JAVA_JRE}\"");
			pw.println("echo \"JAVA_JDK: ${JAVA_JDK}\"");
			pw.println("");
			pw.println("JAVA_HOME=$JAVA_JRE");
			pw.println("WAS_EXT_DIRS=\"${JAVA_HOME}/lib/ext" + delimiter + "${JAVA_HOME}/lib" + delimiter + "${WAS_HOME}/lib" + delimiter + "${WAS_HOME}/lib/ext" + delimiter + "${WAS_HOME}/properties" + delimiter + "${WAS_HOME}/plugins\"");
			pw.println("WAS_BOOTCLASSPATH=\"${JAVA_HOME}/lib/ibmorb.jar" + delimiter + "${WAS_HOME}/properties" + delimiter + "${JAVA_HOME}/lib/core.jar" + delimiter + "${JAVA_JDK}/src.jar\"");
			pw.println("CONSOLE_ENCODING=" + consoleEncoding);
			pw.println("");
			pw.println("CONFIG_PATH=" + configPath);
			pw.println("CLIENT_CLASSPATH=" + getClasspathString());
			pw.println("");
			pw.println("APP_CLASSPATH=\"${CONFIG_PATH}" + delimiter + "${CLIENT_CLASSPATH}\"");
			pw.println("");
			pw.println("echo \"JAVA_HOME: ${JAVA_HOME}\"");
			pw.println("echo \"WAS_EXT_DIRS: ${WAS_EXT_DIRS}\"");
			pw.println("echo \"WAS_BOOTCLASSPATH: ${WAS_BOOTCLASSPATH}\"");
			pw.println("echo \"APP_CLASSPATH: ${APP_CLASSPATH}\"");
			
		
			pw.flush();
			pw.close();
		} catch( FileNotFoundException fe){
			fe.printStackTrace();
			throw new MojoFailureException("Could not create config File:" + outputConfigFile);
		}
	}

	/**
	 * Print Input.
	 */
	private void printInput() {
		System.out.println("OutputConfigFile " + outputConfigFile);
		System.out.println("OutputStartFile " + outputStartFile);
		System.out.println("clientClasspathPrefix " + clientClasspathPrefix);
		System.out.println("configPath " + configPath);
		System.out.println("consoleEncoding " + consoleEncoding);
		System.out.println("wasHome " + wasHome);
	}
	
	private String getClasspathString(){
		List list  = getProject().getRuntimeArtifacts();	
		return runtimeClassPathAsString(list);
	}
	
	private String runtimeClassPathAsString(List runtimeArtifacts) {
		StringBuffer buffer = new StringBuffer();
		for (Object object : runtimeArtifacts) {
			if (object instanceof Artifact) {
				Artifact a = (Artifact)object;
				String classpathElement = clientClasspathPrefix + a.getArtifactId() + "-" + a.getVersion() + "." + a.getType();
				buffer.append(classpathElement);
				buffer.append(delimiter);
			}		
		}
		buffer = buffer.deleteCharAt(buffer.length()-1);
		return "\"" + buffer.toString() + "\"";
	}

	/**
	 * @return the project
	 */
	public MavenProject getProject() {
		return project;
	}

	/**
	 * @param project
	 *            the project to set
	 */
	public void setProject(MavenProject project) {
		this.project = project;
	}

	/**
	 * @return the projectHelper
	 */
	public MavenProjectHelper getProjectHelper() {
		return projectHelper;
	}

	/**
	 * @param projectHelper
	 *            the projectHelper to set
	 */
	public void setProjectHelper(MavenProjectHelper projectHelper) {
		this.projectHelper = projectHelper;
	}

}