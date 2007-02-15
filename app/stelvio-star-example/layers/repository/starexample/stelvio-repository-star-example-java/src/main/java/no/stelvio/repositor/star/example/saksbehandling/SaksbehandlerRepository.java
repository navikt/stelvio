package no.stelvio.repositor.star.example.saksbehandling;

import no.stelvio.domain.star.example.saksbehandling.Saksbehandler;

/**
 * Actions for working with saksbehandler against a repository.
 *
 * @author personf8e9850ed756, Accenture
 */
public interface SaksbehandlerRepository {
	/**
	 * Finds saksbehandler by the given id.
	 *
	 * @param saksbehandlerId the id for the saksbehandler to find.
	 * @return the saksbehandler found.
	 */
	Saksbehandler findSaksbehandlerById(long saksbehandlerId);
}
