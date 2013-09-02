package no.nav.esso;

import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;


/**
 * UsernameToken header.
 * 
 */
public class UsernameTokenHeader {

	private static String 
			passwordType = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText";
	private static String securityURL = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
	private static String soapEncURL = "http://schemas.xmlsoap.org/soap/encoding/";
	private static String soapEnvURL = "http://schemas.xmlsoap.org/soap/envelope/";
	private static String xmlSchemaURL = "http://www.w3.org/2001/XMLSchema";
	private static String xmlSchemaInsURL = "http://www.w3.org/2001/XMLSchema-instance";

	private static final String WSSE = "wsse";


	/**
	 * Creates and returns a new basic authentication based SOAP header.
	 * 
	 * @param user
	 *            the username
	 * @param password
	 *            the password
	 * @return SOAPElement new SOAP header with basic authentication embedded
	 */
	public static SOAPElement createUsernameToken(String user, String password) throws SOAPException {

		// Make sure we have the credentials
		if ((user == null) || (password == null)) {
			return null;
		}

		// Now try to build the header
		SOAPElement header = null;
		SOAPFactory sFactory = SOAPFactory.newInstance();

		// Create header Element
		header = createHeaderElement(sFactory);

		// Create and add userName token Element
		Name userTokenName = sFactory.createName("UsernameToken", WSSE,
				securityURL);
		SOAPElement userToken = sFactory.createElement(userTokenName);

		// Populate token
		Name userElementName = sFactory.createName("Username", WSSE,
				securityURL);
		Name passwordElementName = sFactory.createName("Password", WSSE,
				securityURL);

		SOAPElement userElement = sFactory.createElement(userElementName);
		userElement.addTextNode(user);
		SOAPElement passwordElement = sFactory
				.createElement(passwordElementName);
		Name attrName = sFactory.createName("Type");
		passwordElement.addAttribute(attrName, passwordType);
		passwordElement.addTextNode(password);

		// add populated fields to UNT
		userToken.addChildElement(userElement);
		userToken.addChildElement(passwordElement);

		// add UNT to WSS header
		header.addChildElement(userToken);

		return header;
	}

	/**
	 * Creates the SOAP header.
	 * 
	 * @return header
	 * @throws SOAPException
	 *             Soap exception
	 */
	private static SOAPElement createHeaderElement(SOAPFactory sFactory) throws SOAPException {
		SOAPElement header = null;

		Name headerName = sFactory.createName("Security", WSSE, securityURL);
		header = sFactory.createElement(headerName);
		header.addNamespaceDeclaration("soapenc", soapEncURL);
		header.addNamespaceDeclaration("xsd", xmlSchemaURL);
		header.addNamespaceDeclaration("xsi", xmlSchemaInsURL);
		Name mustName = sFactory.createName("mustUnderstand", "soapenv", soapEnvURL);
		header.addAttribute(mustName, "1");

		return header;
	}

}