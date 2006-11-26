package no.stelvio.web.security.page.parse;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import no.stelvio.web.security.page.PageSecurityFileNotFoundException;
import no.stelvio.web.security.page.PageSecurityFileParseException;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.RuleSetBase;
import org.xml.sax.SAXException;

/**
 * <p>
 * Concrete implementation of the <code>SecurityConfiguration</code> interface. This class obtains 
 * security configuration information from an XML file and stores it in a 
 * <code>JSFApplication</code> object.
 * </p>
 * <p>
 * The filename is obtained from a context parameter and parsed using Apache Digester. Digester
 * is a layer on top of the SAX XML parser API to make it easier to process XML
 * input.
 * </p>
 * <p>
 * The Digester class processes the input XML document based on patterns
 * and rules. The patterns must match XML elements, based on their name and
 * location in the document tree. The ruleset specified for this class' digester conforms
 * with the XML schema, faces-security-config.xsd.  
 * </p>
 * 
 * @author persondab2f89862d3
 * @version $Id$
 */
public class SecurityConfigurationXML implements SecurityConfiguration{


	private JSFApplication jsfApplication;
	private URL fileURL;
	
    
    /**
     * Parses the XML file specified by the url parameter.
     * 
     * @param url the url of the configuration file resource.
     */
    public SecurityConfigurationXML(URL url) {

    	this.fileURL = url;
    	setUpJSFApplication();
    }
    
    /**
     * Private helper method to set up the digester, parse the file and store the parsed
     * representation in the class' internal JSFApplication object. 
     * 
     * @throws MalformedURLException if the url is in incorrect format.
     * @throws SAXException if the parsing of the XML fails.
     * @throws IOException if the specified file cannot be found.
     */
    private void setUpJSFApplication(){
    	if(this.jsfApplication == null && this.fileURL != null){
    		try{
        			Digester digester = new Digester();
        			//don't validate the XML compared to the schema. This setting may change
        			//or made an option in a further version of this class. For now we don't
        			//define application object
        			digester.setValidating(false);
        			digester.addRuleSet(new DefineSecurityRuleSet());
        			this.jsfApplication = parseSecurityXMLFile(digester, this.fileURL);
        			
            } catch (SAXException sax){
                	throw new PageSecurityFileParseException(sax,this.fileURL.toString());
            } catch (IOException io){
                	throw new PageSecurityFileNotFoundException(io.getCause(), this.fileURL.toString());
            }
            
    	}	
    }
    
    /**
     * Private helper method which parses the file specified by URL url using Digester d and returns
     * the security configuration information as a JSFPage object.
     * 
     * @param d the digester used to parse the file.
     * @param url the location of the file.
     * @return a JSFApplication object containing security information.
     * @throws IOException if the file cannot be found.
     * @throws SAXException if the file cannot be parsed.
     */
    private JSFApplication parseSecurityXMLFile(Digester d, URL url) throws IOException, SAXException { 
        JSFApplication jsf = (JSFApplication)d.parse(url.toString());            
        return jsf;
    }
    
    /**
     * Gets the JSFApplication object containing all JSF page security requirements.
     * @return the jsfApplication
     */
    public JSFApplication getJsfApplication() {
        return this.jsfApplication;
    }
    

    /**
     * Internal class DefineSecurityRuleSet contains the DigesterRules to
     * parse the security config file
     */
    class DefineSecurityRuleSet extends RuleSetBase{

    	/**
    	 * {@inheritDoc} 
    	 */
        @Override
		public void addRuleInstances(Digester d) {

            d.addObjectCreate( "jsf-application", JSFApplication.class );

            //define rules for one-time ssl-config element
            d.addObjectCreate( "jsf-application/ssl-config", SSLConfig.class );
            d.addBeanPropertySetter("jsf-application/ssl-config/http-port","httpPort");
            d.addBeanPropertySetter("jsf-application/ssl-config/https-port","httpsPort");
            d.addBeanPropertySetter("jsf-application/ssl-config/keep-ssl-mode","keepSsl");
            //add completed bean to parent
            d.addSetNext( "jsf-application/ssl-config","addSslConfig");

            //define the JSF Page object
            d.addObjectCreate( "jsf-application/jsf-page", JSFPage.class );
            d.addBeanPropertySetter("jsf-application/jsf-page/page-name","pageName");

            //add element atributes
            d.addCallMethod("jsf-application/jsf-page","setRequiresAuthentication",1);
            d.addCallParam("jsf-application/jsf-page",0,"requires-authentication");

            d.addCallMethod("jsf-application/jsf-page","setRequiresAuthorization",1);
            d.addCallParam("jsf-application/jsf-page",0,"requires-authorization");

            d.addCallMethod("jsf-application/jsf-page","setRequiresSSL",1);
            d.addCallParam("jsf-application/jsf-page",0,"requires-ssl");

            d.addObjectCreate( "jsf-application/jsf-page/j2ee-roles", J2EERoles.class );

            d.addCallMethod("jsf-application/jsf-page/j2ee-roles","setRoleConcatenationType",1);
            d.addCallParam("jsf-application/jsf-page/j2ee-roles",0,"role-concatenation");

            d.addObjectCreate("jsf-application/jsf-page/j2ee-roles/role-name", J2EERole.class );
            d.addBeanPropertySetter("jsf-application/jsf-page/j2ee-roles/role-name","role");
            //add role to roles
             d.addSetNext( "jsf-application/jsf-page/j2ee-roles/role-name","addRole");
            //add security roles to the page
            d.addSetNext( "jsf-application/jsf-page/j2ee-roles", "addRoles" );
            //set jsf-page to the jsf application
            d.addSetNext( "jsf-application/jsf-page", "addJsfPage" );
        }
    }
}
