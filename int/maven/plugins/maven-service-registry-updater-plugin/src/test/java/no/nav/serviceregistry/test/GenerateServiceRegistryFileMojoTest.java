package no.nav.serviceregistry.test;

import static org.junit.Assert.*;
import static no.nav.serviceregistry.test.util.TestUtils.getResource;

import java.io.FileNotFoundException;
import java.util.List;

import no.nav.maven.plugins.GenerateServiceRegistryFileMojo;
import no.nav.serviceregistry.exception.ServiceRegistryException;
import no.nav.serviceregistry.mocker.MyMocker;
import no.nav.serviceregistry.test.util.TestUtils;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Test;

public class GenerateServiceRegistryFileMojoTest {

	GenerateServiceRegistryFileMojo globalMojo = new GenerateServiceRegistryFileMojo();
	MyMocker globalMocker = new MyMocker();
	static final String TEST_APPLICATION = "autodeploy-test";
	static final String SERVICE_REGISTRY_FILE = GenerateServiceRegistryFileMojoTest.class.getClass().getResource("/serviceregistry-simple.xml").getFile();
	static final String TEST_URL = "http://test.url";
	static final String ENVIRONMENT = "u3";
	
	@Test
	public void testPossitiveMojoExecutor() throws MojoExecutionException {
		String orgServiceRegistryFile = getResource("/serviceregistry-simple.result.xml");

		globalMojo.testableMojoExecutor(ENVIRONMENT, TEST_URL, TEST_APPLICATION, SERVICE_REGISTRY_FILE, globalMocker);
		
		List<String> orgServiceLines = TestUtils.fileToLines(orgServiceRegistryFile);
		List<String> serviceLines = TestUtils.fileToLines(SERVICE_REGISTRY_FILE);
		assertArrayEquals(orgServiceLines.toArray(), serviceLines.toArray());
	}
	
	@Test(expected=RuntimeException.class) //TODO: her burde Platform teamet ha sendt en egendefinert exception
	public void testEnvConfigNotAccesible() throws MojoExecutionException, FileNotFoundException{
		globalMojo.testableMojoExecutor(ENVIRONMENT, TEST_URL, TEST_APPLICATION, SERVICE_REGISTRY_FILE, null);	
	}
	
	@Test(expected=ServiceRegistryException.class)
	public void testSercviceRegistryFileNotFound() throws MojoExecutionException{
		globalMojo.testableMojoExecutor(ENVIRONMENT, TEST_URL, TEST_APPLICATION, "/serviceregistry-does-not-exists.xml", globalMocker);	
	}
	
	@Test(expected=ServiceRegistryException.class)
	public void testSercviceRegistryFileCorrupt() throws MojoExecutionException{
		String corruptServiceRegistryFile = getResource("/serviceregistry-corrupt.xml");
		globalMojo.testableMojoExecutor(ENVIRONMENT, TEST_URL, TEST_APPLICATION, corruptServiceRegistryFile, globalMocker);	
	}
	
	@Test
	public void testApplicationNotInEnvConfig() throws MojoExecutionException{
		globalMojo.testableMojoExecutor(ENVIRONMENT, TEST_URL, "MySuper-Duper_Test_app_that_is_not_in_envConfig_32410978786978", SERVICE_REGISTRY_FILE, globalMocker);	
	}
}
