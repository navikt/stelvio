package no.nav.serviceregistry.test;

import static no.nav.serviceregistry.test.util.TestUtils.getResource;

import java.io.FileNotFoundException;
import java.util.HashSet;

import no.nav.aura.envconfig.client.ApplicationInfo;
import no.nav.maven.plugins.GenerateServiceRegistryFileMojo;
import no.nav.serviceregistry.exception.ApplicationConfigException;
import no.nav.serviceregistry.exception.ApplicationNotInEnvConfigException;
import no.nav.serviceregistry.exception.MavenArtifactResolevException;
import no.nav.serviceregistry.exception.ServiceRegistryException;
import no.nav.serviceregistry.test.mocker.MyMockLogger;
import no.nav.serviceregistry.test.mocker.MyMocker;
import no.nav.serviceregistry.test.util.TestUtils;
import no.nav.serviceregistry.util.Testdata;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Test;

public class GenerateServiceRegistryFileMojoTest {
	
	static final String TEST_APPLICATION = "autodeploy-test";
	static final String ORIGINAL_SERVICE_REGISTRY_FILE = getResource("/serviceregistry-simple.org.xml");
	static final String SERVICE_REGISTRY_FILE = getResource("/serviceregistry-simple.xml");
	static final String TEST_URL = "http://test.url";
	static final String ENVIRONMENT = "u3";
	static final String EMPTY_DIR = getResource("/emptyDir");
	

	GenerateServiceRegistryFileMojo globalMojo;
	
	public GenerateServiceRegistryFileMojoTest() {
		globalMojo = mojoFactory(new MyMocker());
	}
	
	@Test
	public void testPossitiveMojoExecutor() throws MojoExecutionException {
		String sr = getResource("/serviceregistry-testPossitiveMojoExecutor/serviceregistry-simple.result.xml");
		String result = getResource("/serviceregistry-testPossitiveMojoExecutor/serviceregistry-simple.result.xml");
		globalMojo.testableMojoExecutor(ENVIRONMENT, TEST_URL, TEST_APPLICATION, sr);
		TestUtils.assertNoFileDiff(result, sr);
	}
	
	@Test(expected=RuntimeException.class) //her burde Platform teamet ha sendt en egendefinert exception
	public void testEnvConfigNotAccesible() throws MojoExecutionException, FileNotFoundException{
		GenerateServiceRegistryFileMojo myMojo = mojoFactory(null);
		myMojo.testableMojoExecutor(ENVIRONMENT, TEST_URL, TEST_APPLICATION, SERVICE_REGISTRY_FILE);	//Det er denne linjen(dvs. envConfig) som produserer "log4j:WARN..." linjene i consollet
	}
	
	@Test(expected=ServiceRegistryException.class)
	public void testSercviceRegistryFileNotFound() throws MojoExecutionException{
		globalMojo.testableMojoExecutor(ENVIRONMENT, TEST_URL, TEST_APPLICATION, "/serviceregistry-does-not-exists.xml");	
	}
	
	@Test(expected=ServiceRegistryException.class)
	public void testSercviceRegistryFileCorrupt() throws MojoExecutionException{
		String corruptServiceRegistryFile = getResource("/serviceregistry-corrupt.xml");
		globalMojo.testableMojoExecutor(ENVIRONMENT, TEST_URL, TEST_APPLICATION, corruptServiceRegistryFile);	
	}
	
	@Test(expected=ApplicationNotInEnvConfigException.class)
	public void testApplicationNotInEnvConfig() throws MojoExecutionException{
		globalMojo.testableMojoExecutor(ENVIRONMENT, TEST_URL, "MySuper-Duper_Test_app_that_is_not_in_envConfig_32410978786978", SERVICE_REGISTRY_FILE);
	}
	
	@Test
	public void testNoApplicationsSpessified() throws MojoExecutionException{
		String sr = getResource("/serviceregistry-testNoApplicationsSpessified/serviceregistry-simple.xml");
		String result = getResource("/serviceregistry-testNoApplicationsSpessified/serviceregistry-simple.result.xml");
		globalMojo.testableMojoExecutor(ENVIRONMENT, TEST_URL, "", sr);
		TestUtils.assertNoFileDiff(result, sr);
	}

	@Test
	public void testNoServicesInAppConfig_EmptyServicesList() throws MojoExecutionException{
		String sr = getResource("/serviceregistry-testNoServicesInAppConfig_EmptyServicesList/serviceregistry-simple.xml");
		String result = getResource("/serviceregistry-testNoServicesInAppConfig_EmptyServicesList/serviceregistry-simple.result.xml");
		Testdata myMocker = new MyMocker();
		myMocker.setAppConfigExtractDir(getResource("/appConfigNoServices-EmptyList"));
		GenerateServiceRegistryFileMojo myMojo = mojoFactory(myMocker);
		myMojo.testableMojoExecutor(ENVIRONMENT, TEST_URL, TEST_APPLICATION, sr);
		TestUtils.assertNoFileDiff(result, sr);
	}
	
