package no.stelvio.batch.repository;

import no.stelvio.batch.domain.FileIdentity;

/**
 * Interace for repository component used to perform Read, Create and Update a {@link FileIdentity}.
 * 
 * @author person983601e0e117 (Accenture)
 * 
 */
public interface FileIdentityRepository {

	/**
	 * Retrieves a FileIdentity with the given fileIdentityId.
	 * 
	 * @param fileIdentityId
	 *            the unique id of a <code>FileIdentity</code> object
	 * @return fileIdentity with the identifier specified or null if no <code>FileIdentity</code> with corresponding id found
	 */
	FileIdentity findById(Long fileIdentityId);

	/**
	 * Persists a <code>FileIdentity</code> in the underlying persistence store.
	 * 
	 * @param fileIdentity
	 *            an instance of a <code>FileIdentity</code>
	 * @return the id that uniquely identifies the persisted fileIdentity
	 */
	Long save(FileIdentity fileIdentity);

}
