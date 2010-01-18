package no.nav.maven.plugins.datapower;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;
import java.util.Map;


import no.nav.datapower.xmlmgmt.DeviceFileStore;
import no.nav.datapower.xmlmgmt.XMLMgmtException;
import no.nav.datapower.xmlmgmt.XMLMgmtRequestFactory;
import no.nav.datapower.xmlmgmt.XMLMgmtRequest;
import no.nav.datapower.xmlmgmt.XMLMgmtUtil;


import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;


/**
 * Goal which imports files into a Datapower domain.
 *
 * @goal importFiles
 * 
 */
public class ImportFilesMojo extends AbstractDeploymentMojo {

	
	/**
	 * @parameter expression="${importDirectory}" alias="importDirectory"
	 */
	private File importDirectory;
	/**
	 * @parameter expression="${dirExclusions}" alias="dirExclusions"
	 */
	private String dirExclusions;
	/**
	 * @parameter expression="${fileExclusions}" alias="fileExclusions"
	 */
	private String fileExclusions;
	
	protected void doExecute() throws MojoExecutionException, MojoFailureException {
		
		getLog().info("ImportFilesMojo:");
		try{	
			String[] dirExcludes = dirExclusions != null ? dirExclusions.split(",") : new String[0];
			String[] fileExcludes = fileExclusions != null ? fileExclusions.split(",") : new String[0];
			List<File> children = XMLMgmtUtil.getDirectoriesToTraverse(importDirectory, dirExcludes);
			
			for (File child : children) {
				
				
				DeviceFileStore childLocation = DeviceFileStore.fromString(child.getName());
				DeviceFileStore location = childLocation != null ? childLocation : DeviceFileStore.LOCAL;
				if (location == DeviceFileStore.LOCAL) {
					XMLMgmtRequest request = XMLMgmtRequestFactory.createCreateDirectoriesRequest(getDomain(),child, DeviceFileStore.LOCAL);
					if(request != null){
						String response = getXMLMgmtSession().doSoapRequest(request);
						//System.out.println("*********** CreateDir Response:" + response);
					}
				}
				XMLMgmtRequest request = XMLMgmtRequestFactory.createSetFilesRequest(getDomain(), child,location, fileExcludes);
				if(request != null){
					String response = getXMLMgmtSession().doSoapRequest(request);
					//System.out.println("***********set-file Response:" + response);
				}
				
				getLog().info("Importing files to device location '" + location + "'");	
				
			}	
		} catch (XMLMgmtException e){
			throw new MojoExecutionException("Failed to import files from directory '" + importDirectory + "' to domain '" + getDomain() + "'", e);
		} catch (IOException e){
			throw new MojoExecutionException("Failed to import files from directory '" + importDirectory + "' to domain '" + getDomain() + "'", e);
		}
		
	}
	
	
	
}
