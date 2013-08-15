package no.stelvio.presentation.security.sso;
/**
 * Enumeration representing the valid types of values that can be retrieved from the request
 * during single-sign-on.
 * 
 * @author persondab2f89862d3
 */
public enum RequestValueType {
	HEADER,
	PARAMETER,
	ATTRIBUTE,
	SESSION_ATTRIBUTE,
	COOKIE;
}
