package no.nav.datapower.config.secgw;

import java.io.File;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import junit.framework.TestCase;
import no.nav.datapower.config.ConfigGenerator;
import no.nav.datapower.config.ConfigResources;
import no.nav.datapower.config.ConfigUnit;
import no.nav.datapower.util.DPFileUtils;
import no.nav.datapower.util.DPPropertiesUtils;

public class ConfigGeneratorImplTest extends TestCase {

	private ConfigGenerator gen;
	
	public ConfigGeneratorImplTest(String arg0) {
		super(arg0);
	}

	protected void setUp() throws Exception {
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGenerate() {
/*		gen = new ConfigGeneratorImpl();
		ConfigResources cfg = new ConfigResources();
//		cfg.addWsdlArchive(new File("E:\\Develop\\wsdl\\wsdl-pselv-kjempen.zip"));
//		cfg.setWsdlDirectory(new File("E:\\data\\builds\\BUSTAG_K_2_19_2_Full\\target\\classes\\builds\\zip"));
		cfg.setWsdlDirectory(new File("E:\\data\\builds\\BUSTAG_K_2_19_2_Full\\target\\classes\\builds\\ear"));
		cfg.addProperties(DPPropertiesUtils.load(DPFileUtils.getResource(getClass(), "/cfg-secgw-utv.properties")));
		cfg.addProperties(DPPropertiesUtils.load(DPFileUtils.getResource(getClass(), "/cfg-secgw-u1.properties")));
		File domainDir = DPFileUtils.append(new File("/data/datapower"), cfg.getProperty("cfgDomain"));
		File outputDir = DPFileUtils.append(domainDir, Long.toString(new Date().getTime()));
		gen.setOutputDirectory(outputDir);
		gen.setConfigResources(cfg);
		ConfigUnit unit = gen.generate();
		System.out.println("Directory import-config     = " + unit.getImportConfigDir());
		System.out.println("Directory files/local       = " + unit.getFilesLocalDir());
		System.out.println("Directory files/local/aaa   = " + unit.getFilesLocalAaaDir());
		System.out.println("Directory files/local/wsdl  = " + unit.getFilesLocalWsdlDir());
		System.out.println("Directory files/local/xslt  = " + unit.getFilesLocalXsltDir());*/
	}
}
