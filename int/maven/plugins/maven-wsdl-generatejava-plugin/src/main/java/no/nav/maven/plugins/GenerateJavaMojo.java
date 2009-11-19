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

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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

	protected static final String WSDLEXPORT_SUFFIX = "WSEXP.wsdl";

	/**
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

	/**
	 * @parameter expression="${was.home}"
	 * @required
	 * @readonly
	 */
	private String wasRuntime;

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

	/**
	 * Source Dir
	 * 
	 * @parameter expression="${project.build.directory}"
	 * @required
	 */
	private File buildDirectory;

	public void execute() throws MojoExecutionException {
		// Necessary hack. Or so is the rumor, at least.
		if (!wsdlIfArtifactHandler.equals(artifactHandlerManager
				.getArtifactHandler(WSDL_INTERFACE_ARTIFACT_TYPE))) {
			getLog()
					.debug(
							"Adding project interchange artifact handler to artifact handler manager");
			artifactHandlerManager.addHandlers(Collections.singletonMap(
					WSDL_INTERFACE_ARTIFACT_TYPE, wsdlIfArtifactHandler));
		}

		// First unpack the wsdl-artifact from the dependency
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

		// Next, call the WSDL2Java script for all wsdl-files in the artifact
		String exec;
		if (Os.isFamily("windows")) {
			exec = wasRuntime + "/bin/WSDL2Java.bat";
		} else {
			exec = wasRuntime + "/bin/WSDL2Java.sh";
		}

		List<File> wsdlFiles = listFilesRecursive(tempDirfile,
				new FilenameFilter() {
					public boolean accept(File dir, String name) {
						return name.endsWith(WSDLEXPORT_SUFFIX);
					}
				});

		String genDirectory = buildDirectory.getAbsolutePath() + "/genWSDL";
		for (File wsdlFile : wsdlFiles) {
			Commandline commandLine = new Commandline();
			Commandline.Argument arg = new Commandline.Argument();
			arg.setLine("-role client -container web -o \"" + genDirectory
					+ "\" -j Overwrite -f \"" + tempDirfile.getAbsolutePath()
					+ "\\NStoPkg.properties\" \"" + wsdlFile.getAbsolutePath()
					+ "\"");
			commandLine.setExecutable(exec);
			commandLine.addArg(arg);
			arg = new Commandline.Argument();
			executeCommand(commandLine);
		}
		project.addCompileSourceRoot(genDirectory);

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
	public Artifact getWSDLIfArtifact(MavenProject project) {
		String wsdlIfClassifier = wsdlIfArtifactHandler.getClassifier();

		Set artifacts = project.getDependencyArtifacts();
		for (Iterator artifactIterator = artifacts.iterator(); artifactIterator
				.hasNext();) {
			Artifact artifact = (Artifact) artifactIterator.next();
			System.out.println(artifact.getClassifier());
			if (wsdlIfClassifier.equals(artifact.getClassifier())) {
				return artifact;
			}
		}

		throw new RuntimeException(
				"No attached artifact of type wsdl-interface (classifier '"
						+ wsdlIfClassifier + "') found ");
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

			CommandLineUtils.executeCommandLine(command,
					new StreamConsumerChain(systemOut).add(errorChecker),
					new StreamConsumerChain(systemErr).add(errorChecker));

			if (errorChecker.isError()) {

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
