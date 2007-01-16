package no.stelvio.service.tasklist.support;

import no.stelvio.common.transferobject.EntityListResponse;
import no.stelvio.domain.sak.Task;
import no.stelvio.service.tasklist.TasklistService;

/**
 * Class implementing the interface TasklistService, that retrives lists of
 * tasks for an employee/case worker or a Nav unit.
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @author person15754a4522e7
 * @version $Id$
 */
public class DefaultTasklistService implements TasklistService {

	/**
	 * {@inheritDoc TasklistService#finnOppgaveListe(String)}
	 */
	public EntityListResponse<Task> finnOppgaveListe(String saksbehandlerID) {
		// TODO Call repository-method.
		return null;
	}
}