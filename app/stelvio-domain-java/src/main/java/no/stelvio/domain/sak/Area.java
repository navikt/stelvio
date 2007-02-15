package no.stelvio.domain.sak;

import java.util.List;

/**
 * Represents an Area
 * 
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 * @deprecated use <code>no.nav.domain.pensjon.generellsak.Omrade<code>
 * 
 */
public class Area {
	private String id;
	private String description;
	private List<TaskType> taskTypes;
	
	/**
	 * Gets description
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Sets description
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Gets id
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Sets the id
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Gets task types
	 * @return the taskTypes
	 */
	public List<TaskType> getTaskTypes() {
		return taskTypes;
	}
	
	/**
	 * Sets task types
	 * @param taskTypes the taskTypes to set
	 */
	public void setTaskTypes(List<TaskType> taskTypes) {
		this.taskTypes = taskTypes;
	}
}