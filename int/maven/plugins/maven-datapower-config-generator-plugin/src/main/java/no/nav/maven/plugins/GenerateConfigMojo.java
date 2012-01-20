package no.nav.maven.plugins;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.wsdl.Definition;

import no.nav.datapower.config.Policy;
import no.nav.datapower.config.WSDLFile;
import no.nav.datapower.config.WSProxy;
import no.nav.datapower.config.WsdlArtifact;
import no.nav.datapower.config.freemarker.Freemarker;
import no.nav.datapower.util.DPCollectionUtils;
import no.nav.datapower.util.DPPropertiesUtils;
import no.nav.datapower.util.DPWsdlUtils;
import no.nav.datapower.util.PropertiesBuilder;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.InvalidRepositoryException;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.ArtifactRepositoryFactory;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolutionResult;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.apache.maven.project.ProjectUtils;
import org.apache.maven.project.artifact.InvalidDependencyVersionException;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.UnArchiver;

import freemarker.template.TemplateException;

/**
 * Goal which generates a DataPower configuration.
 * 
 * @goal generate
 * @phase compile
 * 
 * @author Øystein Gisnås, Accenture
 */
public class GenerateConfigMojo extends AbstractMojo {

	private static final String MAIN_TEMPLATE = "main.ftl";

	/**
	 * The maven project
	 * 
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

	/**
	 * Policies to configure proxies for, containing wsdl-interface dependencies
	 * 
	 * @parameter
	 * @required
	 */
	private Policy[] policies;
	
	/**
	 * Artifact repository factory component.
	 * 
	 * @component
	 * @readonly
	 * @required
	 */
	private ArtifactRepositoryFactory artifactRepositoryFactory;

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
	 * The local repository taken from Maven's runtime. Typically $HOME/.m2/repository.
	 * 
	 * @parameter expression="${localRepository}"
	 * @readonly
	 * @required
	 */
	private ArtifactRepository localRepository;

	/**
	 * @parameter expression="${session}"
	 * @readonly
	 * @required
	 */
	private MavenSession mavenSession;

	/**
	 * The remote repositories used as specified in your POM.
	 * 
	 * @parameter expression="${project.repositories}"
	 * @readonly
	 * @required
	 */
	private List repositories;

	/**
	 * secgw or partner-gw
	 * 
	 * @parameter expression="${gateway-name}"
	 * @readonly
	 * @required
	 */
	private String gateway;	
	
	/**
	 * 
	 * @parameter
	 * @required
	 */
	private WsdlDependency[] wsdlDeps;	
	
	/**
	 * @component roleHint="zip"
	 * @required
	 * @readonly
	 */
	private UnArchiver unArchiver;
	
	/** @component */
	private ArtifactResolver resolver;
	
	/** @component */
	private MavenProjectBuilder mavenProjectBuilder;
	
	/** @component */
	private ArtifactMetadataSource artifactMetadataSource;

	// Properties for template interpolation
	private Properties properties;

	private List remoteRepos;

	private File localFilesDir;

	/**
	 * The mojo method doing the actual work when goal is invoked
	 */
	public void execute() throws MojoExecutionException, MojoFailureException {
		// Welcome
		File outputDirectory = new File(project.getBuild().getOutputDirectory());
		File localFilesDirectory = new File(outputDirectory, "files/local");
		File wsdlFilesDirectory = new File(localFilesDirectory, "/wsdl");
		wsdlFilesDirectory.mkdir();
		unArchiver.setDestDirectory(wsdlFilesDirectory);
		
		File propertiesFile = new File(project.getBasedir(), "target/dependency/busconfiguration-jar/datapower/" + gateway+ "/src/main/filters/main.properties");
		getLog().info("Generating Datapower config");
		getLog().debug("ConfigDirectory=" + outputDirectory);
		File tempDir = new File(project.getBasedir(), "target/temp");
		tempDir.mkdir();
		File configFile = new File(tempDir, "configuration.xml");
		getLog().debug("ConfigFile=" + configFile);

		properties = loadProperties(propertiesFile.getPath());
		// Create list of trusted partner certificates and add to properties
		addTrustCerts(properties);

		// Instantiate the remote repositories object
		try {
			remoteRepos = ProjectUtils.buildArtifactRepositories(repositories, artifactRepositoryFactory, mavenSession.getContainer());
		} catch (InvalidRepositoryException e) {
			throw new MojoExecutionException("[ERROR] Error building remote repositories", e);
		}

		// Map WSDLs to proxies
		try {
			mapWSDLsToProxies(policies, wsdlFilesDirectory, localFilesDirectory);
		} catch (IOException e) {
			throw new IllegalStateException("Error while mapping from WSDLs to proxies", e);
		} catch (ArtifactResolutionException e) {
			throw new MojoExecutionException("Unable to resolve interface artifact", e);
		} catch (ArtifactNotFoundException e) {
			throw new MojoExecutionException("Unable to find interface artifact", e);
		} catch (ArchiverException e) {
			throw new MojoExecutionException("An error occured during extraction of WSDL interface ZIP", e);
		}

		// Merge templates with properties and output to config file
		try {
			processFreemarkerTemplates(configFile);
		} catch (IOException e) {
			throw new IllegalStateException("Caught IOException while generating DataPower configuration", e);
		} catch (TemplateException e) {
			throw new IllegalStateException("Caught IllegalStateException while generating DataPower configuration", e);
		}
	}

