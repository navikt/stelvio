package no.stelvio.web.taglib.tasklist.util;

import java.util.List;

import no.stelvio.web.taglib.tasklist.Responsible;

/**
 * TODO: Document me
 * 
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 */
public interface TasklistTreeUtil {
	/**
	 * TODO: Document me
	 * 
	 * @param responsibleId
	 * @return
	 */
	public List<Responsible> getTaskTreeModel(String responsibleId);
}