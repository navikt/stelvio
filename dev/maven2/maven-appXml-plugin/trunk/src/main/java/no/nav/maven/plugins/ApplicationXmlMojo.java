package no.nav.maven.plugins;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
 * @goal changeDisplayName
 * 
 *  
 */
public class ApplicationXmlMojo extends AbstractMojo {

	private final String WORKING_DIR = "target/appXml";

	/**
	 * This parameter is the directory where the wid ear files are placed.
	 * 
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


	public void execute() throws MojoExecutionException {

		// kjører først på rotdirectory, så alle underdirectory
		doDirecotry(earDirectory);
		File[] fileNames = earDirectory.listFiles();
		for (int i = 0; i < fileNames.length; i++) {
			File filElem = fileNames[i];
			if (filElem.isDirectory()) {
				doDirecotry(filElem);
			}
		}
		
	}

	private void doDirecotry(File directory) throws MojoExecutionException {
		try {
			File[] files = directory.listFiles();
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				if (file.isFile()) {
					String fileName = file.getName();
					String moduleName = fileName
							.substring(0, fileName.length() - 4);
					String unpackDir = WORKING_DIR + "/"
							+ fileName.substring(0, fileName.length() - 4);
					getLog().info("pakker ut: " + moduleName);
					final File destination = new File(unpackDir);
					destination.mkdirs();
					ZipUtils.extract(file, destination);
					getLog().info("\tdone unpacking ear files");
					String tag = getTagName(file);
					changeDisplayName(new File(WORKING_DIR+"/"+moduleName+"/"+"/META-INF"+"/"+"application.xml"), tag);
					ZipUtils.compress(destination, file);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new MojoExecutionException("Error parsing inputfile", e);
		}
	}


	/**
	 * @param file
	 */
	private String getTagName(File file) {
		String path = file.getAbsolutePath();
		String sep = File.separator;
		int last = path.lastIndexOf(sep);
		int tmp = path.substring(0,last-1).lastIndexOf(sep);
		String tag = path.substring(tmp+1,last);
		if (earDirectory.getAbsolutePath().indexOf(sep+tag) != -1) {
			return "";
		} else {
			return tag;
		}
	}

	private void changeDisplayName(File inputFile, String tagName) throws MojoExecutionException {

		// Dett er litt klønete, men jeg får ikke x-path til å fungere.
		SAXReader reader = new SAXReader();
		try {
			Document doc = reader.read(inputFile);

			Element root = doc.getRootElement();
			Iterator iter = root.elementIterator();
			while (iter.hasNext()) {
				Element elem = (Element) iter.next();
				if (elem.getName().equals("display-name")) {
					String oldValue = elem.getStringValue();
					if (oldValue.indexOf(tagName) == -1) {
						String newVal = oldValue+" "+tagName;
						System.out.println("\tTag ikke satt, setter til: "+newVal);
						elem.clearContent();
						List tmp = new ArrayList();
						tmp.add(newVal);
						elem.setContent(tmp);
					}
				}

				}

			XMLWriter writer;
			OutputFormat format = OutputFormat.createPrettyPrint();
			writer = new XMLWriter(new FileWriter(inputFile), format);
			writer.write(doc);
			writer.close();
			
		} catch (DocumentException e) {
			throw new MojoExecutionException("Error parsing inputfile", e);
		} catch (IOException e) {
			throw new MojoExecutionException("Error writing outputfile", e);
		}
	}

}
