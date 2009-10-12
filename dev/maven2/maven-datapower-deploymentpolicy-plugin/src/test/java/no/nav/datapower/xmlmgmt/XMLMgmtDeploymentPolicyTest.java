package no.nav.datapower.xmlmgmt;

import java.io.File;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import junit.framework.TestCase;

public class XMLMgmtDeploymentPolicyTest extends TestCase {

	private XMLMgmtDeploymentPolicy policy;
	private Properties overrides;
	
	@Override
	protected void setUp() throws Exception {
		
		super.setUp();
		/*File file = new File("src/test/resources/export-signature-gw-DeploymentPolicy-T1.xcfg");
		File overriddenProperties = new File("src/test/resources/overrides.properties");
		policy = new XMLMgmtDeploymentPolicy(file);
		overrides = new Properties();
		overrides.load(FileUtils.openInputStream(overriddenProperties));*/
		//overrides.setProperty("signature-gw-CryptoKey@Password", "JADA");
		
		
	}

	public void testGetDeploymentPolicyXML() throws Exception {
		/*policy.overrideProperties(overrides);
		String xml = policy.getDeploymentPolicyXML();
		System.out.println("Policy:");
		System.out.println(xml);*/
	}

}
