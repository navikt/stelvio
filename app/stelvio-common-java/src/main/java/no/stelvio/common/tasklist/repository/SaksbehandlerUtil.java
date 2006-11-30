package no.stelvio.common.tasklist.repository;

import java.util.List;

import no.stelvio.common.tasklist.domain.Responsible;

/**
 * TODO: Document me
 * 
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 */
public interface SaksbehandlerUtil {
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