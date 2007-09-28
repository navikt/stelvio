package no.nav.maven.plugins;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;


/**
 * Goal which that adds the Stelvio jaxrpc handler to webservices.xml
 * 
 * @goal addAuthentication
 * 
 *  
 */
public class BasicAuthMojo extends EarExtractor {

	/**
     * List of modules that needs this authentication-info added
     *
     * @parameter
     * @required
     */
    private Set modules;

	/**
	 * This parameter is the directory where the wid ear files are placed.
	 * 
	 * @parameter
	 * @required
	 */
	protected File earDirectory;
    
	
	protected boolean changeFile(String outputDir) throws MojoExecutionException {
		File settings = new File(outputDir + "/" + "META-INF" + "/" + "ibm-webservicesclient-bnd.xmi");

		SAXReader reader = new SAXReader();
		boolean funnet = false;
		try {
			Document doc = reader.read(settings);

			Element root = doc.getRootElement();
			Iterator iter = root.elementIterator("componentScopedRefs");
			while (iter.hasNext()) {				
				Element elem = (Element) iter.next();
				Element elem2 = elem.element("serviceRefs");
				
				Element elem3 = elem2.element("portQnameBindings");

				Iterator portIterator = elem3.elementIterator();
				while (portIterator.hasNext()) {
					Element sub = (Element) portIterator.next();
					if (sub.getName().equals("BasicAuth_1187868680921")) {
							funnet = true;
					}
				}

				if (!funnet) {
					getLog().info("ikke funnet basic-auth, legger til");
					Element auth = elem3.addElement("BasicAuth_1187868680921");
					
					auth.addAttribute("userid","perskort");
					auth.addAttribute("password","{xor}LzotLDQwLSs=");
					XMLWriter writer;
					OutputFormat format = OutputFormat.createPrettyPrint();
					writer = new XMLWriter(new FileWriter(settings), format);
					writer.write(doc);
					writer.close();
				} else {
					getLog().info("Basic-auth allerede lagt til.");
					
				}

			}
		} catch (DocumentException e) {
			throw new MojoExecutionException("Error parsing inputfile", e);
		} catch (IOException e) {
			throw new MojoExecutionException("Error writing outputfile", e);
		}
		
		return !funnet;
	}

	public void execute() throws MojoExecutionException {


		// kjører først på rotdirectory, så alle underdirectory
		getLog().info("eardir: "+earDirectory);
		doDirectory(earDirectory);
		File[] fileNames = earDirectory.listFiles();
		for (int i = 0; i < fileNames.length; i++) {
			File filElem = fileNames[i];
			if (filElem.isDirectory()) {
				doDirectory(filElem);
			}
		}

	}



	/* (non-Javadoc)
	 * @see no.nav.maven.plugins.EarExtractor#isValidFile(java.lang.String, boolean)
	 */
	protected boolean isValidFile(String fileName, boolean isFile) {
		String moduleName = fileName.substring(0, fileName.length() - 4);
		boolean valid = false;
		if (modules.contains(moduleName) && isFile) {
			valid = true;
			getLog().info("legger til autentisering i modul "+moduleName);
		} else {
			getLog().debug("skipper modul "+moduleName);
		}
		return valid;
	}
	
	
}