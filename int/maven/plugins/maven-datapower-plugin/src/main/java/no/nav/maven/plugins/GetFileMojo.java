package no.nav.maven.plugins;

import no.nav.datapower.xmlmgmt.DeviceFileStore;
import no.nav.datapower.xmlmgmt.XMLMgmtException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import javax.xml.*;
import java.io.*;
import java.util.regex.*;

/**
 * 
 * Goal which gets a specific file from the specified locations in the specified domain
 * 
 * @goal getFile
 * 
 * @author Christer Idland
 *
 */
public class GetFileMojo extends AbstractDeviceMgmtMojo {

    /**
     * FileName for file to get from DataPower
     * 
     * @parameter expression="${getFileName}"
     * @required
     */    
    private String fileName;
	
	 /**
     * Domain
     * 
     * @parameter expression="${datapower.domain}"
     * @required
     */    
    private String domain;
	
	protected void doExecute() throws MojoExecutionException, MojoFailureException {
		getLog().info("Executing GetFileMojo");
		try {
			String responseFile = getXMLMgmtSession().getFile(DeviceFileStore.LOCAL, fileName, domain);
			responseFile = responseFile.replace("\n","").replace("\r","");
			getLog().debug("File from datapower: " + responseFile);
			
			Pattern p = Pattern.compile(".*<dp:file name=\"(.*?)\">(.*?)</dp:file>.*");
			Matcher m = p.matcher(responseFile);
			Boolean match = m.matches();
			
			String base64value = m.group(2);
			byte[] decodedFile = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64value);
			
			File outputFile = new File(fileName);
			FileOutputStream fos = new FileOutputStream(outputFile);
			fos.write(decodedFile);
			fos.flush();
			
		} catch (XMLMgmtException e) {
			throw new MojoExecutionException("Failed to get " + DeviceFileStore.LOCAL.toString() + fileName + " on the DataPower device",e);
		} catch (Exception e) {
			getLog().info("XML parsing error", e);
		}
	}
}