	/*
	 * Load properties from property file
	 */
	private Properties loadProperties(String propertiesFile) {
		Properties properties = DPPropertiesUtils.load(propertiesFile);
		PropertiesBuilder builder = new PropertiesBuilder();
		builder.putAll(properties);
		builder.interpolate();
		return builder.buildProperties();
	}

	/*
	 * Expand trust certificate property and inject new expanded property TODO
	 * Move out of the generic configuration generator
	 */
	private void addTrustCerts(Properties props) {
		if (props.getProperty("partnerTrustCerts") != null) {
			List<String> trustCertList = DPCollectionUtils.listFromString(props.getProperty("partnerTrustCerts"));
			List<Map<String, String>> trustCertMapList = DPCollectionUtils.newArrayList();
			for (String trustCert : trustCertList) {
				Map<String, String> cert = DPCollectionUtils.newHashMap();
				cert.put("name", trustCert.substring(trustCert.lastIndexOf("/") + 1));
				cert.put("file", trustCert);
				trustCertMapList.add(cert);
			}
			props.put("partnerTrustedCerts", trustCertMapList);
		}
	}

	/*
	 * Go through list of policies containing lists of WSDLs and add a property
	 * to 'properties' for the list of proxies and WSDLs for each policy
	 */
	private void mapWSDLsToProxies(Policy[] policies, File wsdlFilesDir, File localFilesDir) throws IOException, ArtifactResolutionException, ArtifactNotFoundException, ArchiverException, MojoExecutionException {
		// Map each policy as specified in the configuration
		for (Policy policy : policies) {
			getLog().info("Adding WSDLs for policy '" + policy.getName() + "'");
			Set<WSDLFile> wsdlFiles = new LinkedHashSet<WSDLFile>();
			// Try to match groupId/artifactId in configuration against the list
			// of dependencies
			ArtifactResolutionResult arr;
			
			for (WsdlDependency dep : wsdlDeps){

				Artifact pomArtifact = artifactFactory.createArtifact(dep.getGroupId(), dep.getArtifactId(), dep.getVersion(), null, dep.getType());
				
				if (dep.getType().equals("pom")){
					MavenProject pomProject = null;
					try {
						pomProject = mavenProjectBuilder.buildFromRepository( pomArtifact, remoteRepos, localRepository);
					} catch (ProjectBuildingException e) {
						throw new MojoExecutionException("Unable to build project, " + e);
					}
					Set<?> artifacts = null;
					try {
						artifacts = pomProject.createArtifacts(this.artifactFactory, null, null);
					} catch (InvalidDependencyVersionException e) {
						throw new MojoExecutionException("Invalid dependency: " + e);
					}
					arr = resolver.resolveTransitively(artifacts, pomArtifact, remoteRepos, localRepository, artifactMetadataSource);
				
		
					if (arr.getArtifacts().size() == 0){
						throw new MojoExecutionException("Unable to retrieve artifact, " + dep.toString());
					}
					
					Iterator<?> iter = arr.getArtifacts().iterator();
					while (iter.hasNext()){
						Artifact a = (Artifact) iter.next();						
						//wsdlFiles.addAll(getWsdls(policy, a, wsdlFilesDir));
					//}

						for (WsdlArtifact wsdlArtifact : policy.getArtifacts()) {
							getLog().debug("Checking if " + wsdlArtifact + " matches " + a);
							if (wsdlArtifact.equals(a) || wsdlArtifact.equals(a, "ear", null)) {
								// Direct dependency
								Artifact wsdlInterfaceArtifact = a;
								// Dependency via esb
								if (wsdlArtifact.equals(a, "ear", null)) {
									// Resolve interface artifact
									wsdlInterfaceArtifact = artifactFactory.createArtifactWithClassifier(a.getGroupId(), a.getArtifactId(), a.getVersion(), wsdlArtifact.getType(), wsdlArtifact.getClassifier());
									artifactResolver.resolve(wsdlInterfaceArtifact, remoteRepos, localRepository);
								}
								// Add all hits
								ZipFile zipFile = new ZipFile(wsdlInterfaceArtifact.getFile());
								// Extract interface from ZIP
								unArchiver.setSourceFile(wsdlInterfaceArtifact.getFile());
								unArchiver.extract();
								Set<WSDLFile> wsdls = findWsdlFiles(zipFile.entries(), wsdlFilesDir, localFilesDir, policy.isRewriteEndpoints());
								wsdlFiles.addAll(wsdls);
								getLog().debug("Added " + wsdls.size() + " WSDLs, new size of WSDL-set is " + wsdlFiles.size());
								break;
							}

						}
					}
			
				} else {
					Artifact art = artifactFactory.createArtifact(dep.getGroupId(), dep.getArtifactId(), dep.getVersion(), null, dep.getType());

					resolver.resolve(art, remoteRepos, localRepository);
//					wsdlFiles.addAll(getWsdls(policy, pomArtifact, wsdlFilesDir));

					for (WsdlArtifact wsdlArtifact : policy.getArtifacts()) {
						getLog().debug("Checking if " + wsdlArtifact + " matches " + art);
						if (wsdlArtifact.equals(art) || wsdlArtifact.equals(art, "ear", null)) {
							// Direct dependency
							Artifact wsdlInterfaceArtifact = art;
							// Dependency via esb
							if (wsdlArtifact.equals(art, "ear", null)) {
								// Resolve interface artifact
								wsdlInterfaceArtifact = artifactFactory.createArtifactWithClassifier(art.getGroupId(), art.getArtifactId(), art.getVersion(), wsdlArtifact.getType(), wsdlArtifact.getClassifier());
								artifactResolver.resolve(wsdlInterfaceArtifact, remoteRepos, localRepository);
							}
							// Add all hits
							ZipFile zipFile = new ZipFile(wsdlInterfaceArtifact.getFile());
							// Extract interface from ZIP
							unArchiver.setSourceFile(wsdlInterfaceArtifact.getFile());
							unArchiver.extract();
							Set<WSDLFile> wsdls = findWsdlFiles(zipFile.entries(), wsdlFilesDir, localFilesDir, policy.isRewriteEndpoints());
							wsdlFiles.addAll(wsdls);
							getLog().debug("Added " + wsdls.size() + " WSDLs, new size of WSDL-set is " + wsdlFiles.size());
							break;
							}

						}
					}
			
				// Since one proxy can have multiple WSDLs, make a list of proxies
				// each containing a list of WSDLs
				Collection<WSProxy> proxies = getProxies(wsdlFiles);
				// Assume naming standard "<policyname>Proxies" for property with
				// proxy list
				String proxiesVariableName = policy.getName() + "Proxies"; 
				properties.put(proxiesVariableName, proxies);
				getLog().info("Added " + proxies.size() + " proxies to template variable " + proxiesVariableName);
			}
		}
	}

	
	private Set<WSDLFile> getWsdls(Policy policy, Artifact art, File wsdlFilesDir) throws ArchiverException, IOException, ArtifactResolutionException, ArtifactNotFoundException{
		Set<WSDLFile> wsdlFiles = new LinkedHashSet<WSDLFile>();
		
		for (WsdlArtifact wsdlArtifact : policy.getArtifacts()) {
			getLog().debug("Checking if " + wsdlArtifact + " matches " + art);
			if (wsdlArtifact.equals(art) || wsdlArtifact.equals(art, "ear", null)) {
				// Direct dependency
				Artifact wsdlInterfaceArtifact = art;
				// Dependency via esb
				if (wsdlArtifact.equals(art, "ear", null)) {
					// Resolve interface artifact
					wsdlInterfaceArtifact = artifactFactory.createArtifactWithClassifier(art.getGroupId(), art.getArtifactId(), art.getVersion(), wsdlArtifact.getType(), wsdlArtifact.getClassifier());
					artifactResolver.resolve(wsdlInterfaceArtifact, remoteRepos, localRepository);
				}
				// Add all hits
				ZipFile zipFile = new ZipFile(wsdlInterfaceArtifact.getFile());
				// Extract interface from ZIP
				unArchiver.setSourceFile(wsdlInterfaceArtifact.getFile());
				unArchiver.extract();
				Set<WSDLFile> wsdls = findWsdlFiles(zipFile.entries(), wsdlFilesDir, localFilesDir, policy.isRewriteEndpoints());
				wsdlFiles.addAll(wsdls);
				getLog().debug("Added " + wsdls.size() + " WSDLs, new size of WSDL-set is " + wsdlFiles.size());
				break;
				}
			}
		return wsdlFiles;
	}
	/*
	 * Iterates through a ZIP file and find all WSDL ports (filename *.wsdl and
	 * contains a SOAP service) The WSDL (same relative path as in the ZIP file)
	 * is lookuped in a directory and added to the list of WSDL files returned
	 */
	private Set<WSDLFile> findWsdlFiles(Enumeration<? extends java.util.zip.ZipEntry> name, File wsdlFilesDir,
			File localFilesDir, boolean rewriteEndpoints) {
		Set<WSDLFile> wsdlFiles = new LinkedHashSet<WSDLFile>();
		// Iterate through the ZIP file
		while (name.hasMoreElements()) {
			ZipEntry zipEntry = (ZipEntry) name.nextElement();
			String filename = zipEntry.getName();
			// Only check files with extension .wsdl
			if (filename.endsWith(".wsdl")) {
				File file = new File(wsdlFilesDir, zipEntry.getName());
				// Load WSDL and check if it has a port (eliminate WSDLs with
				// port type)
				Definition definition = DPWsdlUtils.getDefinition(file.getPath());
				if (definition.getServices().size() > 0) { // Only keep ports,
															// not port types
					WSDLFile wsdlFile = new WSDLFile(file, localFilesDir);
					if (rewriteEndpoints) {
						wsdlFile.rewriteEndpoint();
					}
					wsdlFiles.add(wsdlFile);
					getLog().info("Found WSDL " + wsdlFile.getProxyName());
				}
			}
		}
		return wsdlFiles;
	}

