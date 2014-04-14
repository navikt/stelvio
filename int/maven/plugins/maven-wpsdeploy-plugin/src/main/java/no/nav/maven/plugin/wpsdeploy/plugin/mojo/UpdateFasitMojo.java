package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import javax.xml.bind.DatatypeConverter;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

import no.nav.maven.plugin.wpsdeploy.plugin.utils.FasitUtil;

/**
*  This class updates fasit with versions of the esb. Each version is connected to a different application (esb*) which acts as a placeholder. This makes VERA able to disblay esb versions.
*  
* @author test@example.com
*
* @goal update-fasit
* @requiresDependencyResolution
*/
public class UpdateFasitMojo extends WebsphereUpdaterMojo{
	
	/**
	 * @parameter expression="${fasitUsername}"
	 */
	private String fasitUsername;

	/**
	 * @parameter expression="${fasitPasswordBase64}"
	 */
	private String fasitPasswordBase64;
	
	/**
	 * @parameter expression="${fasitPassword}"
	 */
	private String fasitPassword;

	/**
	 * @parameter expression="${environment}"
	 */
	private String environmentLowerOrUpperCase;
	
	/**
	 * @parameter expression="${esb-authorization-configuration}"
	 */
	private String authorizationConfig;
	
	/**
	 * @parameter expression="${esb-enviroment-configuration}"
	 */
	private String environmentConfig;
	
	/**
	 * @parameter expression="${esb-nonenviroment-configuration}"
	 */
	private String nonenvironmentConfig;
	
	/**
	 * @parameter expression="${esb-modules-version}"
	 */
	private String esbVersion;
	
	private static final String FASIT_APPLICATION_NAME_ESB = "esb";
	private static final String FASIT_APPLICATION_NAME_AUT_CONF = "esb-auth-conf";
	private static final String FASIT_APPLICATION_NAME_ENV_CONF = "esb-env-conf";
	private static final String FASIT_APPLICATION_NAME_NONENV_CONF = "esb-nonenv-conf";
	
	@Override
	protected void applyToWebSphere(Commandline wsadminCommandline)
			throws MojoExecutionException, MojoFailureException {
		
		if (fasitPassword == null){
			fasitPassword = new String(DatatypeConverter.parseBase64Binary(fasitPasswordBase64));
		}
		
		try {
			getLog().info("Registering application versions to fasit");
			
			getLog().debug("Registering application " + FASIT_APPLICATION_NAME_ESB + " with version " + esbVersion);
			FasitUtil.registerApplication(FASIT_APPLICATION_NAME_ESB, esbVersion, environmentLowerOrUpperCase.toLowerCase(), fasitUsername, fasitPassword);
			
			getLog().debug("Registering application " + FASIT_APPLICATION_NAME_AUT_CONF + " with version " + authorizationConfig);
			FasitUtil.registerApplication(FASIT_APPLICATION_NAME_AUT_CONF, authorizationConfig, environmentLowerOrUpperCase.toLowerCase(), fasitUsername, fasitPassword);
			
			getLog().debug("Registering application " + FASIT_APPLICATION_NAME_ENV_CONF + " with version " + environmentConfig);
			FasitUtil.registerApplication(FASIT_APPLICATION_NAME_ENV_CONF, environmentConfig, environmentLowerOrUpperCase.toLowerCase(), fasitUsername, fasitPassword);
			
			getLog().debug("Registering application " + FASIT_APPLICATION_NAME_NONENV_CONF + " with version " + nonenvironmentConfig);
			FasitUtil.registerApplication(FASIT_APPLICATION_NAME_NONENV_CONF, nonenvironmentConfig, environmentLowerOrUpperCase.toLowerCase(), fasitUsername, fasitPassword);
		} catch (Exception e) {
			getLog().warn("Registering application versions in Fasit failed. Continuing deploy.");
			e.printStackTrace();
		}
		
	}

	@Override
	protected String getGoalPrettyPrint() {

		return "Write esb versions to Fasit.";
	}
	
}
