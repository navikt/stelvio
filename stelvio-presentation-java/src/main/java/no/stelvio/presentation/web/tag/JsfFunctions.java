package no.stelvio.presentation.web.tag;

import javax.faces.context.FacesContext;

/**
 * Functions to aid developing JSF applications. The class <code>JsfCoreLibrary</code> uses this class to make these methods
 * available from xhtml (facelet) pages.
 * 
 */
public final class JsfFunctions {

	/**
	 * The different fields in a client_id field is delimited with a ':'.
	 */
	private static final String CLIENT_ID_DELIM = ":";

	/**
	 * Stops creation of a new JsfFunctions object.
	 */
	private JsfFunctions() {
	}

	/**
	 * Checks to see if the specified field id in the specified form contains error messages.
	 * 
	 * @param form
	 *            the id of the form
	 * @param id
	 *            the id of the field
	 * @return true if the field contains error, false otherwise
	 */
	public static boolean hasMessage(String form, String id) {
		boolean tja = false;
		FacesContext context = FacesContext.getCurrentInstance();
		String clientId = form + CLIENT_ID_DELIM + id;
		tja = context.getMessages(clientId).hasNext();
		return tja;
	}

}
