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
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.wsdl.Definition;
import javax.wsdl.Import;
import javax.wsdl.Types;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.schema.Schema;
import javax.wsdl.extensions.schema.SchemaImport;
import javax.wsdl.extensions.schema.SchemaReference;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.artifact.handler.manager.ArtifactHandlerManager;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.apache.maven.project.path.PathTranslator;
import org.apache.maven.shared.filtering.MavenFilteringException;
import org.apache.maven.shared.filtering.MavenResourcesExecution;
import org.apache.maven.shared.filtering.MavenResourcesFiltering;
import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

/**
 * Goal which touches a timestamp file.
 * 
 * @goal exportwsdl
 * @requiresDependencyResolution compile
 */
@SuppressWarnings("unchecked")
public class ExportWsdlMojo extends AbstractMojo {
	private static final String TYPE_WSDL_INTERFACE = "wsdl-interface";
	private static final String TYPE_WPS_LIBRARY_JAR = "wps-library-jar";

	/**
	 * @component
	 */
	private MavenProjectHelper projectHelper;

	/**
	 * @component roleHint="zip"
	 * @required
	 * @readonly
	 */
	private UnArchiver unArchiver;

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
	 * @component
	 */
	private MavenResourcesFiltering mavenResourcesFiltering;

	/**
	 * @component
	 */
	private PathTranslator pathTranslator;

	/**
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

	/**
	 * @parameter expression="${session}"
	 * @required
	 * @readonly
	 */
	private MavenSession session;

	/**
	 * @parameter default-value="${project.build.sourceEncoding}"
	 */
	private String encoding;

	/**
	 * @parameter default-value="false"
	 */
	private boolean verbose;

	private SAXBuilder saxBuilder;
	private XPath webServiceExportXPath;
	// private XPath webServiceImportXPath;
	private WSDLReader wsdlReader;

	public void execute() throws MojoExecutionException, MojoFailureException {
		String packaging = project.getPackaging();
		// This plugin is only applicable to WPS Module artifacts
		if ("wps-module-ear".equals(packaging)) {
			init();
			executeInternal();
		} else {
			getLog().debug("Skipping exportwsdl because packaging is " + packaging + " and not wps-module-ear.");
		}
	}

	private void init() throws MojoExecutionException {
		try {
			saxBuilder = new SAXBuilder();
			webServiceExportXPath = XPath
					.newInstance("//esbBinding[@xsi:type='webservice:WebServiceExportBinding' or @xsi:type='jaxws:JaxWsExportBinding']");
			// webServiceImportXPath = XPath
			// .newInstance("//esbBinding[@xsi:type='webservice:WebServiceImportBinding' or @xsi:type='jaxws:JaxWsImportBinding']");
			wsdlReader = WSDLFactory.newInstance().newWSDLReader();
			wsdlReader.setFeature("javax.wsdl.verbose", verbose);
			wsdlReader.setFeature("javax.wsdl.importDocuments", false);
		} catch (JDOMException e) {
			throw new MojoExecutionException("Error initalizing XML processing framework", e);
		} catch (WSDLException e) {
			throw new MojoExecutionException("Error initalizing WSDL framework", e);
		}
	}

