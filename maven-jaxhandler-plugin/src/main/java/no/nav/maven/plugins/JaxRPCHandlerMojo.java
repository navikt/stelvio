package no.nav.maven.plugins;

import java.io.*;
import java.util.Iterator;
import java.util.Set;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.dom4j.*;
import org.dom4j.io.*;

/**
 * Goal which that adds the Stelvio jaxrpc handler to webservices.xml
 * 
 * @goal addHandler
 * 
 * 
 */
public class JaxRPCHandlerMojo extends AbstractMojo {

	private final String TEMP_OUTPUT = "target";

	/**
	 * This parameter is the directory where the wid ear files are placed.
	 * @parameter
	 * @required
	 */
	private File earDirectory;
	
	/**
     * The maven project's helper.
     *
     * @parameter expression="${component.org.apache.maven.project.MavenProjectHelper}"
     * @required
     * @readonly
     */
    private MavenProjectHelper projectHelper;
	
	/**
     * The maven project.
     *
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

	/**
     * List of consumer modules without webservice interface
     *
     * @parameter
     * @required
     */
    private Set exceptionModules;
    
	public void execute() throws MojoExecutionException {

		String unpackDir;

		try {
			File[] files = earDirectory.listFiles();
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				String fileName = file.getName();
				String moduleName = fileName.substring(0,fileName.length()-4);
				if (exceptionModules.contains(moduleName)) {
					System.out.println("skipper modul med navn "+moduleName);				
				} else if (fileName.startsWith("nav-cons")) {
					unpackDir = TEMP_OUTPUT + "/" + "ear" + "/" + fileName.substring(0, fileName.length() - 4);
					getLog().info("pakker: "+moduleName);
					final File destination = new File(unpackDir);
					destination.mkdirs();
					ZipUtils.extract(file, destination);
					getLog().info("\tdone unpacking ear files");
					extraxtEJBJarFiles(unpackDir);
					getLog().info("\tdone unpacking and repacking jar files");
					ZipUtils.compress(destination, file);
				}
			}
		} catch (IOException e) {
			throw new MojoExecutionException("Error parsing inputfile", e);
		}

	}

	/*
	 * This method extracts the EJB jar files, and adds the handler element to
	 * webservices.xml
	 */
	private void extraxtEJBJarFiles(String unpackDir) throws MojoExecutionException, IOException {
		File dir = new File(unpackDir);
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if (file.getName().startsWith("nav-cons") && file.getName().endsWith("EJB.jar")) {
				final String outputDir = TEMP_OUTPUT + "/" + "jar" + "/" + file.getName().substring(0, file.getName().length() - 4);
				final File jarDir = new File(outputDir);
				ZipUtils.extract(file, jarDir);
				File settings = new File(outputDir + "/" + "META-INF" + "/" + "webservices.xml");
				addHandler(settings);
				ZipUtils.compress(jarDir, file);
			}

		}
	}

	private void addHandler(File inputFile) throws MojoExecutionException {
		
		// Detter litt kl�nete, men jeg f�r ikke x-path til � fungere.
		SAXReader reader = new SAXReader();
		final String HANDLER_NAME = "no.stelvio.common.bus.handlers.jaxrpc.StelvioCommonContextHandler";
		try {
			Document doc = reader.read(inputFile);

			Element root = doc.getRootElement();
			Iterator iter = root.elementIterator("webservice-description");
			while (iter.hasNext()) {
				Element elem = (Element) iter.next();
				Element elem2 = elem.element("port-component");
				
				Iterator portIterator = elem2.elementIterator();
				boolean funnet = false;
				while (portIterator.hasNext()) {
					Element sub = (Element) portIterator.next();
					if (sub.getName().equals("handler")) {
						Element handlerName = sub.element("handler-name");
						if (handlerName != null && handlerName.getData().equals(HANDLER_NAME)) {
							funnet = true;
						}
					} 
				}

				if (!funnet) {
					System.out.println("ikke funnet handler, legger til");
					Element handler = elem2.addElement("handler");
					handler.addElement("handler-name").addText(HANDLER_NAME);
					handler.addElement("handler-class").addText(HANDLER_NAME);
				}

				XMLWriter writer;
				OutputFormat format = OutputFormat.createPrettyPrint();
				writer = new XMLWriter(new FileWriter(inputFile), format);
				writer.write(doc);
				writer.close();
			}
		} catch (DocumentException e) {
			throw new MojoExecutionException("Error parsing inputfile", e);
		} catch (IOException e) {
			throw new MojoExecutionException("Error writing outputfile", e);
		}
	}

}
