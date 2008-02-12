/*
 * Created on Jan 24, 2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package no.nav.maven.plugins;  

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * @author utvikler
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DataPowerRequest {

	private String content;
	private Dictionary HTTPHeaders = new Hashtable();
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Dictionary getHTTPHeaders() {
		return HTTPHeaders;
	}
	public void setHTTPHeaders(Dictionary header) {
		HTTPHeaders = header;
	}
}