	private void executeInternal() throws MojoExecutionException, MojoFailureException {
		try {
			// TODO: The following must be done because of one (or more) bug(s)
			// in Maven. See Maven bugs MNG-3506 and MNG-2426 for more info.
			if (!wsdlInterfaceArtifactHandler.equals(artifactHandlerManager.getArtifactHandler(TYPE_WSDL_INTERFACE))) {
				getLog().debug("Adding wsdlif interchange artifact handler to artifact handler manager");
				artifactHandlerManager.addHandlers(Collections.singletonMap(TYPE_WSDL_INTERFACE, wsdlInterfaceArtifactHandler));
			}

			// TODO: All related to imported WS is commented out until we can come up with a solution for modules that have both
			// WS exports and imports.

			Collection<QName> exportedWebServices = getExportedWebServices();
			// Collection<QName> importedWebServices = getImportedWebServices();

			if (!exportedWebServices.isEmpty()) {
				// if (!exportedWebServices.isEmpty() || !importedWebServices.isEmpty()) {
				File workingDir = createWorkingDir();

				extractDependendentResources(workingDir);
				copyProjectResources(workingDir);

				Map<QName, Definition> webServiceToWsdlMap = getWebServiceToWsdlMap(workingDir);

				if (!exportedWebServices.isEmpty()) {
					createWebServicesArchive(workingDir, webServiceToWsdlMap, exportedWebServices);
				}

				// if (!importedWebServices.isEmpty()) {
				// createWebServicesArchive(workingDir, webServiceToWsdlMap, importedWebServices);
				// }
			}
		} catch (IOException e) {
			throw new MojoExecutionException("Error accessing file system", e);
		} catch (JDOMException e) {
			throw new MojoExecutionException("Error processing XML", e);
		} catch (ArchiverException e) {
			throw new MojoExecutionException("Error accessing archive", e);
		} catch (MavenFilteringException e) {
			throw new MojoExecutionException("Error filtering resources", e);
		} catch (WSDLException e) {
			throw new MojoExecutionException("Error processing WSDL", e);
		}

	}

	private void createWebServicesArchive(File workingDir, Map<QName, Definition> webServiceToWsdlMap,
			Collection<QName> webServices) throws MojoFailureException, WSDLException, ArchiverException, IOException {
		Collection<String> documentUris = new HashSet();

		for (QName webService : webServices) {
			Definition wsdl = webServiceToWsdlMap.get(webService);
			if (wsdl == null) {
				throw new MojoFailureException("WSDL for web service " + webService + " not found.");
			}
			addFilesForWsdl(documentUris, wsdl);
		}

		Collection<File> files = new ArrayList<File>(documentUris.size());
		for (String documentUri : documentUris) {
			// TODO: WSDL4J generates invalid URIs (with whitespace)
			// that will have to be escaped
			URI uri = URI.create(documentUri.replace(" ", "%20"));
			files.add(new File(uri));
		}

		createArchive(workingDir, files);
	}

	private void addFilesForWsdl(Collection<String> documentUris, Definition wsdl) throws WSDLException {
		String documentBaseURI = wsdl.getDocumentBaseURI();
		documentUris.add(documentBaseURI);

		for (Collection<Import> imports : (Collection<Collection<Import>>) wsdl.getImports().values()) {
			for (Import _import : imports) {
				Definition importedWsdl = wsdlReader.readWSDL(documentBaseURI, _import.getLocationURI());
				addFilesForWsdl(documentUris, importedWsdl);
			}
		}

		Types types = wsdl.getTypes();
		if (types != null) {
			for (ExtensibilityElement extensibilityElement : (Collection<ExtensibilityElement>) types
					.getExtensibilityElements()) {
				if (extensibilityElement instanceof Schema) {
					Schema schema = (Schema) extensibilityElement;
					for (Collection<SchemaImport> schemaImports : (Collection<Collection<SchemaImport>>) schema.getImports()
							.values()) {
						for (SchemaImport schemaImport : schemaImports) {
							documentUris.add(schemaImport.getReferencedSchema().getDocumentBaseURI());
						}
					}
					for (SchemaReference schemaInclude : (Collection<SchemaReference>) schema.getIncludes()) {
						documentUris.add(schemaInclude.getReferencedSchema().getDocumentBaseURI());
					}
				}
			}
		}
	}

	private Map<QName, Definition> getWebServiceToWsdlMap(File workingDir) throws IOException, WSDLException,
			MojoFailureException {
		Map<QName, Definition> webServiceToWsdlMap = new HashMap<QName, Definition>();

		Collection<File> wsdlFiles = FileUtils.getFiles(workingDir, "**/*.wsdl", null);
		for (File wsdlFile : wsdlFiles) {
			Definition wsdl = wsdlReader.readWSDL(wsdlFile.getAbsolutePath());
			Collection<QName> services = wsdl.getServices().keySet();
			for (QName service : services) {
				Definition previousWsdl = webServiceToWsdlMap.put(service, wsdl);
				if (previousWsdl != null) {
					throw new MojoFailureException("Duplicate service definition for service " + service
							+ ". Service defined in " + wsdl.getDocumentBaseURI() + ", but already defined in "
							+ previousWsdl.getDocumentBaseURI() + ".");
				}
			}
		}

		return webServiceToWsdlMap;
	}

