package no.nav.maven.plugins;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import org.apache.maven.plugin.MojoExecutionException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * Goal which that adds the Stelvio jaxrpc handler to webservices.xml
 * 
 * @goal addHandler
 * 
 *  
 */
public class JaxRPCHandlerMojo extends EarExtractor {

	public void execute() throws MojoExecutionException {

		// kjører først på rotdirectory, så alle underdirectory
		getLog().info("eardir: " + earDirectory);
		doDirectory(earDirectory);
		File[] fileNames = earDirectory.listFiles();
		for (int i = 0; i < fileNames.length; i++) {
			File filElem = fileNames[i];
			if (filElem.isDirectory()) {
				doDirectory(filElem);
			}
		}

	}

	/**
	 * This parameter is the directory where the wid ear files are placed.
	 * 
	 * @parameter
	 * @required
	 */
	protected File earDirectory;

	/**
	 * List of consumer modules without webservice interface
	 * 
	 * @parameter
	 * @required
	 */
	private Set exceptionModules;

	protected boolean changeFile(String outputDir)
			throws MojoExecutionException {

		File settings = new File(outputDir + "/" + "META-INF" + "/"
				+ "webservices.xml");

		// Detter litt klønete, men jeg får ikke x-path til å fungere.
		SAXReader reader = new SAXReader();
		final String HANDLER_NAME = "no.stelvio.common.bus.handlers.jaxrpc.StelvioCommonContextHandler";
		boolean funnet = false;
		try {
			Document doc = reader.read(settings);

			Element root = doc.getRootElement();
			Iterator iter = root.elementIterator("webservice-description");
			while (iter.hasNext()) {
				Element elem = (Element) iter.next();
				Element elem2 = elem.element("port-component");

				Iterator portIterator = elem2.elementIterator();
				while (portIterator.hasNext()) {
					Element sub = (Element) portIterator.next();
					if (sub.getName().equals("handler")) {
						Element handlerName = sub.element("handler-name");
						if (handlerName != null
								&& handlerName.getData().equals(HANDLER_NAME)) {
							funnet = true;
						}
					}
				}

				if (!funnet) {
					System.out.println("ikke funnet handler, legger til");
					Element handler = elem2.addElement("handler");
					handler.addElement("handler-name").addText(HANDLER_NAME);
					handler.addElement("handler-class").addText(HANDLER_NAME);

					XMLWriter writer;
					OutputFormat format = OutputFormat.createPrettyPrint();
					writer = new XMLWriter(new FileWriter(settings), format);
					writer.write(doc);
					writer.close();
				}

			}
		} catch (DocumentException e) {
			throw new MojoExecutionException("Error parsing inputfile", e);
		} catch (IOException e) {
			throw new MojoExecutionException("Error writing outputfile", e);
		}
		return !funnet;
	}

	protected boolean isValidFile(String fileName, boolean isFile) {
		String moduleName = fileName.substring(0, fileName.length() - 4);
		boolean valid = false;
		if (exceptionModules.contains(moduleName)) {
			System.out.println("skipper modul med navn " + moduleName);
		} else if (fileName.startsWith("nav-cons") && isFile) {
			valid = true;
			getLog().info("legger til handler i modul "+moduleName);
		}
		return valid;
	}

}
