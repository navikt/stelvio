/*
 * Created on Jan 22, 2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package no.nav.maven.plugins;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
import org.w3c.dom.NodeList;

/**
 * @author bwa2812
 */
/**
 * Goal which touches a timestamp file.
 *
 * @goal setupconfig
 * 
 */
public class SetupConfig extends AbstractMojo{
	
	/**
	 * This parameter is the directory where work is being done.
	 * @parameter expression="${workingDir}"
	 * @required
	 */
	private File workingDir ;
	/**
	 * This parameter is the directory where the version mapping file is located.
	 * @parameter expression="${mapFile}"
	 * @required
	 */
	private String versionMapFile  ;
	/**
	 * This parameter is the directory where the environment xml file is located.
	 * @parameter expression="${envFile}"
	 * @required
	 */
	private String envPropsPath  ;

	/**
	 * This parameter is the name of the module being deployed.
	 * @parameter expression="${module}"
	 * @required
	 */
	private String moduleName  ;
	
	/**
	 * This parameter is the build ID from moose.
	 * @parameter expression="${buildId}"
	 * @required
	 */
	private String buildId ;
	/**
	 * This parameter is the host IP for the server you want to deploy to.
	 * @parameter expression="${host}"
	 */
	private String host;

	/**
	 * This parameter is the username for the host.
	 * @parameter expression="${username}"
	 */
	private String username ; 
	/**
	 * This parameter is the password for the host.
	 * @parameter expression="${password}"
	 */
	private String password ;
	
	private Dictionary appProps = null;
	private Dictionary envProps = null;
	private Dictionary newProps = null;
	private String module = null;
	private String version = null ;
	private String appPropsPath = null ;

	
	/* (non-Javadoc)
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	public void execute() throws MojoExecutionException, MojoFailureException{
		// TODO Auto-generated method stub
		if(buildId!=null && versionMapFile!=null){
			try {
				version = getVersionId(buildId,versionMapFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
		if (parseArgs(versionMapFile,envPropsPath))
		{		
			try {
				module = moduleName ;
				appPropsPath = download(module, version); 
				appProps = readProperties(appPropsPath);
				envProps = readXmlProperties(envPropsPath);
				if (containsNewProperties())
				{
					throw new IOException("Property file contains new properties!\n\n" + dictToString(newProps));
				}
				createPropertyFile(appPropsPath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}		
	}
	/**
	 * Searches through the mapping file, and returns the correct Maven version id 
	 * based on the parameter bid (Moose's build id).
	 * 
	 * @param bid the BuildId from Moose
	 * @param mapFile The path of the version-mapping file
	 * @return A String representing the maven version Id for the build
	 * @throws IOException
	 */
	public String getVersionId(String bid, String mapFile) throws IOException{
		if(!new File(mapFile).exists()){
			getLog().info("The Version Map File '"+mapFile+"' can not be found");
			throw new IOException("The Version Map File '"+mapFile+"' can not be found");
		}
		BufferedReader reader = new BufferedReader(new FileReader(mapFile));
		String line ;
		while((line=reader.readLine())!=null){
			if(line.startsWith(bid)){
				version = line.substring(bid.length()+1);
			}
		}
		return version ;
	}
	
