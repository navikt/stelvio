package no.nav.web.framework.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.displaytag.tags.TableTag;
import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;

/**
 * Tag to be nested inside a display:table tag to preserve sorting and paging
 * after a HTTP POST.
 * 
 * @author person7553f5959484, Accenture
 * @version $Id: DisplayHiddenParamsTag.java 2864 2006-04-25 11:18:37Z psa2920 $
 */
public class DisplayHiddenParamsTag extends TagSupport {

	/** Logger to be used for writing <i>TRACE</i> and <i>DEBUG</i> messages. */
	protected final Log log = LogFactory.getLog(this.getClass());

	/** Must correspond to value of "uid" on desired displaytag table. */
	protected String name = null;

	/**
	 * Returns the name (uid of displaytable tag).
	 * 
	 * @return the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name (uid of displaytable tag).
	 * 
	 * @param string
	 *            the name
	 */
	public void setName(String string) {
		name = string;
	}

	/**
	 * Write the necessary hidden tags to the response.
	 * 
	 * {@inheritDoc}
	 * 
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	public int doStartTag() throws JspException {

		// Is this tag nested inside TableTag from
		// http://displaytag.sourceforge.net/
		TableTag tableTag = (TableTag) TagSupport.findAncestorWithClass(this, TableTag.class);
		if (null != tableTag) {
			name = tableTag.getUid();
			if (log.isDebugEnabled()) {
				log.debug("Tag is enclosed by " + TableTag.class.getName() + ". Ignores the \"name\" attribute, uses "
						+ name);
			}
		} else if (null != name) {
			if (log.isDebugEnabled()) {
				log.debug("Uses the \"name\" attribute, " + name);
			}
		} else {
			throw new JspException("If tag is not enclosed by " + TableTag.class.getName()
					+ " the \"name\" attribute must be set to the value of desired display:table \"uid\" attribute.");
		}

		ParamEncoder paramEncoder = new ParamEncoder(name);
		doHidden(paramEncoder, TableTagParameters.PARAMETER_PAGE);
		doHidden(paramEncoder, TableTagParameters.PARAMETER_SORT);
		doHidden(paramEncoder, TableTagParameters.PARAMETER_ORDER);

		// Skip body evaluation
		return TagSupport.SKIP_BODY;
	}

	/**
	 * Write a hidden tag for the specified parameter and encoder.
	 * 
	 * @param paramEncoder
	 *            parameter encoder instance.
	 * @param parameterName
	 *            parameter to write as hidden tag.
	 * @throws JspException
	 *             if something weird happens.
	 */
	private void doHidden(ParamEncoder paramEncoder, String parameterName) throws JspException {
		String name = paramEncoder.encodeParameterName(parameterName);
		String value = (String) pageContext.getRequest().getAttribute(name);
		if (null == value) {
			value = pageContext.getRequest().getParameter(name);
		}
		if (log.isDebugEnabled()) {
			log.debug(name + " = " + value);
		}
		if (null != value) {
			StringBuffer hidden = new StringBuffer("<input type=\"hidden\" name=\"");
			hidden.append(name).append("\" value=\"").append(value).append("\"/>\n");
			write(hidden.toString());
		}
	}

	/**
	 * Write the specified text as the response to the writer associated with
	 * this page.
	 * 
	 * @param text
	 *            the text to be written.
	 * @throws JspException
	 *             if an input/output error occurs.
	 */
	private void write(String text) throws JspException {
		JspWriter writer = pageContext.getOut();
		try {
			writer.print(text);
		} catch (IOException io) {
			throw new JspException("Failed to print '" + text + "' to current JspWriter", io);
		}
	}

}
