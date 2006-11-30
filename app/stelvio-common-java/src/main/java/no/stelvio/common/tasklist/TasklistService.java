package no.stelvio.common.tasklist;

import java.util.List;

import no.nav.domain.sak.generellsak.Oppgave;

/**
 * Interface defining methods for retrieval of tasks belonging to an employee or an unit.
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
//TODO: This is the interface of an EJB that should be placed outside of the common module
public interface TasklistService {
	
	/**
	 * Retrieves a list of tasks belonging to an employee/case worker.
	 * @param saksbehandlerID the employee's ID.
	 * @return A list of <code>Oppgave</code>'s.
	 */
	List<Oppgave> getCaseworkerTasks(String saksbehandlerID);
	
	
	/**
	 * Retrieves a list of tasks belonging to a NAV unit.
	 * @param enhetsID the NAV units ID.
	 * @return A list of <code>Oppgave</code>'s.
	 */
	List<Oppgave> getUnitTasks(String enhetsID);
}
