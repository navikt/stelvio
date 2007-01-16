package no.stelvio.repository.sak;

import java.util.List;

import no.stelvio.domain.sak.Responsible;

/**
 * TODO: Document me
 * 
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 */
public interface TaskRepository {
	/**
	 * TODO: Document me
	 * 
	 * @param responsibleId
	 * @return
	 */
	public List<Responsible> getTaskTreeModel(String responsibleId);
}