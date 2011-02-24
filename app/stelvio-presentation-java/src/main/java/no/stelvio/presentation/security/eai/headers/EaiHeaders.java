package no.stelvio.presentation.security.eai.headers;

import java.util.Map;
import java.util.Set;

/**
 * Interface for retrieval of EAI headers.
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 * 
 */
public interface EaiHeaders {
	
	/**
	 * Returns Eai header names.
	 * 
	 * @return Eai header names
	 */
	Set<String> getHeaderNames();
	
	/**
	 * Returns Eai header values.
	 * 
	 * @return Eai header values
	 */
	Set<String> getHeaderValues();
	
	/**
	 * Returns Eai headers.
	 * 
	 * @return Eai headers
	 */
	Map<String, String> getHeaders();
}
