package no.nav.datapower.xmlmgmt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import no.nav.datapower.util.DPFileUtils;

public class XMLMgmtSessionTester {

	private File wsdlArchive;
	private File outputDirectory;
//	File importDirectory = new File(outputDirectory.getAbsolutePath() + "\\" + new Date().getTime());
//	File wsdlDirectory = new File(importDirectory.getAbsolutePath() + "\\wsdl");
	private File wsdlDirectory;
	private String domain;


	public XMLMgmtSessionTester() {
		wsdlArchive = new File("E:\\Develop\\wsdl\\wsdl-pselv-kjempen.zip");
		outputDirectory = new File("E:\\Develop\\wsdl\\tmp\\");
		wsdlDirectory = new File(outputDirectory.getAbsolutePath() + "\\" + new Date().getTime());
		domain = "test-config";
		try {
			System.out.println("Extracting ZIP archive to directory '" + wsdlDirectory + "'");
			DPFileUtils.extractArchive(wsdlArchive, wsdlDirectory);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		XMLMgmtSessionTester tester = new XMLMgmtSessionTester();
		try {
			tester.testCreateDirs();
			tester.testImportFiles();
		} catch (XMLMgmtException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void testImportFiles() throws XMLMgmtException {
		getXMLMgmtSession(domain).importFiles(wsdlDirectory, DeviceFileStore.LOCAL);				
	}
	
	public void testCreateDirs() throws XMLMgmtException {
		getXMLMgmtSession(domain).createDirs(wsdlDirectory, DeviceFileStore.LOCAL);				
	}
	
	private XMLMgmtSession getXMLMgmtSession(String domain) {
		return new XMLMgmtSession.Builder("https://secgw-01.utv.internsone.local:5550").user("mavendeployer").password("Test1234").domain(domain).build();
	}
}
