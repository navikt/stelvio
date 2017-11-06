package no.stelvio.batch.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Object used to identify a file that has been used by a batch.
 * 
 * @author person983601e0e117 (Accenture)
 * 
 */
@Entity
public class FileIdentity implements Serializable {

	private static final long serialVersionUID = 8945679075174856089L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long fileIdentityId;

	@Column(name = "sti")
	private String path;

	/**
	 * No arg constructor, only to be used by ORM, not by client/developer.
	 * 
	 */
	protected FileIdentity() {
	}

	/**
	 * Constructs a new FileIdentity object with a file name.
	 * 
	 * @param path
	 *            the fully qualified file name
	 */
	public FileIdentity(String path) {
		this.path = path;
	}

	/**
	 * Gets the path.
	 * 
	 * @return path fully qualified file name
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Sets the path.
	 * 
	 * @param path
	 *            fully qualified file name
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Gets the unique identifier for this file.
	 * 
	 * @return fileIdentityId identifying the file.
	 */
	public long getFileIdentityId() {
		return fileIdentityId;
	}

}
