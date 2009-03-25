package no.nav.maven.plugins.fixers;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import no.nav.maven.plugins.common.utils.EarUtils;
import no.nav.maven.plugins.common.utils.XMLUtils;
import no.nav.maven.plugins.common.utils.ZipUtils;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;

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
	protected String handlerName;

	/**
	 * This parameter is a boolean to choose wether authentication should or should not be added..
	 * 
	 * @parameter expression="${isAddAuthentication}"
	 * @required
	 */
	protected final boolean isAddAuthentication;
	
	/**
	 * This parameter is the directory where the wid ear files are placed.
	 * 
	 * @parameter expression="${handlerClass}"
	 * @required
	 */
	protected String handlerClass;
	
	/**
	 * This parameter is the directory where the wid ear files are placed.
	 * 
	 * @parameter expression="${earDirectory}" 
	 * @required
	 */
	protected File earDirectory;

	/**
	 * This parameter is the directory where the wid ear files are placed.
	 * 
	 * @parameter expression="${overriddenEndpointURI}" 
	 * @required
	 */
	protected String overriddenEndpointURI;
	
	/**
	 * This parameter is the directory where the wid ear files are placed.
	 * 
	 * @parameter expression="${basicAuthUserid}" 
	 * @required
	 */
	protected String basicAuthUserid;
	
	/**
	 * This parameter is the directory where the wid ear files are placed.
	 * 
	 * @parameter expression="${basicAuthPassword}" 
	 * @required
	 */
	protected String basicAuthPassword;
	
	/**
	 * This parameter is needed to support old deployment scripts, can be removed when all scripts have been updated.
	 * 
	 * @parameter expression="${isSubGoal}" default-value="false"
	 * @required
	 */
	protected boolean isSubGoal;
	
	public FixArenaSak(){
		this.isAddAuthentication=false;
	}
	
	public FixArenaSak(File flattenEarDirectory, String handlerName, String handlerClass, String overriddenEndpointURI, String basicAuthUserid, String basicAuthPassword, final boolean addAuthentication){
		this.earDirectory = flattenEarDirectory;
		this.handlerClass = handlerClass;
		this.handlerName = handlerName;
		this.basicAuthUserid = basicAuthUserid;
		this.basicAuthPassword = basicAuthPassword;
		this.overriddenEndpointURI = overriddenEndpointURI;
		this.isAddAuthentication = addAuthentication;
		isSubGoal = true;
	}
	
	public void execute() throws MojoExecutionException {
		File ejb = null;
		
		if(isSubGoal){
			getLog().info("-------------------- Start FixArenaSak --------------------");
			executeSubGoal();
			getLog().info("-------------------- End FixArenaSak --------------------");
			return;
		}
		getLog().info("-------------------- Start FixArenaSak --------------------");
		//checking if arenasak is present
		File arenasak = findArenaSak();
		if(arenasak != null){
			getLog().info("Fixing nav-prod-sak-arenaApp...");
			
			//extracting ear
			File tmp = new File(earDirectory.getAbsolutePath() + "/" + new Date().getTime());
			tmp.mkdirs();
			try {
				ZipUtils.extract(arenasak, tmp);
				
				ejb = new File(tmp + EarUtils.EJB_SUBPATH);
				ejb.mkdirs();
				ZipUtils.extract(new File(tmp + "/nav-prod-sak-arenaEJB.jar"), ejb);
			} catch (IOException e) {
				throw new MojoExecutionException("Error extracting nav-prod-sak-arenaApp: " + e.getMessage());
			}
			
			try {
				if(changeEjbJar(new File(ejb.getAbsolutePath() + "/META-INF/ejb-jar.xml")) ||
				   changeWebservicesClientBnd(new File(ejb.getAbsolutePath() + "/META-INF/ibm-webservicesclient-bnd.xmi"))){ //true if changes made
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
		getLog().info("-------------------- End FixArenaSak --------------------");
	}
	
	private void executeSubGoal() throws MojoExecutionException{
		//earDirectory is now set to the flattened temporary directory
		File[] tmp = earDirectory.listFiles(new FileFilter(){
			public boolean accept(File pathname) {
				return (pathname.isDirectory() && pathname.getName().contains("nav-prod-sak-arena"));
			}
		});
		if(tmp != null && tmp.length >= 1){
			getLog().info("Found nav-prod-sak-arena...editing");
			try {
				changeEjbJar(new File(tmp[0].getAbsolutePath() + EarUtils.EJB_SUBPATH + "/META-INF/ejb-jar.xml"));
				
				changeWebservicesClientBnd(new File(tmp[0].getAbsolutePath() + EarUtils.EJB_SUBPATH + "/META-INF/ibm-webservicesclient-bnd.xmi"));
			} catch (Exception e) {
				throw new MojoExecutionException("Error editing xml files",e);
			}
			getLog().info("Done!");
		}
		
	}

	private boolean changeEjbJar(File ejbjar) throws DocumentException, IOException{
		Document doc;
		SAXReader reader;
		Element handler;
		XPath search;
		HashMap<String, String> uris = new HashMap<String, String>();
		
		getLog().info("Opening ejb-jar.xml...");
		
		reader = new SAXReader();
		doc = reader.read(ejbjar);
		uris.put("ns","http://java.sun.com/xml/ns/j2ee");
		
		search = doc.createXPath("/ns:ejb-jar/ns:enterprise-beans/ns:session[@id='Module']/ns:service-ref/ns:handler");
		
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
			
			XMLUtils.writeXMLDocument(doc, ejbjar);
			return true;
		}else{
			getLog().warn("Unable to find session bean with id 'Module'!");
			return false;
		}
	}
	
	
	private boolean changeWebservicesClientBnd(File ws) throws DocumentException, IOException {
		Document doc;
		SAXReader reader;
		Element binding=null, basicAuth;
		Attribute endpoint;
		XPath search;
		HashMap<String, String> uris = new HashMap<String, String>();
		
		getLog().info("Opening ibm-webservicesclient-bnd.xmi...");
		
		reader = new SAXReader();
		doc = reader.read(ws);
		uris.put("ns","http://www.ibm.com/websphere/appserver/schemas/5.0.2/wscbnd.xmi");
		
		search = doc.createXPath("//portQnameBindings");
		search.setNamespaceURIs(uris);
		//binding = (Element)search.selectSingleNode(doc);
		
		List<Element> bindings = search.selectNodes(doc);
		for(Element bind : bindings) {
			if(bind.attribute("portQnameLocalNameLink").getText().equals("ArenaServicePort")) {
				binding = bind;
				break;
			}
		}			
			
		if(binding != null){
			endpoint = binding.attribute("overriddenEndpointURI");
			if(endpoint != null) endpoint.setText(overriddenEndpointURI);
			else binding.addAttribute("overriddenEndpointURI", overriddenEndpointURI);
			
			getLog().info("[" + ws.getName() + "] Changed overriddenEndpointURI to '" + overriddenEndpointURI + "'");
			
			if(isAddAuthentication==true) {
				getLog().info("[" + ws.getName() + "] Authentication is added. isAddAuthentication flag is true");
				basicAuth = binding.addElement("basicAuth");
				basicAuth.addAttribute("xmi:id", "BasicAuth_1187868680921");
				basicAuth.addAttribute("userid", basicAuthUserid);
				basicAuth.addAttribute("password", basicAuthPassword);
				getLog().info("[" + ws.getName() + "] added basic authentication with userid=" + basicAuthUserid + " password=" + basicAuthPassword);
			} else {
				getLog().info("[" + ws.getName() + "] Authentication is not added. isAddAuthentication flag is false");
			}
			
			XMLUtils.writeXMLDocument(doc, ws);
			return true;
		}
		
		
		return false;
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

	public File getEarDirectory() {
		return earDirectory;
	}

	public void setEarDirectory(File earDirectory) {
		this.earDirectory = earDirectory;
	}

	public boolean isSubGoal() {
		return isSubGoal;
	}

	public void setSubGoal(boolean isSubGoal) {
		this.isSubGoal = isSubGoal;
	}
}