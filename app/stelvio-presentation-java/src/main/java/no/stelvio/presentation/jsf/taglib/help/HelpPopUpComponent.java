package no.stelvio.presentation.jsf.taglib.help;

import java.io.IOException;
import java.text.MessageFormat;

import javax.faces.component.html.HtmlGraphicImage;
import javax.faces.component.html.HtmlOutputLink;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;

import no.stelvio.domain.cm.Content;
import no.stelvio.presentation.jsf.renderkit.StelvioResourceHandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.renderkit.html.util.AddResourceFactory;


/**
 * The helpPopUpComponent renders an image (default is a small icon with a question mark), when the 
 * user clicks on the icon, a help text is displayed in a separate window. This component can also be
 * used to render the link as a text and not as an icon. 
 * 
 * There are three different ways to supply the help text to be displayed
 * <ul>
 * <li>If the key attribute is given, the key is used to look up the help text in the vertical site content
 * management system. TODO: This feature is yet not implemented</li>
 * <li>If a value binding expression is given in the value attribute, this value binding expression is resolved and
 * the help text is the result of the value binding</li>
 * <li>The value attribute may consist of a static string which will be displayed as the help text</li>
 * </ul>
 * 
 * This component uses the javascript located in no.stelvio.presentation.jsf.taglib.help.resources.helpPopup.js
 * 
 * @author person6045563b8dec, Accenture
 * @version $Id$
 *
 */
public class HelpPopUpComponent extends HtmlOutputLink {

	/**
	 * The component type
	 */
	public static final String COMPONENT_TYPE = "no.stelvio.HelpPopUpComponent";

	protected final Log log = LogFactory.getLog(this.getClass());

	// constant values used in this component
	private static final String HELP_TARGET = "helpTarget";
	private static final String JS_POPUP_HELP = "helpPopup.js";
	private static final String JS_ONCLICK_POPUP_HELP = "javascript:openWindow(''{0}'', ''{1}'', {2}, {3});";
	private static final String JAVASCRIPT_ENCODED = 
							"no.stelvio.presentation.jsf.taglib.help.HelpPopUpLinkComponent.JAVASCRIPT_ENCODED";
	
	private static final String DEFAULT_WINDOW_WIDTH = "800";
	private static final String DEFAULT_WINDOW_HEIGHT = "540";
	
	// attribute for this component
	private String key;
	private String helpUrl;
	private String imageUrl;
	private boolean textOnly;
	private String linkText;
	
	/**
	 * Render this component, before rendering, the help icon and url
	 * are fetched. A popup window is displayed when the icon/text link is 
	 * clicked
	 * 
	 * @param context the current FacesContext instance
	 * @throws IOException if input/output error occurs while rendering
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void encodeBegin(FacesContext context) throws IOException {
		
		if(!textOnly) {
			HtmlGraphicImage image = new HtmlGraphicImage();
			HelpUtils.addHelpIconUrl(context, image, imageUrl);
			this.getChildren().add(image);
		} else {
			HtmlOutputText text = new HtmlOutputText();
			text.setValue(linkText);
			this.getChildren().add(text);
		}
		createPopupLink(HelpUtils.getHelpContent(key));
		super.encodeBegin(context);
	}

	/**
	 * Creates the popup link. The url is either fetched from 
	 * a Content object or from the values given in the tag.
	 * 
	 * @param helpContent The Content object to use for the link
	 */
	private void createPopupLink(Content helpContent) {
		if(helpContent != null) {
			helpUrl = helpContent.getUrl();
		} 
		addPopupScriptToPage();
		
		this.setValue(helpUrl);
		this.setTarget(HELP_TARGET);
		this.setOnclick(MessageFormat.format(JS_ONCLICK_POPUP_HELP, 
											helpUrl, 
											HELP_TARGET, 
											DEFAULT_WINDOW_WIDTH, 
											DEFAULT_WINDOW_HEIGHT));
	}

	
	/**
	 * A resource handler is used to queue the javascript for inclusion in the
	 * page when it is rendered.
	 * When the response is written, the extension-filter will add the javascript
	 * to the header of the page.
	 *
	 */
	@SuppressWarnings("unchecked")
	private void addPopupScriptToPage() {
		FacesContext context = FacesContext.getCurrentInstance();
		
		if(context.getExternalContext().getRequestMap().containsKey(JAVASCRIPT_ENCODED)) {
			return;
		}
		
		StelvioResourceHandler resourceHandler = new StelvioResourceHandler(this.getClass(), JS_POPUP_HELP );
		AddResource addResource = AddResourceFactory.getInstance(context);
		addResource.addJavaScriptAtPosition(context, 
											AddResource.HEADER_BEGIN, 
											resourceHandler, false);

		context.getExternalContext().getRequestMap().put(JAVASCRIPT_ENCODED, Boolean.TRUE);
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
	 * @return the helpUrl
	 */
	public String getHelpUrl() {
		return helpUrl;
	}

	/**
	 * @param helpUrl the helpUrl to set
	 */
	public void setHelpUrl(String helpUrl) {
		this.helpUrl = helpUrl;
	}

	/**
	 * @return the imageUrl
	 */
	public String getImageUrl() {
		return imageUrl;
	}

	/**
	 * @param imageUrl the imageUrl to set
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	/**
	 * @return the linkText
	 */
	public String getLinkText() {
		return linkText;
	}

	/**
	 * @param linkText the linkText to set
	 */
	public void setLinkText(String linkText) {
		this.linkText = linkText;
	}

	/**
	 * @return the textOnly
	 */
	public boolean isTextOnly() {
		return textOnly;
	}

	/**
	 * @param textOnly the textOnly to set
	 */
	public void setTextOnly(boolean textOnly) {
		this.textOnly = textOnly;
	}
	
	
	
}
