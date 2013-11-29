package no.nav.maven.plugins;

import no.nav.datapower.xmlmgmt.XMLMgmtException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * 
 * Goal which deletes the specified domain
 * 
 * @goal deleteDomain
 * 
 * @author person4fdbf4cece95, Petter Solberg
 *
 */
public class DeleteDomainMojo extends AbstractDeviceMgmtMojo {
	
	private static final String RESPONSE_START = "<dp:result>";
	private static final String RESPONSE_END = "</dp:result>";
	private static final String RESPONSE_OK = "OK";
	private static final String RESPONSE_NOT_FOUND_PREFIX = "Domain '";
	private static final String RESPONSE_NOT_FOUND_POSTFIX = "' not found";
	
	protected void doExecute() throws MojoExecutionException, MojoFailureException {
		getLog().info("Executing DeleteDomainMojo");
		try {
			getLog().info("DELETING DOMAIN:" + getDomain());
			String response =getXMLMgmtSession().deleteDomain(getDomain());
			getLog().debug("DELETE RESPONSE: \r\n" + response);
			// Throw an error if the deletion of the domain failed. Does not throw an error if the domain did not exist.
			int resultBeginningIndex =response.indexOf(RESPONSE_START, 0);
			int resultEndIndex = response.indexOf(RESPONSE_END, 0);
			if(resultBeginningIndex > 0 && resultEndIndex > resultBeginningIndex){
				if(response.substring(resultBeginningIndex, resultEndIndex).indexOf(RESPONSE_OK) > 0){
					return;
				}
				else if(response.contains(RESPONSE_NOT_FOUND_PREFIX + getDomain() + RESPONSE_NOT_FOUND_POSTFIX)){
					getLog().warn("Domain " + getDomain() + " does not exist.");
					return;
				}
			}
			throw new MojoExecutionException("Failed to delete the application domain" + getDomain() + ". Got response:\r\n" + response);
			
		} catch (XMLMgmtException e) {
			throw new MojoExecutionException("Failed to delete the application domain" + getDomain(),e);
		}
	}
}
