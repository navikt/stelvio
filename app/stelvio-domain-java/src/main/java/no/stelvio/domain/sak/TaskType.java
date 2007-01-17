package no.stelvio.domain.sak;

/**
 * 
 * Represents type of task
 * 
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 */
public class TaskType {
	private String id;
	private String description;
	
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
	 * Gets the id
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
}