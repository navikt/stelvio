package no.stelvio.repository.sak;

import java.util.List;

import no.stelvio.domain.sak.Responsible;

/**
 * 
 * Calls GSAK service (Remove this and copy into implementation when ready)
 * 
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 * @deprecated use <code>no.nav.consumer.pensjon.psak.genrellsak.SaksbehandlerService<code>
 * 
 */
public interface SaksbehandlerRepository {
	
	/**
	 * Retrieves case workers based on unit id
	 * 
	 * @param enhetId to retrieve list of Responsibles for 
	 * @return List of Responsible
	 */
	public List<Responsible> getSaksbehandlere(String enhetId);
	
	/**
	 * Retrieves case worker
	 * 
	 * @param saksbehandlerNr id for case worker
	 * @return Responsible
	 */
	public Responsible getSaksbehandler(String saksbehandlerNr);
}