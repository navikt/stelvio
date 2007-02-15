package no.stelvio.domain.sak;

import java.util.List;

/**
 * Represent a responsible
 * 
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 * @deprecated use <code>no.nav.domain.pensjon.generellsak.Ansvarlig<code>
 * 
 */
public class Responsible {
	private String id;
	private String description;
	private List<Area> areas;
	
	/**
	 * Gets areas
	 * @return the areas
	 */
	public List<Area> getAreas() {
		return areas;
	}
	
	/**
	 * Sets areas
	 * @param areas the areas to set
	 */
	public void setAreas(List<Area> areas) {
		this.areas = areas;
	}
	
	/**
	 * Gets desription
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