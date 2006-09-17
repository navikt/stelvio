package no.stelvio.integration.framework.stubs;

import java.util.Map;

/**
 * Repository for TSS Stub.
 * 
 * @author person356941106810, Accenture
 * @version $Id: TSSStubDataRepository.java 2011 2005-03-02 11:31:45Z psa2920 $
 */
public class TSSStubDataRepository {

	private Map samhandlere = null;

	/**
	 * Constructs the repository.
	 * 
	 * @param samhandlere map of samhandler objects.
	 */
	public TSSStubDataRepository(Map samhandlere) {
		this.samhandlere = samhandlere;
	}

	/**
	 * Gets the name of a samhandler.
	 * 
	 * @param type search type.
	 * @param kontoNrNorsk account no. for Norway.
	 * @param kontoNrUtland account no. for other countries.
	 * @return the samhandler name.
	 */
	public final String getSamhandler(final String type, final String kontoNrNorsk, final String kontoNrUtland) {
		return (String) samhandlere.get(type + ":" + kontoNrNorsk + ":" + kontoNrUtland);
	}

	/**
	 * Gets the name of a samhandler.
	 * 
	 * @param eksternSamhandlerId external id of the samhandler
	 * @return the samhandler name.
	 */
	public final String getSamhandler(final String eksternSamhandlerId) {
		return (String) samhandlere.get(eksternSamhandlerId);
	}

}
