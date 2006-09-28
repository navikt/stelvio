package no.stelvio.web.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import servletunit.ServletContextSimulator;
import no.stelvio.web.constants.Constants;

/**
 * Description
 *
 * @author Jonas Lindholm
 * @version $Revision: 995 $ $Author: tsb2920 $ $Date: 2004-08-10 10:51:22 +0200 (Tue, 10 Aug 2004) $
 */
public class ServletContextMock extends ServletContextSimulator {

	// Logging
	private Log log = LogFactory.getLog(this.getClass());

	public String getAttribute() {

		log.info("getAttribute - Start");

		return Constants.BUSINESS_CONFIG_ATTRIBUTE; 
	}
}
