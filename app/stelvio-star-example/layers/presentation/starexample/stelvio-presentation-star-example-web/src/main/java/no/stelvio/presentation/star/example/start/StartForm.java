package no.stelvio.presentation.star.example.start;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Holds the parameters put in by the user on the web form.
 */
public class StartForm {
	private static final Log log = LogFactory.getLog(StartForm.class);
	
	long saksbehandlernr;

	public long getSaksbehandlernr() {
		log.debug("returns saksbehandlernr = " + saksbehandlernr);
		return saksbehandlernr;
	}

	public void setSaksbehandlernr(long saksbehandlernr) {
		log.debug("sets saksbehandlernr = " + saksbehandlernr);
		this.saksbehandlernr = saksbehandlernr;
	}
}
