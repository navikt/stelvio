package no.nav.maven.plugins.datapower;

import java.io.File;
import java.net.URL;
import java.security.Security;

import no.nav.datapower.xmlmgmt.XMLMgmtSession;
import no.nav.datapower.xmlmgmt.XMLMgmtUtil;
import no.nav.datapower.xmlmgmt.net.DPHttpClient;
import no.nav.datapower.xmlmgmt.net.DPHttpClientPrintOut;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.pyx4j.log4j.MavenLogAppender;

public abstract class AbstractDeploymentMojo extends AbstractMojo {

	 /**
     * The DataPower configuration domain
     * 
     * @parameter expression="${domain}"
     * @required
     */
    private String domain;    
    
    protected String getDomain() {
    	return domain;
    }
    /**
     * 
     * @parameter expression="${trustStorePath}"
     * @required
     */
    private String trustStorePath;
    /**
     * 
     * @parameter expression="${trustStorePwd}"
     * @required
     */
    private String trustStorePwd;
    
	 /**
     * The URL of the DataPower device to manage. Note: HTTPS required:
     * 		https://hostname:5550
     * 
     * @parameter expression="${host}"
     * @required
     */
    private URL deviceUrl;

    /**
     * The XML Management interface endpoint to use
     * 
     * @parameter expression="${xmlMgmtEndpoint}" alias="xmlMgmtEndpoint"
     */    
    private String xmlMgmtEndpoint;
    
    /**
     * The device username used to authenticate the XML Management Interface
     * 
     * @parameter expression="${user}" alias="user"
     * @required
     */    
    private String username;

    /**
     * The device password used to authenticate the XML Management Interface
     * 
     * @parameter expression="${pwd}" alias="pwd"
     * @required
     */    
    private String password;

    
    /**
     * The directory in which to store the request/response correspondance
     * 
     * @parameter expression="${logDirectory}" alias="logDirectory"
     */
    private File logDirectory;
    
	/**
	 * @parameter expression="${httpClientClass}" alias="httpClientClass"
	 */
	private String httpClientClass;
	
    public AbstractDeploymentMojo(){
    	
    }
    
    private boolean isNotDPHttpClientPrintOut(){
    	return httpClientClass != null ? 
    			!httpClientClass.equalsIgnoreCase(DPHttpClientPrintOut.class.getCanonicalName()) : true; 
    }
    
    private void setupSystemProperties(){
    	//System.setProperty(prop, value)
    	//getLog().info("isNotDPHttpClientPrintOut()=" + isNotDPHttpClientPrintOut() );
    	if(trustStorePath != null && trustStorePwd != null && isNotDPHttpClientPrintOut()){
    		//Security.setProperty("ssl.SocketFactory.provider", "com.ibm.jsse2.SSLSocketFactoryImpl");
    		System.setProperty("javax.net.ssl.trustStore", trustStorePath);
    		System.setProperty("javax.net.ssl.trustStorePassword", trustStorePwd);
    		System.setProperty("javax.net.ssl.trustStoreType", "jks");
    	}
    	
    	
    }
    
    public void execute() throws MojoExecutionException, MojoFailureException {
		MavenLogAppender.startPluginLog(this);
		try {
			setupSystemProperties();
			doExecute();
		}
		finally {
			MavenLogAppender.endPluginLog(this);
		}
    }
    
    
    protected URL getDeviceUrl() {
    	return deviceUrl;
    }
    
    protected String getXmlMgmtEndpoint() {
    	if(xmlMgmtEndpoint != null){
    		xmlMgmtEndpoint = !xmlMgmtEndpoint.startsWith("/") && !getDeviceUrl().toString().endsWith("/") ? 
    							"/" + xmlMgmtEndpoint  : xmlMgmtEndpoint;	
    	} else {
    		xmlMgmtEndpoint = "";
    	}
    	return xmlMgmtEndpoint;
    }

	private XMLMgmtSession mgmtSession;
	
	public XMLMgmtSession getXMLMgmtSession() {		
		if (mgmtSession == null) {
			String xmlMgmtUrl = getDeviceUrl().toString() + getXmlMgmtEndpoint();
			getLog().info("Creating new XMLMgmtSession, host = " + getDeviceUrl() +", endpoint=" + getXmlMgmtEndpoint() + ", user = " + username + ", pwd = " + password);
			getLog().info("Registered httpClient:" + httpClientClass);
			if(StringUtils.isNotBlank(httpClientClass)){	
				DPHttpClient client = XMLMgmtUtil.createHttpClient(httpClientClass);
				mgmtSession = new XMLMgmtSession(xmlMgmtUrl,username,password,client);
			} else {
				mgmtSession = new XMLMgmtSession(xmlMgmtUrl,username,password);
			}
			mgmtSession.setLogDirectory(logDirectory);
		}
		return mgmtSession;
	}
	
	protected abstract void doExecute() throws MojoExecutionException, MojoFailureException;
}
