package no.stelvio.repository.sak;

import java.util.List;

import no.stelvio.domain.sak.Task;

/**
 * 
 * Calls integration service "HentOppgaveliste" in PSAK
 * 
 * See Documents PP4C2005 (describes service) & PP4C2010 (describes ASBO)
 * 
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 * @deprecated use <code>no.nav.consumer.pensjon.psak.saksbehandling.SakService<code>
 */
public interface TaskRepository {
	/**
	 * Calls integration service "HentOppgaveliste" in PSAK
	 * 
	 * See Documents PP4C2005 (describes service) & PP4C2010 (describes ASBO)
	 * 
	 * @param responsibleId id for the responsible that the tasklist will be retrieved for
	 * @return task list
	 */
	public List<Task> getTaskList(String responsibleId);
}