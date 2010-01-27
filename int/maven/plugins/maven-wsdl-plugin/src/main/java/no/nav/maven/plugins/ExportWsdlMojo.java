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
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collections;

import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.artifact.handler.manager.ArtifactHandlerManager;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.UnArchiver;

/**
 * Goal which touches a timestamp file.
 * 
 * @goal exportwsdl
 * 
 * @phase package
 */
public class ExportWsdlMojo extends AbstractMojo {
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
	 * EAR-file.
	 * 
	 * @parameter expression="${project.build.directory}/${project.build.finalName}.${project.artifact.artifactHandler.extension}"
	 */
	private File earFile;

	public void execute() throws MojoExecutionException {
		String packaging = project.getPackaging();
		// This plugin is only applicable to WPS Module artifacts
		if ("wps-module-ear".equals(packaging)) {
			executeInternal();
		} else {
			getLog().debug("Skipping exportwsdl because packaging is " + packaging + " and not wps-module-ear.");
		}
	}

	private void executeInternal() throws MojoExecutionException {
		try {
			// TODO: The following must be done because of one (or more) bug(s)
			// in Maven. See Maven bugs MNG-3506 and MNG-2426 for more info.
			if (!wsdlInterfaceArtifactHandler.equals(artifactHandlerManager.getArtifactHandler(WSDL_INTERFACE_ARTIFACT_TYPE))) {
				getLog().debug("Adding wsdlif interchange artifact handler to artifact handler manager");
				artifactHandlerManager.addHandlers(Collections.singletonMap(WSDL_INTERFACE_ARTIFACT_TYPE,
						wsdlInterfaceArtifactHandler));
			}

			File tempDir = new File(project.getBuild().getDirectory(), "wsdltemp");
			tempDir.mkdir();

			File tempEarFileDir = extractFile(earFile, tempDir);
			File[] warFiles = tempEarFileDir.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.endsWith(WAR_SUFFIX);
				}
			});

			// Does this module have WSDL exports?
			if (warFiles.length != 0) {
				File warFile = warFiles[0];

				File tempWarFileDir = extractFile(warFile, tempDir);

				File wsdlDirectory = new File(tempWarFileDir, WSDL_PATH_IN_WAR);

				File wsdlZipArtifactFile = new File(project.getBuild().getDirectory(), project.getBuild().getFinalName() + "-"
						+ wsdlInterfaceArtifactHandler.getClassifier() + "." + ZIP_SUFFIX);
				archiver.setDestFile(wsdlZipArtifactFile);

				archiver.addDirectory(wsdlDirectory);
				archiver.createArchive();
				projectHelper.attachArtifact(project, WSDL_INTERFACE_ARTIFACT_TYPE, wsdlInterfaceArtifactHandler
						.getClassifier(), wsdlZipArtifactFile);
			}
		} catch (Exception e) {
			throw new MojoExecutionException("Creating wsdl export file failed ", e);
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
}
