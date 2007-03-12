package no.stelvio.service.star.example.saksbehandling.to;

import no.stelvio.common.transferobject.ServiceResponse;
import no.stelvio.domain.star.example.saksbehandling.Saksbehandler;

/**
 * @author personf8e9850ed756, Accenture
 * @todo write javadoc
 */
public class SaksbehandlerResponse extends ServiceResponse {
	private static final long serialVersionUID = 332868411249701608L;
	private Saksbehandler saksbehandler;

	public SaksbehandlerResponse(Saksbehandler saksbehandler) {
		this.saksbehandler = saksbehandler;
	}

	public Saksbehandler getSaksbehandler() {
		return saksbehandler;
	}
}
