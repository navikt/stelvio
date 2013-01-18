package no.nav.serviceregistry.test;

import static org.junit.Assert.*;
import static no.nav.serviceregistry.test.util.TestUtils.getResource;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.List;

import no.nav.aura.envconfig.client.ApplicationInfo;
import no.nav.maven.plugins.GenerateServiceRegistryFileMojo;
import no.nav.serviceregistry.exception.ApplicationConfigException;
import no.nav.serviceregistry.exception.MavenArtifactResolevException;
import no.nav.serviceregistry.exception.ServiceRegistryException;
import no.nav.serviceregistry.mocker.MyMockLogger;
import no.nav.serviceregistry.mocker.MyMocker;
import no.nav.serviceregistry.test.util.TestUtils;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Test;

public class GenerateServiceRegistryFileMojoTest {

	GenerateServiceRegistryFileMojo globalMojo = new GenerateServiceRegistryFileMojo();
	MyMocker globalMocker = new MyMocker();
	MyMockLogger mLog = new MyMockLogger();
	static final String TEST_APPLICATION = "autodeploy-test";
	static final String SERVICE_REGISTRY_FILE = GenerateServiceRegistryFileMojoTest.class.getClass().getResource("/serviceregistry-simple.xml").getFile();
	static final String TEST_URL = "http://test.url";
	static final String ENVIRONMENT = "u3";
	static final String EMPTY_DIR = getResource("/emptyDir");
	
	@Test
	public void testPossitiveMojoExecutor() throws MojoExecutionException {
		String orgServiceRegistryFile = getResource("/serviceregistry-simple.result.xml");

		globalMojo.testableMojoExecutor(ENVIRONMENT, TEST_URL, TEST_APPLICATION, SERVICE_REGISTRY_FILE, globalMocker, mLog);
		
		List<String> orgServiceLines = TestUtils.fileToLines(orgServiceRegistryFile);
		List<String> serviceLines = TestUtils.fileToLines(SERVICE_REGISTRY_FILE);
		assertArrayEquals(orgServiceLines.toArray(), serviceLines.toArray());
	}
	
	@Test(expected=RuntimeException.class) //her burde Platform teamet ha sendt en egendefinert exception
	public void testEnvConfigNotAccesible() throws MojoExecutionException, FileNotFoundException{
		globalMojo.testableMojoExecutor(ENVIRONMENT, TEST_URL, TEST_APPLICATION, SERVICE_REGISTRY_FILE, null, mLog);	//Det er denne linjen(dvs. envConfig) som produserer "log4j:WARN..." linjene i consollet
	}
	
	@Test(expected=ServiceRegistryException.class)
	public void testSercviceRegistryFileNotFound() throws MojoExecutionException{
		globalMojo.testableMojoExecutor(ENVIRONMENT, TEST_URL, TEST_APPLICATION, "/serviceregistry-does-not-exists.xml", globalMocker, mLog);	
	}
	
	@Test(expected=ServiceRegistryException.class)
	public void testSercviceRegistryFileCorrupt() throws MojoExecutionException{
		String corruptServiceRegistryFile = getResource("/serviceregistry-corrupt.xml");
		globalMojo.testableMojoExecutor(ENVIRONMENT, TEST_URL, TEST_APPLICATION, corruptServiceRegistryFile, globalMocker, mLog);	
	}
	
	@Test//TODO: sjekk om dette er godt nok
	public void testApplicationNotInEnvConfig() throws MojoExecutionException{
		globalMojo.testableMojoExecutor(ENVIRONMENT, TEST_URL, "MySuper-Duper_Test_app_that_is_not_in_envConfig_32410978786978", SERVICE_REGISTRY_FILE, globalMocker, mLog);	
	}
	
	@Test
	public void testNoApplicationsSpessified() throws MojoExecutionException{
		globalMojo.testableMojoExecutor(ENVIRONMENT, TEST_URL, "", SERVICE_REGISTRY_FILE, globalMocker, mLog);
	}

	@Test
	public void testNoServicesInAppConfig_EmptyServicesList() throws MojoExecutionException{
		MyMocker myMocker = new MyMocker();
		myMocker.setAppConfigExtractDir(getResource("/appConfigNoServices-EmptyList"));
		globalMojo.testableMojoExecutor(ENVIRONMENT, TEST_URL, TEST_APPLICATION, SERVICE_REGISTRY_FILE, myMocker, mLog);
	}
	
