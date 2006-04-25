package no.trygdeetaten.web.framework.taglib;

import java.net.URL;
import java.util.Hashtable;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.util.ResponseUtils;

import no.trygdeetaten.common.framework.config.Config;
import no.trygdeetaten.common.framework.context.TransactionContext;
import no.trygdeetaten.common.framework.error.ErrorHandler;

/**
 * Custom tag for rendering a link to the help page. The tag uses the Spring Framework configuration file,
 * usually <u>presentation-services.xml</u>. The tag can be used to render a window specific link or the same link
 * for all windows.
 * 
 * <p/>
 * 
 * JSP Usage: <br/>
 * 
 * <pre>
 * <%@taglib uri="/WEB-INF/bidrag.tld" prefix="bidrag"%>
 * 
 * ...
 * 
 * <bidrag:help label"Hjelp" beanId="hjelpetekstURL" contextAware="true" extention=".html" target="hjelpTarget"/>
 * </pre>
 * 
 * <p/>
 * 
 * Spring Configuration Usage: <br/>
 * 
 * <pre>
 * <bean id="hjelpetekstURL" class="java.lang.String">
 *     <constructor-arg index="0"><value>http://[servername]:[port]/help/index.jsp?topic=/[project]/[path]/</value></constructor-arg>
 * </bean>
 * </pre>
 * 
 * <p/>
 * 
 * The first example shows how to create a context aware help link, and 
 * the next example shows how to create a general help link: <br/>
 * 
 * JSP: <br/>
 * 
 * <pre>
 * <bidrag:help label"Hjelp" beanId="CONTEXT_AWARE_BISYS_HELP" contextAware="true" extention=".html" target="hjelpTarget"/>
 * <bidrag:help label"Hjelp" beanId="GENERAL_BISYS_HELP" contextAware="false" extention=".html" target="hjelpTarget"/>
 * </pre>
 * 
 * <p/>
 * 
 * presentation-services.xml: <br/>
 * 
 * <pre>
 * <bean id="CONTEXT_AWARE_BISYS_HELP" class="java.net.URL">
 *     <constructor-arg index="0"><value>http://hpapt03:8081/help/index.jsp?topic=/rtv-bidrag-help/html/hjelp/</value></constructor-arg>
 * </bean>
 * <bean id="GENERAL_BISYS_HELP" class="java.net.URL">
 *     <constructor-arg index="0"><value>http://hpapt03:8081/help/index.jsp?topic=/rtv-bidrag-help/html/innhold.html</value></constructor-arg>
 * </bean>
 * </pre>
 * 
 * @author person7553f5959484, Accenture
 * @version $Id: HelpLinkTag.java 2201 2005-04-07 08:16:41Z psa2920 $
 */
public class HelpLinkTag extends TagSupport {

	/** 
	 * The url cache used to avoid unnecessary round trips to lower tiers. 
	 * Hashtable is used because cache is maintained by multiple threads.
	 */
	private static Hashtable urls = new Hashtable();

	/** The text to be displayed. MANDATORY. */
	private String label = null;

	/** The name of the bean. MANDATORY. */
	private String beanId = null;

	/** The name of the window. OPTIONAL. */
	private String target = null;

	/** The url extention to use if link is to be context aware. OPTIONAL. */
	private String extention = null;

	/** The context aware setting. OPTIONAL. */
	private boolean isContextAware = false;

	/**
	 * Sets the text to be displayed as the link.
	 * 
	 * @param label the text.
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Sets the name of the bean to look up in the Spring Framework configuration file.
	 * 
	 * @param beanId the name of the bean.
	 */
	public void setBeanId(String beanId) {
		this.beanId = beanId;
	}

	/**
	 * Sets the name of the window where the help page should be opened.
	 * 
	 * @param target the window name.
	 */
	public void setTarget(String target) {
		this.target = target;
	}

	/**
	 * Sets the extention to use if link is to be context aware.
	 * 
	 * @param extention the extention.
	 */
	public void setExtention(String extention) {
		this.extention = extention;
	}

	/**
	 * Sets if the link should is context aware or not. By default it is not.
	 * 
	 * @param contextAware true or false.
	 */
	public void setContextAware(String contextAware) {
		// This is null pointer safe. In case of null, false will be the winner.
		isContextAware = Boolean.valueOf(contextAware).booleanValue();
	}

	/**
	 * Renders an HTML A tag.
	 * 
	 * {@inheritDoc}
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	public int doStartTag() throws JspException {

		StringBuffer html = new StringBuffer("<a href=\"");
		URL url = (URL) urls.get(beanId);
		if (null == url) {
			try {
				Config config = (Config) pageContext.getServletContext().getAttribute(Config.PRESENTATION_SERVICES);
				url = (URL) config.getBean(beanId);
				if (null != url) {
					urls.put(beanId, url);
				}
			} catch (RuntimeException re) {
				ErrorHandler.handleError(re);
			}
		}
		html.append(url);
		if (isContextAware) {
			html.append(TransactionContext.getModuleId());
			if (null != extention) {
				html.append(extention);
			}
		}

		if (null != target) {
			html.append("\" target=\"").append(target);
		}
		html.append("\">").append(label).append("</a>");

		ResponseUtils.write(pageContext, html.toString());
		return SKIP_BODY;
	}
}
