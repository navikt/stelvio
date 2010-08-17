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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.InvalidRepositoryException;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.artifact.handler.manager.ArtifactHandlerManager;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.ArtifactRepositoryFactory;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectUtils;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.cli.Arg;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

/**
 * Goal which generates a Java API for web services provided by the Integration Platform
 * 
 * @goal wsdl2java
 * 
 * @phase generate-sources
 */
public class Wsdl2JavaMojo extends AbstractMojo {
	private static final String WSDL_INTERFACE_ARTIFACT_TYPE = "wsdl-interface";

	private static final String WSDLEXPORT_SUFFIX = "wsdl";
	private static final String WSDLEXPORT_TOKEN = "WSEXP";

	/**
	 * The Maven session.
	 * 
	 * @parameter expression="${session}"
	 * @readonly
	 * @required
	 */
	private MavenSession mavenSession;

	/**
	 * @parameter expression="${project}"
	 * @readonly
	 * @required
	 */
	private MavenProject project;

	/**
	 * Artifact repository factory component.
	 * 
	 * @component
	 * @readonly
	 * @required
	 */
	private ArtifactRepositoryFactory artifactRepositoryFactory;

	/**
	 * The remote repositories used as specified in your POM.
	 * 
	 * @parameter expression="${project.repositories}"
	 * @readonly
	 * @required
	 */
	private List repositories;

	/**
	 * The local repository taken from Maven's runtime. Typically $HOME/.m2/repository.
	 * 
	 * @parameter expression="${localRepository}"
	 * @readonly
	 * @required
	 */
	private ArtifactRepository localRepository;

	/**
	 * Artifact factory, needed to create artifacts.
	 * 
	 * @component
	 * @readonly
	 * @required
	 */
	private ArtifactFactory artifactFactory;

	/**
	 * @component
	 * @readonly
	 * @required
	 */
	private ArtifactResolver artifactResolver;

	/**
	 * @component roleHint="wsdl-interface"
	 */
	private ArtifactHandler wsdlInterfaceArtifactHandler;

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
	 * @parameter expression="${was.home}"
	 * @readonly
	 */
	private String wasRuntime;

	/**
	 * @parameter expression="${wid.runtime}"
	 * @readonly
	 */
	private String widRuntime;
	
	/**
	 * @parameter default-value="false"
	 */
	private boolean noWrappedOperations;
	
	/**
	 * @parameter default-value="false"
	 */
	private boolean noWrappedArrays;

	/**
	 * @parameter
	 */
	private WsdlOption wsdlOptions[];

	/**
	 * @parameter default-value="${project.build.sourceEncoding}"
	 */
	private String encoding = System.getProperty("file.encoding");

	/**
	 * Set a location to generate CLASS files into.
	 * 
	 * @parameter default-value="${project.build.directory}/generated-sources/wsdl2java"
	 * @required
	 */
	private File classGenerationDirectory;

