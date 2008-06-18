package no.nav.maven.plugins.common.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class XMLUtils {
	
	/**
	 * Writes an XML document node to a file using prettyPrint
	 * @param doc, document node to flush
	 * @param targetFile, file to flush node to
	 * @throws IOException
	 */
	public static void writeXMLDocument(Document doc, File targetFile) throws IOException{
		targetFile.delete();
		XMLWriter writer = new XMLWriter(new FileWriter(targetFile.getAbsoluteFile()),
				OutputFormat.createPrettyPrint());
		writer.write(doc);
		writer.close(); 
	}
}
