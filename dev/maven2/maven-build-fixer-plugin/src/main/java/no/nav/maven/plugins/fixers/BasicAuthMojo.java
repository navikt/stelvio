package no.nav.maven.plugins.fixers;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import no.nav.maven.plugins.EarExtractor;

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
 * Maven Goal that alters the META-INF/ibm-webservicesclient-bnd.xmi file and adds the following tag:
 * <basicAuth xmi:id="BasicAuth_1187868680921" userid="perskort" password="{xor}LzotLDQwLSs="/>
 *  
 * @goal addAuthentication
 */
public class BasicAuthMojo extends EarExtractor {

	/**
     * List of modules that needs this authentication-info added
     *
     * @parameter
     * @required
     */
    protected Set modules;

	/**
	 * This parameter is the directory where the wid ear files are placed.
	 * 
	 * @parameter
	 * @required
	 */
	protected File earDirectory;

	
	/**
	 * NON MAVEN VARIABLES
	 */
	private boolean isSubGoal;
	
	public BasicAuthMojo(){
		
	}
	
	public BasicAuthMojo(File flattenEarDir, Set modules){
		this.earDirectory = flattenEarDir;
		this.modules = modules;
		isSubGoal = true;
	}
	
	public void execute() throws MojoExecutionException {
		getLog().info("-------------------- Start BasicAuth --------------------");
		if(isSubGoal){
			executeSubGoal();
		}else{
			//kjører først på rotdirectory, så alle underdirectory
			getLog().info("EARdir: "+earDirectory);		
			doDirectory(earDirectory);
			File[] fileNames = earDirectory.listFiles();
			for (int i = 0; i < fileNames.length; i++) {
				File filElem = fileNames[i];
				if (filElem.isDirectory()) {
					doDirectory(filElem);
				}
			}
		}
		getLog().info("-------------------- End BasicAuth --------------------");
	}
	
	private void executeSubGoal() throws MojoExecutionException{
		//earDirectory is now set to the flattened temporary directory
		File[] tmp = earDirectory.listFiles(new FileFilter(){
			public boolean accept(File pathname) {
				return (pathname.isDirectory() && modules.contains(pathname.getName()));
			}
		});
		
		for(File targetModule : tmp){
			getLog().info("Editing '" + targetModule.getName() + "'");
			changeFile(targetModule.getAbsolutePath() + "/ejb");
		}
	}

	
	
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
					if (sub.getName().equals("basicAuth")) {
							funnet = true;
					}
				}

				if (!funnet) {
					getLog().info("Did not find basicAuth, adding it now..");
					Element auth = elem3.addElement("basicAuth");
									
					auth.addAttribute("xmi:id","BasicAuth_1187868680921");
					auth.addAttribute("userid","perskort");
					auth.addAttribute("password","{xor}LzotLDQwLSs=");
					XMLWriter writer;
					OutputFormat format = OutputFormat.createPrettyPrint();
					writer = new XMLWriter(new FileWriter(settings), format);
					writer.write(doc);
					writer.close();
				} else {
					getLog().info("basicAuth was already added.");					
				}
			}
		} catch (DocumentException e) {
			throw new MojoExecutionException("Error parsing inputfile", e);
		} catch (IOException e) {
			throw new MojoExecutionException("Error writing outputfile", e);
		}
		
		return !funnet;
	}


	/**
	 * Checks if the file is supposed to be altered.
	 */
	protected boolean isValidFile(String fileName, boolean isFile) {
		String moduleName = fileName.substring(0, fileName.length() - 4);
		boolean valid = false;
		if (modules.contains(moduleName) && isFile) {
			valid = true;
			getLog().info("Adds authentication to module "+moduleName);
		} else {
			getLog().debug("Skipping module "+moduleName);
		}
		return valid;
	}	
}