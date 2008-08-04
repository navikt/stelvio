package no.nav.datapower.deployer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import no.nav.datapower.util.DPFileUtils;
import no.nav.datapower.util.DPPropertiesUtils;

import org.apache.commons.collections.ExtendedProperties;

public class DeploymentManagerTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File wsdlArchive = new File("E:\\Develop\\wsdl\\wsdl-pselv-kjempen.zip");
		File outputDirectory = new File("E:\\Develop\\wsdl\\tmp\\");
//		File importDirectory = new File(outputDirectory.getAbsolutePath() + "\\" + new Date().getTime());
//		File wsdlDirectory = new File(importDirectory.getAbsolutePath() + "\\wsdl");
		File wsdlDirectory = new File(outputDirectory.getAbsolutePath() + "\\" + new Date().getTime());
		try {
			//deviceProps.load(new FileInputStream(new File("")));
			//envProps.load(new FileInputStream(new File("")));
			System.out.println("Extracting ZIP archive to directory '" + wsdlDirectory + "'");
			DPFileUtils.extractArchive(wsdlArchive, wsdlDirectory);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ExtendedProperties props = new DPPropertiesUtils.Builder().add("cfg-secgw-utv.properties").add("cfg-secgw-u1.properties").interpolate(true).buildExtendedProperties();				
		Properties cfg = DPPropertiesUtils.convertToNestedSubsets(props, "cfg", "frontside", "backside", "aaa");
		
		DeploymentManager deployer = new DeploymentManager("https://secgw-01.utv.internsone.local:5550", "mavendeployer", "Test1234");
		deployer.importFilesAndDeployConfiguration("secgw-configuration.ftl", cfg, wsdlDirectory, true);
//		deployer.importFilesFromDirectory("test-config", wsdlDirectory, true);
//		deployer.deployConfiguration("secgw-configuration.ftl", cfg, wsdlDirectory);
	}
}
