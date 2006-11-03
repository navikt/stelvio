package no.stelvio.web.action;

import javax.servlet.ServletException;

import org.apache.struts.action.ActionServlet;

import no.stelvio.common.config.Config;
import no.stelvio.web.constants.Constants;


/**
 * FrameworkActionServlet initializes configuration of presentation services.
 * 
 * @author   Jonas Lindholm, Accenture
 * @version  $Revision: 1184 $ $Author: psa2920 $ $Date: 2004-08-30 12:51:27 +0200 (Mon, 30 Aug 2004) $
 */
public class FrameworkActionServlet extends ActionServlet {

	/**
	 *	Overriding init of the ActionServlet. 
	 *	Getting <i>BusinessDelegate</i> with Spring and set the BusinessDelegate context 
	 *	for the servlet. 
	 *	
	 *	@throws ServletException an servlet exception
	 */
	public void init() throws ServletException {

		if (log.isInfoEnabled()) {
			log.info("*** Start initializing Struts ***");
		}
		super.init();

		// Initialize Spring Configuration, if not already initialized
		if (null == getServletContext().getAttribute(Config.PRESENTATION_SERVICES)) {

			String filename = getServletConfig().getInitParameter(Constants.PRESENTATION_SERVICES);
			try {
				if (log.isDebugEnabled()) {
					log.debug("Initialize Spring Configuration using " + filename);
				}
				getServletContext().setAttribute(Config.PRESENTATION_SERVICES, Config.getConfig(filename));
			} catch (Throwable t) {
				throw new ServletException(
					"Failed to Initialize Spring Configuration using " + filename
					/*ErrorHandler.handleError(t) TODO: handled differently*/);
			}
		}
		if (log.isInfoEnabled()) {
			log.info("*** End initializing Struts ***");
		}
	}
}
