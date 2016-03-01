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

import no.stelvio.Version;
import no.stelvio.ibm.websphere.esb.SCAModuleName;
import no.stelvio.ibm.websphere.esb.WSDLEditor;

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
import org.codehaus.plexus.archiver.manager.ArchiverManager;
import org.codehaus.plexus.archiver.manager.NoSuchArchiverException;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.StringUtils;
import org.jdom.Attribute;
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
	private static final String TYPE_SERVICE_SPECIFICATION = "service-specification";
	private static final String TYPE_MESSAGE_SPECIFICATION = "message-specification";

	/**
	 * @component
	 */
	private MavenProjectHelper projectHelper;

	/**
	 * @component
	 */
	private ArchiverManager archiverManager;

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

	/**
	 * @parameter default-value="true"
	 */
	private boolean processExports;

	/**
	 * @parameter default-value="true"
	 */
	private boolean processImports;

	private SAXBuilder saxBuilder;
	private WSDLReader wsdlReader;

	private XPath webServiceExportXPath;
	private XPath webServiceImportXPath;

	public void execute() throws MojoExecutionException, MojoFailureException {
		String packaging = project.getPackaging();
		// This plugin is only applicable to WPS Module artifacts
		if ("wps-module-ear".equals(packaging)) {
			executeInternal();
		} else {
			getLog().debug("Skipping exportwsdl because packaging is " + packaging + " and not wps-module-ear.");
		}
	}

	private void executeInternal() throws MojoExecutionException, MojoFailureException {
		try {
			// TODO: The following must be done because of one (or more) bug(s)
			// in Maven. See Maven bugs MNG-3506 and MNG-2426 for more info.
			if (!wsdlInterfaceArtifactHandler.equals(artifactHandlerManager.getArtifactHandler(TYPE_WSDL_INTERFACE))) {
				getLog().debug("Adding wsdl-interface artifact handler to artifact handler manager");
				artifactHandlerManager.addHandlers(Collections.singletonMap(TYPE_WSDL_INTERFACE, wsdlInterfaceArtifactHandler));
			}

			Collection<QName> exportedWebServices = Collections.emptyList();
			if (processExports) {
				exportedWebServices = getExportedWebServices();
			}
			Collection<QName> importedWebServices = Collections.emptyList();
			if (processImports) {
				importedWebServices = getImportedWebServices();
			}

			if (!exportedWebServices.isEmpty() || !importedWebServices.isEmpty()) {
				File workingDir = createWorkingDir();

				extractDependendentResources(workingDir);
				copyProjectResources(workingDir);

				Map<QName, Definition> webServiceToWsdlMap = getWebServiceToWsdlMap(workingDir);

				for (QName name : exportedWebServices) {
					WSDLEditor wsdlEditor = new WSDLEditor(webServiceToWsdlMap.get(name));

					// Determine if module is versioned
					File scaModuleAttributesFile = new File(project.getBuild().getOutputDirectory(), "sca.module.attributes");
					getLog().debug("Extracting version from " + scaModuleAttributesFile.getAbsolutePath());
					if (scaModuleAttributesFile.canRead()) {
						Document scaModuleAttributes = getSAXBuilder().build(scaModuleAttributesFile);
						XPath versionXPath = XPath.newInstance("/scdl:moduleAndLibraryAttributes/@versionValue");
						versionXPath.addNamespace("scdl", "http://www.ibm.com/xmlns/prod/websphere/scdl/6.0.0");
						Object result = versionXPath.selectSingleNode(scaModuleAttributes);
						if (result != null && result instanceof Attribute) {
							String versionString = ((Attribute) result).getValue();
							if (versionString.trim().length() > 0) {
								try {
									Version version = new Version(((Attribute) result).getValue());
									// Modify SOAP endpoint with versioned module name
									String versionedModuleName = SCAModuleName.createVersionedModuleName(project.getArtifactId(), version);
									getLog().debug("Modifying context root of SOAP endpoint with module name " + versionedModuleName + " (file=" + webServiceToWsdlMap.get(name).getDocumentBaseURI() + ")");
									try {
										wsdlEditor.modifySOAPAddresses(versionedModuleName);
									} catch (WSDLException e) {
										throw new MojoExecutionException("Error modifying WSDL SOAP address", e); 
									}
								} catch (NumberFormatException e) {
									throw new MojoExecutionException("Invalid version string in attribute /scdl:moduleAndLibraryAttributes/@versionValue in file " + scaModuleAttributesFile.getAbsolutePath(), e);
								}
	
							}
						}
					}

					// Write changes back to WSDL file
					try {
						wsdlEditor.saveChanges();
					} catch (Exception e) {
						throw new MojoExecutionException("Error saving modified WSDL file", e); 
					}
				}
				
				if (!exportedWebServices.isEmpty()) {
					// TODO: The exported web services archive is created twice to maintain backwards compatability.
					// Should be removed sometime, but that will break backwards compatability. BEWARE!
					createWebServicesArchive(workingDir, webServiceToWsdlMap, exportedWebServices, "wsdlif");
					createWebServicesArchive(workingDir, webServiceToWsdlMap, exportedWebServices, "wsexports");
				}

				if (!importedWebServices.isEmpty()) {
					createWebServicesArchive(workingDir, webServiceToWsdlMap, importedWebServices, "wsimports");
				}
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
		} catch (NoSuchArchiverException e) {
			throw new MojoExecutionException("Error creating archiver/unarchiver", e);
		}

	}

	@SuppressWarnings("serial")
	private void createWebServicesArchive(File workingDir, Map<QName, Definition> webServiceToWsdlMap,
			Collection<QName> webServices, String classifier) throws MojoFailureException, WSDLException, ArchiverException,
			IOException, NoSuchArchiverException {
		Collection<String> documentUris = new HashSet<String>() {
			@Override
			public boolean add(String documentUri) {
				if (getLog().isDebugEnabled()) {
					getLog().debug("Adding document URI: " + documentUri);
				}
				return super.add(documentUri);
			}

		};

		for (QName webService : webServices) {
			Definition wsdl = webServiceToWsdlMap.get(webService);
			if (wsdl == null) {
				throw new MojoFailureException("WSDL for web service " + webService + " not found.");
			}
			addWsdl(documentUris, wsdl);
		}

		Collection<File> files = new ArrayList<File>(documentUris.size());
		for (String documentUri : documentUris) {
			// TODO: WSDL4J generates invalid URIs (with whitespace)
			// that will have to be escaped
			URI uri = URI.create(documentUri.replace(" ", "%20"));
			files.add(new File(uri));
		}

		createArchive(workingDir, files, classifier);
	}

	private void addWsdl(Collection<String> documentUris, Definition wsdl) throws WSDLException {
		String documentUri = wsdl.getDocumentBaseURI();
		if (documentUris.add(documentUri)) {
			for (Collection<Import> imports : (Collection<Collection<Import>>) wsdl.getImports().values()) {
				for (Import _import : imports) {
					Definition importedWsdl = getWsdlReader().readWSDL(documentUri, _import.getLocationURI());
					addWsdl(documentUris, importedWsdl);
				}
			}

			Types types = wsdl.getTypes();
			if (types != null) {
				for (ExtensibilityElement extensibilityElement : (Collection<ExtensibilityElement>) types
						.getExtensibilityElements()) {
					if (extensibilityElement instanceof Schema) {
						Schema schema = (Schema) extensibilityElement;
						for (Collection<SchemaImport> schemaImports : (Collection<Collection<SchemaImport>>) schema
								.getImports().values()) {
							for (SchemaImport schemaImport : schemaImports) {
								if (null != schemaImport.getReferencedSchema()) 
									addSchema(documentUris, schemaImport.getReferencedSchema());
							}
						}
						for (SchemaReference schemaInclude : (Collection<SchemaReference>) schema.getIncludes()) {
							addSchema(documentUris, schemaInclude.getReferencedSchema());
						}
					}
				}
			}
		}
	}

	private void addSchema(Collection<String> documentUris, Schema schema) {
		if (documentUris.add(schema.getDocumentBaseURI())) {
			for (Collection<SchemaImport> schemaImports : (Collection<Collection<SchemaImport>>) schema.getImports().values()) {
				for (SchemaImport schemaImport : schemaImports) {
					addSchema(documentUris, schemaImport.getReferencedSchema());
				}
			}
			for (SchemaReference schemaInclude : (Collection<SchemaReference>) schema.getIncludes()) {
				addSchema(documentUris, schemaInclude.getReferencedSchema());
			}
		}
	}

	private Map<QName, Definition> getWebServiceToWsdlMap(File workingDir) throws IOException, WSDLException,
			MojoFailureException {
		Map<QName, Definition> webServiceToWsdlMap = new HashMap<QName, Definition>();

		Collection<File> wsdlFiles = FileUtils.getFiles(workingDir, "**/*.wsdl", null);
		for (File wsdlFile : wsdlFiles) {
			Definition wsdl = getWsdlReader().readWSDL(wsdlFile.getAbsolutePath());
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

	private void extractDependendentResources(File workingDir) throws ArchiverException, IOException, NoSuchArchiverException {
		UnArchiver unArchiver = archiverManager.getUnArchiver("zip");
		for (Artifact artifact : (Collection<Artifact>) project.getCompileArtifacts()) {
			if (TYPE_WPS_LIBRARY_JAR.equals(artifact.getType()) || TYPE_SERVICE_SPECIFICATION.equals(artifact.getType()) || TYPE_MESSAGE_SPECIFICATION.equals(artifact.getType())) {
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

	private void createArchive(File baseDir, Collection<File> files, String classifier) throws ArchiverException, IOException,
			NoSuchArchiverException {
		Archiver archiver = archiverManager.getArchiver("zip");

		File wsdlZipArtifactFile = new File(project.getBuild().getDirectory(), project.getBuild().getFinalName() + "-"
				+ classifier + "." + wsdlInterfaceArtifactHandler.getExtension());
		archiver.setDestFile(wsdlZipArtifactFile);

		for (File file : files) {
			String destFileName = pathTranslator.unalignFromBaseDirectory(file.getAbsolutePath(), baseDir);
			archiver.addFile(file, destFileName);
		}

		archiver.createArchive();
		projectHelper.attachArtifact(project, TYPE_WSDL_INTERFACE, classifier, wsdlZipArtifactFile);
	}

	private Collection<QName> getExportedWebServices() throws IOException, JDOMException {
		if (webServiceExportXPath == null) {
			webServiceExportXPath = XPath
					.newInstance("//esbBinding[@xsi:type='webservice:WebServiceExportBinding' or @xsi:type='jaxws:JaxWsExportBinding']");
		}
		return getWebServices("**/*.export", webServiceExportXPath);
	}

	private Collection<QName> getImportedWebServices() throws IOException, JDOMException {
		if (webServiceImportXPath == null) {
			webServiceImportXPath = XPath
					.newInstance("//esbBinding[@xsi:type='webservice:WebServiceImportBinding' or @xsi:type='jaxws:JaxWsImportBinding']");
		}
		return getWebServices("**/*.import", webServiceImportXPath);
	}

	private Collection<QName> getWebServices(String filenamePattern, XPath xpath) throws IOException, JDOMException {
		Collection<QName> webServices = new HashSet<QName>();

		Collection<File> files = FileUtils.getFiles(project.getBasedir(), filenamePattern, null);
		getLog().debug("Found " + files.size() + " files matching pattern " + filenamePattern + " in module");
		for (File file : files) {
			Document fileDocument = getSAXBuilder().build(file);
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

	private SAXBuilder getSAXBuilder() {
		if (saxBuilder == null) {
			saxBuilder = new SAXBuilder();
		}
		return saxBuilder;
	}

	private WSDLReader getWsdlReader() throws WSDLException {
		if (wsdlReader == null) {
			wsdlReader = WSDLFactory.newInstance().newWSDLReader();
			wsdlReader.setFeature("javax.wsdl.verbose", verbose);
			wsdlReader.setFeature("javax.wsdl.importDocuments", false);
		}
		return wsdlReader;
	}
}
