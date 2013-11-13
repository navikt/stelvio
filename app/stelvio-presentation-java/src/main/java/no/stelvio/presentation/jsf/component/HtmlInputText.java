package no.stelvio.presentation.jsf.component;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.JSFAttr;

/**
 * Custom component for escaping illegal characters.
 * 
 * @author persone38597605f58 (Capgemini)
 * @version $Id$
 */
public class HtmlInputText extends org.apache.myfaces.component.html.ext.HtmlInputText {

	private static final Boolean DEFAULT_ESCAPE = Boolean.TRUE;

	private Boolean escape = null;

	private static final String[] ESCAPE_STRINGS = new String[] { "&lt;", "&gt;", "&amp;", "+", "%", "<", ">", "&" };

	/**
	 * Creates a new instance of HtmlInputText.
	 */
	public HtmlInputText() {
		super();

		setRendererType(DEFAULT_RENDERER_TYPE);
	}

	/**
	 * Set escape.
	 * 
	 * @param escape
	 *            escape
	 */
	public void setEscape(Boolean escape) {
		this.escape = Boolean.valueOf(escape);
	}

	/**
	 * Get escape.
	 * 
	 * @return escape
	 */
	public Boolean getEscape() {
		if (escape != null) {
			return escape.booleanValue();
		}

		ValueExpression vb = getValueExpression(JSFAttr.ESCAPE_ATTR);
		Boolean v = vb != null ? (Boolean) vb.getValue(getFacesContext().getELContext()) : null;
		return v != null ? v : DEFAULT_ESCAPE;
	}

	/** {@inheritDoc} */
	public Object getSubmittedValue() {
		Object submittedValue = super.getSubmittedValue();
		if (submittedValue != null && submittedValue instanceof CharSequence) {
			submittedValue = filterString((String) submittedValue);
		}
		return submittedValue;
	}

	/** {@inheritDoc} */
	public void setSubmittedValue(Object submittedValue) {
		if (submittedValue != null && submittedValue instanceof CharSequence) {
			super.setSubmittedValue(filterString((String) submittedValue));
		} else {
			super.setSubmittedValue(submittedValue);
		}

	}

	/**
	 * The method removes illegal characters.
	 * 
	 * @param string
	 *            the string to be filtered
	 * @return filtered string
	 */
	private String filterString(String string) {
		String filteredString = string;
		for (int i = 0; i < ESCAPE_STRINGS.length; i++) {
			filteredString = StringUtils.remove(filteredString, ESCAPE_STRINGS[i]);
		}
		return filteredString;
	}

	/**
	 * Saves the state of this component.
	 * 
	 * @param context
	 *            The current FacesContext intstance
	 * @return <ul>
	 *         <li>values[0] = super.saveState(context);</li>
	 *         <li>values[1] = getEscape();</li>
	 *         </ul>
	 */
	@Override
	public Object saveState(FacesContext context) {
		Object[] values = new Object[2];
		values[0] = super.saveState(context);
		values[1] = getEscape();
		return values;
	}

	/**
	 * Restores the state of this component.
	 * 
	 * @see javax.faces.context.FacesContext
	 * @param context
	 *            FacesContext.
	 * @param state
	 *            Object.
	 */
	@Override
	public void restoreState(FacesContext context, Object state) {
		Object[] values = (Object[]) state;

		super.restoreState(context, values[0]);
		setEscape((Boolean) values[1]);
	}
}
