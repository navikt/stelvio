package no.stelvio.web.taglib.tasklist;

import java.util.List;

/**
 * TODO: Document me
 * 
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 */
public class Area {
	private String id;
	private String description;
	private List<TaskType> taskTypes;
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * @return the taskTypes
	 */
	public List<TaskType> getTaskTypes() {
		return taskTypes;
	}
	
	/**
	 * @param taskTypes the taskTypes to set
	 */
	public void setTaskTypes(List<TaskType> taskTypes) {
		this.taskTypes = taskTypes;
	}
}