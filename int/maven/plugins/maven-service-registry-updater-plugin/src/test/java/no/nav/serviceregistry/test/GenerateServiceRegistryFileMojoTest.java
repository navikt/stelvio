package no.nav.serviceregistry.test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import no.nav.maven.plugins.GenerateServiceRegistryFileMojo;
import no.nav.serviceregistry.exception.ServiceRegistryException;
import no.nav.serviceregistry.mocker.MyMocker;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Test;

public class GenerateServiceRegistryFileMojoTest {

	static final String TEST_APPLICATION = "autodeploy-test";
	static final String SERVICE_REGISTRY_FILE = GenerateServiceRegistryFileMojoTest.class.getClass().getResource("/serviceregistry-simple.xml").getFile();
	static final String TEST_URL = "http://test.url";
	static final String ENVIRONMENT = "u3";
	
    // Helper method for get the file content
	private static List<String> fileToLines(String filename) {
		List<String> lines = new LinkedList<String>();
		String line = "";
		try {
			BufferedReader in = new BufferedReader(new FileReader(filename));
			while ((line = in.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines;
	}
	
	private String getResource(String r){
		return this.getClass().getResource(r).getFile();
	}
	
	@Test
	public void testPossitiveMojoExecutor() throws MojoExecutionException {
		String orgServiceRegistryFile = getResource("/serviceregistry-simple.result.xml");
		MyMocker myMocker = new MyMocker();
		GenerateServiceRegistryFileMojo mojo = new GenerateServiceRegistryFileMojo();

		mojo.testableMojoExecutor(ENVIRONMENT, TEST_URL, TEST_APPLICATION, SERVICE_REGISTRY_FILE, myMocker);
		
		List<String> orgServiceLines = fileToLines(orgServiceRegistryFile);
		List<String> serviceLines = fileToLines(SERVICE_REGISTRY_FILE);
		assertArrayEquals(orgServiceLines.toArray(), serviceLines.toArray());
	}
	
	@Test(expected=RuntimeException.class) //TODO: her burde Platform teamet ha sendt en egendefinert exception
	public void testEnvConfigNotAccesible() throws MojoExecutionException, FileNotFoundException{
		GenerateServiceRegistryFileMojo mojo = new GenerateServiceRegistryFileMojo();
		mojo.testableMojoExecutor(ENVIRONMENT, TEST_URL, TEST_APPLICATION, SERVICE_REGISTRY_FILE, null);	
	}
	
	@Test(expected=ServiceRegistryException.class)
	public void testSercviceRegistryFileNotFound() throws MojoExecutionException{
		MyMocker myMocker = new MyMocker();
		GenerateServiceRegistryFileMojo mojo = new GenerateServiceRegistryFileMojo();
		mojo.testableMojoExecutor(ENVIRONMENT, TEST_URL, TEST_APPLICATION, "/serviceregistry-does-not-exists.xml", myMocker);	
	}
	
	@Test(expected=ServiceRegistryException.class)
	public void testSercviceRegistryFileCorrupt() throws MojoExecutionException{
		String corruptServiceRegistryFile = getResource("/serviceregistry-corrupt.xml");
		MyMocker myMocker = new MyMocker();
		GenerateServiceRegistryFileMojo mojo = new GenerateServiceRegistryFileMojo();
		mojo.testableMojoExecutor(ENVIRONMENT, TEST_URL, TEST_APPLICATION, corruptServiceRegistryFile, myMocker);	
	}
	
	@Test
	public void testApplicationNotInEnvConfig() throws MojoExecutionException{
		MyMocker myMocker = new MyMocker();
		GenerateServiceRegistryFileMojo mojo = new GenerateServiceRegistryFileMojo();
		mojo.testableMojoExecutor(ENVIRONMENT, TEST_URL, "MySuper-Duper_Test_app_that_is_not_in_envConfig_32410978786978", SERVICE_REGISTRY_FILE, myMocker);	
	}
}
