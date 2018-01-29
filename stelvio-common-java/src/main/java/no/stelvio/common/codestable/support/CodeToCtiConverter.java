package no.stelvio.common.codestable.support;

import no.stelvio.common.codestable.CodesTableManager;


/**
 * Simple utility class for getting the Cti from the corresponding Code.
 * 
 * @author personf4fa79ebc3ad (Capgemini) - HORISONTEN
 * @version $Id$
 */
public class CodeToCtiConverter {

	/** The CodesTableManager. */
	private CodesTableManager codesTableManager;

	/**
	 * Constructor.
	 * 
	 * @param codesTableManager
	 *            The CodesTableManager to get the Cti from.
	 */
	public CodeToCtiConverter(CodesTableManager codesTableManager) {
		this.codesTableManager = codesTableManager;
	}

	/**
	 * Finds the Cti from the corresponding code.
	 * 
	 * @param <T>
	 *            A subclass of AbstractCodesTablePeriodicItem - Defines which type of Cti to return.
	 * @param <K>
	 *            The type of the code (something that extends Enum)
	 * @param code
	 *            The Code we want to find the corresponding Cti for.
	 * @return the found Cti.
	 */
	@SuppressWarnings("unchecked")
	public <T extends AbstractCodesTablePeriodicItem<K, String>, K extends Enum> T getCti(CtiConvertable<T, K> code) {
		if (code != null) {
			return codesTableManager.getCodesTablePeriodic(code.getCti()).getCodesTableItem((K) code);
		} else {
			return null;
		}
	}

}