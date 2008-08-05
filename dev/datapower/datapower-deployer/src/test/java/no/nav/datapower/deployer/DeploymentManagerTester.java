package no.nav.datapower.deployer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import no.nav.datapower.util.DPFileUtils;
import no.nav.datapower.util.DPPropertiesUtils;

import org.apache.commons.io.FilenameUtils;

import freemarker.template.TemplateException;

public class DeploymentManagerTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File wsdlArchive = new File("E:\\Develop\\wsdl\\wsdl-pselv-kjempen.zip");
		File outputDirectory = new File("E:\\Develop\\wsdl\\tmp\\");
		File importDirectory = new File("E:\\Develop\\wsdl\\pselv-kjempen\\");
		File dpLocalDirectory = new File(importDirectory.getAbsolutePath() + "\\local\\");
		File dpLocalAaaDirectory = new File(dpLocalDirectory.getAbsolutePath() + "\\aaa\\");
		File dpLocalWsdlDirectory = new File(dpLocalDirectory.getAbsolutePath() + "\\wsdl\\");
		File dpLocalXsltDirectory = new File(dpLocalDirectory.getAbsolutePath() + "\\xslt\\");
		Properties requiredProperties = new Properties();
//		File wsdlDirectory = new File(outputDirectory.getAbsolutePath() + "\\" + new Date().getTime());
		try {
			//deviceProps.load(new FileInputStream(new File("")));
			//envProps.load(new FileInputStream(new File("")));
			requiredProperties.load(new FileInputStream("E:\\Develop\\ws-datapower-tools\\datapower-deployer\\src\\main\\resources\\cfg-secgw-required.properties"));
			System.out.println("Extracting ZIP archive to directory '" + dpLocalWsdlDirectory + "'");
			DPFileUtils.extractArchive(wsdlArchive, dpLocalDirectory);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		Properties props = new DPPropertiesUtils.Builder().add("cfg-secgw-utv.properties").add("cfg-secgw-u1.properties").listDelimiter(',').interpolate(true).buildProperties();				
		Properties props = new DPPropertiesUtils.Builder().
								properties("cfg-secgw-utv.properties").
								properties("cfg-secgw-u1.properties").
								interpolate().
								buildProperties();
		System.out.println("Properties:");
		props.list(System.out);
		
		Map<String, String> invalidProperties = DPPropertiesUtils.validate(props, requiredProperties);
		if (!invalidProperties.isEmpty()) {
			throw new IllegalArgumentException("Configuration contains invalid Properties:\r\n" + invalidProperties.values());
			//DPCollectionUtils.printLines(invalidProperties.values(), System.err, "ERROR: Invalid Property: ");
		}
		
		DeploymentManager deployer = new DeploymentManager("https://secgw-01.utv.internsone.local:5550", "mavendeployer", "Test1234");
		try {
			String aaaMappingFilename = FilenameUtils.concat(dpLocalAaaDirectory.getAbsolutePath(), (String) props.getProperty("aaaFileName"));
			System.out.println("AAAInfo file = " + aaaMappingFilename);
			File aaaMappingFile = new File(aaaMappingFilename);
			FileWriter aaaWriter = new FileWriter(aaaMappingFile);
			deployer.getConfigBuilder().buildAAAInfoFile("aaa-mapping-file.ftl", props, aaaWriter);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		deployer.importFilesAndDeployConfiguration("secgw-configuration.ftl", props, importDirectory, true);
//		deployer.importFilesAndDeployConfiguration("secgw-configuration.ftl", cfg, wsdlDirectory, true);
//		deployer.importFilesFromDirectory("test-config", importDirectory, true);
//		deployer.deployConfiguration("secgw-configuration.ftl", cfg, wsdlDirectory);
	}
}
