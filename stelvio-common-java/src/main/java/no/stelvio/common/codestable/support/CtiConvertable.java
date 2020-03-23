package no.stelvio.common.codestable.support;

/**
 * Interface for used for getting Cti class for enum classes.
 * 
 * @version $Id$
 * 
 * @param <T> The code's corresponding Cti 
 * @param <K> The Enum to get Cti for
 */
@SuppressWarnings("unchecked")
public interface CtiConvertable<T extends AbstractCodesTablePeriodicItem<K, String>, K extends Enum> {

	/**
	 * Finds the Cti corresponding to the Enum.
	 * 
	 * @return the found Cti
	 */
	Class<T> getCti();

}