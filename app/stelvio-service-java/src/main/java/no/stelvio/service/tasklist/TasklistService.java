package no.stelvio.service.tasklist;

import no.stelvio.common.transferobject.EntityListResponse;
//import no.stelvio.domain.sak.Saksbehandler;
import no.stelvio.domain.sak.Task;

/**
 * Interface defining methods for retrieval of tasks belonging to an employee or
 * an unit.
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @author person15754a4522e7
 * @version $Id$
 * @deprecated use <code>no.nav.service.pensjon.saksbehandling.SakService<code>
 * 
 */
public interface TasklistService {

	/**
	 * Retrieves a list of tasks belonging to an employee/case worker or the
	 * belonging organizational unit.
	 * 
	 * @param saksbehandlerID
	 *            the employee's ID.
	 * @return A list of <code>Oppgave</code>'s.
	 */
	EntityListResponse<Task> finnOppgaveListe(String saksbehandlerID);

	/**
	 * Retrieves a list of caseworkers belonging to an organizational unit.
	 * 
	 * @return List of caseworkers.
	 */
	// EntityListResponse<Saksbehandler> finnSaksbehandlerListe();
}
