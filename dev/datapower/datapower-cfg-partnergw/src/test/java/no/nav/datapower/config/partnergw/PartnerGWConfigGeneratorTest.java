package no.nav.datapower.config.partnergw;

import junit.framework.TestCase;

public class PartnerGWConfigGeneratorTest extends TestCase {

	private PGWConfigGeneratorImpl gen;
	
	public PartnerGWConfigGeneratorTest(String arg0) {
		super(arg0);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGenerate() {
//		gen = new PartnerGWConfigGenerator();
//		ConfigResources cfg = new ConfigResources();
//		cfg.addWsdlArchive(new File("E:\\Develop\\wsdl\\wsdl-tptilb-kjempen.zip"));
//		cfg.addProperties(DPPropertiesUtils.load(DPFileUtils.getResource(getClass(), "/cfg-partnergw-utv.properties")));
////		cfg.addProperties(DPPropertiesUtils.load(DPFileUtils.getResource(getClass(), "/cfg-partnergw-u1.properties")));
//		File domainDir = DPFileUtils.append(new File("/data/datapower"), cfg.getProperty("cfgDomain"));
//		File outputDir = DPFileUtils.append(domainDir, Long.toString(new Date().getTime()));
//		gen.setOutputDirectory(outputDir);
//		gen.setConfigResources(cfg);
//		ConfigUnit unit = gen.generate();
//		System.out.println("Directory import-config     = " + unit.getImportConfigDir());
//		System.out.println("Directory files/local       = " + unit.getFilesLocalDir());
//		System.out.println("Directory files/local/aaa   = " + unit.getFilesLocalAaaDir());
//		System.out.println("Directory files/local/wsdl  = " + unit.getFilesLocalWsdlDir());
//		System.out.println("Directory files/local/xslt  = " + unit.getFilesLocalXsltDir());
	}
}
