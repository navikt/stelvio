package no.stelvio.presentation.security.context.parse;

import java.io.IOException;
import java.net.URL;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.RuleSetBase;
import org.xml.sax.SAXException;

/**
 * This class parses and reads in all the &lt;security-role> elements from web.xml and stores the roles in a
 * <code>WebAppRoles</code> object. The parsing of the file is done using <code>org.apache.commons.digester.Digester</code>.
 * 
 * @see WebAppRoles
 * @see org.apache.commons.digester.Digester
 * @version $Id$
 */
public class WebXmlParser {

	private WebAppRoles webAppRoles;

	/**
	 * Constructs a WebXmlParser object and parses web.xml represented by the url argument.
	 * 
	 * @param url
	 *            the url of the web.xml file
	 * @throws IOException
	 *             from the digester
	 * @throws SAXException
	 *             from the digester
	 */
	public WebXmlParser(final URL url) throws IOException, SAXException {

			Digester digester = new Digester();
			// don't validate the XML compared to the schema. This setting
			// may change
			// or made an option in a further version of this class. For now
			// we don't
			// define application object
			digester.setValidating(false);
			digester.addRuleSet(new DefineSecurityRuleSet());
			this.webAppRoles = parseWebXml(digester, url);
	}

	/**
	 * Gets the WebAppRoles object containing the parsed security role elements.
	 * 
	 * @return the webAppRoles containing a collection of security roles
	 */
	public WebAppRoles getWebAppRoles() {
		return webAppRoles;
	}

	/**
	 * Parses web.xml using the specified digester and returns a WebAppRoles object.
	 * 
	 * @param digester
	 *            the digester to use
	 * @param url
	 *            the url of the web.xml file
	 * @return a WebAppRoles objecy containing a collection of security roles
	 * @throws IOException
	 *             from the digester
	 * @throws SAXException
	 *             from the digester
	 */
	private WebAppRoles parseWebXml(final Digester digester, final URL url) throws IOException, SAXException {
		final WebAppRoles webapp = (WebAppRoles) digester.parse(url.toString());

		return webapp;
	}

	/**
	 * Internal class DefineSecurityRuleSet contains the DigesterRules to parse the web.xml file.
	 */
	static class DefineSecurityRuleSet extends RuleSetBase {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void addRuleInstances(Digester digester) {
			digester.addObjectCreate("web-app", WebAppRoles.class);
			digester.addObjectCreate("web-app/security-role", SecurityRole.class);
			digester.addBeanPropertySetter("web-app/security-role/role-name", "roleName");
			digester.addSetNext("web-app/security-role", "addSecurityRole");
		}
	}
}
