package no.stelvio.presentation.security.eai.support;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.HttpRequestHandler;

import no.stelvio.presentation.security.session.SecuritySessionAttribute;

/**
 * StepUpDestinationUrlServlet.
 * 
 */
public class StepUpDestinationUrlServlet implements HttpRequestHandler, InitializingBean {

	private static final Log LOGGER = LogFactory.getLog(StepUpDestinationUrlServlet.class);
	private static final String DESTINATION_PARAM_NAME = "destination";
	private String destinationParamName = DESTINATION_PARAM_NAME;
	private String logonPage;

	@Override
	public void afterPropertiesSet() {
		if (destinationParamName == null || logonPage == null) {
			throw new IllegalArgumentException("You must specify the beanproperties: destinationParamName, logonPage.");
		}
	}

	@Override
	public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Handling logout request. Checking for parameters..");
		}

		String param = request.getParameter(destinationParamName);
		request.getSession().setAttribute(SecuritySessionAttribute.EAI_URL_UPON_SUCCESSFUL_STEPUP.getName(), param);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Extracting parameter 'destination':" + param);
			LOGGER.debug("Redirecting to the logonpage:" + logonPage);
		}

		response.sendRedirect(logonPage);
	}

	/**
	 * Set destination parameter name.
	 * 
	 * @param destinationParamName destination parameter name
	 */
	public void setDestinationParamName(String destinationParamName) {
		this.destinationParamName = destinationParamName;
	}

	/**
	 * Set logon page.
	 * 
	 * @param logonPage logon page
	 */
	public void setLogonPage(String logonPage) {
		this.logonPage = logonPage;
	}
}
