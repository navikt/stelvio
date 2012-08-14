package no.nav.maven.plugin.batchclientplugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import no.nav.maven.common.ProjectUtil;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.versioning.OverConstrainedVersionException;
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
	 * The resulting stopbatch file.
	 * 
	 * @parameter
	 */
	private File outputStopFile;
	
	/**
	 * The resulting stopbatch file.
	 * 
	 * @parameter
	 */
	private Boolean createStopFile;
	
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
	 * The Depenendcies folder for WAS_BOOTCLASSPATH
	 * 
	 * @parameter
	 * @required
	 */
	private List dependencies;
	
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
		if(createStopFile != null && createStopFile == true) {
			writeStopBatchFile();
		}
			
	}
	
	private void writeStartBatchFile() throws MojoFailureException {
		
		try{
			if(outputStartFile.getParent() != null){
				File f = new File(outputStartFile.getParent());
				if(!f.exists()){
					f.mkdir();
				}
			}
			FileWriter fw = new FileWriter(outputStartFile);
			fw.write("#!/bin/bash\n");
			fw.write("cd $(dirname \"${BASH_SOURCE[0]}\")\n");
			fw.write("source ./" + outputConfigFile.getName() + "\n");
			fw.write("\n");
			fw.write("${JAVA_HOME}/bin/java ${SECURITY_JVM_PARAMS} ${CONSOLE_ENCODING} -Dwas.install.root=${WAS_HOME} -Djava.ext.dirs=${WAS_EXT_DIRS} -Xbootclasspath/p:${WAS_BOOTCLASSPATH} -classpath ${APP_CLASSPATH} " + javaClass + " $*\n");
			fw.flush();
			fw.close();
			
		} catch( IOException fe){
			fe.printStackTrace();
			throw new MojoFailureException("Could not create StartBatch File:" + outputStartFile);
		}
	}
	
	private void writeStopBatchFile() throws MojoFailureException {
		
		try{
			if(outputStopFile.getParent() != null){
				File f = new File(outputStopFile.getParent());
				if(!f.exists()){
					f.mkdir();
				}
			}
			FileWriter fw = new FileWriter(outputStopFile);
			fw.write("#!/bin/bash\n");
			fw.write("cd $(dirname \"${BASH_SOURCE[0]}\")\n");
			fw.write("source ./" + outputConfigFile.getName() + "\n");
			fw.write("\n");
			fw.write("${JAVA_HOME}/bin/java ${SECURITY_JVM_PARAMS} ${CONSOLE_ENCODING} -Dwas.install.root=${WAS_HOME} -Djava.ext.dirs=${WAS_EXT_DIRS} -Xbootclasspath/p:${WAS_BOOTCLASSPATH} -classpath ${APP_CLASSPATH} " + javaClass + " $1 $2 stop\n");
			fw.flush();
			fw.close();
			
		} catch( IOException fe){
			fe.printStackTrace();
			throw new MojoFailureException("Could not create StopBatch File:" + outputStopFile);
		}
	}
	
	private String getAddtionalBootString(){
		StringBuffer buffer = new StringBuffer();
		buffer.append(delimiter);
		  if(dependencies != null)
          {
              for(Iterator i = dependencies.iterator(); i.hasNext();)
              {
            	  
                  ArtifactItem artifactItem = (ArtifactItem)i.next();
                  Artifact artifact = ProjectUtil.getArtifact(project, artifactItem.getGroupId(), artifactItem.getArtifactId());
                  if(artifact != null)
                  {
                	  buffer.append(clientClasspathPrefix + artifact.getArtifactId() + "-" + artifact.getVersion() + "." + artifact.getType() + delimiter);
                  	} else
                  {
                  		
                  }
              }
          }
		  buffer = buffer.deleteCharAt(buffer.length()-1);
		return buffer.toString();

	}
	
	private void writeConfigFile() throws MojoFailureException {
		try{
			
			if(outputConfigFile.getParent() != null){
				File f = new File(outputConfigFile.getParent());
				if(!f.exists()){
					f.mkdir();
				}
			}
			FileWriter fw = new FileWriter(outputConfigFile);
			
			fw.write("#!/bin/bash\n");
			fw.write("\n");
			fw.write("WAS_HOME=" + wasHome + "\n");
			fw.write("PROFILE_PROPS=\"${WAS_HOME}/profiles/AppSrv01/properties\"\n");
			fw.write("JAVA_JRE=\"${WAS_HOME}/java/jre\"\n");
			fw.write("JAVA_JDK=\"${WAS_HOME}/java\"\n");
			fw.write("\n");
			fw.write("echo \"WAS_HOME: ${WAS_HOME}\"\n");
			fw.write("echo \"JAVA_JRE: ${JAVA_JRE}\"\n");
			fw.write("echo \"JAVA_JDK: ${JAVA_JDK}\"\n");
			fw.write("\n");
			fw.write("JAVA_HOME=$JAVA_JRE\n");
			fw.write("WAS_EXT_DIRS=\"${JAVA_HOME}/lib/ext" + delimiter + "${JAVA_HOME}/lib" + delimiter + "${WAS_HOME}/lib" + delimiter + "${WAS_HOME}/lib/ext" + delimiter + "${WAS_HOME}/properties" + delimiter + "${WAS_HOME}/plugins\"\n");
			fw.write("WAS_BOOTCLASSPATH=\"${JAVA_HOME}/lib/ibmorb.jar" + delimiter + "${WAS_HOME}/properties" + delimiter + "${JAVA_HOME}/lib/core.jar" + delimiter + "${JAVA_JDK}/src.jar" + getAddtionalBootString() +  "\"\n");
			fw.write("CONSOLE_ENCODING=" + consoleEncoding + "\n");
			fw.write("\n");
			fw.write("CONFIG_PATH=" + configPath +"\n");
			fw.write("CLIENT_CLASSPATH=" + getClasspathString() + "\n");
			fw.write("\n");
			fw.write("APP_CLASSPATH=\"${CONFIG_PATH}" + delimiter + "${CLIENT_CLASSPATH}\"\n");
			fw.write("\n");
			fw.write("echo \"JAVA_HOME: ${JAVA_HOME}\"\n");
			fw.write("echo \"WAS_EXT_DIRS: ${WAS_EXT_DIRS}\"\n");
			fw.write("echo \"WAS_BOOTCLASSPATH: ${WAS_BOOTCLASSPATH}\"\n");
			fw.write("\n");
			fw.write("#######################\n");
			fw.write("# SECURITY PARAMETERS #\n");
			fw.write("#######################\n");
			fw.write("CLIENTSAS=\"-Dcom.ibm.CORBA.ConfigURL=file:${PROFILE_PROPS}/sas.client.props\"\n");
			fw.write("CORBA_LOGIN_SOURCE=\"-Dcom.ibm.CORBA.loginSource=properties\"\n");
			fw.write("CLIENTSSL=\"-Dcom.ibm.SSL.ConfigURL=file:${PROFILE_PROPS}/ssl.client.props\"\n");
			fw.write("JAASSOAP=\"-Djava.security.auth.login.config=file:${PROFILE_PROPS}/wsjaas_client.conf\"\n");
			fw.write("SSL_LIB=\"${JAVA_JRE}/lib/ext\"\n");
			fw.write("WAS_EXT_DIRS=\"${WAS_EXT_DIRS}" + delimiter + "${SSL_LIB}\"\n");
			fw.write("\n");
			fw.write("counter=0\n");
			fw.write("userNameArg=\"\"\n");
			fw.write("args=(\"$@\")\n");
			fw.write("\n");
			fw.write("for arg in $*;\n");
			fw.write("do\n");
			fw.write("\n");
			fw.write("if [ \"$arg\" = \"-username\" ]\n");	
			fw.write("then\n");	
			fw.write("userNameArg=${args[$counter + 1]}\n");	
			fw.write("fi\n");	
			fw.write("if [ \"$arg\" = \"-password\" ]\n");	
			fw.write("then\n");
			fw.write("pwdArg=${args[$counter + 1]}\n");	
			fw.write("fi\n");	
			fw.write("((counter++))\n");	
			fw.write("done\n");	
			fw.write("\n");	
			fw.write("if [ \"$userNameArg\" = \"\" ]\n");	
			fw.write("then\n");	
			fw.write("echo \"WARNING: No username or password supplied. Invocation will not succeed if security is enabled on the target server.\"\n");	
			fw.write("echo \"Security options: -username <username> -password <password>\"\n");	
			fw.write("else\n");	
			fw.write("echo \"Starting script with user: $userNameArg\"\n");	
			fw.write("fi\n");
			fw.write("\n");
			fw.write("CORBA_USERNAME=\"-Dcom.ibm.CORBA.loginUserid=${userNameArg}\"\n");
			fw.write("CORBA_PWD=\"-Dcom.ibm.CORBA.loginPassword=${pwdArg}\"\n");
			fw.write("\n");
			fw.write("SECURITY_JVM_PARAMS=\"${JAASSOAP} ${CLIENTSSL} ${CLIENTSAS} ${CORBA_USERNAME} ${CORBA_PWD} ${CORBA_LOGIN_SOURCE}\"\n");
			
			
		
			fw.flush();
			fw.close();
	
		} catch( IOException fe){
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
				String classpathElement;				
				try {
				if(a.isSnapshot() && (a.getType().equals("ejb-client"))){
					classpathElement = clientClasspathPrefix + a.getArtifactId() + "-" + a.getSelectedVersion() + "-client.jar";
				} else if(a.isSnapshot() && (a.getType().equals("ejb"))){
					classpathElement = clientClasspathPrefix + a.getArtifactId() + "-" + a.getSelectedVersion() + "-client.jar";
				} else if( a.isSnapshot()){
					classpathElement = clientClasspathPrefix + a.getArtifactId() + "-" + a.getSelectedVersion() + "." + a.getType();
				} else if( a.getType().equals("ejb-client")){
					classpathElement = clientClasspathPrefix + a.getArtifactId() + "-" + a.getVersion() + "-client.jar";
				} else if( a.getType().equals("ejb")){
					classpathElement = clientClasspathPrefix + a.getArtifactId() + "-" + a.getVersion() + "-client.jar";
				}else {
					classpathElement = clientClasspathPrefix + a.getArtifactId() + "-" + a.getVersion() + "." + a.getType();	
				}
				buffer.append(classpathElement);
				buffer.append(delimiter);
				} catch (OverConstrainedVersionException e){
					System.out.println("Problems when running getSelectedVersion on Artifact:" + a.getArtifactId());
					e.printStackTrace();
				} catch (Exception e){
					System.out.println("Problems when running getSelectedVersion on Artifact:" + a.getArtifactId());
					e.printStackTrace();
				}
				
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