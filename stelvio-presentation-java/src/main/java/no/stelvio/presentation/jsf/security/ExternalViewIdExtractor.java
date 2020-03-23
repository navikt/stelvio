package no.stelvio.presentation.jsf.security;

import javax.faces.context.FacesContext;

/**
 * ExternalViewIdExtractor.
 * 
 *
 */
public interface ExternalViewIdExtractor {
	/**
	 * Extract view id.
	 * 
	 * @param ctx context
	 * @return id
	 */
	String extractViewId(FacesContext ctx);
}
