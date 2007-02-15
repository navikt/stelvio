package no.stelvio.service.star.example.saksbehandling;

import no.stelvio.service.star.example.saksbehandling.support.SaksbehandlingException;

/**
 * Exception thrown when saksbehandler is not found.
 */
public class SaksbehandlerNotFoundException extends SaksbehandlingException {
	private static final long serialVersionUID = -4349385568920627868L;

	public SaksbehandlerNotFoundException(long saksbehandlerNr) {
		super(saksbehandlerNr);
	}

	public SaksbehandlerNotFoundException(Throwable cause, long saksbehandlerNr) {
		super(cause, saksbehandlerNr);
	}

	protected String messageTemplate(final int numArgs) {
		return "Did not find the saksbehandler with num: {0}";
	}
}
