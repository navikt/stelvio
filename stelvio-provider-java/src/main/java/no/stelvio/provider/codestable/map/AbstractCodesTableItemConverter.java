package no.stelvio.provider.codestable.map;

import no.stelvio.common.codestable.CodesTableManager;
import org.dozer.CustomConverter;

/**
 * Base class for code table item converters. Common properties for concrete converter implementations like
 * <code>CodesTableItemConverter</code> and <code>CodesTablePeriodicItem</code> resides here.
 * 
 * @author person19fa65691a36
 * @version $Id$
 */
public abstract class AbstractCodesTableItemConverter implements CustomConverter {

	private CodesTableManager codesTableManager;

	/**
	 * A CodesTableManager should be dependency injected by Spring.
	 * 
	 * @param codesTableManager
	 *            the codesTableManager to set
	 */
	public void setCodesTableManager(CodesTableManager codesTableManager) {
		this.codesTableManager = codesTableManager;
	}

	/**
	 * Get the codesTableManager.
	 *
	 * @return the codesTableManager
	 */
	public CodesTableManager getCodesTableManager() {
		return codesTableManager;
	}

}