	/**
	 * Download the config-jar for the module and version you provide, and extracts the content to the $workingDir
	 * 
	 * @param module The name of the module for which to download the confg-jar.
	 * @param version The Maven version of the config-jar to download
	 * @return A string representing the path to the environmentproperties for the module.
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public String download(String module, String version) throws MalformedURLException, IOException{
		String repo = "http://maven.adeo.no/m2internal/no/nav/" + module + "-layers/config/nav-config-pensjon-" 
						+ module + "/" + version + "/nav-config-pensjon-" + module + "-" + version + ".jar";
		File inputFile = new File(workingDir.getAbsolutePath()+"\\nav-config-pensjon-" + module + "-" + version + ".jar");
		File outputFile = new File(workingDir.getAbsolutePath()+"\\nav-config-pensjon-" + module + "-" + version);
		BufferedInputStream bis = null ;
		BufferedOutputStream bos = null ;
		try{
			URL url = new URL(repo);
			URLConnection urlc = url.openConnection();
			
			bis = new BufferedInputStream(urlc.getInputStream());
			bos = new BufferedOutputStream(new FileOutputStream(inputFile));
			
			int i ;
			while((i=bis.read())!=-1){
				bos.write(i);
			}
			
		}finally{
			if(bis!=null){
				bis.close();
			}
			if(bos!=null){
				bos.close();
			}
			getLog().info("Download Done!");
		}
		
		ZipUtils.extract2(inputFile,outputFile );
		getLog().info(outputFile.toString()+"\\cfg-"+module+"-environment.properties");
		return outputFile.toString()+"\\cfg-"+module+"-environment.properties" ;
	}
	/**
	 * Strictly for error logging.
	 * If the environment properties contains new properties, a new collection is being 
	 * populated. The content of this collection is printed here.
	 * 
	 * @param dict The collection containing the new properties
	 * @return A string representation of the new properties
	 */
	private  String dictToString(Dictionary dict){
		String ret = "";
		String key = "";
		for(Enumeration e = dict.keys(); e.hasMoreElements();){
			key = (String) e.nextElement();
			ret+= key + "=" + dict.get(key)+ System.getProperty("line.separator");
		}
		return ret ;
	}
	/**
	 * The method checks if the files needed exists.
	 * 
	 * @param appFile the path to the application properties file
	 * @param mapFile the path to the version mapping file.
	 * @return boolean
	 */
	private  boolean parseArgs(String appFile, String mapFile){
		File file = new File(appFile);
		if (!file.exists())
		{
			getLog().info(appFile + " does not exist!");
			return false;
		}
		File file2 = new File(mapFile);
		if (!file2.exists())
		{
			getLog().info(mapFile + " does not exist!");
			return false;
		}
		return true;
	}
	/**
	 * Reads all the properties in the application environment file, and puts them in a collection
	 * 
	 * @param path the path to the application properties file
	 * @return Dictionary - a collection containing the properties for the application
	 * @throws IOException
	 */
	private  Dictionary readProperties(String path) throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(path));
		String line;
		Dictionary props = new Hashtable();
		
		while((line=reader.readLine())!=null)
		{
			line = line.trim();
			if (!line.startsWith("#") && line.split("=").length == 2) // ignore empty line and comment lines
			{
				props.put(line.split("=".trim())[0], line.split("=".trim())[1]);
			}
		}
		reader.close();
		return props;
		
	}
	/**
	 * Reads the environment properties from an xml-file, and puts them in a collection.
	 * 
	 * @param path the path to the environment properties xml-file to be parsed
	 * @return a collection containing the properties in the xml file
	 * @throws IOException
	 * @throws DocumentException
	 */
	private  Dictionary readXmlProperties(String path) throws IOException, DocumentException
	{
		File xmlPath = new File(path);
		NodeList list = null;
		Element prop = null ;
		Dictionary props = new Hashtable();
		
		XPath search ;
		Map uris = new HashMap();
		uris.put("ns","");

		SAXReader reader = new SAXReader();
		Document doc = reader.read(path);
		
		
		search = doc.createXPath("/ns:environment/ns:config/ns:" + module + "-properties/ns:property");
		search.setNamespaceURIs(uris);
		List results = search.selectNodes(doc);
		Iterator it = results.iterator() ;
		while(it.hasNext()){
			prop = (Element)it.next();
			
			if(prop.attributeValue("name") !=null && prop.attributeValue("value") !=null){
				props.put(prop.attributeValue("name"), prop.attributeValue("value")) ;
			}else{
				throw new DocumentException("Invalid attributes on node '" +prop.getName()+"'") ;
			}
			
			
		}
		return props;
	}
	/**
	 * The method compares the to environment files, to check if there is 
	 * a mismatch between the two. If the application properties contains more, or different, elements
	 * than the environment property, a new collection is created containing these new properties.
	 * @return boolean
	 */
	private  boolean containsNewProperties()
	{
		newProps = new Hashtable();
		String key = "";
		for(Enumeration e = appProps.keys(); e.hasMoreElements();){
			key = (String) e.nextElement();
			if(!((Hashtable)envProps).containsKey(key)){
				newProps.put(key, appProps.get(key));
			}
		}
		return newProps.size() > 0;
	}
	
	/**
	 * Writes a new environment property file, and, if the host parameter is set, uploads the
	 * config files for the module to the server provided in the host-parameter. 
	 * If the host param is not set, the config-files are compressed into a new jar-file.
	 * 
	 * @param outputPath the path to where the environment property file is to be saved.
	 * @throws IOException
	 */
	private  void createPropertyFile(String outputPath) throws IOException
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath));
		writer.write("####################################################\n");
		writer.write("#####      Moose generated property file      ######\n");
		writer.write("####################################################\n");
		getLog().info("Writing current properties to '" + outputPath + "'");
		String key = "";
		for(Enumeration e = envProps.keys(); e.hasMoreElements();){
			key = (String) e.nextElement();
			getLog().info(key + "=" + envProps.get(key));
			writer.write(key + "=" + envProps.get(key)+System.getProperty("line.separator"));
		}
		writer.close();
		getLog().info("Done!");
		if(host!=null){
			//send opp config-filene til server.
			boolean status = UploadConfig.uploadConfigFilesToHost(host, username, password, new File(outputPath).getParent(), module);
			
			if(status){
				getLog().info("Configfiles upload complete");
			}else{
				getLog().info("Configfiles upload failed");
			}
		}else{
			//pakk sammen Jar-filen igjen.
			ZipUtils.compress(new File(new File(appPropsPath).getParent()), new File(new File(appPropsPath).getParent()+".jar"));
			getLog().info("Config-Jar made");
		}
	}
	
	

}
