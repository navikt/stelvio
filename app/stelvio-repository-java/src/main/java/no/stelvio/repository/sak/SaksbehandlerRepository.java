package no.stelvio.repository.sak;

import java.util.List;

import no.stelvio.domain.tasklist.Responsible;

/**
 * TODO: Document me
 * 
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 */
public interface SaksbehandlerRepository {
	/**
	 * TODO: Document me
	 * 
	 * @param enhetId
	 * @return
	 */
	public List<Responsible> getSaksbehandlere(String enhetId);
	
	/**
	 * TODO: Document me
	 * 
	 * @param saksbehandlerNr
	 * @return
	 */
	public Responsible getSaksbehandler(String saksbehandlerNr);
}