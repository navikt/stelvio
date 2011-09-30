package no.nav.maven.plugins.datapower;

import java.io.File;



import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.jdom.Element;


public class DatapowerDeployPluginTest extends AbstractMojoTestCase {

		
		private enum MojoTest {
			ESGW_CREATE_DOMAIN(					"createDomain",				"src/test/resources/test-es-gw-createDomain.xml"),
			SIGNATUREGW_SETUP_DOMAIN(			"setupDomain",				"src/test/resources/test-signature-gw-setupDomain.xml"),
			SIGNATUREGW_IMPORT_DEPLOYMENTPOLICY("importDeploymentPolicy",	"src/test/resources/test-signature-gw-importDeploymentPolicy.xml"),
			SIGNATUREGW_IMPORT_FILES(			"importFiles",				"src/test/resources/test-signature-gw-importFiles.xml"),
			SIGNATUREGW_IMPORT_CONFIG(			"importConfig",				"src/test/resources/test-signature-gw-importConfig.xml"),
			SIGNATUREGW_SAVE_CONFIG(			"saveConfig",				"src/test/resources/test-signature-gw-saveConfig.xml"),
			SIGNATUREGW_RESTART_DOMAIN(			"restartDomain",			"src/test/resources/test-signature-gw-restartDomain.xml"),
			;
			
			private String goal;
			private String pom;
			private MojoTest(String goal, String pom) { this.goal = goal; this.pom = pom; }
			protected String getGoal() { return goal; }
			protected String getPom() { return pom; }
		}
		
		protected void setUp() throws Exception {
			super.setUp();
			/*System.setProperty("javax.net.ssl.trustStore", "E:/wsDatapower/deployment/maven-datapower-deploy-plugin/src/test/resources/trustStore.jks");
			System.setProperty("javax.net.ssl.trustStorePassword", "Passord1234");
			System.setProperty("javax.net.ssl.trustStoreType", "jks");*/
			
			
			
		}

		protected void tearDown() throws Exception {
			super.tearDown();
		}
		
		private File getTestPom(String pomName) {
			File pom = getTestFile(pomName);
			assertNotNull("Test POM '" + pomName + "' not found!", pom);
			assertTrue("Test POM '" + pomName + "' does not exists!", pom.exists());
			return pom;
		}

		private Mojo getMojo(String goal, File pom) {
			Mojo mojo = null;
			try {
				mojo = lookupMojo(goal, pom);
			} catch (Exception e) {
				e.printStackTrace();
				fail("Failed to lookup Mojo '" + goal + "' from POM '" + pom + "'");
			}
			return mojo;
		}
		
		private Mojo getMojo(MojoTest test) {
			return getMojo(test.getGoal(), getTestPom(test.getPom()));
		}


		private void executeMojo(MojoTest test) {
			//Need to update testdata
			/*Mojo wsClient = getMojo(test);
			try {
				wsClient.execute();
			} catch (MojoExecutionException e) {
				e.printStackTrace();
				fail("Caught MojoExecutionException");
			} catch (MojoFailureException e) {
				e.printStackTrace();
				fail("Caught MojoFailureException");
			}*/	
		}
		
		public void test_es_gw_CreateDomainMojo() {
			executeMojo(MojoTest.ESGW_CREATE_DOMAIN);		
		}
		
		public void test_signature_gw_SetupDomainMojo() {
			executeMojo(MojoTest.SIGNATUREGW_SETUP_DOMAIN);		
		}
		
		public void test_signature_gw_ImportDeploymentPolicyMojo() {
			executeMojo(MojoTest.SIGNATUREGW_IMPORT_DEPLOYMENTPOLICY);		
		}
		public void test_signature_gw_ImportFilesMojo() {
			executeMojo(MojoTest.SIGNATUREGW_IMPORT_FILES);		
		}
		public void test_signature_gw_ImportConfigMojo() {
			executeMojo(MojoTest.SIGNATUREGW_IMPORT_CONFIG);
		}
		public void test_signature_gw_SaveConfigMojo() {
			executeMojo(MojoTest.SIGNATUREGW_SAVE_CONFIG);
		}
		public void test_signature_gw_RestartDomainMojo() {
			executeMojo(MojoTest.SIGNATUREGW_RESTART_DOMAIN);
		}
}

	
	

