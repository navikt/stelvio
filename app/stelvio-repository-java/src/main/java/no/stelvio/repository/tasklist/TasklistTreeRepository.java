package no.stelvio.repository.tasklist;

import java.util.List;

import no.stelvio.domain.tasklist.Responsible;

/**
 * TODO: Document me
 * 
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 */
public interface TasklistTreeRepository {
	/**
	 * TODO: Document me
	 * 
	 * @param responsibleId
	 * @return
	 */
	public List<Responsible> getTaskTreeModel(String responsibleId);
}