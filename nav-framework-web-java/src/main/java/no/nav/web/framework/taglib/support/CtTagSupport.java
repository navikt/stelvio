package no.nav.web.framework.taglib.support;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.apache.commons.lang.StringUtils;

import no.nav.common.framework.codestable.CodesTableManager;
import no.nav.common.framework.config.Config;
import no.nav.web.framework.constants.Constants;


/**
 * Support class for the codes table tags.
 *
 * @author personf8e9850ed756, Accenture
 * @version $Id: CtTagSupport.java 2499 2005-09-26 10:40:27Z skb2930 $
 */
public final class CtTagSupport {

	private static final String DO_PACKAGE_NAME = "no.trygdeetaten.common.domain.";

	/** Not to be instantiated. */
	private CtTagSupport() {
	}

	/**
	 * Retrieves the codes table manager from the Config instance on the page context.
	 *
	 * @param pageContext the page context which holds the Config instance.
	 * @return a codes table manager.
	 */
	public static CodesTableManager retrieveCodesTableManager(PageContext pageContext) {
		final Config config = (Config) pageContext.getServletContext().getAttribute(Config.PRESENTATION_SERVICES);

		return (CodesTableManager) config.getBean(Constants.CODES_TABLE_MANAGER_BEAN);
	}

	/**
	 * Retrieves a class to be used for looking up a codes table.
	 *
	 * @param codestable the name of the codes table
	 * @return the class to be used for looking up a codes table.
	 * @throws JspException if the class for the codes table could not be found.
	 */
	public static Class retrieveClass(String codestable) throws JspException {
		final String className = DO_PACKAGE_NAME + StringUtils.trim(codestable); // Handles codestable == null
		final Class doClass;

		try {
			doClass = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new JspException(
				"Could not find named codes table DO '" + codestable + "' when looking in package '" + DO_PACKAGE_NAME + "'.",
				e);
		}

		return doClass;
	}

	/**
	 * Returns the decode of the value.
	 *
	 * @param value the value to find the decode for.
	 * @param codestable the codes table to look up the decode in.
	 * @param pageContext the page context that holds the Config instance used to look up the CodesTableManager.
	 * @return the decode of the value.
	 * @throws JspException if the codes table was not found.
	 */
	public static String decode(Object value, String codestable, PageContext pageContext) throws JspException {
		final Class doClass = retrieveClass(codestable);
		final CodesTableManager codesTableManager = retrieveCodesTableManager(pageContext);

		return codesTableManager.getDecode(doClass, value);
	}
}
