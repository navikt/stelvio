package no.nav.datapower.config.partnergw;

import java.io.File;
import java.io.FileFilter;

import junit.framework.TestCase;
import no.nav.datapower.config.ConfigPackage;
import no.nav.datapower.config.EnvironmentResources;
import no.nav.datapower.util.DPPropertiesUtils;

import org.apache.commons.io.filefilter.NameFileFilter;

public class PGWConfigGeneratorTest extends TestCase {

	private PGWConfigGeneratorImpl gen;
	
	public PGWConfigGeneratorTest(String arg0) {
		super(arg0);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGenerate() {
//		String domain = "partner-gw-u1";
//		gen = new PGWConfigGeneratorImpl();
//		File bustagDir = new File("E:/data/builds/BUSTAG_K_2_22_0/");
//		File propertiesDirectory = new File("E:/data/builds/BUSTAG_K_2_22_0/src/main/resources/scripts/environments");
//		File moduleDirectory = new File("E:/data/builds/BUSTAG_K_2_22_0/target/classes/builds/ear");
//		File outputDirectory = new File("E:/data/builds/BUSTAG_K_2_22_0/target/datapower-config/" + domain);
////		File overridesDir = new File("E:/data/builds/BUSTAG_K_2_19_6/target/datapower-config/partner-gw-u1");
//		EnvironmentResources cfg = new EnvironmentResources();
//		cfg.addProperties(DPPropertiesUtils.load(getPropertiesFile(propertiesDirectory, domain)));
//		cfg.setModuleDirectory(moduleDirectory);
////		ConfigGenerator gen = new PGWConfigGeneratorImpl();
//		gen.setEnvironmentResources(cfg);
//		gen.setOutputDirectory(outputDirectory);
//
//		System.out.println("START config generation");				
//		ConfigPackage cfgPackage = gen.generate();
//		System.out.println("END config generation");				
	}
	
	private File getPropertiesFile(File dir, String domain) {
		System.out.println("getPropertiesFile(), dir = " + dir);
		final String propsFilename = "cfg-" + domain + ".properties";
		System.out.println("Properties filename = " + propsFilename);
		return dir.listFiles((FileFilter) new NameFileFilter(propsFilename))[0];
	}

}
