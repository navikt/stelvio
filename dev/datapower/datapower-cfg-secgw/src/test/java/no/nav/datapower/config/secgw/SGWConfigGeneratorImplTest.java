package no.nav.datapower.config.secgw;

import java.io.File;
import java.io.FileFilter;

import no.nav.datapower.util.DPFileUtils;
import no.nav.datapower.util.DPPropertiesUtils;

public class SGWConfigGeneratorImplTest extends TestCase {

	private ConfigGenerator gen;
	
	public SGWConfigGeneratorImplTest(String arg0) {
		super(arg0);
	}

	protected void setUp() throws Exception {
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testGenerate() {
//		String domain = "secgw-u1";
//		gen = new SGWConfigGeneratorImpl();
//		File bustagDir = new File("E:/data/builds/BUSTAG_K_2_19_2_FULL/");
//		File propertiesDirectory = DPFileUtils.append(bustagDir,"src/main/resources/scripts/environments");
//		File moduleDirectory = DPFileUtils.append(bustagDir,"target/classes/builds/ear");
//		File outputDirectory = DPFileUtils.append(bustagDir,"target/datapower-config/" + domain);
////		File overridesDir = new File("E:/data/builds/BUSTAG_K_2_19_6/target/datapower-config/partner-gw-u1");
//		EnvironmentResources cfg = new EnvironmentResources();
//		cfg.addProperties(DPPropertiesUtils.load(getPropertiesFile(propertiesDirectory, domain)));
//		cfg.setModuleDirectory(moduleDirectory);
////		ConfigGenerator gen = new PGWConfigGeneratorImpl();
//		gen.setEnvironmentResources(cfg);
//		gen.setOutputDirectory(outputDirectory);
//
//		System.out.println("START config generation");				
//		ConfigPackage unit = gen.generate();
//		System.out.println("END config generation");				
//
//		System.out.println("Directory import-config     = " + unit.getImportConfigDir());
//		System.out.println("Directory files/local       = " + unit.getFilesLocalDir());
//		System.out.println("Directory files/local/aaa   = " + unit.getFilesLocalAaaDir());
//		System.out.println("Directory files/local/wsdl  = " + unit.getFilesLocalWsdlDir());
//		System.out.println("Directory files/local/xslt  = " + unit.getFilesLocalXsltDir());
	}
	
	private File getPropertiesFile(File dir, String domain) {
		System.out.println("getPropertiesFile(), dir = " + dir);
		final String propsFilename = "cfg-" + domain + ".properties";
		System.out.println("Properties filename = " + propsFilename);
		return dir.listFiles((FileFilter) new NameFileFilter(propsFilename))[0];
	}

}
