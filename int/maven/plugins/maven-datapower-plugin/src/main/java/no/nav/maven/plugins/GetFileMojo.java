package no.nav.maven.plugins;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import no.nav.datapower.xmlmgmt.DeviceFileStore;
import no.nav.datapower.xmlmgmt.XMLMgmtException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

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
	
	/**
     * 
     * @parameter default-value="false"
     */    
    private boolean ignoreFileNotFound;
	
	protected void doExecute() throws MojoExecutionException, MojoFailureException {
		getLog().info("Executing GetFileMojo: Getting " + DeviceFileStore.LOCAL + fileName + " @ " + domain);
		try {
			String responseFile = getXMLMgmtSession().getFile(DeviceFileStore.LOCAL, fileName, domain);
			responseFile = responseFile.replace("\n","").replace("\r","");
			getLog().debug("File from datapower: " + responseFile);
			
			Pattern p = Pattern.compile(".*<dp:file name=\"(.*?)\">(.*?)</dp:file>.*");
			Matcher m = p.matcher(responseFile);
			Boolean match = m.matches();
			if(match){
				String base64value = m.group(2);
				byte[] decodedFile = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64value);
				
				(new File(outputDir)).mkdirs();
				File outputFile = new File(outputDir + fileName);
				FileOutputStream fos = new FileOutputStream(outputFile);
				fos.write(decodedFile);
				fos.flush();
			}else{
				if (ignoreFileNotFound){
					getLog().info("The file couldn't be downloaded! Ignoring this because the \"ignoreFileNotFound\" parameter is set to true");
				}else{
					throw new FileNotFoundException("Couldn't download the file!");
				}
			}
			
		} catch (XMLMgmtException e) {
			throw new MojoExecutionException("Failed to get " + DeviceFileStore.LOCAL.toString() + fileName + " on the DataPower device",e);
		} catch (Exception e) {
			throw new MojoExecutionException("An unexpected error occured", e);
		}
	}
}
