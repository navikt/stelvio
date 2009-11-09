package no.nav.maven.plugins;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.handler.ArtifactHandler;
import org.codehaus.plexus.util.Os;
import org.apache.maven.artifact.handler.manager.ArtifactHandlerManager;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.UnArchiver;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.codehaus.plexus.util.cli.Commandline;

import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

/**
 * Goal which touches a timestamp file.
 *
 * @goal generatejava
 * 
 * @phase process-sources
 * @requiresDependencyResolution 
 */
public class GenerateJavaMojo
    extends AbstractMojo
{
	private static final String ZIP_SUFFIX = "zip";

	private static final String WAR_SUFFIX = "war";

	private static final String WSDL_PATH_IN_WAR = "/META-INF/wsdl";

	private static final String WSDL_INTERFACE_ARTIFACT_TYPE = "wsdl-interface";

	/**
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

	
	/**
	 * @parameter expression="${wid.runtime}"
	 * @required
	 * @readonly
	 */
	private String widRuntime;
	/**
	 * @component
	 */
	private MavenProjectHelper projectHelper;

	/**
	 * @component roleHint="zip"
	 * @required
	 * @readonly
	 */
	private Archiver archiver;

	/**
	 * @component roleHint="wsdl-interface"
	 */
	private ArtifactHandler wsdlIfArtifactHandler;

	/**
	 * @component
	 */
	private ArtifactHandlerManager artifactHandlerManager;

	/**
	 * @component roleHint="zip"
	 * @required
	 * @readonly
	 */
	private UnArchiver unArchiver;


	/**
	 * EAR-file.
	 * 
	 * @parameter expression="${project.artifact.file}"
	 */
	private File earFile;
	
//	/**
//	 * @parameter expression="${project.dependencies}"
//	 * @required
//	 */
//	protected Set<Artifact> dependencyArtifacts;


	/**
     * Location of the file.
     * @parameter expression="${project.build.directory}"
     * @required
     */
    private File outputDirectory;
    

    public void execute()
        throws MojoExecutionException
    {    	
		if (!wsdlIfArtifactHandler.equals(artifactHandlerManager
				.getArtifactHandler(WSDL_INTERFACE_ARTIFACT_TYPE))) {
			getLog().debug("Adding project interchange artifact handler to artifact handler manager");
			artifactHandlerManager.addHandlers(Collections.singletonMap(WSDL_INTERFACE_ARTIFACT_TYPE,
					wsdlIfArtifactHandler));
		}
		unArchiver.setSourceFile(getWSDLIfArtifact(project).getFile());
		String tempDir = project.getBuild().getDirectory() + "/wsdltemp/"
				+ System.currentTimeMillis() + "/";
		File tempDirfile = new File(tempDir);
		tempDirfile.mkdirs();

		unArchiver.setDestDirectory(tempDirfile);
		try {
			unArchiver.extract();
		} catch (Exception e) {
			throw new RuntimeException("Unable to unzip ear file "
					+ earFile.getPath() + " to directory "
					+ unArchiver.getDestDirectory(), e);
		}
		
		
		
    	String exec;
    	if(Os.isFamily("windows")) {
			exec=widRuntime + "/bin/WSDL2Java.bat";
		} else {
			exec=widRuntime + "/bin/WSDL2Java.sh";
		}	
    	//String wsdlPath = getWSDLIfArtifact(project).getFile().getAbsolutePath();
    	//commandLine=exec+" -r deploy-client -c web -o e:\\tmp -j Overwrite -D -f namespacemapping.properties \""+wsdlPath+"\"";
    	//System.out.println(commandLine);
		List<File> wsdlFiles = listFilesRecursive(tempDirfile, new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith("WSEXP.wsdl");
			}
		});		
		for (File wsdlFile:wsdlFiles ){    	
			Commandline commandLine = new Commandline();
			Commandline.Argument arg = new Commandline.Argument();
			arg.setLine("-r deploy-client -c web -o e:\\tmp -j Overwrite -D -f "+tempDirfile.getAbsolutePath()+"\\NStoPkg.properties \""+wsdlFile.getAbsolutePath()+"\"");
			commandLine.setExecutable(exec);
			commandLine.addArg(arg);
			arg = new Commandline.Argument();		
			executeCommand(commandLine);
		}
		
		
		
    	//Cli.runCommandLine(commandLine, "Error");
    	
       //CLI
    	//TODO: generer java med cli og følgende kommandolinje:
    	//		<exec dir="${wsdlGenerateJava.dir}" executable="${wsdlGenerateJava.executable}" failonerror="true">
		//	<arg line="-r deploy-client -c web -o e:\tmp -j Overwrite -D -f namespacemapping.properties *.wsdl" />
		// </exec>
    	
    }
    
    private List<File> listFilesRecursive(File tempDirfile, FilenameFilter filter) {
		List<File> foundFiles=new ArrayList<File>();	
		for(File f:tempDirfile.listFiles(filter)){
			foundFiles.add(f);
		}		
		File[] subdirs = tempDirfile.listFiles(new FileFilter(){
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}		
		});
		for (File subdir:subdirs){
			foundFiles.addAll(listFilesRecursive(subdir, filter));
		}
		return foundFiles;
	}

	public Artifact getWSDLIfArtifact(MavenProject project) {
		String wsdlIfClassifier = wsdlIfArtifactHandler.getClassifier();

		//Collection<Artifact> artifacts = new ArrayList<Artifact>();
		
		//Collection<Artifact> attachedArtifacts = dependencyArtifacts;
		 
//		for (Dependency d:(List<Dependency>)project.getDependencies())		
//			ArrayList<Artifact> dependencyArtifacts = (ArrayList<Artifact>)
		Set artifacts=project.getDependencyArtifacts();
			for (Iterator artifactIterator=artifacts.iterator();artifactIterator.hasNext();) {
				Artifact artifact=(Artifact)artifactIterator.next();
				System.out.println(artifact.getClassifier());
				if (wsdlIfClassifier.equals(artifact.getClassifier())) {
					return artifact;
				}
			}
		
		
		throw new RuntimeException("No attached artifact of type wsdl-interface (classifier '"+wsdlIfClassifier+"') found ");
    }
    
	protected final void executeCommand(Commandline command) {
		try {
			getLog().info("Executing the following command: " + command.toString());

			StreamConsumer systemOut = new StreamConsumer() {
				public void consumeLine(String line) {
					getLog().info(line);
				}
			};
			StreamConsumer systemErr = new StreamConsumer() {
				public void consumeLine(String line) {
					getLog().error(line);
				}
			};
			ErrorCheckingStreamConsumer errorChecker = new ErrorCheckingStreamConsumer();

			CommandLineUtils.executeCommandLine(command, new StreamConsumerChain(systemOut).add(errorChecker),
					new StreamConsumerChain(systemErr).add(errorChecker));

			if (errorChecker.isError()) {
				
				throw new RuntimeException("An error occured during deploy. Stopping deployment. Consult the logs.");
				
			}
		} catch (CommandLineException e) {
			throw new RuntimeException("An error occured executing: " + command, e);
		}
	}
	private static class StreamConsumerChain implements StreamConsumer {
		private final Collection<StreamConsumer> chain = new ArrayList<StreamConsumer>();

		public StreamConsumerChain() {
		}

		public StreamConsumerChain(StreamConsumer streamConsumer) {
			add(streamConsumer);
		}

		public StreamConsumerChain add(StreamConsumer streamConsumer) {
			chain.add(streamConsumer);
			return this;
		}

		public void consumeLine(String line) {
			for (StreamConsumer streamConsumer : chain) {
				streamConsumer.consumeLine(line);
			}
		}
	}

	private static class ErrorCheckingStreamConsumer implements StreamConsumer {
		private boolean error;

		public void consumeLine(String line) {
			if (line.toLowerCase().contains("error")) {
				error = true;
			}
		}

		public boolean isError() {
			return error;
		}
	}
}
