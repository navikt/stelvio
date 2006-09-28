package no.stelvio.web.action;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import no.stelvio.web.action.FrameworkActionServlet;

/**
 * Description
 *
 * @author Jonas Lindholm
 * @version $Revision: 1136 $ $Author: tsb2920 $ $Date: 2004-08-24 08:56:27 +0200 (Tue, 24 Aug 2004) $
 */
public class ActionServletMock extends FrameworkActionServlet {
	
	private ServletContext ctx = new ServletContextMock();
	
	public void init() throws ServletException {

		log.info("Start initializing servlet");
		super.init();
	}
	
	public ServletContext getServletContext() {
		log.info("getServletContext -");
		return ctx;
	}


}
