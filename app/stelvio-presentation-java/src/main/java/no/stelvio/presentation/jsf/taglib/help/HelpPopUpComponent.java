package no.stelvio.presentation.jsf.taglib.help;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlGraphicImage;
import javax.faces.component.html.HtmlOutputLink;
import javax.faces.component.html.HtmlOutputText;
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
 */public class HelpPopUpComponent extends HtmlOutputLink {

	protected final Log log = LogFactory.getLog(this.getClass());
	public static final String COMPONENT_TYPE = "no.stelvio.HelpPopUpComponent";

	private static final String JS_POPUP_HELP = "helpPopup.js";
	private static final String JS_ONCLICK_POPUP_HELP = "javascript:openWindow(''{0}'', ''{1}'', {2}, {3});";
	private static final String JAVASCRIPT_ENCODED = "no.stelvio.presentation.jsf.taglib.help.HelpPopUpLinkComponent.JAVASCRIPT_ENCODED";

	
	private static final String DEFAULT_WINDOW_WIDTH = "800";
	private static final String DEFAULT_WINDOW_HEIGHT = "540";

	
	// attribute for this component
	private String key;
	private String helpUrl;
	private String imageUrl;
	private boolean textOnly;
	private String linkText;
	
	/**
	 * Render this component, before rendering, the help icon and help text
	 * is fetched. The help text is inserted in the javascript which is executed
	 * when the help icon is clicked.
	 * 
	 * @param context the current FacesContext instance
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
	 * 
	 */
	private void createPopupLink(Content helpContent) {
		if(helpContent != null) {
			helpUrl = helpContent.getUrl();
		} 
		addPopupScriptToPage();
		
		this.setValue(helpUrl);
		this.setTarget("helpTarget");
		this.setOnclick(MessageFormat.format(JS_ONCLICK_POPUP_HELP, 
											helpUrl, 
											"", 
											DEFAULT_WINDOW_WIDTH, 
											DEFAULT_WINDOW_HEIGHT));
	}

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
