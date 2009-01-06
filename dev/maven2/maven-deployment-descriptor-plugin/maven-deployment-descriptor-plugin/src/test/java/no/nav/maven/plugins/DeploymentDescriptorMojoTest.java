package no.nav.maven.plugins;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.wagon.util.FileUtils;

public class DeploymentDescriptorMojoTest extends AbstractMojoTestCase{
	
	private static final String TEST_POM_DIRECTORY = "src/test/resources/pom/";
	
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
	
	private File getEar(String earName) {
		URL earUrl = getClass().getResource(earName);
		return new File(earUrl.getPath().toString());
	}
	
	private File getTestEar(File ear) {
		File testEar = new File(ear.getPath().toString().replace(".", "-test."));
		if (!testEar.exists()) {
			try {
				FileUtils.copyFile(ear, testEar);
			} catch (IOException e1) {
				fail("Failed to copy source EAR file");
			}
		}
		return testEar;
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

	public void executeEjbSecurityMojo(){
		
		File pom = getTestPom(TEST_POM_DIRECTORY + "ejb-security-pom.xml");
		Mojo mojo = getMojo("addEjbSecurity", pom);
		try {
			mojo.execute();
		} catch (Throwable t){
			t.printStackTrace();
		}
	}
	public void executeWsSecurityOutboundMojo(){
		
		File pom = getTestPom(TEST_POM_DIRECTORY + "ws-security-outbound-pom.xml");
		File earFile = getEar("/ear/test-prod-oppkoblingstestApp.ear");
		File testEarFile = getTestEar(earFile); 
		Mojo mojo = getMojo("addWsSecurityOutbound", pom);
		try {	
			mojo.execute();
		} catch (Throwable t){
			t.printStackTrace();
		}	
	}
	
	public void testExecuteDeploymentDescriptorMojo(){
		executeEjbSecurityMojo();
		//executeWsSecurityOutboundMojo();
		//executeWsSecurityInboundMojo();
	}
	
	public void executeWsSecurityInboundMojo(){
		File pom = getTestPom(TEST_POM_DIRECTORY + "ws-security-inbound-pom.xml");
		File earFile = getEar("/ear/nav-cons-pen-pselv-person-NOTOKENS.ear");
		File testEarFile = getTestEar(earFile); 
		Mojo mojo = getMojo("addWsSecurityInbound", pom);
		try {	
			mojo.execute();
		} catch (Throwable t){
			t.printStackTrace();
		}	
	}
	

}
