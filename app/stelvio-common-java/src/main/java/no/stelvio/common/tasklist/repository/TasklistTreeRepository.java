package no.stelvio.common.tasklist.repository;

import java.util.List;

import no.stelvio.common.tasklist.domain.Responsible;

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