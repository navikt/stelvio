package no.nav.maven.utilities.sca;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;

import org.apache.maven.model.Dependency;
import org.apache.maven.project.MavenProject;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * Utility class that is used to generate SCA Attributes files
 * (sca.module.attributes or sca.library.attributes files).
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

	private MavenProject project;

	public ScaAttributesBuilder(MavenProject project) {
		this.project = project;
	}

	@SuppressWarnings("unchecked")
	public void writeTo(Writer writer) throws IOException {
		Document document = new Document();
		Element rootElement = new Element("moduleAndLibraryAttributes", TARGET_NAMESPACE);
		rootElement.setAttribute("versionValue", "");
		rootElement.setAttribute("versionProvider", "");
		document.setRootElement(rootElement);

		rootElement.addContent(new Element("boImplementation").addContent(new Element("emf")));

		for (Dependency dependency : (Collection<Dependency>) project.getCompileDependencies()) {
			if (PACKAGING_WPS_LIBRARY_JAR.equals(dependency.getType())) {
				Element dependencyElement = new Element("libraryDependency", TARGET_NAMESPACE);
				dependencyElement.setAttribute("name", dependency.getArtifactId());
				dependencyElement.setAttribute("version", "");
				rootElement.addContent(dependencyElement);
			}
		}

		XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
		xmlOutputter.output(document, writer);
	}

	public void writeFile(File directory) throws IOException {
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
