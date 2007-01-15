package no.stelvio.domain.tasklist;

/**
 * TODO: Document me
 * 
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 */
public class TaskType {
	private String id;
	private String description;
	
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
}