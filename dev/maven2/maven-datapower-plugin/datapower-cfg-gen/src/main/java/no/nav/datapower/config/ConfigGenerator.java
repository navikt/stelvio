package no.nav.datapower.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.wsdl.Definition;

import no.nav.datapower.util.DPWsdlUtils;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.zip.ZipEntry;
import org.codehaus.plexus.archiver.zip.ZipFile;

public abstract class ConfigGenerator {

	private EnvironmentResources environmentResources;

	private File outputDirectory;

	private Properties requiredProperties;

	private String name;

	private MavenProject mavenProject;

	private Policy[] policies;

	private ArtifactResolver artifactResolver;

	public ConfigGenerator() {
	}

	public ConfigGenerator(String name, Properties requiredProperties) {
		this.name = name;
		this.requiredProperties = requiredProperties;
	}

	public String getName() {
		return name;
	}

	public Properties getRequiredProperties() {
		return requiredProperties;
	}

	protected void setRequiredProperties(Properties props) {
		this.requiredProperties = props;
	}

	public EnvironmentResources getEnvironmentResources() {
		return environmentResources;
	}

	public void setEnvironmentResources(EnvironmentResources resources) {
		this.environmentResources = resources;
	}

	public String getEnvironmentProperty(String property) {
		return getEnvironmentResources().getProperty(property);
	}

	public void setEnvironmentProperty(String property, Object value) {
		getEnvironmentResources().getProperties().put(property, value);
	}

	public File getOutputDirectory() {
		return outputDirectory;
	}

	public void setOutputDirectory(File outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	public abstract ConfigPackage generate();

	public MavenProject getMavenProject() {
		return mavenProject;
	}

	public void setMavenProject(MavenProject mavenProject) {
		this.mavenProject = mavenProject;
	}

	public Policy[] getPolicies() {
		return policies;
	}

	public void setPolicies(Policy[] policies) {
		this.policies = policies;
	}

	@SuppressWarnings("unchecked")
	public void resolveProxies(File wsdlDir) throws IOException {
		for (Policy policy : getPolicies()) {
			System.out.println("Adding policy '" + policy.getName() + "'");
			List<WSDLFile> wsdlFiles = new ArrayList<WSDLFile>();
			for (WsdlArtifact wsdlArtifact : policy.getArtifacts()) {
				List<Artifact> artifacts = new ArrayList<Artifact>(); 
				for (Iterator iter = mavenProject.getDependencyArtifacts().iterator(); iter.hasNext();) {
					Artifact artifact = (Artifact) iter.next();
					System.out.println("Checking if " + wsdlArtifact + " matches " + artifact);
					if (wsdlArtifact.equals(artifact)) {
						artifacts.add(artifact);
					}
				}
				if (artifacts.size() != 1) {
					throw new RuntimeException("Artifact " + wsdlArtifact + " matched " + artifacts.size() + " dependencies - expected 1");
				}
				for (Artifact artifact : artifacts) {
					ZipFile zipFile = new ZipFile(artifact.getFile());
					wsdlFiles.addAll(findWsdlFiles(zipFile.getEntries(), wsdlDir));
					
				}
			}
			List<WSProxy> proxies = getProxies(wsdlFiles);
			setEnvironmentProperty(policy.getName()+"Proxies", proxies);
			setEnvironmentProperty("outboundProxies", proxies);
			setEnvironmentProperty("outboundWsdls", wsdlFiles);
		}
	}
	
	private List<WSProxy> getProxies(List<WSDLFile> wsdlFiles) {
		List<WSProxy> proxies = new ArrayList<WSProxy>();

		for (WSDLFile wsdl : wsdlFiles) {
			getProxyByName(proxies, wsdl.getProxyName()).addWsdl(wsdl);
		}
		return proxies;
	}

	private WSProxy getProxyByName(List<WSProxy> proxies, String name) {
		WSProxy proxy = null;
		for (WSProxy p : proxies) {
			if (p.getName().equals(name)) {
				proxy = p;
			}
		}
		if (proxy == null) {
			proxy = new WSProxy(name);
			proxies.add(proxy);
		}
		return proxy;
	}

	private List<WSDLFile> findWsdlFiles(Enumeration<ZipEntry> entries, File wsdlDir) {
		List<WSDLFile> wsdlFiles = new ArrayList<WSDLFile>();
		while (entries.hasMoreElements()) {
			ZipEntry zipEntry = (ZipEntry) entries.nextElement();
			String filename = zipEntry.getName();
			if (filename.endsWith(".wsdl")) {
				File file = new File(wsdlDir, zipEntry.getName());
				Definition definition = DPWsdlUtils.getDefinition(file.getPath());
				if (definition.getServices().size() > 0) { // Only keep ports, not port types
					WSDLFile wsdlFile = new WSDLFile(file, wsdlDir);
					wsdlFiles.add(wsdlFile);
				}
			}
		}
		return wsdlFiles;
	}

	public ArtifactResolver getArtifactResolver() {
		return artifactResolver;
	}

	public void setArtifactResolver(ArtifactResolver artifactResolver) {
		this.artifactResolver = artifactResolver;
	}

}