	public void execute() throws MojoExecutionException {
		if (wsdlOptions == null || wsdlOptions.length == 0) {
			getLog().info("Nothing to generate");
			return;
		}

		// TODO: The following must be done because of one (or more) bug(s)
		// in Maven. See Maven bugs MNG-3506 and MNG-2426 for more info.
		if (!wsdlInterfaceArtifactHandler.equals(artifactHandlerManager.getArtifactHandler(WSDL_INTERFACE_ARTIFACT_TYPE))) {
			getLog().debug("Adding project interchange artifact handler to artifact handler manager");
			artifactHandlerManager.addHandlers(Collections.singletonMap(WSDL_INTERFACE_ARTIFACT_TYPE, wsdlInterfaceArtifactHandler));
		}

		String exec;
		if (wasRuntime == null) {
			wasRuntime = widRuntime;
		}
		if (Os.isFamily("windows")) {
			exec = wasRuntime + "/bin/WSDL2Java.bat";
		} else {
			exec = wasRuntime + "/bin/WSDL2Java.sh";
		}
		Commandline commandLine = new Commandline();
		commandLine.setExecutable(exec);

		File workingDir = createWorkingDir();

		// First unpack the wsdl-artifact from the dependency
		for (Artifact artifact : getWSDLArtifacts()) {
			File wsdlZipDir = extractFile(artifact.getFile(), workingDir);

			// Generate the NStoPkg.properties-file that will make sensible
			// packages for the wsdl
			File nameSpaceToPackageFile = generateNameSpaceToPackageFile(wsdlZipDir);

			// Next, call the WSDL2Java script for all wsdl-files in the
			// artifact
			List<File> wsdlFiles = listFilesRecursive(wsdlZipDir, new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.contains(WSDLEXPORT_TOKEN) && name.endsWith(WSDLEXPORT_SUFFIX);
				}
			});
			for (File wsdlFile : wsdlFiles) {
				commandLine.clearArgs();
				Arg arg = commandLine.createArg();
				
				StringBuilder argLineBuilder = new StringBuilder();
				argLineBuilder.append(" -role client");
				argLineBuilder.append(" -container none");
				argLineBuilder.append(" -genJava Overwrite");
				if (noWrappedOperations) {
					argLineBuilder.append(" -noWrappedOperations");
				}
				if (noWrappedArrays) {
					argLineBuilder.append(" -noWrappedArrays");
				}
				argLineBuilder.append(" -fileNStoPkg ").append('"').append(nameSpaceToPackageFile.getAbsolutePath()).append('"');
				argLineBuilder.append(" -output ").append('"').append(classGenerationDirectory.getAbsolutePath()).append('"');
				argLineBuilder.append(" ").append('"').append(wsdlFile.getAbsolutePath()).append('"');
				
				arg.setLine(argLineBuilder.toString());
				executeCommand(commandLine);
			}
		}

		project.addCompileSourceRoot(classGenerationDirectory.getAbsolutePath());
	}

	private File generateNameSpaceToPackageFile(File wsdlZipDir) throws MojoExecutionException {
		try {
			Map<String, String> namespaceToPackageMap = new NamespaceToPackageMapGenerator(encoding)
					.createNameSpaceToPackageMapFromWSDLDirectory(wsdlZipDir);
			File nameSpaceToPackageFile = new File(wsdlZipDir, "NStoPkg.properties");
			PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
					nameSpaceToPackageFile), encoding)));
			for (String namespace : namespaceToPackageMap.keySet()) {
				pw.println(namespace + "=" + namespaceToPackageMap.get(namespace));
			}
			pw.close();
			return nameSpaceToPackageFile;
		} catch (UnsupportedEncodingException e) {
			throw new MojoExecutionException("Error generating NStoPkg.properties file", e);
		} catch (FileNotFoundException e) {
			throw new MojoExecutionException("Error generating NStoPkg.properties file", e);
		}
	}

	private File createWorkingDir() {
		File parentDir = new File(project.getBuild().getDirectory(), "wsdl2java");
		File workingDir = new File(parentDir, String.valueOf(System.currentTimeMillis()));
		workingDir.mkdirs();
		return workingDir;
	}

	private File extractFile(File file, File parentDirectory) throws MojoExecutionException {
		try {
			File extractDir = new File(parentDirectory, file.getName());
			extractDir.delete();
			extractDir.mkdir();

			unArchiver.setSourceFile(file);
			unArchiver.setDestDirectory(extractDir);
			unArchiver.extract();

			return extractDir;
		} catch (ArchiverException e) {
			throw new MojoExecutionException("Error extracting archive", e);
		} catch (IOException e) {
			throw new MojoExecutionException("Error extracting archive", e);
		}
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
	 * @return the first wsdl-if artifact found in the dependency list
	 * @throws MojoExecutionException
	 */
	@SuppressWarnings("unchecked")
	public Collection<Artifact> getWSDLArtifacts() throws MojoExecutionException {
		List remoteRepos;
		try {
			remoteRepos = ProjectUtils.buildArtifactRepositories(repositories, artifactRepositoryFactory, mavenSession
					.getContainer());
		} catch (InvalidRepositoryException e) {
			throw new MojoExecutionException("Error build repositories for remote wsdls", e);
		}

		Collection<Artifact> wsdlArtifacts = new ArrayList<Artifact>(wsdlOptions.length);
		for (WsdlOption wsdlOption : wsdlOptions) {
			WsdlArtifact wsdlA = wsdlOption.getWsdlArtifact();
			if (wsdlA == null) {
				// TODO: Improve error handling
				continue;
			}
			Artifact wsdlArtifact = artifactFactory.createArtifactWithClassifier(wsdlA.getGroupId(), wsdlA.getArtifactId(), wsdlA
					.getVersion(), wsdlA.getType(), wsdlA.getClassifier());
			wsdlArtifact = resolveRemoteWsdlArtifact(remoteRepos, wsdlArtifact);
			if (wsdlArtifact.getFile() == null) {
				throw new MojoExecutionException("Unable to resolve artifact: " + wsdlArtifact);
			} else {
				String path = wsdlArtifact.getFile().getAbsolutePath();
				getLog().info("Resolved WSDL artifact to file " + path);
			}
			wsdlArtifacts.add(wsdlArtifact);
		}
		return wsdlArtifacts;
	}

	@SuppressWarnings("unchecked")
	public Artifact resolveRemoteWsdlArtifact(List remoteRepos, Artifact artifact) throws MojoExecutionException {
		// First try to find the artifact in the reactor projects of the maven
		// session. So an artifact that is not yet built can be resolved
		List<MavenProject> rProjects = mavenSession.getSortedProjects();
		for (MavenProject rProject : rProjects) {
			if (artifact.getGroupId().equals(rProject.getGroupId())
					&& artifact.getArtifactId().equals(rProject.getArtifactId())
					&& artifact.getVersion().equals(rProject.getVersion())) {
				Set<Artifact> artifacts = rProject.getArtifacts();
				for (Artifact pArtifact : artifacts) {
					if (artifact.getType().equals(pArtifact.getType())) {
						return pArtifact;
					}
				}
			}
		}

		// If this did not work resolve the artifact using the artifactResolver
		try {
			artifactResolver.resolve(artifact, remoteRepos, localRepository);
		} catch (ArtifactResolutionException e) {
			throw new MojoExecutionException("Error downloading wsdl artifact.", e);
		} catch (ArtifactNotFoundException e) {
			throw new MojoExecutionException("Resource can not be found.", e);
		}

		return artifact;
	}

	private void executeCommand(Commandline command) {
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
