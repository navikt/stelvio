package no.nav.datapower.xmlmgmt;


public class ImportConfigTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
//		String rootPath = "E:\\Develop\\wdp\\datapower-utils";
//		File templateFile = new File(rootPath + "\\src\\main\\resources\\template.xcfg");
//		File environmentFile = new File(rootPath + "\\src\\main\\resources\\environment.properties");
//
//		String host = "https://secgw-01.utv.internsone.local:5550";
//		String domain = "test-config";
//		String user = "mavendeployer";
//		String password = "Test1234";
//
//		Properties envProps = new Properties();
//		String templateContent = null;
//		try {
//			envProps.load(new FileInputStream(environmentFile));
//			templateContent = FileUtils.getFileAsString(templateFile);
//		} catch (Exception e) {
//			System.out.println("Failed to read template or environment properties files");
//			throw e;
//		}		
//	
//		// Create config formatter
//		System.out.println("Reformatting config...");
//		XCFGFormatter cfgFormatter = new XCFGFormatter(templateContent);
//		String newConfig = cfgFormatter.format(envProps);
//
//		//ZipUtils.compress(source, destination
//		System.out.println("Adding config to zip file...");
//		byte[] zipBytes = FileUtils.createZipArchive(newConfig.getBytes("UTF-8"), "export.xml");
//		
//		// Open connection to DataPower device
//		System.out.println("Opening connection to DataPower device...");
//		XMLMgmtInterface dp = new XMLMgmtInterface.Builder(host).domain(domain).user(user).password(password).build();
//		System.out.println("Importing configuration...");
//		String compressedData = new String(Base64.encodeBase64(zipBytes));
//		dp.importConfig(compressedData, ImportFormat.ZIP);
//		
//		System.out.println("Done!");
		
	}

}
