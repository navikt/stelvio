package no.nav.datapower.templates.freemarker;

import org.apache.commons.configuration.ConfigurationException;

public class FreemarkerConfigBuilderTester {

	/**
	 * @param args
	 * @throws ConfigurationException 
	 */
	public static void main(String[] args) {
		
//		File wsdlArchive = new File("E:\\Develop\\wsdl\\wsdl-pselv-kjempen.zip");
//		File outputDirectory = new File("E:\\Develop\\wsdl\\tmp\\");
////		File importDirectory = new File(outputDirectory.getAbsolutePath() + "\\" + new Date().getTime());
////		File wsdlDirectory = new File(importDirectory.getAbsolutePath() + "\\wsdl");
//		File wsdlDirectory = new File(outputDirectory.getAbsolutePath() + "\\" + new Date().getTime());
//		
//		try {
//			System.out.println("Extracting ZIP archive to directory '" + wsdlDirectory + "'");
//			FileUtils.extractArchive(wsdlArchive, wsdlDirectory);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		
//		System.out.println("Listing files in directory, " + wsdlDirectory.getName());
//		ExtendedProperties props = new PropertiesUtils.Builder().
//										add("cfg-secgw.properties").add("cfg-secgw-t1.properties").
//										interpolate(true).buildExtendedProperties();				
//		Hashtable cfg = new Hashtable();
//		cfg.put("cfg", props.subset("cfg"));
//		cfg.put("frontside", props.subset("frontside"));
//		cfg.put("backside", props.subset("backside"));
//		cfg.put("aaa", props.subset("aaa"));
//		
//		FreemarkerConfigBuilder configBuilder = new FreemarkerConfigBuilder(new File("E:/Develop/wdp/datapower-config-builder/src/main/resources"));
//		try {
//			Writer out = new OutputStreamWriter(System.out);
//			configBuilder.buildConfig("secgw-configuration.ftl", cfg, wsdlFiles, out);
//			out.flush();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (TemplateException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	public FreemarkerConfigBuilderTester() {
		
	}

}
