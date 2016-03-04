package no.nav.maven.utilities.sca;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

import org.apache.maven.model.Dependency;
import org.apache.maven.project.MavenProject;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * Utility class that is used to generate SCA Attributes files (sca.module.attributes or sca.library.attributes files).
 * 
 * Note: Use of the class requires dependency resolution to work as expected.
 * 
 * @see MavenProject#getArtifacts()
 * @author <a href="mailto:test@example.com">Erik Godding Boye</a>
 */
public class ScaAttributesBuilder {
	private static final Namespace TARGET_NAMESPACE = Namespace.getNamespace("scdl",
			"http://www.ibm.com/xmlns/prod/websphere/scdl/6.0.0");

	private static final String PACKAGING_WPS_MODULE_EAR = "wps-module-ear";
	private static final String PACKAGING_WPS_LIBRARY_JAR = "wps-library-jar";
	private static final String PACKAGING_SERVICE_SPECIFICATION = "service-specification";
	private static final String PACKAGING_MESSAGE_SPECIFICATION = "message-specification";

	private MavenProject project;
	private boolean versioned;
	private String versionScheme;
	private Set<BORuntimeFramework> boRuntimeFrameworks = EnumSet.of(BORuntimeFramework.VERSION6);

	public ScaAttributesBuilder(MavenProject project) {
		this.project = project;
	}

	public ScaAttributesBuilder setVersioned(boolean versioned) {
		this.versioned = versioned;
		return this;
	}
	
	public ScaAttributesBuilder setVersionScheme(String versionScheme) {
		this.versionScheme = versionScheme;
		return this;
	}

	public ScaAttributesBuilder setBusinessObjectRuntimeFrameworks(Set<BORuntimeFramework> boRuntimeFrameworks) {
		if (boRuntimeFrameworks.isEmpty()) {
			throw new IllegalArgumentException("boRuntimeFrameworks cannot be empty");
		}
		if (boRuntimeFrameworks.size() > 1 && PACKAGING_WPS_MODULE_EAR.equals(project.getPackaging())) {
			throw new IllegalArgumentException(PACKAGING_WPS_MODULE_EAR
					+ " supports exactly one business object runtime framework.");
		}
		this.boRuntimeFrameworks = boRuntimeFrameworks;
		return this;
	}

	public ScaAttributesBuilder setBusinessObjectRuntimeFramework(BORuntimeFramework boRuntimeFramework) {
		return setBusinessObjectRuntimeFrameworks(EnumSet.of(boRuntimeFramework));
	}

	@SuppressWarnings("unchecked")
	public void writeTo(Writer writer) throws IOException {
		Document document = new Document();
		Element rootElement = new Element("moduleAndLibraryAttributes", TARGET_NAMESPACE);
		
		String version;
		
		if(versioned){
			// version is set based on versionScheme, either FULL or MAJOR (default)
			
			if (versionScheme != null && "FULL".equals(versionScheme)){
				version = project.getVersion();
			} else{ //using MAJOR
				version = convertVersion(project.getVersion());
			}
		} else {
			version = "";
		}
		
		rootElement.setAttribute("versionValue", versioned ? version : "");
		rootElement.setAttribute("versionProvider", versioned && version.length() > 0 ? "IBM_VRM" : "");
		document.setRootElement(rootElement);

		Element boImplementationElement = new Element("boImplementation");
		rootElement.addContent(boImplementationElement);
		for (BORuntimeFramework boRuntimeFramework : boRuntimeFrameworks) {
			boImplementationElement.addContent(new Element(boRuntimeFramework.getElementName()));
		}

		for (Dependency dependency : (Collection<Dependency>) project.getCompileDependencies()) {
			if (PACKAGING_WPS_LIBRARY_JAR.equals(dependency.getType()) || PACKAGING_SERVICE_SPECIFICATION.equals(dependency.getType()) || PACKAGING_MESSAGE_SPECIFICATION.equals(dependency.getType())) {
				Element dependencyElement = new Element("libraryDependency", TARGET_NAMESPACE);
				dependencyElement.setAttribute("name", dependency.getArtifactId());
				dependencyElement.setAttribute("version", "");
				rootElement.addContent(dependencyElement);
			}
		}

		XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
		xmlOutputter.output(document, writer);
	}

	private String convertVersion(String version) {
		return version.substring(0, version.indexOf("."));
	}

	public void writeToDirectory(File directory) throws IOException {
		if (!directory.isDirectory()) {
			throw new IllegalArgumentException("Input parameter directory is not a directory, or directory does not exist.");
		}

		File file;

		String packaging = project.getPackaging();
		if (PACKAGING_WPS_MODULE_EAR.equals(packaging)) {
			file = new File(directory, "sca.module.attributes");
		} else if (PACKAGING_WPS_LIBRARY_JAR.equals(packaging)) {
			file = new File(directory, "sca.library.attributes");
		} else {
			throw new UnsupportedOperationException("Packaging " + packaging + " is not supported");
		}

		writeTo(new FileWriter(file));
	}
}