	private void extractDependendentResources(File workingDir) throws ArchiverException, IOException {
		for (Artifact artifact : (Collection<Artifact>) project.getCompileArtifacts()) {
			if (TYPE_WPS_LIBRARY_JAR.equals(artifact.getType())) {
				File artifactFile = artifact.getFile();
				unArchiver.setSourceFile(artifactFile);
				unArchiver.setDestDirectory(workingDir);
				unArchiver.extract();
			}
		}
	}

	private void copyProjectResources(File workingDir) throws MavenFilteringException {
		MavenResourcesExecution mavenResourcesExecution = new MavenResourcesExecution();
		mavenResourcesExecution.setMavenProject(project);
		mavenResourcesExecution.setMavenSession(session);
		mavenResourcesExecution.setResources(project.getResources());
		mavenResourcesExecution.setInjectProjectBuildFilters(true);
		mavenResourcesExecution.setOutputDirectory(workingDir);
		mavenResourcesExecution.setEncoding(encoding);

		mavenResourcesFiltering.filterResources(mavenResourcesExecution);
	}

	private File createWorkingDir() {
		File parentDir = new File(project.getBuild().getDirectory(), "exportwsdl");
		File workingDir = new File(parentDir, String.valueOf(System.currentTimeMillis()));
		workingDir.mkdirs();
		return workingDir;
	}

	private void createArchive(File baseDir, Collection<File> files) throws ArchiverException, IOException {
		File wsdlZipArtifactFile = new File(project.getBuild().getDirectory(), project.getBuild().getFinalName() + "-"
				+ wsdlInterfaceArtifactHandler.getClassifier() + "." + wsdlInterfaceArtifactHandler.getExtension());
		archiver.setDestFile(wsdlZipArtifactFile);

		for (File file : files) {
			String destFileName = pathTranslator.unalignFromBaseDirectory(file.getAbsolutePath(), baseDir);
			archiver.addFile(file, destFileName);
		}

		archiver.createArchive();
		projectHelper.attachArtifact(project, TYPE_WSDL_INTERFACE, wsdlInterfaceArtifactHandler.getClassifier(),
				wsdlZipArtifactFile);
	}

	private Collection<QName> getExportedWebServices() throws IOException, JDOMException {
		return getWebServices("**/*.export", webServiceExportXPath);
	}

	// private Collection<QName> getImportedWebServices() throws IOException, JDOMException {
	// return getWebServices("**/*.import", webServiceImportXPath);
	// }

	private Collection<QName> getWebServices(String filenamePattern, XPath xpath) throws IOException, JDOMException {
		Collection<QName> webServices = new HashSet<QName>();

		Collection<File> files = FileUtils.getFiles(project.getBasedir(), filenamePattern, null);
		getLog().debug("Found " + files.size() + " files matching pattern " + filenamePattern + " in module");
		for (File file : files) {
			Document fileDocument = saxBuilder.build(file);
			Collection<Element> webServiceBindingElements = xpath.selectNodes(fileDocument);
			getLog().debug("Found " + webServiceBindingElements.size() + " web service bindings in file " + file.getName());
			for (Element webServiceBindingElement : webServiceBindingElements) {
				String webServiceAttributeValue = webServiceBindingElement.getAttributeValue("service");
				String[] webServiceAttributeValueParts = StringUtils.split(webServiceAttributeValue, ":");
				String webServicePrefix = webServiceAttributeValueParts[0];
				Namespace webServiceNamespace = webServiceBindingElement.getNamespace(webServicePrefix);
				String webServiceName = webServiceAttributeValueParts[1];
				QName webService = new QName(webServiceNamespace.getURI(), webServiceName);
				webServices.add(webService);
			}
		}

		return webServices;
	}
}
