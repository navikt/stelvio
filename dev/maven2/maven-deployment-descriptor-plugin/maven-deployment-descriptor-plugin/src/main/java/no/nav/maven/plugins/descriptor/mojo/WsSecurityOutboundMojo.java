package no.nav.maven.plugins.descriptor.mojo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import no.nav.maven.plugins.descriptor.config.EarConfig;
import no.nav.maven.plugins.descriptor.websphere.bnd.IbmWebServiceClientBndEditor;
import no.nav.maven.plugins.descriptor.websphere.ext.IbmWebServiceClientExtEditor;

import org.apache.maven.plugin.MojoExecutionException;
import org.eclipse.jst.j2ee.commonarchivecore.internal.Archive;
import org.eclipse.jst.j2ee.commonarchivecore.internal.EARFile;
import org.eclipse.jst.j2ee.commonarchivecore.internal.EJBJarFile;
import org.eclipse.osgi.framework.internal.core.Tokenizer;

import com.ibm.icu.util.StringTokenizer;



/**
 * 
 * @goal addWsSecurityOutbound
 * @requiresProject false
 *
 */
public class WsSecurityOutboundMojo extends AbstractDeploymentDescriptorMojo {

	private static final String AUTH_WSS_LTPATOKEN = "WSSLTPAToken";
	private static final String AUTH_WSS_USERNAMETOKEN = "WSSUsernameToken";
	private static final String AUTH_HTTP_BASIC = "HTTPBasicAuth";
	
	/**
	 * @parameter expression="${authMechanism}"
	 */	
	private String authMechanism;		

	/**
	 * @parameter expression="${endpointPortServerAddress}"
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
	
	/**
	 * @parameter
	 */
	private Properties myEndpoints;
	
	
	/**
	 * @parameter
	 */
	private String myEndpointsString;

	/**
	 * @parameter
	 */
	private String exludeAuthMechanismForEndpoints;
	
	public void executeDescriptorOperations(EARFile earFile, EarConfig earConfig) throws MojoExecutionException {		
		EJBJarFile ejbJarFile = (EJBJarFile) earFile.getEJBJarFiles().get(0);
		
		// Configure Client SSL transport security
		if (sslConfig != null && !sslConfig.equals("")) {
			getLog().info("Setting up SSL config: '" + sslConfig + "'");
			setupSslConfig(ejbJarFile, sslConfig);	
		}
		// Configure overridden endpoint URI
		if (endpointPortServerAddress != null && !endpointPortServerAddress.equals("")) {
			if (myEndpoints != null)
				getLog().info("Setting up endpoints with these addresses: " + myEndpoints);				
			else if (myEndpointsString != null){
				getLog().info("Converting from String to Properties, old String: " + myEndpointsString);
				myEndpoints = convertStringToProperties(myEndpointsString, ",", "=");
				getLog().info("Setting up endpoints with these addresses: " + myEndpoints);
			}
			else
				getLog().info("Setting up endpoint with portServerAddress: '" + endpointPortServerAddress + "'");
			setupOverriddenEndpoint(ejbJarFile, endpointPortServerAddress, myEndpoints);
		}
		
		// Set up Authentication mechanisms
		List<String> excludes = new ArrayList<String>();
		if(exludeAuthMechanismForEndpoints!=null) {
			StringTokenizer tokenizer = new StringTokenizer(exludeAuthMechanismForEndpoints,",");
			while(tokenizer.hasMoreTokens()) {
				excludes.add(tokenizer.nextToken());
			}
		}
		
		if (authMechanism != null && authMechanism.equals(AUTH_WSS_LTPATOKEN)){
			getLog().info("Setting up LTPATokenGenerator");
			setupLtpaTokenGenerator(ejbJarFile,excludes);
		} else if (authMechanism != null && authMechanism.equals(AUTH_WSS_USERNAMETOKEN)){
			getLog().info("Setting up UsernameTokenGenerator");
			setupUsernameTokenGenerator(ejbJarFile,excludes);	
		}
	}
	
	public Properties convertStringToProperties(final String keysAndValues, final String propertySeparator, final String keyValueSeparator) {
		Properties props = new Properties();
		StringTokenizer stringTokenizer = new StringTokenizer(keysAndValues.trim(), propertySeparator);
	
		while(stringTokenizer.hasMoreTokens()) {
			String token = stringTokenizer.nextToken();
			StringTokenizer tokenTokenizer = new StringTokenizer(token, keyValueSeparator);
			
			if(tokenTokenizer.countTokens()==2) {
				props.setProperty(tokenTokenizer.nextToken().trim(), tokenTokenizer.nextToken().trim());				
			}
		}	
		return props;
	}	
	
	private void setupOverriddenEndpoint(Archive archive, String portServerAddress, Properties endpoints) throws MojoExecutionException {
		try {
			IbmWebServiceClientBndEditor wscBnd = new IbmWebServiceClientBndEditor(archive);						
			wscBnd.setEndpointUri(portServerAddress, endpoints);
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
	
	private void setupLtpaTokenGenerator(Archive archive, final List<String> excludes) throws MojoExecutionException {
		try {
			String tokenPartReference = "LTPATokenPartRef";
			IbmWebServiceClientExtEditor wscExt = new IbmWebServiceClientExtEditor(archive);
			wscExt.addRequestGeneratorLTPA(tokenPartReference, excludes);
			wscExt.save();
			IbmWebServiceClientBndEditor wscBnd = new IbmWebServiceClientBndEditor(archive);
			wscBnd.addRequestTokenGeneratorLTPA(tokenPartReference,excludes);
			wscBnd.save();
		} catch (IOException e) {
			throw new MojoExecutionException("Caught IOException while setting up LTPATokenGenerator", e);
		}		
	}
	
	private void setupUsernameTokenGenerator(Archive archive, final List<String> excludes) throws MojoExecutionException {
		try {
			String tokenPartReference = "UsernameTokenPartRef";
			IbmWebServiceClientExtEditor wscExt = new IbmWebServiceClientExtEditor(archive);
			wscExt.addRequestGeneratorUsername(tokenPartReference, excludes);
			wscExt.save();
			IbmWebServiceClientBndEditor wscBnd = new IbmWebServiceClientBndEditor(archive);
			wscBnd.addRequestTokenGeneratorUsername(tokenPartReference, username, password,excludes);
			wscBnd.save();
		} catch (IOException e) {
			throw new MojoExecutionException("Caught IOException while setting up LTPATokenGenerator", e);
		}		
	}

	public String getExludeAuthMechanismForEndpoints() {
		return exludeAuthMechanismForEndpoints;
	}

	public void setExludeAuthMechanismForEndpoints(
			String exludeAuthMechanismForEndpoints) {
		this.exludeAuthMechanismForEndpoints = exludeAuthMechanismForEndpoints;
	}	
}