	@Test
	public void testNoServicesInAppConfig_StartStopServicesTag() throws MojoExecutionException{
		MyMocker myMocker = new MyMocker();
		myMocker.setAppConfigExtractDir(getResource("/appConfigNoServices-StartStopTag"));
		globalMojo.testableMojoExecutor(ENVIRONMENT, TEST_URL, TEST_APPLICATION, SERVICE_REGISTRY_FILE, myMocker, mLog);
	}
	
	@Test
	public void testNoServicesInAppConfig_NoServicesTag() throws MojoExecutionException{
		MyMocker myMocker = new MyMocker();
		myMocker.setAppConfigExtractDir(getResource("/appConfigNoServices-NoTag"));
		globalMojo.testableMojoExecutor(ENVIRONMENT, TEST_URL, TEST_APPLICATION, SERVICE_REGISTRY_FILE, myMocker, mLog);
	}
	
	@Test(expected=ApplicationConfigException.class)
	public void testNoAppconfigFound() throws MojoExecutionException{
		MyMocker myMock = new MyMocker();
		myMock.setAppConfigExtractDir(EMPTY_DIR);
		globalMojo.testableMojoExecutor(ENVIRONMENT, TEST_URL, TEST_APPLICATION, SERVICE_REGISTRY_FILE, myMock, mLog);
	}
	
	@Test(expected=ServiceRegistryException.class)
	public void testNoWsdlFound() throws MojoExecutionException{
		MyMocker myMock = new MyMocker();
		myMock.setServiceExtractDir(EMPTY_DIR);
		globalMojo.testableMojoExecutor(ENVIRONMENT, TEST_URL, TEST_APPLICATION, SERVICE_REGISTRY_FILE, myMock, mLog);
	}

	@Test(expected=ServiceRegistryException.class)
	public void testEnvConfigDoesNotReturnAllArtifactCoordinates() throws MojoExecutionException{
		MyMocker myMocker = new MyMocker();
		HashSet<ApplicationInfo> hashSet = new HashSet<ApplicationInfo>();
		ApplicationInfo applicationInfo = new ApplicationInfo();
		applicationInfo.setName(TEST_APPLICATION);
		hashSet.add(applicationInfo);
		myMocker.setEnvConfigApplications(hashSet);
		globalMojo.testableMojoExecutor(ENVIRONMENT, TEST_URL, TEST_APPLICATION, SERVICE_REGISTRY_FILE, myMocker, mLog);
	}

	@Test(expected=ApplicationConfigException.class)
	public void testCorruptAppconfig() throws MojoExecutionException{
		MyMocker myMocker = new MyMocker();
		myMocker.setAppConfigExtractDir(getResource("/appConfig-Corrupt"));
		globalMojo.testableMojoExecutor(ENVIRONMENT, TEST_URL, TEST_APPLICATION, SERVICE_REGISTRY_FILE, myMocker, mLog);
	}

	@Test(expected=ApplicationConfigException.class)
	public void testMissigWsdlCoordinates() throws MojoExecutionException{
		MyMocker myMocker = new MyMocker();
		myMocker.setAppConfigExtractDir(getResource("/appConfig-MissingWSDLCoordinates"));
		globalMojo.testableMojoExecutor(ENVIRONMENT, TEST_URL, TEST_APPLICATION, SERVICE_REGISTRY_FILE, myMocker, mLog);
	}

	@Test(expected=MavenArtifactResolevException.class)
	public void testServiceResolveException() throws MojoExecutionException{
		MyMocker myMocker = new MyMocker();
		myMocker.setMockServiceResolveException(true);
		globalMojo.testableMojoExecutor(ENVIRONMENT, TEST_URL, TEST_APPLICATION, SERVICE_REGISTRY_FILE, myMocker, mLog);
	}

	@Test(expected=MavenArtifactResolevException.class)
	public void testApplicationInfoResolceException() throws MojoExecutionException{
		MyMocker myMocker = new MyMocker();
		myMocker.setMockApplicationInfoResolveException(true);
		globalMojo.testableMojoExecutor(ENVIRONMENT, TEST_URL, TEST_APPLICATION, SERVICE_REGISTRY_FILE, myMocker, mLog);
	}
	
	
}
