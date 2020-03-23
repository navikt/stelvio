package no.stelvio.presentation.security.eai.support;
/**
 * The names of the headers sent from WebSEAL to the application server during 
 * authentication or reauthentication.
 * 
 *
 */
public final class WebsealHeaderNames {
	/** Original user id. */
	public static final String ORIGINAL_USERID = "original-user-id";
	/** Authentication level. */
	public static final String AUTHENTICATION_LEVEL = "authentication-level";
	/** Access manager user. */
	public static final String ACCESS_MANAGER_USER = "iv-user";
	/** Authorized as. */
	public static final String AUTHORIZED_AS = "authorized-as";
	/** Authrization type. */
	public static final String AUTHORIZATION_TYPE = "authorization-type";
	/** Webseal junction cookie. */
	public static final String WEBSEAL_JUNCTION_COOKIE = "IV_JCT";
}
