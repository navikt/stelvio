package no.nav.maven.plugins.fixers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import no.nav.maven.plugins.common.utils.EarUtils;
import no.nav.maven.plugins.common.utils.XMLUtils;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;

/**
 * Goal which that adds the Stelvio jaxrpc handler to webservices.xml
 * 
 * @goal fixRoleBinding
 * 
 *  
 */
public class FixRoleBinding extends AbstractMojo{
	
	private Properties properties = null;
	private List<RoleBinding> roleBindings = null;
	private List<RunAsValues> runAsValues = null ;
	private File consModuleFolder = null;
	private boolean foundBindings = false;
	private boolean foundRunAsValues = false ;
	
	public void execute() throws MojoExecutionException{
		try {
			getLog().info("-------------------- Start Role Binding for [" + consModuleFolder.getName() + "]");
			setupRoleBindings();
			setupRunAsBindings();
			
			if(foundBindings){
				getLog().info("Found following bindings:");
				for(RoleBinding binding : roleBindings){
					getLog().info(binding.toString());
				}
				scanApplicationXml();
				
				createBindingFile();
			}else{
				getLog().info("No binding information found!");
			}
			getLog().info("-------------------- End Role Binding --------------------");
		} catch (Exception e) {
			throw new MojoExecutionException("Error setting up role binding!", e);
		}
	}
	
	private void setupRoleBindings() throws DocumentException{
		String[] grpNames,userNames;
		List<String> roles;
		RoleBinding binding;
		roleBindings = new ArrayList<RoleBinding>();
		
		roles = getDeclaredRoles(consModuleFolder);
		
		if(roles != null){
			for(String role : roles){
				grpNames = properties.getProperty(role + "GroupNames") != null ? properties.getProperty(role + "GroupNames").split(";") : null;
				userNames= properties.getProperty(role + "UserNames") != null ? properties.getProperty(role + "UserNames").split(";") : null;
				
				if(grpNames != null || userNames != null){
					binding = new RoleBinding();
					binding.setRoleName(role);
					
					if(grpNames != null) binding.setGroupNames(Arrays.asList(grpNames));
					if(userNames != null) binding.setUserNames(Arrays.asList(userNames));
					
					roleBindings.add(binding);
					foundBindings = true;
				}
			}
		}
	}
	
