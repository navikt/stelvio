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
     * 
     * @parameter expression="${fileName}"
     * @required
     */    
    private String fileName;
	
	 /**
     * 
     * @parameter expression="${domain}"
     * @required
     */    
    private String domain;
	
	/**
     * 
     * @parameter expression="${outputDir}"
     * @required
     */    
    private String outputDir;
	
	protected void doExecute() throws MojoExecutionException, MojoFailureException {
		getLog().info("Executing GetFileMojo: Getting " + DeviceFileStore.LOCAL + fileName + " @ " + domain);
		try {
			String responseFile = getXMLMgmtSession().getFile(DeviceFileStore.LOCAL, fileName, domain);
			responseFile = responseFile.replace("\n","").replace("\r","");
			getLog().debug("File from datapower: " + responseFile);
			
			Pattern p = Pattern.compile(".*<dp:file name=\"(.*?)\">(.*?)</dp:file>.*");
			Matcher m = p.matcher(responseFile);
			Boolean match = m.matches();
			
			String base64value = m.group(2);
			byte[] decodedFile = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64value);
			
			(new File(outputDir)).mkdirs();
			File outputFile = new File(outputDir + fileName);
			FileOutputStream fos = new FileOutputStream(outputFile);
			fos.write(decodedFile);
			fos.flush();
			
		} catch (XMLMgmtException e) {
			throw new MojoExecutionException("Failed to get " + DeviceFileStore.LOCAL.toString() + fileName + " on the DataPower device",e);
		} catch (Exception e) {
			throw new MojoExecutionException("An unexpected error occured", e);
		}
	}
}
