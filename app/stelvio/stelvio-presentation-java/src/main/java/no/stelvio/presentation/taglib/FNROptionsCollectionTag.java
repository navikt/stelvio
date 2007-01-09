package no.stelvio.web.taglib;

/**
 * Tag for displaying FNR in a optionsCollection. This will display on the follwoing format:
 * ddmmyy xxxxx
 *
 * @author Jonas Lindholm, Accenture
 * @version $Revision: 2049 $ $Author: psa2920 $ $Date: 2005-03-03 15:28:52 +0100 (Thu, 03 Mar 2005) $
 * @todo we might need this in new framework, but then as a JSF-tag.
 */
public class FNROptionsCollectionTag {

	/**
	 * The style associated with this tag.
	 */
	private String style = null;

	/** 
	 * {@inheritDoc}
	 * @see org.apache.struts.taglib.html.OptionsCollectionTag#getStyle()
	 */
	public String getStyle() {
		return style;
	}

	/** 
	 * {@inheritDoc}
	 * @see org.apache.struts.taglib.html.OptionsCollectionTag#setStyle(java.lang.String)
	 */
	public void setStyle(String style) {
		this.style = style;
	}

	/**
	 * The named style class associated with this tag.
	 */
	private String styleClass = null;

	/** 
	 * {@inheritDoc}
	 * @see org.apache.struts.taglib.html.OptionsCollectionTag#getStyleClass()
	 */
	public String getStyleClass() {
		return styleClass;
	}

	/** 
	 * {@inheritDoc}
	 * @see org.apache.struts.taglib.html.OptionsCollectionTag#setStyleClass(java.lang.String)
	 */
	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	/** 
	 * {@inheritDoc}
	 * @see org.apache.struts.taglib.html.OptionsCollectionTag#addOption(
	 * 	java.lang.StringBuffer, java.lang.String, java.lang.String, boolean)
	 */
	protected void addOption(StringBuffer sb, String label, String value, boolean matched) {

		sb.append("<option value=\"");
		sb.append(value);
		sb.append("\"");
		if (matched) {
			sb.append(" selected=\"selected\"");
		}
		if (style != null) {
			sb.append(" style=\"");
			sb.append(style);
			sb.append("\"");
		}
		if (styleClass != null) {
			sb.append(" class=\"");
			sb.append(styleClass);
			sb.append("\"");
		}
		sb.append(">");

		// Split the fnr.
		if (label != null) {
			label = label.trim();
			if (label.length() == 11) {
				label = label.substring(0, 6) + " " + label.substring(6);
			}
		}

        // TODO what about filtering?
//		if (filter) {
//			sb.append(ResponseUtils.filter(label));
//		} else {
			sb.append(label);
//		}
		sb.append("</option>\r\n");
	}
}