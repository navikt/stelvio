/*
 * Created on Jan 22, 2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package no.nav.maven.plugins;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
 * @author utvikler
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
/**
 * Goal which touches a timestamp file.
 *
 * @goal setupconfig
 * 
 */
public class SetupConfig extends AbstractMojo{
	
	/**
	 * This parameter is the temporary directory where the files are unpacked.
	 * @parameter expression="${propertyFile}"
	 * @required
	 */
	private String appPropsPath = "E:\\DevArch\\WAS_Environment\\cfg-psak-environment.properties" ;
	/**
	 * This parameter is the temporary directory where the files are unpacked.
	 * @parameter expression="${envFile}"
	 * @required
	 */
	private String envPropsPath = "E:\\DevArch\\WAS_Environment\\54-Server.xml" ;
	/**
	 * This parameter is the temporary directory where the files are unpacked.
	 * @parameter expression="${module}"
	 * @required
	 */
	private String moduleName = "psak" ;
	
	
	private String [] propArray = {moduleName, appPropsPath, envPropsPath} ;
	private  Dictionary appProps = null;
	private  Dictionary envProps = null;
	private  Dictionary newProps = null;
	private  String module = null;
	
	
	/* (non-Javadoc)
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	public void execute() throws MojoExecutionException, MojoFailureException {
		// TODO Auto-generated method stub
		if (parseArgs(propArray))
		{
			
			
			try {
				module = moduleName ;
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
	public void testClass(String []args) throws IOException, DocumentException {
		
		if (parseArgs(args))
		{
			
			module = args[0];
			appProps = readProperties(args[1]);
			envProps = readXmlProperties(args[2]);
			if (containsNewProperties())
			{
				throw new IOException("Property file contains new properties!\n\n" + dictToString(newProps));
			}
			createPropertyFile(args[1]);
		}
	}
	private  String dictToString(Dictionary dict){
		String ret = "";
		String key = "";
		for(Enumeration e = dict.keys(); e.hasMoreElements();){
			key = (String) e.nextElement();
			ret+= key + "=" + dict.get(key)+ System.getProperty("line.separator");
		}
		return ret ;
	}
	
	private  boolean parseArgs(String[] args){
		if (args.length != 3)
		{
			getLog().info("Invalid arguments passed!\n\nUsage: SetupConfig module appPropertyFilePath envPropertyFilePath ");
			return false;
		}
		File file = new File(args[1]);
		if (!file.exists())
		{
			getLog().info(args[1] + " does not exist!");
			return false;
		}
		File file2 = new File(args[2]);
		if (!file2.exists())
		{
			getLog().info(args[2] + " does not exist!");
			return false;
		}
		return true;
	}
	
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
	}
	
	

}
