package no.nav.maven.plugins.descriptor.mojo;

import java.io.IOException;

import no.nav.maven.plugins.descriptor.config.EarConfig;
import no.nav.maven.plugins.descriptor.websphere.bnd.IbmWebServiceClientBndEditor;
import no.nav.maven.plugins.descriptor.websphere.ext.IbmWebServiceClientExtEditor;
import no.nav.maven.plugins.descriptor.websphere.bnd.IbmWebServiceBndEditor;
import no.nav.maven.plugins.descriptor.websphere.ext.IbmWebServiceExtEditor;



import org.apache.maven.plugin.MojoExecutionException;
import org.eclipse.jst.j2ee.commonarchivecore.internal.Archive;
import org.eclipse.jst.j2ee.commonarchivecore.internal.EARFile;
import org.eclipse.jst.j2ee.commonarchivecore.internal.EJBJarFile;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;


/**
 * @goal addWsSecurityInbound
 * @requiresProject false
 *
 */
public class WsSecurityInboundMojo extends AbstractDeploymentDescriptorMojo {
	private static final String AUTH_WSS_LTPATOKEN = "WSSLTPAToken";
	private static final String AUTH_WSS_USERNAMETOKEN = "WSSUsernameToken";
	private static final String AUTH_WSS_BOTH = "WSSBoth";
	
	
	/**
	 * @parameter expression="${authMechanism}"
	 */
	private String authMechanism;		

	/**
	 * @parameter expression="${username}"
	 */
	private String username;

	/**
	 * @parameter expression="${password}"
	 */	
	private String password;
	
	

	public void executeDescriptorOperations(EARFile earFile, EarConfig earConfig)
			throws MojoExecutionException {
		
		EJBJarFile ejbJarFile = (EJBJarFile) earFile.getEJBJarFiles().get(0);
		
		if (authMechanism != null && authMechanism.equalsIgnoreCase(AUTH_WSS_LTPATOKEN)){
			getLog().info("Setting up LTPATokenConsumer");
			//setupLtpaTokenGenerator(ejbJarFile);
			setupLtpaTokenConsumer(ejbJarFile);
		} else if (authMechanism != null && authMechanism.equalsIgnoreCase(AUTH_WSS_USERNAMETOKEN)){
			getLog().info("Setting up UsernameTokenConsumer");
			setupUsernameTokenConsumer(ejbJarFile);
		} else if (authMechanism != null && authMechanism.equalsIgnoreCase(AUTH_WSS_BOTH)){
			getLog().info("Setting up both LTPATokenConsumer and UsernameTokenConsumer");
			setupBothTokenConsumer(ejbJarFile);
		} else{
			getLog().info("Couldn't find valid authMechanism parameter. Aborting.");	
			throw new MojoExecutionException("Couldn't find valid authMechanism parameter. Aborting.");		
		}
	}

	
	private void setupLtpaTokenConsumer(Archive archive) throws MojoExecutionException{
		try{
			String tokenPartReference = "RequiredLTPAToken";
			boolean usageRequired = true; 	// sets that LTPA Token Consumer is REQUIRED
			IbmWebServiceExtEditor wsExt = new IbmWebServiceExtEditor(archive);
			wsExt.deleteExistingConfig();			
			wsExt.addRequestConsumerLTPAToken(usageRequired, tokenPartReference); 
			wsExt.save();
			
			IbmWebServiceBndEditor wsBnd = new IbmWebServiceBndEditor(archive);
			wsBnd.deleteExistingConfig();
			wsBnd.addLTPATokenConsumer(tokenPartReference);
			wsBnd.save();
		}catch (IOException e){
			throw new MojoExecutionException("Caught IOException while setting up LTPATokenConsumer", e);
		}	
	}
	
	private void setupUsernameTokenConsumer(Archive archive) throws MojoExecutionException{
		try{
			String tokenPartReference = "RequiredUsernameToken";
			boolean usageRequired = true;			// sets that Username Token Consumer is REQUIRED
			IbmWebServiceExtEditor wsExt = new IbmWebServiceExtEditor(archive);
			wsExt.deleteExistingConfig();			
			wsExt.addRequestConsumerUsernameToken(usageRequired, tokenPartReference); 
			wsExt.save();
			
			IbmWebServiceBndEditor wsBnd = new IbmWebServiceBndEditor(archive);
			wsBnd.deleteExistingConfig();
			wsBnd.addUsernameTokenConsumer(tokenPartReference);			
			wsBnd.save();
		}catch (IOException e){
			throw new MojoExecutionException("Caught IOException while setting up UsernameTokenConsumer", e);
		}	
	}
	
	private void setupBothTokenConsumer(Archive archive) throws MojoExecutionException{
		try{
			String usernamePartRef = "OptionalUsernameToken";
			String ltpaPartRef = "OptionalLTPAToken";
			boolean usageRequired = false; //When two tokens are present, neither can be required
			IbmWebServiceExtEditor wsExt = new IbmWebServiceExtEditor(archive);			
			wsExt.deleteExistingConfig();
			wsExt.addRequestConsumerLTPAToken(usageRequired, ltpaPartRef);
			wsExt.addRequestConsumerUsernameToken(usageRequired, usernamePartRef); 
			wsExt.save();
			
			IbmWebServiceBndEditor wsBnd = new IbmWebServiceBndEditor(archive);
			wsBnd.deleteExistingConfig();
			wsBnd.addLTPATokenConsumer(ltpaPartRef);
			wsBnd.addUsernameTokenConsumer(usernamePartRef);			
			wsBnd.save();
		}catch (IOException e){
			throw new MojoExecutionException("Caught IOException while setting up optional username and ltpa token consumers", e);
		}	
	}	
}
