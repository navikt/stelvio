package no.nav.datapower.config.secgw;

import java.io.File;
import java.io.FileFilter;

import junit.framework.TestCase;
import no.nav.datapower.config.ConfigGenerator;
import no.nav.datapower.config.ConfigPackage;
import no.nav.datapower.config.EnvironmentResources;
import no.nav.datapower.util.DPFileUtils;
import no.nav.datapower.util.DPPropertiesUtils;

import org.apache.commons.io.filefilter.NameFileFilter;
import org.omg.PortableInterceptor.SUCCESSFUL;

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
		/*
		String domain = "secgw-u1";
		gen = new SGWConfigGeneratorImpl();
		File bustagDir = new File("E:/data/builds/BUSTAG_R_3_8_0_FULL/");
		File propertiesDirectory = DPFileUtils.append(bustagDir,"src/main/resources/scripts/environments");
		File moduleDirectory = DPFileUtils.append(bustagDir,"target/classes/builds/ear");
		File outputDirectory = DPFileUtils.append(bustagDir,"target/datapower-config/" + domain);
		//File overridesDir = new File("E:/data/builds/BUSTAG_R_3_8_0_FULL/target/datapower-config/secgw-u1");
		EnvironmentResources cfg = new EnvironmentResources();
		cfg.addProperties(DPPropertiesUtils.load(getPropertiesFile(propertiesDirectory, domain)));
		cfg.setModuleDirectory(moduleDirectory);

		

		gen.setEnvironmentResources(cfg);
		gen.setOutputDirectory(outputDirectory);
		
		
		System.out.println("START config generation");				
		ConfigPackage unit = gen.generate();
		System.out.println("END config generation");				

		System.out.println("Directory import-config     = " + unit.getImportConfigDir());
		System.out.println("Directory files/local       = " + unit.getFilesLocalDir());
		System.out.println("Directory files/local/aaa   = " + unit.getFilesLocalAaaDir());
		System.out.println("Directory files/local/wsdl  = " + unit.getFilesLocalWsdlDir());
		System.out.println("Directory files/local/xslt  = " + unit.getFilesLocalXsltDir());
		*/
	}
	
	private File getPropertiesFile(File dir, String domain) {
		System.out.println("getPropertiesFile(), dir = " + dir);
		final String propsFilename = "cfg-" + domain + ".properties";
		System.out.println("Properties filename = " + propsFilename);
		return dir.listFiles((FileFilter) new NameFileFilter(propsFilename))[0];
	}

}
