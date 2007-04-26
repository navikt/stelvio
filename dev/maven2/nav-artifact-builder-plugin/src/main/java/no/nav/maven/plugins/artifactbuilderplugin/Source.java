/**
 * 
 */
package no.nav.maven.plugins.artifactbuilderplugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.codehaus.plexus.util.DirectoryScanner;

/**
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 */
public class Source {
	/**
	 * @parameter
	 */
	private String name;
	
	/**
	 * @parameter
	 * @required
	 */
	private File basedir;
	
	/**
	 * @parameter
	 */
	private List<String> includes;
	
	/**
	 * @parameter
	 */
	private List<String> excludes;

	/**
	 * @return the basedir
	 */
	public File getBasedir() {
		return basedir;
	}

	/**
	 * @param basedir the basedir to set
	 */
	public void setBasedir(File basedir) {
		this.basedir = basedir;
	}

	/**
	 * @return the includes
	 */
	public List<String> getIncludes() {
		if (includes == null) {
			includes = new ArrayList<String>();
			includes.add(ArtifactBuilderMojo.DEFAULT_INCLUDES);
		}
		
		return includes;
	}

	/**
	 * @param includes the includes to set
	 */
	public void setIncludes(List<String> includes) {
		this.includes = includes;
	}
	
	/**
	 * @return the excludes
	 */
	public List<String> getExcludes() {
		if (excludes == null) {
			excludes = new ArrayList<String>();
			excludes.addAll(Arrays.asList(DirectoryScanner.DEFAULTEXCLUDES));
		}
		
		return excludes;
	}
	
	/**
	 * @param excludes the excludes to set
	 */
	public void setExcludes(List<String> excludes) {
		this.excludes = excludes;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name == null ? basedir.toString() : name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String projectName) {
		this.name = projectName;
	}
	
	@Override
	public String toString() {
		return name+":"+basedir+":"+includes+":"+excludes;
	}
}