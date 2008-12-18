package no.nav.maven.plugins.descriptor.mojo;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import no.nav.maven.plugins.descriptor.config.EarConfig;
import no.nav.maven.plugins.descriptor.websphere.bnd.IbmWebServiceClientBndEditor;
import no.nav.maven.plugins.descriptor.websphere.ext.IbmWebServiceClientExtEditor;

import org.apache.maven.plugin.MojoExecutionException;
import org.eclipse.jst.j2ee.commonarchivecore.internal.Archive;
import org.eclipse.jst.j2ee.commonarchivecore.internal.EARFile;
import org.eclipse.jst.j2ee.commonarchivecore.internal.EJBJarFile;



/**
 * 
 * @goal addWsSecurityOutbound
 *
 */
public class WsSecurityOutboundMojo extends AbstractDeploymentDescriptorMojo {

	private static final String AUTH_WSS_LTPATOKEN = "WSSLTPAToken";
	private static final String AUTH_WSS_USERNAMETOKEN = "WSSUsernameToken";
	private static final String AUTH_HTTP_BASIC = "HTTPBasicAuth";
	
	/**
	 * @parameter expression="${authMechanism}"
	 */
	//private String[] authMechanisms;		
	private String authMechanism;		

	/**
	 * @parameter
	 */
	private String endpointPortServerAddress;
	
	/**
	 * @parameter
	 */
	private String sslConfig;

	/**
	 * @parameter
	 */
	private String username;

	/**
	 * @parameter
	 */	
	private String password;
	
	
	public void executeDescriptorOperations(EARFile earFile, EarConfig earConfig) throws MojoExecutionException {
		
		//List<String> authList = authMechanisms != null ? Arrays.asList(authMechanisms) : null;
		EJBJarFile ejbJarFile = (EJBJarFile) earFile.getEJBJarFiles().get(0);
		
		// Configure Client SSL transport security
		if (sslConfig != null && !sslConfig.equals("")) {
			getLog().info("Setting up SSL config: '" + sslConfig + "'");
			setupSslConfig(ejbJarFile, sslConfig);	
		}
		// Configure overridden endpoint URI
		if (endpointPortServerAddress != null && !endpointPortServerAddress.equals("")) {
			getLog().info("Setting up endpoint with portServerAddress: '" + endpointPortServerAddress + "'");
			setupOverriddenEndpoint(ejbJarFile, endpointPortServerAddress);
		}
		// Set up Authentication mechanisms
		if (authMechanism != null && authMechanism.equals(AUTH_WSS_LTPATOKEN)){
			getLog().info("Setting up LTPATokenGenerator");
			setupLtpaTokenGenerator(ejbJarFile);
		}
//		if (authList != null) {
//			// Configure message layer authentication using WS-Security BinarySecurityToken LTPA
//			if (authList.contains(AUTH_WSS_LTPATOKEN)) {
//				getLog().info("Setting up LTPATokenGenerator");
//				setupLtpaTokenGenerator(ejbJarFile);
//			}
//			// Configure message layer authentication using WS-Security UsernameToken
//			if (authList.contains(AUTH_WSS_USERNAMETOKEN)) {
//				getLog().info("Setting up UsernameTokenGenerator: '" + username + ":" + password + "'");
//				//TODO: not yet implemented
//				throw new MojoExecutionException("UsernameToken setup is not yet implemented");
//			}
//			// Configure transport layer authentication using HTTP Basic (HTTP Authorization header)
//			if (authList.contains(AUTH_HTTP_BASIC)) {
//				getLog().info("Setting up HTTP Basic Authentication: '" + username + ":" + password + "'");
//				//TODO: not yet implemented
//				throw new MojoExecutionException("UsernameToken setup is not yet implemented");
//			}			
//		}
		
	}
	
	private void setupOverriddenEndpoint(Archive archive, String portServerAddress) throws MojoExecutionException {
		try {
			IbmWebServiceClientBndEditor wscBnd = new IbmWebServiceClientBndEditor(archive);
			wscBnd.setEndpointUri(portServerAddress);
			wscBnd.save();
		} catch (IOException e) {
			throw new MojoExecutionException("Caught IOException while setting up overriddenEndpoint", e);
		}
		
	}
	
	private void setupSslConfig(Archive archive, String sslConfigName) throws MojoExecutionException {
		try {
			IbmWebServiceClientBndEditor wscBnd = new IbmWebServiceClientBndEditor(archive);
			wscBnd.setSslConfig(sslConfigName);
			wscBnd.save();
		} catch (IOException e) {
			throw new MojoExecutionException("Caught IOException while setting up SSLconfig", e);
		}
	}
	
	private void setupLtpaTokenGenerator(Archive archive) throws MojoExecutionException {
		try {
			String tokenPartReference = "LTPATokenPartRef";
			IbmWebServiceClientExtEditor wscExt = new IbmWebServiceClientExtEditor(archive);
			wscExt.addRequestGeneratorLTPA(tokenPartReference);
			wscExt.save();
			IbmWebServiceClientBndEditor wscBnd = new IbmWebServiceClientBndEditor(archive);
			wscBnd.addRequestTokenGeneratorLTPA(tokenPartReference);
			wscBnd.save();
		} catch (IOException e) {
			throw new MojoExecutionException("Caught IOException while setting up LTPATokenGenerator", e);
		}		
	}
	
	
}
