package no.nav.maven.utilities.sca;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class ScaLibraryBuilder {
	private static final String SCA_LIBRARY_FILENAME = "sca.library";

	private static final Namespace TARGET_NAMESPACE = Namespace.getNamespace("scdl",
				"http://www.ibm.com/xmlns/prod/websphere/scdl/6.0.0");

	private String libraryName;
	
	public ScaLibraryBuilder(String libraryName) {
		this.libraryName = libraryName;
	}
	
	public void writeTo(Writer writer) throws IOException {
		Document document = new Document();
		Element rootElement = new Element("library", TARGET_NAMESPACE);
		document.setRootElement(rootElement);
		rootElement.setAttribute("name", libraryName);
		rootElement.setAttribute("libraryType", "shareByValue");
		XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
		xmlOutputter.output(document, writer);
	}
	
	public void writeToDirectory(File directory) throws IOException {
		if (!directory.isDirectory()) {
			throw new IllegalArgumentException("Input parameter directory is not a directory, or directory does not exist.");
		}
		File file = new File(directory, SCA_LIBRARY_FILENAME);
		FileWriter writer = new FileWriter(file);
		writeTo(writer);
		writer.close();
	}
	
	public static boolean scaLibraryFileExistsIn(File directory) {
		return new File(directory, SCA_LIBRARY_FILENAME).exists();
	}
}
