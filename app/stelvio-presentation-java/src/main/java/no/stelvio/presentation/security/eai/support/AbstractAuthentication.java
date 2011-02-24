package no.stelvio.presentation.security.eai.support;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.stelvio.presentation.security.eai.Authentication;
import no.stelvio.presentation.security.eai.headers.EaiHeaders;
import no.stelvio.presentation.security.eai.headers.config.EaiHeaderConfig;
import no.stelvio.presentation.security.eai.headers.support.EaiHeaderType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * AbstractAuthentication.
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 * @see Authentication
 * @see HeaderData
 * @see EaiHeaderConfig
 * @see EaiHeaderType
 * @see EaiHeaders
 * 
 */
public abstract class AbstractAuthentication implements Authentication {
	/** Logger. */
	protected final Log log = LogFactory.getLog(this.getClass());
	/** Request. */
	protected HttpServletRequest request;
	/** Response. */
	protected HttpServletResponse response;
	private HeaderData headerData;

	// injected via Spring
	private EaiHeaderConfig config;
	private EaiHeaderType eaiHeaderType;
	private String accessManagerUser;

	/**
	 * Put EAI header on response.
	 */
	public void putEaiHeadersOnResponse() {
		EaiHeaders eai = buildEaiHeaders();

		if (eai != null) {
			Map<String, String> map = eai.getHeaders();

			if (log.isDebugEnabled()) {
				log.debug("Attempting to send eai specific headers. Map size is: " + map.size());
			}

			if (!map.isEmpty()) {

				Iterator iter = map.entrySet().iterator();
				while (iter.hasNext()) {
					Entry entry = (Entry) iter.next();

					if (log.isDebugEnabled()) {
						log.debug("Adding header to response: " + (String) entry.getKey() + " - " + (String) entry.getValue());
					}

					response.setHeader((String) entry.getKey(), (String) entry.getValue());
				}
			}
		} else {
			if (log.isDebugEnabled()) {
				log.debug("Could not send headers as the EaiHeaders are null.");
			}
		}
	}

	/**
	 * Build EAI headerss.
	 * 
	 * @return headers
	 */
	private EaiHeaders buildEaiHeaders() {
		if (this.headerData == null) {
			return null;
		} else if (this.getEaiHeaderType() == EaiHeaderType.PAC_HEADERS) {
			return this.headerData.buildPacHeaders();
		} else if (this.getEaiHeaderType() == EaiHeaderType.USER_IDENTITY_HEADERS) {
			return this.headerData.buildUserIdentityHeaders();
		}

		return null;
	}

	/**
	 * authenticate.
	 */
	public abstract void authenticate();

	/**
	 * Get accessManagerUser.
	 * 
	 * @return the accessManagerUser
	 */
	public String getAccessManagerUser() {
		return accessManagerUser;
	}

	/**
	 * Set accessManagerUser.
	 * 
	 * @param accessManagerUser
	 *            the accessManagerUser to set
	 */
	public void setAccessManagerUser(String accessManagerUser) {
		this.accessManagerUser = accessManagerUser;
	}

	/**
	 * Get config.
	 * 
	 * @return the config
	 */
	public EaiHeaderConfig getConfig() {
		return config;
	}

	/**
	 * Set config.
	 * 
	 * @param config
	 *            the config to set
	 */
	public void setConfig(EaiHeaderConfig config) {
		this.config = config;
	}

	/**
	 * Set EAI header type.
	 * 
	 * @return the type
	 */
	public EaiHeaderType getEaiHeaderType() {
		return eaiHeaderType;
	}

	/**
	 * Set Set EAI header type.
	 * 
	 * @param eaiHeaderType
	 *            the type to set
	 */
	public void setEaiHeaderType(EaiHeaderType eaiHeaderType) {
		this.eaiHeaderType = eaiHeaderType;
	}

	/**
	 * Set request.
	 * 
	 * @param request
	 *            the request to set
	 */
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * Set response.
	 * 
	 * @param response
	 *            the response to set
	 */
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	/**
	 * Get request.
	 * 
	 * @return request
	 */
	public HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * Get response.
	 * 
	 * @return response
	 */
	public HttpServletResponse getResponse() {
		return response;
	}

	/**
	 * Set header data.
	 * 
	 * @param data header data
	 */
	protected void setHeaderData(HeaderData data) {
		this.headerData = data;
	}

	/**
	 * Get geader data.
	 * 
	 * @return header data
	 */
	protected HeaderData getHeaderData() {
		return this.headerData;
	}
}
