package no.nav.maven.plugins.datapower;
import no.nav.datapower.xmlmgmt.XMLMgmtException;
import no.nav.datapower.xmlmgmt.XMLMgmtRequestFactory;
import no.nav.datapower.xmlmgmt.XMLMgmtRequest;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Goal which executes a restart of the specified domain.
 *
 * @goal restartDomain
 * 
 */
public class RestartDomainMojo extends AbstractDeploymentMojo {

	protected void doExecute() throws MojoExecutionException, MojoFailureException {
		try {
			XMLMgmtRequest request = XMLMgmtRequestFactory.createRestartDomainRequest(getDomain());
			String response = getXMLMgmtSession().doSoapRequest(request);
			//getLog().debug("Response restartDomain:\n" + response);
		} catch (XMLMgmtException e) {
			throw new MojoExecutionException("Failed restart domain '" + getDomain() + "'",e);
		}
	}
}

