package no.stelvio.service.star.example.saksbehandling.to;

import no.stelvio.common.transferobject.ServiceRequest;

/**
 * @author personf8e9850ed756, Accenture
 * @todo write javadoc
 */
public class SaksbehandlerRequest extends ServiceRequest {
	private static final long serialVersionUID = -7067360164502715798L;
	private long saksbehandlerNr;

	public SaksbehandlerRequest(long saksbehandlerNr) {
		this.saksbehandlerNr = saksbehandlerNr;
	}

	public long getSaksbehandlerNr() {
		return saksbehandlerNr;
	}
}
