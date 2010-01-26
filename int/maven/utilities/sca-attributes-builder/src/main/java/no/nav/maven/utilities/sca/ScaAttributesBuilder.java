package no.nav.maven.utilities.sca;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.apache.maven.model.Dependency;
import org.apache.maven.project.MavenProject;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class ScaAttributesBuilder {
	private static final Namespace TARGET_NAMESPACE = Namespace.getNamespace(
			"scdl", "http://www.ibm.com/xmlns/prod/websphere/scdl/6.0.0");

	private static final String TYPE_WPS_LIBRARY_JAR = "wps-library-jar";

	private MavenProject project;

	public ScaAttributesBuilder(MavenProject project) {
		this.project = project;
	}

	public void writeTo(Writer writer) throws IOException {
		Document document = new Document();
		Element rootElement = new Element("moduleAndLibraryAttributes",
				TARGET_NAMESPACE);
		rootElement.setAttribute("versionValue", "");
		rootElement.setAttribute("versionProvider", "");
		document.setRootElement(rootElement);

		rootElement.addContent(new Element("boImplementation")
				.addContent(new Element("emf")));

		List<Dependency> dependencies = project.getDependencies();
		for (Dependency dependency : dependencies) {
			if (TYPE_WPS_LIBRARY_JAR.equals(dependency.getType())) {
				Element dependencyElement = new Element("libraryDependency",
						TARGET_NAMESPACE);
				dependencyElement.setAttribute("name", dependency
						.getArtifactId());
				dependencyElement.setAttribute("version", "");
				rootElement.addContent(dependencyElement);
			}
		}

		XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
		xmlOutputter.output(document, writer);
	}
}
