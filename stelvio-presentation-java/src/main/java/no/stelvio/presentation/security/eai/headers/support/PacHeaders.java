package no.stelvio.presentation.security.eai.headers.support;

import no.stelvio.presentation.security.eai.headers.config.EaiHeaderConfig;
import no.stelvio.presentation.security.eai.headers.config.support.StanzaEntryEnum;

/**
 * PacHeaders.
 * 
 * @version $Id$
 * @see AbstractEaiHeaders
 * @see EaiHeaderConfig
 * @see StanzaEntryEnum
 * 
 */
public class PacHeaders extends AbstractEaiHeaders {

	/**
	 * Creates a new instance of PacHeaders.
	 * 
	 * @param config
	 *            configuration
	 */
	public PacHeaders(EaiHeaderConfig config) {
		super(config);
	}

	/**
	 * Set pac.
	 * 
	 * @param pac
	 *            pac
	 */
	public void setPac(String pac) {
		super.addHeader(StanzaEntryEnum.EAI_PAC_HEADER, pac);
	}

	/**
	 * Set pac service id.
	 * 
	 * @param pacServiceId
	 *            pac service id
	 */
	public void setPacServiceId(String pacServiceId) {
		super.addHeader(StanzaEntryEnum.EAI_PAC_SVC_HEADER, pacServiceId);
	}
}
