package no.nav.maven.plugins;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * Maven Goal that alters the META-INF/ejb-jar.xml file by changing handler-name and handler-class:
 *  
 * @goal fixArenaSak
 */
public class FixArenaSak extends AbstractMojo {

	/**
	 * This parameter is the directory where the wid ear files are placed.
	 * 
	 * @parameter expression="${handlerName}"
	 * @required
	 */
	protected String handlerName; // = "ArenaResponseHandler";

	/**
	 * This parameter is the directory where the wid ear files are placed.
	 * 
	 * @parameter expression="${handlerClass}"
	 * @required
	 */
	protected String handlerClass; // = "no.nav.java.ArenaResponseHandler";
	
	/**
	 * This parameter is the directory where the wid ear files are placed.
	 * 
	 * @parameter expression="${earDirectory}"
	 * @required
	 */
	protected File earDirectory; // = new File("E:/ControllerScript/kjempen/target/classes/builds/eardist");


	public void execute() throws MojoExecutionException {
		File ejb = null;
		
		//checking if arenasak is present
		File arenasak = findArenaSak();
		if(arenasak != null){
			getLog().info("Fixing nav-prod-sak-arenaApp...");
			
			//extracting ear
			File tmp = new File(earDirectory.getAbsolutePath() + "/" + new Date().getTime());
			tmp.mkdirs();
			try {
				ZipUtils.extract(arenasak, tmp);
				
				ejb = new File(tmp + "/ejb");
				ejb.mkdirs();
				ZipUtils.extract(new File(tmp + "/nav-prod-sak-arenaEJB.jar"), ejb);
			} catch (IOException e) {
				throw new MojoExecutionException("Error extracting nav-prod-sak-arenaApp: " + e.getMessage());
			}
			
			try {
				if(changeEjbJar(new File(ejb.getAbsolutePath() + "/META-INF/ejb-jar.xml"))){ //true if changes made
					try {
						getLog().info("Compressing nav-prod-sak-arena.ear...");
						delete(new File(tmp.getAbsolutePath() + "/nav-prod-sak-arenaEJB.jar"));
						ZipUtils.compress(ejb, new File(tmp.getAbsolutePath() + "/nav-prod-sak-arenaEJB.jar"));
						
						delete(ejb);
						delete(new File(earDirectory.getAbsolutePath() + "/nav-prod-sak-arena.ear"));
						delete(arenasak);
						
						ZipUtils.compress(tmp, arenasak);
						
						delete(tmp);
					} catch (IOException e) {
						throw new MojoExecutionException("Error compressing nav-prod-sak-arenaApp.ear: " + e.getMessage());
					}
				}
			} catch (Exception e) {
				throw new MojoExecutionException("Error reading ejb-jar.xml: " + e.getMessage());
			}
			
			getLog().info("Done!");
		}
	}

	private boolean changeEjbJar(File ejbjar) throws DocumentException, IOException{
		Document doc;
		SAXReader reader;
		Element handler;
		XPath search;
		HashMap uris = new HashMap();
		
		getLog().info("Opening ejb-jar.xml...");
		
		reader = new SAXReader();
		doc = reader.read(ejbjar);
		uris.put("ns","http://java.sun.com/xml/ns/j2ee");
		
		search = doc.createXPath("/ns:ejb-jar/ns:enterprise-beans/ns:session[@id='Module']/ns:service-ref/ns:handler");
		//search = doc.createXPath("/ns:ejb-jar/ns:enterprise-beans/ns:session");
		search.setNamespaceURIs(uris);
		handler = (Element)search.selectSingleNode(doc);
		if(handler != null){
			if(handler.element("handler-name") != null){
				getLog().info("handler-name changed from '" + handler.element("handler-name").getText() + "' to '" + handlerName + "'");
				handler.element("handler-name").setText(handlerName);
			}
			if(handler.element("handler-class") != null){
				getLog().info("handler-class changed from '" + handler.element("handler-class").getText() + "' to '" + handlerClass + "'");
				handler.element("handler-class").setText(handlerClass);
			}
			
			writeXml(doc, ejbjar);
			return true;
		}else{
			getLog().warn("Unable to find session bean with id 'Module'!");
			return false;
		}
	}
	
	private boolean delete(File path){
		if(path.isDirectory()){
			File[] children = path.listFiles();
			for(int i = 0; i < children.length; i++){
				boolean success = delete(children[i]);
				if(!success){
					return false;
				}
			}
		}
		System.gc();
		return path.delete();
		
	}

	private void writeXml(Document doc, File out) throws IOException
	{
		out.delete();
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out), "UTF-8"));
		XMLWriter writer = new XMLWriter(bw ,OutputFormat.createPrettyPrint());
		writer.write(doc);
		writer.close();
	}

	private File findArenaSak(){
		File[] arenaFolder = earDirectory.listFiles(new FilenameFilter() {
		
			public boolean accept(File dir, String name) {
				// TODO Auto-generated method stub
				return name.endsWith("sah_arenasak");
			}
		
		});
		
		if(arenaFolder != null && arenaFolder.length == 1){
			File arenaSak = new File(arenaFolder[0].getAbsolutePath() + "/nav-prod-sak-arena.ear");
			if(arenaSak.exists()) return arenaSak;
		}
		
		return null;
	}
}