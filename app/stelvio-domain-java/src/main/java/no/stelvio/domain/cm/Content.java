package no.stelvio.domain.cm;

import java.io.Serializable;

/**
 * Object for holding information retrieved from the content management
 * system.
 * 
 * @author person6045563b8dec, Accenture
 * @version $Id$
 *
 */
public class Content implements Serializable {

	String contentKey;
	String text;
	String url;
	
	public String getContentKey() {
		return contentKey;
	}
	public void setContentKey(String contentKey) {
		this.contentKey = contentKey;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
