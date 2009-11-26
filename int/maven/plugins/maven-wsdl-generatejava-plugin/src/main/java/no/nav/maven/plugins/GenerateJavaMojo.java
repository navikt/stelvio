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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.artifact.handler.manager.ArtifactHandlerManager;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

/**
 * Goal which touches a timestamp file.
 * 
 * @goal generate
 * 
 * @phase generate-sources
 * @requiresDependencyResolution compile
 */
public class GenerateJavaMojo extends AbstractMojo {

	private static final String WSDL_INTERFACE_ARTIFACT_TYPE = "wsdl-interface";

	protected static final String WSDLEXPORT_SUFFIX = "wsdl";
	protected static final String WSDLEXPORT_TOKEN = "WSEXP";

	/**
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

	/**
	 * @parameter expression="${was.home}"
	 * @readonly
	 */
	private String wasRuntime;
	
	/**
	 * @parameter expression="${wid.home}"
	 * @readonly
	 */
	private String widRuntime;

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
	 * Source Dir
	 * 
	 * @parameter expression="${project.build.directory}"
	 * @required
	 */
	private File buildDirectory;

	public void execute() throws MojoExecutionException {
		try{
			// Necessary hack. Or so is the rumor, at least.
			if (!wsdlIfArtifactHandler.equals(artifactHandlerManager
					.getArtifactHandler(WSDL_INTERFACE_ARTIFACT_TYPE))) {
				getLog()
						.debug(
								"Adding project interchange artifact handler to artifact handler manager");
				artifactHandlerManager.addHandlers(Collections.singletonMap(
						WSDL_INTERFACE_ARTIFACT_TYPE, wsdlIfArtifactHandler));
			}
			String genDirectory = buildDirectory.getAbsolutePath() + "/genWSDL";
	
			// First unpack the wsdl-artifact from the dependency
			long timestamp = System.currentTimeMillis();
			for(Artifact artifact:getWSDLIfArtifacts(project)){
				File wsdlArtifactFile = artifact.getFile();
				unArchiver.setSourceFile(wsdlArtifactFile);
				String tempDir = project.getBuild().getDirectory() + "/wsdltemp_"+artifact.getArtifactId()+"_"
						+ timestamp + "/";
				File tempDirfile = new File(tempDir);
				tempDirfile.mkdirs();
				unArchiver.setDestDirectory(tempDirfile);
				try {
					unArchiver.extract();
				} catch (Exception e) {
					throw new RuntimeException("Unable to unzip wsdl artifact file "
							+ wsdlArtifactFile.getPath() + " to directory "
							+ unArchiver.getDestDirectory(), e);
				}
				
				//Generate the NStoPkg.properties-file that will make sensible packages for the wsdl  
				Map<String, String> namespaceToPackageMap = createNameSpaceToPackageMapFromWSDLDirectory(tempDirfile);
				File nameSpaceToPackageFile = new File(tempDir, /*project.getBuild().getFinalName()
						+ "-*/"NStoPkg.properties");
				PrintWriter pw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(nameSpaceToPackageFile)));
				for (String namespace : namespaceToPackageMap.keySet()) {
					pw.write(namespace + "=" + namespaceToPackageMap.get(namespace) + "\n");
				}
				pw.close();
		
		
				// Next, call the WSDL2Java script for all wsdl-files in the artifact
				String exec;
				if (wasRuntime==null){
					wasRuntime=widRuntime+"/runtimes/bi_v61";
				}
				if (Os.isFamily("windows")) {
					exec = wasRuntime + "/bin/WSDL2Java.bat";
				} else {
					exec = wasRuntime + "/bin/WSDL2Java.sh";
				}
		
				List<File> wsdlFiles = listFilesRecursive(tempDirfile,
						new FilenameFilter() {
							public boolean accept(File dir, String name) {
								return name.contains(WSDLEXPORT_TOKEN) && name.endsWith(WSDLEXPORT_SUFFIX);
							}
						});
		
				
				

				for (File wsdlFile : wsdlFiles) {
					Commandline commandLine = new Commandline();
					Commandline.Argument arg = new Commandline.Argument();
					arg.setLine("-role client -container none -o \"" + genDirectory
							+ "\" -j Overwrite -f \"" + nameSpaceToPackageFile.getAbsolutePath()+"\" \"" + wsdlFile.getAbsolutePath()
							+ "\"");
					commandLine.setExecutable(exec);
					commandLine.addArg(arg);
					arg = new Commandline.Argument();
					executeCommand(commandLine);
				}
			}
			project.addCompileSourceRoot(genDirectory);
		}catch(RuntimeException re){
			throw re;
		}catch(Exception e)	{
			new MojoExecutionException("Unable to generate Java",e);
		}

	}

	/**
	 * Recursive variant of regular listFiles()-method
	 * 
	 * @param dirToTraverse
	 * @param filter
	 * @return files matching filter in all subdirectories of dirToTraverse
	 */
	private List<File> listFilesRecursive(File dirToTraverse,
			FilenameFilter filter) {
		List<File> foundFiles = new ArrayList<File>();
		for (File f : dirToTraverse.listFiles(filter)) {
			foundFiles.add(f);
		}
		File[] subdirs = dirToTraverse.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		});
		for (File subdir : subdirs) {
			foundFiles.addAll(listFilesRecursive(subdir, filter));
		}
		return foundFiles;
	}

	/**
	 * @param project
	 * @return the first wsdl-if artifact found in the dependency list
	 */
	public List<Artifact> getWSDLIfArtifacts(MavenProject project) {
		String wsdlIfClassifier = wsdlIfArtifactHandler.getClassifier();
		ArrayList<Artifact> artifactList=new ArrayList<Artifact>();
		Set artifacts = project.getDependencyArtifacts();
		for (Iterator artifactIterator = artifacts.iterator(); artifactIterator
				.hasNext();) {
			Artifact artifact = (Artifact) artifactIterator.next();
			System.out.println(artifact.getClassifier());
			if (wsdlIfClassifier.equals(artifact.getClassifier())) {
				artifactList.add(artifact);
			}
		}
		if (artifactList.size()==0){
			throw new RuntimeException(
				"No attached artifact of type wsdl-interface (classifier '"
						+ wsdlIfClassifier + "') found ");
		}
		return artifactList;
	}
	
	public Map<String, String> createNameSpaceToPackageMapFromWSDLDirectory(File wsdlDirectory) {
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			createNameSpaceToPackageMap(wsdlDirectory, map);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return map;
	}

	private void createNameSpaceToPackageMap(File file, Map<String, String> nameSpaceMap) throws IOException {
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				createNameSpaceToPackageMap(f, nameSpaceMap);
			}
		} else {
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));

			final byte[] bytes = new byte[(int) file.length()];
			bis.read(bytes);
			bis.close();
			String fileString = new String(bytes);
			Pattern p = Pattern.compile("\"http://([^\"]+)\"");
			Matcher m = p.matcher(fileString);
			while (m.find()) {
				String nameSpace = m.group(1);
				String packageName = generatePackageNameFromNamespace(nameSpace);
				if (packageName != null) {
					String escapedNameSpaceUrl = "http\\://" + nameSpace;
					nameSpaceMap.put(escapedNameSpaceUrl, packageName);
				}
			}
		}

	}

	private String generatePackageNameFromNamespace(String nameSpace) {
		String[] parts = nameSpace.split("/");
		// Check if this is something else than a namespace
		if (parts[0].startsWith("www") || parts[0].startsWith("localhost") || parts[0].endsWith(".org"))
			return null;

		String packageName = null;
		// Skip parts[0], since this is the module name. Add the other parts,
		// dot-separated;
		for (int i = 1; i < parts.length; i++) {
			if (packageName == null) {
				packageName = parts[i];
			} else {
				packageName = packageName + "." + parts[i];
			}
		}
		return packageName;

	}

	protected final void executeCommand(Commandline command) {
		try {
			getLog().info(
					"Executing the following command: " + command.toString());

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

			int ret=CommandLineUtils.executeCommandLine(command,
					new StreamConsumerChain(systemOut).add(errorChecker),
					new StreamConsumerChain(systemErr).add(errorChecker));

			if (ret!=0 || errorChecker.isError()) {

				throw new RuntimeException(
						"An error occured during deploy. Stopping deployment. Consult the logs.");

			}
		} catch (CommandLineException e) {
			throw new RuntimeException(
					"An error occured executing: " + command, e);
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
