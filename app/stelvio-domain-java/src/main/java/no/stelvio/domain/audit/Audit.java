package no.stelvio.domain.audit;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Class used to represent Audit information. This object is embeddedable, meaning it can be used inside a JPA Entity.
 * 
 * This object is used by embedding it in the object that contains the maps to a 
 * table that contains audit columns. Objects that embed this object should have a parameterized
 * constructor that takes a new <code>Audit</code>. After an <code>Audit</code> has been created
 * for an object it should never be replaced by a new one and only be modified by the {@link #updatedBy(String)} method. 
 * 
 * <p><em>
 * It's the responsibility of the application developer to ensure that {@link #updatedBy(String)} is being called when changes are made.
 * </em></p>
 * 
 * This class holds information about:
 * <ul>
 * <li>Who created the object embedding this <code>Audit</code> object</li>
 * <li>When was the object embedding this <code>Audit</code> object created</li>
 * <li>Who made changes to the object embedding this <code>Audit</code> object</li>
 * <li>When was the changes made to the object embedding this <code>Audit</code> object</li>
 * </ul>
 * 
 *  
 * @author person983601e0e117 (Accenture)
 *
 */
@Embeddable
public class Audit implements Serializable {


	private static final long serialVersionUID = 61541164562562288L;

	@Column(name="created_by")
	private String createdBy;
	
	
	@Column(name="created_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	@Column(name="updated_by")
	private String updatedBy;
	
	
	@Column(name="updated_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;	
	
	/**
	 * Constructs a new Audit. 
	 * The constructor should only be called once, 
	 * when the object embedding this <code>Audit</code> object 
	 * is actually created for the first time.
	 * 
	 * @param userId the user id that creates object embedding this <code>Audit</code> object
	 */
	public Audit(String userId){
		this.createdBy = userId;
		this.updatedBy = userId;
		
		//Updated and Created times are equal at first
		Date createdDate = new Date();		
		this.createdAt = createdDate;
		this.updatedAt = createdDate;
	}
	
	/**
	 * No-arg constructor should only be used by persistence provider.
	 * The application should use the parameterized constructor.
	 *
	 */
	protected Audit(){}
	
	/**
	 * Method called whenever the object embedding this <code>Audit</code> object has been updated
	 * @param userId user id that made the update
	 */
	public void updatedBy(String userId){
		updatedBy = userId;
		updatedAt = new Date();
	}
	

	/**
	 * Gets the change by
	 * @return username that made the change
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * Gets created at date
	 * @return sql timestamp for the creation of the object embedding <code>this</code>
	 */
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * Gets changed at date
	 * @return sql timestamp for the most recent change to the object embedding <code>this</code>
	 */	
	public Date getUpdatedAt() {
		return updatedAt;
	}

	/**
	 * Gets the user id that made the most recent change
	 * @return user id of user that made the most recent change to the object embedding <code>this</code>
	 */
	public String getUpdatedBy() {
		return updatedBy;
	}
	
}
