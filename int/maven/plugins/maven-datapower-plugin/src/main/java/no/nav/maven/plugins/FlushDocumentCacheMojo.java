package no.nav.maven.plugins;

import no.nav.datapower.xmlmgmt.XMLMgmtException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * 
 * Goal which flushes the document cache in the specified domain
 * 
 * @goal flushDocumentCache
 * 
 * @author Christer Idland
 *
 */
public class FlushDocumentCacheMojo extends AbstractDeviceMgmtMojo {
	
	 /**
     * 
     * @parameter expression="${domain}"
     * @required
     */    
    private String domain;
	
	/**
     * 
     * @parameter expression="${xmlManager}"
     * @required
     */
    private String xmlManager;
	
	protected void doExecute() throws MojoExecutionException, MojoFailureException {
		getLog().info("Executing FlushDocumentCacheMojo: flushing " + xmlManager + " @ " + domain);
		try {
			String responseCode = getXMLMgmtSession().flushDocumentCache(xmlManager, domain);
			getLog().info("Response code form datapower on flushing document cache: " + responseCode.replace("\n","").replace("\r",""));
			
		} catch (XMLMgmtException e) {
			throw new MojoExecutionException("Failed to flush document cache on " + xmlManager + " on the DataPower device",e);
		}
	}
}