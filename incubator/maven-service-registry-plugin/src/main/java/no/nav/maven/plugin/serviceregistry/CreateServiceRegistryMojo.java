package no.nav.maven.plugin.serviceregistry;

import java.io.File;
import java.util.List;
import java.util.Map;

import no.stelvio.serviceregistry.ServiceInstance;
import no.stelvio.serviceregistry.ServiceRegistry;
import no.stelvio.websphere.esb.SCAModuleAdministration;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * @goal create-service-registry
 */
public class CreateServiceRegistryMojo extends AbstractMojo {

	/**
	 * @parameter expression="${wid.runtime}"
	 * @required
	 */
	protected String wpsRuntime;

	/**
	 * @parameter
	 * @required
	 */
	protected List<String> environments;

	/**
	 * @parameter
	 * @required
	 */
	protected String jythonScript;

	/**
	 * @parameter
	 * @required
	 */
	protected String datapowerHost;

	/**
	 * @parameter
	 * @required
	 */
	protected String datapowerDomain;

	/**
	 * @parameter
	 * @required
	 */
	protected File environmentDir;
	
	/**
	 * @parameter expression="${project.build.outputDirectory}"
	 * @required
	 */
	protected File buildDirectory;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		ServiceRegistry serviceRegistry = new ServiceRegistry();
		for (String environment : environments) {
			List<Map<String, String>> exports;
			EnvironmentProperties envProps = readEnvironmentProperties(environment);
			getLog().info(
					"Collecting JaxWS exports from environment " + environment);
			exports = retrieveJaxWsExportsFromESB(environment, envProps);
			getLog().info("Found " + exports.size() + " exports in " + environment);
			for (Map<String, String> export : exports) {
				getLog().debug(
						"From module" + export.get("module") + ", add export "
								+ export.get("export") + " for environment "
								+ environment);
				serviceRegistry.addServiceInstance(new ServiceInstance(
						environment, envProps.getProperty("esbHost"), envProps
								.getProperty("esbSoapPort"), export
								.get("module"), export.get("export")));
			}
		}
		
		try {
			buildDirectory.mkdir();
			serviceRegistry.writeToFile(new File(buildDirectory, "service-registry.xml"));
		} catch (Exception e) {
			throw new MojoExecutionException("An error occured while trying to write service registry to file", e);
		}
	}

	private EnvironmentProperties readEnvironmentProperties(String environment)
			throws MojoExecutionException {
		getLog().debug(
				"Reading environment properties for environment " + environment);
		File envFile = new File(environmentDir, environment + ".properties");
		try {
			return new EnvironmentProperties(envFile);
		} catch (Exception e) {
			throw new MojoExecutionException(
					"An error occured while trying to read properties from "
							+ envFile.getAbsolutePath(), e);
		}
	}

	private List<Map<String, String>> retrieveJaxWsExportsFromESB(
			String environment, EnvironmentProperties envProps)
			throws MojoExecutionException {
		return SCAModuleAdministration.getJaxWsExports(getLog(), jythonScript,
				wpsRuntime, envProps.getProperty("dmgrHost"), 8879, envProps
						.getProperty("username"), envProps
						.getProperty("password"));
	}

}
