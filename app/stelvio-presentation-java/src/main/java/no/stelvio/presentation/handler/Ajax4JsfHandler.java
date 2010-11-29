package no.stelvio.presentation.handler;

import java.io.IOException;

import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ajax4jsf.framework.ajax.AjaxContext;
import org.springframework.faces.webflow.FlowLifecycle;
import org.springframework.js.ajax.AjaxHandler;
import org.springframework.js.ajax.SpringJavascriptAjaxHandler;
import org.springframework.util.Assert;
import org.springframework.web.context.support.WebApplicationObjectSupport;

/**
 * Ajax handler that works with Ajax4JSF, allowing support for Web Flow Ajax features with the Ajax4JSF toolkit. Should be
 * changed to RichFacesAjaxHandler when/if upgraded to RichFaces. RichFacesAjaxHandler requires a newer version of Ajax4JSF
 * bundled in RichFaces.
 * 
 * @author persone38597605f58 (Capgemini)
 */
public class Ajax4JsfHandler extends WebApplicationObjectSupport implements AjaxHandler {

	private AjaxHandler delegate = new SpringJavascriptAjaxHandler();

	/**
	 * {@inheritDoc}
	 */
	public boolean isAjaxRequest(HttpServletRequest request, HttpServletResponse response) {
		if (isAjax4JsfRequest(request, response)) {
			return true;
		} else {
			return delegate.isAjaxRequest(request, response);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void sendAjaxRedirect(String targetUrl, HttpServletRequest request, HttpServletResponse response, boolean popup)
			throws IOException {
		if (isAjax4JsfRequest(request, response)) {
			response.sendRedirect(response.encodeRedirectURL(targetUrl));
		} else {
			delegate.sendAjaxRedirect(targetUrl, request, response, popup);
		}
	}

	/**
	 * Returns true if it is a Ajax4JSF request.
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return true if it is a Ajax4JSF request
	 */
	protected boolean isAjax4JsfRequest(HttpServletRequest request, HttpServletResponse response) {
		FacesContextHelper helper = new FacesContextHelper();
		try {
			FacesContext facesContext = helper.getFacesContext(getServletContext(), request, response);
			AjaxContext context = AjaxContext.getCurrentInstance(facesContext);
			if (context != null) {
				return context.isAjaxRequest(facesContext);
			} else {
				return false;
			}
		} finally {
			helper.cleanup();
		}
	}

	/**
	 * helper class that gets the FacesContext.
	 * 
	 * @author persone38597605f58 (Capgemini)
	 */
	private static class FacesContextHelper {

		private boolean created = false;

		/**
		 * Get faces context.
		 * 
		 * @param context
		 *            context
		 * @param request
		 *            request
		 * @param response
		 *            response
		 * @return context
		 */
		protected FacesContext getFacesContext(ServletContext context, HttpServletRequest request, 
				HttpServletResponse response) {
			if (FacesContext.getCurrentInstance() != null) {
				return FacesContext.getCurrentInstance();
			} else {
				FacesContextFactory facesContextFactory = (FacesContextFactory) FactoryFinder
						.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
				FacesContext defaultFacesContext = facesContextFactory.getFacesContext(context, request, response,
						FlowLifecycle.newInstance());
				Assert.notNull(defaultFacesContext, "Creation of the default FacesContext failed.");
				created = true;
				return defaultFacesContext;
			}
		}

		/**
		 * Clean up.
		 */
		protected void cleanup() {
			if (created) {
				FacesContext.getCurrentInstance().release();
			}
		}
	}
}
