package no.stelvio.presentation.jsf.taglib.help;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlGraphicImage;
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
 */public class HelpIconComponent extends HtmlGraphicImage {

	protected final Log log = LogFactory.getLog(this.getClass());
	public static final String COMPONENT_TYPE = "no.stelvio.HelpIconComponent";

	private static final String DEFAULT_IMAGE_URL = "hjelp.gif";
	private static final String JAVASCRIPT_ONCLICK = "javascript:document.getElementById(''{0}'').innerHTML = ''{1}''";

	// attribute for this component
	private String key;
	private String value;
	
	/**
	 * Render this component, before rendering, the help icon and help text
	 * is fetched. The help text is inserted in the javascript which is executed
	 * when the help icon is clicked.
	 * 
	 * @param context the current FacesContext instance
	 */
	@Override
	public void encodeBegin(FacesContext context) throws IOException {
		
		addDefaultHelpIconUrl(context);
		String helpText = null;
		if(key != null) {
			helpText = getHelpContent();
		} else if(value != null){
			helpText = getHelpTextValue(context);
		}
		
		String displayAreaId = findHelpDisplayAreaClientId();
		
		if(displayAreaId != null) {
			this.setOnclick(MessageFormat.format(JAVASCRIPT_ONCLICK, 
												displayAreaId,
												helpText));
		} else {
			if(log.isDebugEnabled()) {
				log.debug("The id of the display area was not found, make sure there is a display area in the page.");
			}
		}
		super.encodeBegin(context);
	}
	
	/**
	 * Finds the help text from the value field of this component. The
	 * value field can be a value-binding expression
	 * 
	 * @param context The current Faces context instance
	 * @return the helpText
	 */
	private String getHelpTextValue(FacesContext context) {
		String helpText = null;
		if(UIComponentTag.isValueReference(value)) {
			ValueBinding vb = getValueBinding(value);
			helpText = vb != null ? (String)vb.getValue(context) : null;
		} else {
			helpText = value;
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
	 * Retrieve the help content which is going to be displayed for this
	 * component.
	 * 
	 * TODO: integration towards Vertical Site
	 * 
	 * @return Content object containing the content to display
	 */
	private String getHelpContent() {
		Content content = new Content();
		
		
		
		return content.getText();
	}

	
	/**
	 * Add default help icon url to the page. If an url is specified in the tag, that url is used, if
	 * not the image url is set to the default image residing in the resource directory of this class. 
	 * 
	 * @param context the current Faces context intance
	 * 
	 */
	private void addDefaultHelpIconUrl(FacesContext context) {
		if(!StringUtils.isEmpty(this.getUrl())) {
			return;
		}
		StelvioResourceHandler resourceHandler = new StelvioResourceHandler(HelpIconComponent.class, DEFAULT_IMAGE_URL );
		AddResource addResource = AddResourceFactory.getInstance(context);
		String uri = addResource.getResourceUri(context, resourceHandler, false);
		this.setUrl(uri);
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
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	
	
}