	private Collection<WSProxy> getProxies(Set<WSDLFile> wsdlFiles) {
		Map<String, WSProxy> proxies = new LinkedHashMap<String, WSProxy>();

		for (WSDLFile wsdl : wsdlFiles) {
			WSProxy proxy = new WSProxy(wsdl.getProxyName());
			if (proxies.containsKey(proxy.getName())) {
				proxy = proxies.get(proxy.getName());
				getLog().debug("Found existing new proxy " + proxy.getName() + " in WSDL " + wsdl.getPortType());
			} else {
				proxies.put(proxy.getName(), proxy);
				getLog().debug("Found new proxy " + proxy.getName() + " in WSDL " + wsdl.getPortType());
			}
			proxy.addWsdl(wsdl);
		}
		return proxies.values();
	}

	private void processFreemarkerTemplates(File configFile) throws IOException, TemplateException {
		// Prepare for fremarker template processing
		Freemarker freemarker = new Freemarker(new File(project.getBasedir(), "target/dependency/busconfiguration-jar/datapower/" + gateway + "/src/main/freemarker-templates"));
		// Open file for writing
		FileWriter writer = new FileWriter(configFile);
		// Process main template
		freemarker.processTemplate(MAIN_TEMPLATE, properties, writer);
		// Flush and close file writer
		writer.flush();
		writer.close();
	}

}
