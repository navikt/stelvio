package no.nav.maven.plugins;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

import no.nav.datapower.xmlmgmt.DeviceFileStore;
import no.nav.datapower.xmlmgmt.XMLMgmtException;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.util.Arrays;

/**
 * Goal which imports a DataPower configuration into a specified device.
 *
 * @goal importFiles
 * 
 */
public class ImportFilesMojo extends AbstractDeviceMgmtMojo {

	/**
	 * @parameter expression="${importDir}" alias="importDir"
	 */
	private File importDirectory;
	
	/**
	 * @parameter expression="${singleFiles}" default-value="false"
	 */
	private boolean singleFiles;

	protected void doExecute() throws MojoExecutionException, MojoFailureException {
		if (!importDirectory.isDirectory())
			throw new IllegalArgumentException("Specified path '" + importDirectory + "'is not a directory");
		if (singleFiles){
			File[] children = importDirectory.listFiles();
			System.out.println(importDirectory + " mappe, lengde " + children.length);
			List<File> filelist = Arrays.asList(children);
			try {
				DeviceFileStore location = DeviceFileStore.LOCAL;
				getLog().info("Importing following files to device location '" + location + "': ");
				for (File child : children) {
					getLog().info("File = " + child);
				}
				getXMLMgmtSession().importFiles(filelist, importDirectory, location);
			} catch (XMLMgmtException e) {
				throw new MojoExecutionException("Failed to import files from directory '" + importDirectory + "' to domain '" + getDomain() + "'", e);
			}
		} else {
			File[] children = importDirectory.listFiles((FileFilter)FileFilterUtils.directoryFileFilter());
			for (File child : children) {
				getLog().info("File = " + child);
				try {
					DeviceFileStore childLocation = DeviceFileStore.fromString(child.getName());
					DeviceFileStore location = childLocation != null ? childLocation : DeviceFileStore.LOCAL;
					if (location == DeviceFileStore.LOCAL) {
						getXMLMgmtSession().createDirs(child, DeviceFileStore.LOCAL);
					}
					getLog().info("Importing files to device location '" + location + "'");
					getXMLMgmtSession().importFiles(child, location);
				} catch (XMLMgmtException e) {
					throw new MojoExecutionException("Failed to import files from directory '" + importDirectory + "' to domain '" + getDomain() + "'", e);
				}
			}
		}
	}
}
