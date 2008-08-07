package no.nav.datapower.deployer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.Validate;

import no.nav.datapower.templates.freemarker.ConfigBuilder;
import no.nav.datapower.util.DPFileUtils;
import no.nav.datapower.util.DPPropertiesUtils;
import no.nav.datapower.util.PropertiesBuilder;
import no.nav.datapower.util.PropertiesValidator;
import freemarker.template.TemplateException;

public class DeploymentManagerTester {

	private File wsdlArchive;
	private File outputDirectory;
	private File importDirectory;
	private File dpLocalDirectory;
	private File dpLocalAaaDirectory;
	private File dpLocalWsdlDirectory;
	private File dpLocalXsltDirectory;
	private Properties requiredProperties;
	private Properties envProperties;
	
	public DeploymentManagerTester() {
		// Set up parameters for Test
		this.wsdlArchive 			= new File("E:\\Develop\\wsdl\\wsdl-pselv-kjempen.zip");
		this.outputDirectory 		= new File("E:\\Develop\\wsdl\\tmp\\");
		this.importDirectory 		= new File("E:\\Develop\\wsdl\\pselv-kjempen\\");
		this.dpLocalDirectory 		= DPFileUtils.append(importDirectory, "local");
		this.dpLocalAaaDirectory 	= DPFileUtils.append(dpLocalDirectory, "aaa");
		this.dpLocalWsdlDirectory 	= DPFileUtils.append(dpLocalDirectory, "wsdl");
		this.dpLocalXsltDirectory 	= DPFileUtils.append(dpLocalDirectory, "xslt");
		this.requiredProperties = DPPropertiesUtils.load(getResource("/properties/cfg-secgw-required.properties"));

		// Build environment properties
		envProperties = new PropertiesBuilder().
								loadAndPutAll(getResource("/properties/cfg-secgw-utv.properties")).
								loadAndPutAll(getResource("/properties/cfg-secgw-u1.properties")).
								interpolate().
								buildProperties();
		System.out.println("Properties:");
		envProperties.list(System.out);
		
		// Validate environment properties
		PropertiesValidator validator = new PropertiesValidator(requiredProperties, envProperties);
		if (validator.hasInvalidProperties()) {
			throw new IllegalArgumentException("Configuration contains invalid Properties:\r\n" + validator.getErrorMessage());
			//DPCollectionUtils.printLines(invalidProperties.values(), System.err, "ERROR: Invalid Property: ");
		}
		
		// Extract wsdl archives
		try {
			DPFileUtils.extractArchive(wsdlArchive, dpLocalDirectory);
		} catch (IOException e1) {
			throw new IllegalStateException("Caught IOException while extracting WSDL archive");
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File resource = getResource(Thread.currentThread().getContextClassLoader(), "templates/aaa-mapping-file.ftl");
		System.out.println("Resource = " + resource);
		
		DeploymentManagerTester tester = new DeploymentManagerTester();
		tester.testImportFilesAndDeployConfiguration();
		
	}
	
	private void testImportFilesAndDeployConfiguration() {
		DeploymentManager deployer = new DeploymentManager("https://secgw-01.utv.internsone.local:5550", "mavendeployer", "Test1234");
		try {
			File aaaMappingFile = DPFileUtils.append(dpLocalAaaDirectory, (String) envProperties.getProperty("aaaFileName"));
			FileWriter aaaWriter = new FileWriter(aaaMappingFile);
			ConfigBuilder aaaBuilder = ConfigBuilder.FACTORY.newConfigBuilder("/templates/aaa-mapping-file.ftl");
			aaaBuilder.build(envProperties, aaaWriter);
		} catch (IOException e) {
			throw new IllegalStateException("Caught IOException while building AAAInfo file", e);
		} catch (TemplateException e) {
			throw new IllegalStateException("Template processing failed for AAAInfo file", e);
		}
		deployer.importFilesAndDeployConfiguration("secgw-configuration.ftl", envProperties, importDirectory, true);		
	}
	
	private File getResource(String resource) {
		return DPFileUtils.getResource(DeploymentManagerTester.class.getClass(), resource);
	}
	
	private static File getResource(ClassLoader clazzLoader, String resource) {
		URL resourceUrl = clazzLoader.getResource(resource);
		File file = FileUtils.toFile(resourceUrl);
		Validate.notNull(file, "Resource '" + resource + "'not found using ClassLoader '" + clazzLoader.toString() +"'");
		return file;
	}
}
