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
import org.apache.maven.plugin.MojoFailureException;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class GenerateServiceRegistryFileMojoTest {
	
	static final String TEST_APPLICATION = "autodeploy-test";
	static final String ORIGINAL_SERVICE_REGISTRY_FILE = getResource("/GenerateServiceRegistryFileMojoTest/serviceregistry-simple.org.xml");
	static final String SERVICE_REGISTRY_FILE = getResource("/GenerateServiceRegistryFileMojoTest/serviceregistry-simple.xml");
	static final String POPP = TestUtils.fileToString(getResource("/popp.xml"));
	static final String TSYS = TestUtils.fileToString(getResource("/tsys.xml"));
	static final String PSELV = TestUtils.fileToString(getResource("/pselv.xml"));
	static final String AUTODEPLOY_TEST = TestUtils.fileToString(getResource("/autodeploy-test.xml"));
	static final String EMPTY_DIR = getResource("/emptyDir");

	private GenerateServiceRegistryFileMojo mojoFactory(){
		return mojoFactory(new MyMocker());
	}
	
	private GenerateServiceRegistryFileMojo mojoFactory(Testdata testdata){
		GenerateServiceRegistryFileMojo myMojo = new GenerateServiceRegistryFileMojo();
		myMojo.setTestdata(testdata);
		myMojo.setFreshInstall(false);
		myMojo.setLog(new MyMockLogger());
		return myMojo;
	}
	
	@Test
	public void testPossitiveMojoExecutor() throws MojoExecutionException, MojoFailureException {
		String sr = getResource("/GenerateServiceRegistryFileMojoTest/testPossitiveMojoExecutor/serviceregistry-simple.xml");
		GenerateServiceRegistryFileMojo myMojo = mojoFactory();
		myMojo.setApplicationsString(TEST_APPLICATION);
		myMojo.setServiceRegistryFile(sr);
		myMojo.execute();
		assertThat(TestUtils.fileToString(sr), containsString(AUTODEPLOY_TEST));
	}
	
	@Test(expected=RuntimeException.class) //her burde Platform teamet ha sendt en egendefinert exception
	public void testEnvConfigNotAccesible() throws MojoExecutionException, FileNotFoundException, MojoFailureException{
		GenerateServiceRegistryFileMojo myMojo = mojoFactory(null);
		myMojo.setApplicationsString(TEST_APPLICATION);
		myMojo.setServiceRegistryFile(SERVICE_REGISTRY_FILE);
		myMojo.execute();	//Det er denne linjen(dvs. envConfig) som produserer "log4j:WARN..." linjene i consollet
	}
	
	@Test(expected=ServiceRegistryException.class)
	public void testSercviceRegistryFileNotFound() throws MojoExecutionException, MojoFailureException{
		GenerateServiceRegistryFileMojo myMojo = mojoFactory();
		myMojo.setApplicationsString(TEST_APPLICATION);
		myMojo.setServiceRegistryFile("/serviceregistry-WTF.xml");
		myMojo.execute();
	}
	
	@Test(expected=ServiceRegistryException.class)
	public void testSercviceRegistryFileCorrupt() throws MojoExecutionException, MojoFailureException{
		String corruptServiceRegistryFile = getResource("/GenerateServiceRegistryFileMojoTest/serviceregistry-corrupt.xml");
		GenerateServiceRegistryFileMojo myMojo = mojoFactory();
		myMojo.setApplicationsString(TEST_APPLICATION);
		myMojo.setServiceRegistryFile(corruptServiceRegistryFile);
		myMojo.execute();	
	}
	
	@Test(expected=ApplicationNotInEnvConfigException.class)
	public void testApplicationNotInEnvConfig() throws MojoExecutionException, MojoFailureException{
		GenerateServiceRegistryFileMojo myMojo = mojoFactory();
		myMojo.setApplicationsString("MySuper-Duper_Test_app_that_is_not_in_envConfig_32410978786978");
		myMojo.setServiceRegistryFile(SERVICE_REGISTRY_FILE);
		myMojo.execute();
	}
	
	@Test(expected=ApplicationConfigException.class)
	public void testNoApplicationsSpessified() throws MojoExecutionException, MojoFailureException{
		GenerateServiceRegistryFileMojo myMojo = mojoFactory();
		myMojo.setApplicationsString("");
		myMojo.setServiceRegistryFile(SERVICE_REGISTRY_FILE);
		myMojo.execute();
	}

	@Test
	public void testNoServicesInAppConfig_EmptyServicesList() throws MojoExecutionException, MojoFailureException{
		String sr = getResource("/GenerateServiceRegistryFileMojoTest/testNoServicesInAppConfig_EmptyServicesList/serviceregistry-simple.xml");
		Testdata myMocker = new MyMocker();
		myMocker.setAppConfigExtractDir(getResource("/GenerateServiceRegistryFileMojoTest/testNoServicesInAppConfig_EmptyServicesList/"));
		GenerateServiceRegistryFileMojo myMojo = mojoFactory(myMocker);
		myMojo.setApplicationsString(TEST_APPLICATION);
		myMojo.setServiceRegistryFile(sr);
		myMojo.execute(); 
		String result = TestUtils.fileToString(sr);
		assertThat(result, containsString(POPP));
		assertThat(result, containsString(TSYS));
		assertThat(result, containsString(PSELV));
	}
	
	@Test
	public void testNoServicesInAppConfig_StartStopServicesTag() throws MojoExecutionException, MojoFailureException{
		String sr = getResource("/GenerateServiceRegistryFileMojoTest/testNoServicesInAppConfig_StartStopServicesTag/serviceregistry-simple.xml");
		Testdata myMocker = new MyMocker();
		myMocker.setAppConfigExtractDir(getResource("/GenerateServiceRegistryFileMojoTest/testNoServicesInAppConfig_StartStopServicesTag/"));
		GenerateServiceRegistryFileMojo myMojo = mojoFactory(myMocker);
		myMojo.setApplicationsString(TEST_APPLICATION);
		myMojo.setServiceRegistryFile(sr);
		myMojo.execute();
		String result = TestUtils.fileToString(sr);
		assertThat(result, containsString(POPP));
		assertThat(result, containsString(TSYS));
		assertThat(result, containsString(PSELV));
	}
	
	@Test
	public void testNoServicesInAppConfig_NoServicesTag() throws MojoExecutionException, MojoFailureException{
		String sr = getResource("/GenerateServiceRegistryFileMojoTest/testNoServicesInAppConfig_NoServicesTag/serviceregistry-simple.xml");
		Testdata myMocker = new MyMocker();
		myMocker.setAppConfigExtractDir(getResource("/GenerateServiceRegistryFileMojoTest/testNoServicesInAppConfig_NoServicesTag/"));
		GenerateServiceRegistryFileMojo myMojo = mojoFactory(myMocker);
		myMojo.setApplicationsString(TEST_APPLICATION);
		myMojo.setServiceRegistryFile(SERVICE_REGISTRY_FILE);
		myMojo.execute();
		String result = TestUtils.fileToString(sr);
		assertThat(result, containsString(AUTODEPLOY_TEST));
		assertThat(result, containsString(POPP));
		assertThat(result, containsString(TSYS));
		assertThat(result, containsString(PSELV));
	}
	
	@Test(expected=ApplicationConfigException.class)
	public void testNoAppconfigFound() throws MojoExecutionException, MojoFailureException{
		Testdata myMocker = new MyMocker();
		myMocker.setAppConfigExtractDir(EMPTY_DIR);
		GenerateServiceRegistryFileMojo myMojo = mojoFactory(myMocker);
		myMojo.setApplicationsString(TEST_APPLICATION);
		myMojo.setServiceRegistryFile(SERVICE_REGISTRY_FILE);
		myMojo.execute();
	}
	
	@Test(expected=ServiceRegistryException.class)
	public void testNoWsdlFound() throws MojoExecutionException, MojoFailureException{
		Testdata myMocker = new MyMocker();
		myMocker.setServiceExtractDir(EMPTY_DIR);
		GenerateServiceRegistryFileMojo myMojo = mojoFactory(myMocker);
		myMojo.setApplicationsString(TEST_APPLICATION);
		myMojo.setServiceRegistryFile(SERVICE_REGISTRY_FILE);
		myMojo.execute();
	}

	@Test(expected=ApplicationConfigException.class)
	public void testEnvConfigDoesNotReturnAllArtifactCoordinates() throws MojoExecutionException, MojoFailureException{
		Testdata myMocker = new MyMocker();
		HashSet<ApplicationInfo> hashSet = new HashSet<ApplicationInfo>();
		ApplicationInfo applicationInfo = new ApplicationInfo();
		applicationInfo.setName(TEST_APPLICATION);
		applicationInfo.setDomain("test.local");
		hashSet.add(applicationInfo);
		myMocker.setEnvConfigApplications(hashSet);
		GenerateServiceRegistryFileMojo myMojo = mojoFactory(myMocker);
		myMojo.setApplicationsString(TEST_APPLICATION);
		myMojo.setServiceRegistryFile(SERVICE_REGISTRY_FILE);
		myMojo.execute();
	}

	@Test(expected=ApplicationConfigException.class)
	public void testCorruptAppconfig() throws MojoExecutionException, MojoFailureException{
		Testdata myMocker = new MyMocker();
		myMocker.setAppConfigExtractDir(getResource("/GenerateServiceRegistryFileMojoTest/appConfig-Corrupt"));
		GenerateServiceRegistryFileMojo myMojo = mojoFactory(myMocker);
		myMojo.setApplicationsString(TEST_APPLICATION);
		myMojo.setServiceRegistryFile(SERVICE_REGISTRY_FILE);
		myMojo.execute();
	}

	@Test(expected=ApplicationConfigException.class)
	public void testMissigWsdlCoordinates() throws MojoExecutionException, MojoFailureException{
		Testdata myMocker = new MyMocker();
		myMocker.setAppConfigExtractDir(getResource("/GenerateServiceRegistryFileMojoTest/appConfig-MissingWSDLCoordinates"));
		GenerateServiceRegistryFileMojo myMojo = mojoFactory(myMocker);
		myMojo.setApplicationsString(TEST_APPLICATION);
		myMojo.setServiceRegistryFile(SERVICE_REGISTRY_FILE);
		myMojo.execute();
	}

	@Test(expected=MavenArtifactResolevException.class)
	public void testServiceResolveException() throws MojoExecutionException, MojoFailureException{
		Testdata myMocker = new MyMocker();
		myMocker.setMockServiceResolveException(true);
		GenerateServiceRegistryFileMojo myMojo = mojoFactory(myMocker);
		myMojo.setApplicationsString(TEST_APPLICATION);
		myMojo.setServiceRegistryFile(SERVICE_REGISTRY_FILE);
		myMojo.execute();
	}

	@Test(expected=MavenArtifactResolevException.class)
	public void testApplicationInfoResolceException() throws MojoExecutionException, MojoFailureException{
		Testdata myMocker = new MyMocker();
		myMocker.setMockApplicationInfoResolveException(true);
		GenerateServiceRegistryFileMojo myMojo = mojoFactory(myMocker);
		myMojo.setApplicationsString(TEST_APPLICATION);
		myMojo.setServiceRegistryFile(SERVICE_REGISTRY_FILE);
		myMojo.execute();
	}

	@Test
	public void testFreshInstallTrue() throws MojoExecutionException, MojoFailureException{
		String sr = getResource("/GenerateServiceRegistryFileMojoTest/testFreshInstallTrue/") + "serviceregistry-simple.test.xml";
		Testdata myMocker = new MyMocker();
		myMocker.setAppConfigExtractDir(getResource("/GenerateServiceRegistryFileMojoTest/testFreshInstallTrue/"));
		GenerateServiceRegistryFileMojo myMojo = mojoFactory(myMocker);
		myMojo.setFreshInstall(true);
		myMojo.setApplicationsString(TEST_APPLICATION);
		myMojo.setServiceRegistryFile(sr);
		myMojo.execute();
		assertThat(TestUtils.fileToString(sr), containsString("serviceRegistry"));
	}
}
