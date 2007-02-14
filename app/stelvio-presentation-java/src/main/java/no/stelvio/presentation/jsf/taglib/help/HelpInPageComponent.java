package no.stelvio.presentation.jsf.taglib.help;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlGraphicImage;
import javax.faces.context.FacesContext;

import no.stelvio.domain.cm.Content;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.custom.div.Div;
import org.apache.myfaces.util.ViewIterator;


/**
 * The helpInPageComponent renders an image (default is a small icon with a question mark), when the 
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
 */
public class HelpInPageComponent extends HtmlGraphicImage {

	/**
	 * 
	 */
	private static final String ATTRIBUTE_KEY = "key";

	/**
	 * 
	 */
	private static final String ATTRIBUTE_TEXT = "text";

	protected final Log log = LogFactory.getLog(this.getClass());
	
	/**
	 * the component type
	 */
	public static final String COMPONENT_TYPE = "no.stelvio.HelpInPageComponent";

	// the javascript to add to the onclick of the image
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
	 * @throws IOException if input/output error occurs while rendering
	 */
	@Override
	public void encodeBegin(FacesContext context) throws IOException {
		
		HelpUtils.addHelpIconUrl(context, this, getUrl());
		createInPageLink(HelpUtils.getHelpContent(getKey()));

		super.encodeBegin(context);
	}


	
	/**
	 * Create the onclick event to happen when the user clicks on 
	 * the icon
	 * 
	 * @param helpContent the Content to display, if null, the text
	 * is fetched from local attribute values 
	 */
	private void createInPageLink(Content helpContent) {
		String helpText = "";
		if(helpContent != null) {
			helpText = helpContent.getText();
		} else {
			helpText = getText();
		}
		
		// the id of the display area is required for the getElementById() javascript function
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
	 * Find the client id for where to place the help information. The id is used
	 * in the javascript triggered when the help icon i clicked.
	 * 
	 * @return the client id of the display area of the help information
	 */
	private String findHelpDisplayAreaClientId() {
		String clientId = null;
		UIComponent component = null;

		ViewIterator viewIterator = new ViewIterator(FacesContext.getCurrentInstance().getViewRoot());
		while(viewIterator.hasNext()) {
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
			for(Iterator i = children.iterator(); i.hasNext();) {
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
		if(getValueBinding(ATTRIBUTE_KEY) != null) {
			key = (String) getValueBinding(ATTRIBUTE_KEY).getValue(FacesContext.getCurrentInstance());
		}
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

		if(getValueBinding(ATTRIBUTE_TEXT) != null ) {
			text = (String) getValueBinding(ATTRIBUTE_TEXT).getValue(FacesContext.getCurrentInstance());
		}
		return text;
	}

	/**
	 * @param text the value to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	
}
