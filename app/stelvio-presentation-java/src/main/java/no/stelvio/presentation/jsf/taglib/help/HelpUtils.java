/**
 * 
 */
package no.stelvio.presentation.jsf.taglib.help;

import javax.faces.component.html.HtmlGraphicImage;
import javax.faces.context.FacesContext;

import no.stelvio.domain.cm.Content;
import no.stelvio.presentation.jsf.renderkit.StelvioResourceHandler;

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.renderkit.html.util.AddResourceFactory;

/**
 * Package local  utility class for the Help components.
 * 
 * @author utvikler
 *
 */
public final class HelpUtils {

	
	private static final String DEFAULT_IMAGE_URL = "hjelp.gif";

	/**
	 * This class should not be instanciated, hence this
	 * private constructor
	 *
	 */
	private HelpUtils() {
		// do nothing
	}
	
	/**
	 * Add default help icon url to the page. If an url is specified in the tag, that url is used, if
	 * not the image url is set to the default image residing in the resource directory of this class. 
	 * 
	 * @param context the current Faces context intance
	 * @param image the HtmlGraphicImage object to add the image url to
	 * @param imageUrl the url of the image to add
	 * 
	 * @return the HtmlGraphicImage object
	 */
	 static HtmlGraphicImage addHelpIconUrl(FacesContext context, 
											HtmlGraphicImage image,
											String imageUrl) {
		if(!StringUtils.isEmpty(imageUrl)) {
			image.setUrl(imageUrl);
			return image;
		}
		StelvioResourceHandler resourceHandler = new StelvioResourceHandler(HelpPopUpComponent.class, DEFAULT_IMAGE_URL );
		AddResource addResource = AddResourceFactory.getInstance(context);
		String uri = addResource.getResourceUri(context, resourceHandler, false);
		image.setUrl(uri);
		image.setStyle("border-style:none");
		
		return image;
	}

		/**
		 * Retrieve the help content which is going to be displayed for this
		 * component.
		 * 
		 * TODO: integration towards Vertical Site
		 * 
		 * @param key The key to fetch content for
		 * @return Content object containing the content to display
		 */
		static Content getHelpContent(String key) {
			
			Content content = null;
			
			return content;
		}

}