	private void setupRunAsBindings() throws DocumentException  {
		String userName, passwd ;
		List<String> roles ;
		RunAsValues runAs ;
		runAsValues = new ArrayList<RunAsValues>();
		roles = getDeclaredRoles(consModuleFolder);
		
		if(roles!=null){
			for(String role : roles){
				userName = properties.getProperty(role+"UserName");
				passwd = properties.getProperty(role+"Password") ;
				
				if(userName!=null || passwd != null){
					runAs = new RunAsValues();
					
					if(userName != null) runAs.setUserName(userName) ;
					if(passwd != null) runAs.setPasswd(passwd) ;
					
					runAsValues.add(runAs);
					foundRunAsValues = true ;
				}
			}
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private List<String> getDeclaredRoles(File consModuleFolder) throws DocumentException{
		Document doc = null;
		List<Element> roles = null;
		List<String> roleNames = null;
		XPath search = null;
		SAXReader reader = null;
		Map<String, String> uris = null;
		
		File ejbJar = new File(consModuleFolder.getAbsolutePath() + EarUtils.EJB_SUBPATH +"/META-INF/ejb-jar.xml");
		if(ejbJar.exists()){
			reader = new SAXReader();
			uris = new HashMap<String, String>();
			
			doc = reader.read(ejbJar);
			uris.put("ns", "http://java.sun.com/xml/ns/j2ee");

			search = doc.createXPath("/ns:ejb-jar/ns:enterprise-beans/ns:session[@id='Module']/ns:security-role-ref");
			search.setNamespaceURIs(uris);
			roles = (List<Element>) search.selectNodes(doc);
			
			roleNames = new ArrayList<String>();
			for(Element role : roles){
				if(role.element("role-name") != null) roleNames.add(role.element("role-name").getTextTrim());
			}
		}
		if(roleNames != null && roleNames.size() > 0){
			//adding Trafikanten
			
			getLog().info("Found following roles in module:");
			for(String roleName : roleNames) getLog().info(roleName);
			
			return roleNames;
		}
		getLog().info("No roles defined in module!");
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private void scanApplicationXml() throws DocumentException, IOException{
		Document doc;
		SAXReader reader;
		Element appNode, newRole;
		XPath search;
		Map<String, String> uris = new HashMap();

		getLog().info("Editing application.xml");

		reader = new SAXReader();
		doc = reader.read(new File(consModuleFolder.getAbsolutePath()
				+ "/META-INF/application.xml"));

		// removing any existing role definitions
		uris = new HashMap<String,String>();
		uris.put("ns", "http://java.sun.com/xml/ns/j2ee");
		search = doc.createXPath("/ns:application/ns:security-role");
		search.setNamespaceURIs(uris);
		List<Element> roles = (List<Element>)search.selectNodes(doc);
		for(Element role : roles){
			role.getParent().remove(role);
		}
		
		//adding new role bindings
		appNode = doc.getRootElement();
		for(RoleBinding binding : roleBindings){
			newRole = appNode.addElement("security-role");
		
			newRole.addAttribute("id", "SecurityRole_" + binding.getId());
			newRole.addElement("description").setText(binding.getRoleName());
			newRole.addElement("role-name").setText(binding.getRoleName());
		}
		
		XMLUtils.writeXMLDocument(doc, new File(consModuleFolder.getAbsolutePath()
				+ "/META-INF/application.xml"));
	}
	
	
	private void createBindingFile() throws IOException {
		Document doc;
		Element root, authTable, appNode, runAsMap;
		
		root = DocumentHelper.createElement("com.ibm.ejs.models.base.bindings.applicationbnd:ApplicationBinding");
		root.addAttribute("xmi:version", "2.0");
		root.addAttribute("xmlns:xmi", "http://www.omg.org/XMI");
		root.addAttribute("xmlns:com.ibm.ejs.models.base.bindings.applicationbnd", "applicationbnd.xmi");
		root.addAttribute("xmlns:com.ibm.ejs.models.base.bindings.commonbnd","commonbnd.xmi");
		root.addAttribute("xmi:id", "ApplicationBinding_" + new Date().getTime());
		
		doc = DocumentHelper.createDocument(root);
		doc.setXMLEncoding("UTF-8");
		
		authTable = root.addElement("authorizationTable");
		authTable.addAttribute("xmi:id", "AuthorizationTable_" + new Date().getTime() + 1);
		
		for(RoleBinding binding : roleBindings){
			authTable.add(binding.getAuthorizationNode());
		}
		appNode = root.addElement("application");
		appNode.addAttribute("href", "META-INF/application.xml#Application_ID");
		
		if(foundRunAsValues){
			runAsMap = root.addElement("runAsMap");
			runAsMap.addAttribute("xmi:id","RunAsMap_"+RunAsValues.getId());
			
			for(RunAsValues value : runAsValues){
				runAsMap.add(value.getAuthenticationNode());
			}
		}	
		
		XMLUtils.writeXMLDocument(doc, new File(consModuleFolder.getAbsolutePath() + "/META-INF/ibm-application-bnd.xmi"));
	}

	
	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public File getConsModuleFolder() {
		return consModuleFolder;
	}

	public void setConsModuleFolder(File consModuleFolder) {
		this.consModuleFolder = consModuleFolder;
	}

	public List<RoleBinding> getRoleBindings() {
		return roleBindings;
	}

	public void setRoleBindings(List<RoleBinding> roleBindings) {
		this.roleBindings = roleBindings;
	}

	public List<RunAsValues> getRunAsValues() {
		return runAsValues;
	}

	public void setRunAsValues(List<RunAsValues> runAsValues) {
		this.runAsValues = runAsValues;
	}
	
}
