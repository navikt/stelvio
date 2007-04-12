/**
 * 
 */
package no.nav.maven.plugins.navcopyplugin;

import java.io.File;

/**
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 */
public class SourceFile {
	/**
	 * @parameter
	 * @required
	 */
	private File path;
    
	/**
	 * @parameter
	 * @required
	 */
	private String targetDirectory;
    
	/**
	 * @return the path
	 */
	public File getPath() {
		return path;
	}
	
	/**
	 * @param path the path to set
	 */
	public void setPath(File path) {
		this.path = path;
	}
	
	/**
	 * @return the targetDirectory
	 */
	public String getTargetDirectory() {
		return targetDirectory;
	}
	
	/**
	 * @param targetDirectory the targetDirectory to set
	 */
	public void setTargetDirectory(String targetDirectory) {
		this.targetDirectory = targetDirectory;
	}
}