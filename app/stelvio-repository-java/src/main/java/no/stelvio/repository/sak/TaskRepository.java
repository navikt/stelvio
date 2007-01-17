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
 */
public interface TaskRepository {
	/**
	 * Calls integration service "HentOppgaveliste" in PSAK
	 * 
	 * See Documents PP4C2005 (describes service) & PP4C2010 (describes ASBO)
	 * 
	 * @param responsibleId
	 * @return task list
	 */
	public List<Task> getTaskList(String responsibleId);
}