	@Test
	public void testNoServicesInAppConfig_StartStopServicesTag() throws MojoExecutionException{
		String sr = getResource("/serviceregistry-testNoServicesInAppConfig_StartStopServicesTag/serviceregistry-simple.xml");
		String result = getResource("/serviceregistry-testNoServicesInAppConfig_StartStopServicesTag/serviceregistry-simple.result.xml");
		Testdata myMocker = new MyMocker();
		myMocker.setAppConfigExtractDir(getResource("/appConfigNoServices-StartStopTag"));
		GenerateServiceRegistryFileMojo myMojo = mojoFactory(myMocker);
		myMojo.testableMojoExecutor(ENVIRONMENT, TEST_URL, TEST_APPLICATION, sr);
		TestUtils.assertNoFileDiff(result, sr);
	}
	
	@Test
	public void testNoServicesInAppConfig_NoServicesTag() throws MojoExecutionException{
		String sr = getResource("/serviceregistry-testNoServicesInAppConfig_NoServicesTag/serviceregistry-simple.xml");
		String result = getResource("/serviceregistry-testNoServicesInAppConfig_NoServicesTag/serviceregistry-simple.result.xml");
		Testdata myMocker = new MyMocker();
		myMocker.setAppConfigExtractDir(getResource("/appConfigNoServices-NoTag"));
		GenerateServiceRegistryFileMojo myMojo = mojoFactory(myMocker);
		myMojo.testableMojoExecutor(ENVIRONMENT, TEST_URL, TEST_APPLICATION, SERVICE_REGISTRY_FILE);
		TestUtils.assertNoFileDiff(result, sr);
	}
	
	@Test(expected=ApplicationConfigException.class)
	public void testNoAppconfigFound() throws MojoExecutionException{
		Testdata myMocker = new MyMocker();
		myMocker.setAppConfigExtractDir(EMPTY_DIR);
		GenerateServiceRegistryFileMojo myMojo = mojoFactory(myMocker);
		myMojo.testableMojoExecutor(ENVIRONMENT, TEST_URL, TEST_APPLICATION, SERVICE_REGISTRY_FILE);
	}
	
	@Test(expected=ServiceRegistryException.class)
	public void testNoWsdlFound() throws MojoExecutionException{
		Testdata myMocker = new MyMocker();
		myMocker.setServiceExtractDir(EMPTY_DIR);
		GenerateServiceRegistryFileMojo myMojo = mojoFactory(myMocker);
		myMojo.testableMojoExecutor(ENVIRONMENT, TEST_URL, TEST_APPLICATION, SERVICE_REGISTRY_FILE);
	}

	@Test(expected=ServiceRegistryException.class)
	public void testEnvConfigDoesNotReturnAllArtifactCoordinates() throws MojoExecutionException{
		Testdata myMocker = new MyMocker();
		HashSet<ApplicationInfo> hashSet = new HashSet<ApplicationInfo>();
		ApplicationInfo applicationInfo = new ApplicationInfo();
		applicationInfo.setName(TEST_APPLICATION);
		hashSet.add(applicationInfo);
		myMocker.setEnvConfigApplications(hashSet);
		GenerateServiceRegistryFileMojo myMojo = mojoFactory(myMocker);
		myMojo.testableMojoExecutor(ENVIRONMENT, TEST_URL, TEST_APPLICATION, SERVICE_REGISTRY_FILE);
	}

	@Test(expected=ApplicationConfigException.class)
	public void testCorruptAppconfig() throws MojoExecutionException{
		Testdata myMocker = new MyMocker();
		myMocker.setAppConfigExtractDir(getResource("/appConfig-Corrupt"));
		GenerateServiceRegistryFileMojo myMojo = mojoFactory(myMocker);
		myMojo.testableMojoExecutor(ENVIRONMENT, TEST_URL, TEST_APPLICATION, SERVICE_REGISTRY_FILE);
	}

	@Test(expected=ApplicationConfigException.class)
	public void testMissigWsdlCoordinates() throws MojoExecutionException{
		Testdata myMocker = new MyMocker();
		myMocker.setAppConfigExtractDir(getResource("/appConfig-MissingWSDLCoordinates"));
		GenerateServiceRegistryFileMojo myMojo = mojoFactory(myMocker);
		myMojo.testableMojoExecutor(ENVIRONMENT, TEST_URL, TEST_APPLICATION, SERVICE_REGISTRY_FILE);
	}

	@Test(expected=MavenArtifactResolevException.class)
	public void testServiceResolveException() throws MojoExecutionException{
		Testdata myMocker = new MyMocker();
		myMocker.setMockServiceResolveException(true);
		GenerateServiceRegistryFileMojo myMojo = mojoFactory(myMocker);
		myMojo.testableMojoExecutor(ENVIRONMENT, TEST_URL, TEST_APPLICATION, SERVICE_REGISTRY_FILE);
	}

	@Test(expected=MavenArtifactResolevException.class)
	public void testApplicationInfoResolceException() throws MojoExecutionException{
		Testdata myMocker = new MyMocker();
		myMocker.setMockApplicationInfoResolveException(true);
		GenerateServiceRegistryFileMojo myMojo = mojoFactory(myMocker);
		myMojo.testableMojoExecutor(ENVIRONMENT, TEST_URL, TEST_APPLICATION, SERVICE_REGISTRY_FILE);
	}
	
	private GenerateServiceRegistryFileMojo mojoFactory(Testdata testdata){
		GenerateServiceRegistryFileMojo myMojo = new GenerateServiceRegistryFileMojo();
		myMojo.setTestdata(testdata);
		myMojo.setFreshInstall(false);
		myMojo.setLog(new MyMockLogger());
		return myMojo;
	}
}
