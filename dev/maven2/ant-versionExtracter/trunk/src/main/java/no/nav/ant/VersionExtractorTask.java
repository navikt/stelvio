package no.nav.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * @author personcca3f1d5452e
 * Created on Aug 15, 2007
 *
 * Denne klassen tar som input en url som i sin tur igjen er hentet fra psf filen. Url'n parses
 * og riktig tag fra svn returneres 
 */
public class VersionExtractorTask extends Task {
	
	private String propertyName;
	private String url;
	private String tag = "";
	
	
	
	/**
	 * Denne er kun til for junit testing
	 * @return Returns the tags. 
	 */
	protected String getTag() {
		return tag;
	}
	
	
	/**
	 * @return Returns the propertyName.
	 */
	public String getPropertyName() {
		return propertyName;
	}
	/**
	 * @return Returns the url.
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url The url to search for tagname in.
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @param propertyName The propertyName to set.
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	
	
	public void execute() throws BuildException {
		
		if (url == null) {
			throw new BuildException("parameter url er ikke satt");
		}
		int pos = url.indexOf("/tags/");
		if (pos == -1) {
			tag = "";
		} else if ((url.indexOf("/stelvio/") != -1)) {
			tag = "";
		} else {
			tag = url.substring(pos+"/tags/".length(),url.indexOf("/",pos+"/tags/".length()));
		}
		getProject().setNewProperty(propertyName,tag);

	}
}
