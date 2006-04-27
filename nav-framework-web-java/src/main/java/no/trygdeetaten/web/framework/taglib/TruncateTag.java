package no.trygdeetaten.web.framework.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Custom tag for truncating the body content.
 * 
 * @author Fredrik Dahl-Jørgensen, Accenture
 * @version $Id: TruncateTag.java 2726 2005-12-30 13:08:35Z skb2930 $
 */
public class TruncateTag extends BodyTagSupport {

	private int length;
	private String concat;

	/** Constructs a default instance of TruncateTag. */
	public TruncateTag() {
		init();
	}

	/** Initializes the TruncateTag. */
	private void init() {
		length = 0;
		concat = "...";
	}

	/**
	 * Truncating the bodycontent to the specified length with the specified string appended.
	 */
	public int doEndTag() throws JspException {
		try {
			if (null != bodyContent) {
				String content = bodyContent.getString().trim();

				if (content.length() > length) {
					content = content.substring(0, length - concat.length());
					content = content.concat(concat);
				}

				pageContext.getOut().write(content);
			}

			return EVAL_PAGE;
		} catch (IOException ex) {
			throw new JspException(ex.getMessage(), ex);
		}
	}

	/**
	 * Sets the maximum length of body content before truncation.
	 *
	 * @param length the maximum length.
	 */
	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * Text to be concatenated to the end of the body content.
	 *
	 * @param concat the text to be concatenated.
	 */
	public void setConcat(String concat) {
		this.concat = concat;
	}
}
