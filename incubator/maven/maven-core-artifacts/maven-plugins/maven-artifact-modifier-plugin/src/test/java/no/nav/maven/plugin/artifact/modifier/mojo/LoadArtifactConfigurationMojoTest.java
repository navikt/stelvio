package no.nav.maven.plugin.artifact.modifier.mojo;

import java.io.File;

import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;

public class LoadArtifactConfigurationMojoTest  extends AbstractMojoTestCase {

	public void setUp() throws Exception {
		super.setUp();
	}
	
	public void testMojoExecution() throws Exception {
		Mojo mojo = getMojo();
	}

	
	private Mojo getMojo() throws Exception {
		Mojo mojo = new LoadArtifactConfigurationMojo();
		File pluginXml = new File(getBasedir(), "src/test/resources/unit/plugin-cfg.xml");
		configureMojo(mojo, "maven-artifact-modifier-plugin", pluginXml);
		return mojo;
	}
}
