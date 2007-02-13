package no.stelvio.presentation.jsf.taglib.help;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlGraphicImage;
import javax.faces.component.html.HtmlOutputLink;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

import no.stelvio.domain.cm.Content;
import no.stelvio.presentation.jsf.renderkit.StelvioResourceHandler;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.custom.div.Div;
import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.renderkit.html.util.AddResourceFactory;
import org.apache.myfaces.util.ViewIterator;


/**
 * The helpIconComponent renders an image (default is a small icon with a question mark), when the 
 * user clicks on the icon, a help text is displayed in the page. This component requires a 
 * HelpDisplayAreaComponent on the same page as the help icon is used.
 * 
 * There are three different ways to supply the help text to be displayed
 * <ul>
 * <li>If the key attribute is given, the key is used to look up the help text in the vertical site content
 * management system. This feature is yet not implemented</li>
 * <li>If a value binding expression is given in the value attribute, this value binding expression is resolved and
 * the help text is the result of the value binding</li>
 * <li>The value attribute may consist of a static string which will be displayed as the help text</li>
 * </ul>
 * 
 * @author person6045563b8dec, Accenture
 * @version $Id$
 *
 */public class HelpInPageComponent extends HtmlGraphicImage {

	protected final Log log = LogFactory.getLog(this.getClass());
	public static final String COMPONENT_TYPE = "no.stelvio.HelpInPageComponent";

	private static final String JS_ONCLICK_INPAGE_HELP = "javascript:document.getElementById(''{0}'').innerHTML = ''{1}'';";

	
	// attribute for this component
	private String key;
	private String text;
	
	/**
	 * Render this component, before rendering, the help icon and help text
	 * is fetched. The help text is inserted in the javascript which is executed
	 * when the help icon is clicked.
	 * 
	 * @param context the current FacesContext instance
	 */
	@Override
	public void encodeBegin(FacesContext context) throws IOException {
		
		HelpUtils.addHelpIconUrl(context, this, getUrl());
		createInPageLink(HelpUtils.getHelpContent(key));

		super.encodeBegin(context);
	}


	
	/**
	 * @param helpText
	 */
	private void createInPageLink(Content helpContent) {
		String helpText = null;
		if(helpContent != null) {
			helpText = helpContent.getText();
		} else if(text != null) {
			helpText = getHelpTextValue();
		}
		
		String displayAreaId = findHelpDisplayAreaClientId();

		if(displayAreaId != null) {
			this.setOnclick(MessageFormat.format(JS_ONCLICK_INPAGE_HELP, 
											displayAreaId,
											helpText));
		} else {
			if(log.isDebugEnabled()) {
				log.debug("The id of the display area was not found, make sure there is a display area in the page.");
			}
		}
	}
	
	/**
	 * Finds the help text from the value field of this component. The
	 * value field can be a value-binding expression
	 * 
	 * @param context The current Faces context instance
	 * @return the helpText
	 */
	private String getHelpTextValue() {
		FacesContext context = FacesContext.getCurrentInstance();
		String helpText = null;
		if(UIComponentTag.isValueReference(text)) {
			ValueBinding vb = getValueBinding(text);
			helpText = vb != null ? (String)vb.getValue(context) : null;
		} else {
			helpText = text;
		}
		
		return helpText;
	}

	/**
	 * Find the client id for where to place the help information. The id is used
	 * in the javascript triggered when the help icon i clicked.
	 * 
	 * @return the client id of the display area of the help information
	 */
	private String findHelpDisplayAreaClientId() {
		String clientId = null;
		UIComponent component = null;

		ViewIterator viewIterator = new ViewIterator(FacesContext.getCurrentInstance().getViewRoot());
		for(; viewIterator.hasNext();) {
			UIComponent nextComponent = (UIComponent) viewIterator.next();
			if(nextComponent instanceof HelpDisplayAreaComponent) {
				component = nextComponent;
				break;
			}
		}
		if(component != null) {
			HelpDisplayAreaComponent displayComponent = (HelpDisplayAreaComponent) component;
			List children = displayComponent.getChildren();
			UIComponent divComponent = null;
			for(Iterator i = children.iterator();i.hasNext();) {
				UIComponent next = (UIComponent) i.next();
				if(next instanceof Div) {
					divComponent = next;
				}
			}
			
			clientId = divComponent.getClientId(FacesContext.getCurrentInstance());
		}
		return clientId;
	}
	



	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the value
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the value to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	
}
