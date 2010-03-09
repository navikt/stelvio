package no.nav.datapower.xmlmgmt;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import no.nav.datapower.util.DPFileUtils;


public class ImportFilesTester {
	

	public static void main(String[] args) {
		File wsdlFile = new File("E:\\Develop\\wsdl\\wsdl-pselv-kjempen.zip");
		File outputDirectory = new File("E:\\Develop\\wsdl\\tmp\\");
		File tmpFolder = new File(outputDirectory.getAbsolutePath() + "/" + new Date().getTime());

		String host = "https://secgw-01.utv.internsone.local:5550";
		String domain = "test-config";
		String user = "mavendeployer";
		String password = "Test1234";

		try {
			System.out.println("Opening connection to DataPower device...");
			XMLMgmtSession dp = new XMLMgmtSession.Builder(host).
																domain(domain).
																user(user).
																password(password).
																build();
			System.out.println("Extracting ZIP archive...");
			DPFileUtils.extractArchive(wsdlFile, tmpFolder);
			
			System.out.println("Creating request...");
			dp.importFiles(tmpFolder, DeviceFileStore.LOCAL);
			System.out.println("Files successfully imported...");
	
		} catch (IOException e) {
			System.out.println("Failed to extract ZIP archive '" + wsdlFile + "' to folder '" + tmpFolder + "'");
			e.printStackTrace();			
		} catch (XMLMgmtException e) {
			System.out.println("Failed to import files...");			
			e.printStackTrace();
		}
	}
}
