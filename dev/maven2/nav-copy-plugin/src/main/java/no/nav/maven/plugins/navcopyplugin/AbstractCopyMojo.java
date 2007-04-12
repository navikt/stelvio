/**
 * 
 */
package no.nav.maven.plugins.navcopyplugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

/**
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 */
public abstract class AbstractCopyMojo extends AbstractMojo {
	private Log log;
	
	/**
	 * @parameter default-value="${basedir}"
	 */
    protected String targetDirectory;
    
    public Log getLog() {
    	if (log == null) {
    		log = new SystemStreamLog();
    	}
    	
    	return log;
    }
}