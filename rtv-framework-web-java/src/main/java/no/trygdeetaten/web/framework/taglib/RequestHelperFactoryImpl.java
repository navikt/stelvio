package no.trygdeetaten.web.framework.taglib;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.displaytag.util.RequestHelper;
import org.displaytag.util.RequestHelperFactory;


/**
 * Custom <code>RequestHelperFactory</code> implementation that returns instances of 
 * {@link RequestHelperImpl}
 * 
 * @author Christian Rømming, Accenture
 */
public class RequestHelperFactoryImpl implements RequestHelperFactory {

	/**
	 * @see org.displaytag.util.RequestHelperFactory#getRequestHelperInstance(javax.servlet.jsp.PageContext)
	 */
	public RequestHelper getRequestHelperInstance(PageContext pageContext) {
		
		return new RequestHelperImpl(
			(HttpServletRequest) pageContext.getRequest(),
			(HttpServletResponse) pageContext.getResponse());
			
	}
}