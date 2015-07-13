package no.nav.datapower.xmlmgmt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;



import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

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
	
	
    @Test
    public void testSynchronizeWSRRSubscription() throws URISyntaxException, XMLMgmtException {

        String domain = "u3_servicegwpepsbs";
        String response;

        XMLMgmtSession xmlMgmtSession = getXMLMgmtSession(domain);
        response = xmlMgmtSession.synchronizeWSRRSubscription("WSRRSavedSearchSubscripton");
        Assert.assertTrue("Response should include OK", response.contains("OK"));
    }


    private XMLMgmtSession getXMLMgmtSession(String domain) {
        return new XMLMgmtSession.Builder("https://dp-utv-02.adeo.no:5550/service/mgmt/3.0").user("mavendeployer").password("Test1234").domain(domain).build();
//        return new XMLMgmtSession.Builder("https://a34dpva001.devillo.no:5550/service/mgmt/3.0").user("sysadmin").password("poc1234").domain(domain).build();
    }
}
