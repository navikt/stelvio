package no.nav.maven.plugins;

import java.io.File;

import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;

public class DataPowerPluginIntegrationTest extends AbstractMojoTestCase {
	
	private enum MojoTest {
		GENERATE_CONFIG("generateConfig","src/test/resources/test-generateConfig-pom.xml"),
		IMPORT_FILES("importFiles","src/test/resources/test-importFiles-pom.xml"),
		IMPORT_CONFIG("importConfig","src/test/resources/test-importConfig-pom.xml");
		
		private String goal;
		private String pom;
		private MojoTest(String goal, String pom) { this.goal = goal; this.pom = pom; }
		protected String getGoal() { return goal; }
		protected String getPom() { return pom; }
	}

	protected void setUp() throws Exception {
		super.setUp();
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
		Mojo wsClient = getMojo(test);
		try {
			wsClient.execute();
		} catch (MojoExecutionException e) {
			fail("Caught MojoExecutionException");
		} catch (MojoFailureException e) {
			fail("Caught MojoFailureException");
		}		
	}
	
	public void test_secgw_GenerateConfigMojo() {
		executeMojo(MojoTest.GENERATE_CONFIG);
	}

	public void test_secgw_ImportFilesMojo() {
		executeMojo(MojoTest.IMPORT_FILES);		
	}

	public void test_secgw_ImportConfigMojo() {
		executeMojo(MojoTest.IMPORT_CONFIG);		
	}
}
