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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.artifact.handler.manager.ArtifactHandlerManager;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.cli.Arg;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

/**
 * Goal which touches a timestamp file.
 * 
 * @goal wsdl2java
 * 
 * @phase generate-sources
 * @requiresDependencyResolution compile
 */
public class Wsdl2JavaMojo extends AbstractMojo {

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
	 * Set a location to generate CLASS files into.
	 * 
	 * @parameter default-value="${project.build.directory}/generated-sources/wsdl2java"
	 * @required
	 */
	private File classGenerationDirectory;

	public void execute() throws MojoExecutionException {
		try {
			// TODO: The following must be done because of one (or more) bug(s)
			// in Maven. See Maven bugs MNG-3506 and MNG-2426 for more info.
			if (!wsdlIfArtifactHandler.equals(artifactHandlerManager.getArtifactHandler(WSDL_INTERFACE_ARTIFACT_TYPE))) {
				getLog().debug("Adding project interchange artifact handler to artifact handler manager");
				artifactHandlerManager.addHandlers(Collections
						.singletonMap(WSDL_INTERFACE_ARTIFACT_TYPE, wsdlIfArtifactHandler));
			}

			String exec;
			if (wasRuntime == null) {
				wasRuntime = widRuntime + "/runtimes/bi_v61";
			}
			if (Os.isFamily("windows")) {
				exec = wasRuntime + "/bin/WSDL2Java.bat";
			} else {
				exec = wasRuntime + "/bin/WSDL2Java.sh";
			}
			Commandline commandLine = new Commandline();
			commandLine.setExecutable(exec);

			File tempDir = new File(project.getBuild().getDirectory(), "wsdltemp");
			tempDir.mkdirs();

			// First unpack the wsdl-artifact from the dependency
			for (Artifact artifact : getWSDLIfArtifacts(project)) {
				File tempWsdlZipDir = extractFile(artifact.getFile(), tempDir);

				// Generate the NStoPkg.properties-file that will make sensible
				// packages for the wsdl
				Map<String, String> namespaceToPackageMap = NamespaceToPackageMapGenerator
						.createNameSpaceToPackageMapFromWSDLDirectory(tempWsdlZipDir);
				File nameSpaceToPackageFile = new File(tempWsdlZipDir, "NStoPkg.properties");
				PrintWriter pw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(nameSpaceToPackageFile)));
				for (String namespace : namespaceToPackageMap.keySet()) {
					pw.write(namespace + "=" + namespaceToPackageMap.get(namespace) + "\n");
				}
				pw.close();

				// Next, call the WSDL2Java script for all wsdl-files in the
				// artifact
				List<File> wsdlFiles = listFilesRecursive(tempWsdlZipDir, new FilenameFilter() {
					public boolean accept(File dir, String name) {
						return name.contains(WSDLEXPORT_TOKEN) && name.endsWith(WSDLEXPORT_SUFFIX);
					}
				});
				for (File wsdlFile : wsdlFiles) {
					commandLine.clearArgs();
					Arg arg = commandLine.createArg();
					arg.setLine("-role client -container none -output \"" + classGenerationDirectory.getAbsolutePath()
							+ "\" -genJava Overwrite -fileNStoPkg \"" + nameSpaceToPackageFile.getAbsolutePath() + "\" \""
							+ wsdlFile.getAbsolutePath() + "\"");
					executeCommand(commandLine);
				}
			}

			project.addCompileSourceRoot(classGenerationDirectory.getAbsolutePath());
		} catch (RuntimeException re) {
			throw re;
		} catch (Exception e) {
			new MojoExecutionException("Unable to generate Java", e);
		}
	}

	private File extractFile(File file, File parentDirectory) throws IOException, ArchiverException {
		File extractDir = new File(parentDirectory, file.getName());
		extractDir.delete();
		extractDir.mkdir();

		unArchiver.setSourceFile(file);
		unArchiver.setDestDirectory(extractDir);
		unArchiver.extract();

		return extractDir;
	}

	/**
	 * Recursive variant of regular listFiles()-method
	 * 
	 * @param dirToTraverse
	 * @param filter
	 * @return files matching filter in all subdirectories of dirToTraverse
	 */
	private List<File> listFilesRecursive(File dirToTraverse, FilenameFilter filter) {
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
	@SuppressWarnings("unchecked")
	public List<Artifact> getWSDLIfArtifacts(MavenProject project) {
		String wsdlIfClassifier = wsdlIfArtifactHandler.getClassifier();
		ArrayList<Artifact> artifactList = new ArrayList<Artifact>();
		Set artifacts = project.getDependencyArtifacts();
		for (Artifact artifact : (Set<Artifact>) artifacts) {
			if (wsdlIfClassifier.equals(artifact.getClassifier())) {
				artifactList.add(artifact);
			}
		}
		if (artifactList.size() == 0) {
			throw new RuntimeException("No attached artifact of type wsdl-interface (classifier '" + wsdlIfClassifier
					+ "') found ");
		}
		return artifactList;
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

			int ret = CommandLineUtils.executeCommandLine(command, new StreamConsumerChain(systemOut).add(errorChecker),
					new StreamConsumerChain(systemErr).add(errorChecker));

			if (ret != 0 || errorChecker.isError()) {

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
