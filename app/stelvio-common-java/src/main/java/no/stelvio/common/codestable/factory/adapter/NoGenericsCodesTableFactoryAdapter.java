package no.stelvio.common.codestable.factory.adapter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import no.stelvio.common.codestable.CodesTable;
import no.stelvio.common.codestable.CodesTableItem;
import no.stelvio.common.codestable.CodesTablePeriodicItem;
import no.stelvio.common.codestable.CodesTableNotFoundException;
import no.stelvio.common.codestable.CodesTablePeriodic;
import no.stelvio.common.codestable.factory.CodesTableFactory;
import no.stelvio.common.codestable.factory.NoGenericsCodesTableFactory;

/**
 * Adapter to bridge the Generics/NoGenerics ({@link CodesTableFactory}/{@link NoGenericsCodesTableFactory}) gap
 * 
 * This is an implementation of the Object Adapter pattern. It becomes obsolete once EJB handles generics in method
 * signatures.
 * 
 * @author person983601e0e117 (Accenture)
 * @deprecated Use {@link no.stelvio.common.codestable.factory.adapter.NoGenericsCodesTableItemsFactoryAdapter} instead.
 */
@Deprecated
public class NoGenericsCodesTableFactoryAdapter implements CodesTableFactory {
	private static final Log log = LogFactory.getLog(NoGenericsCodesTableFactoryAdapter.class);
	private NoGenericsCodesTableFactory noGenericsCodesTableFactory;

	/**
	 * {@inheritDoc}
	 */	
	@SuppressWarnings("unchecked")
	public <T extends CodesTableItem<K, V>, K extends Enum, V>
				CodesTable<T, K, V> createCodesTable(Class<T> codesTableItemClass) throws CodesTableNotFoundException {
		log.warn("DEPRECATED - Use NoGenericsCodesTableItemsFactoryAdapter instead - DEPRECATED");

		Object codesTableObject = noGenericsCodesTableFactory.createCodesTable(codesTableItemClass);
		CodesTable<T, K, V> codeTable = (CodesTable<T, K, V>) codesTableObject;
		
		return codeTable;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public <T extends CodesTablePeriodicItem<K, V>, K extends Enum, V>
				CodesTablePeriodic<T, K, V> createCodesTablePeriodic(Class<T> codesTableItemPeriodicClass)
			throws CodesTableNotFoundException {
		log.warn("DEPRECATED - Use NoGenericsCodesTableItemsFactoryAdapter instead - DEPRECATED");
		
		Object codesTableObject = noGenericsCodesTableFactory.createCodesTablePeriodic(codesTableItemPeriodicClass);
		CodesTablePeriodic<T, K, V> codeTable = (CodesTablePeriodic<T, K, V>) codesTableObject;
		
		return codeTable;
	}

	/**
	 * Sets the noGenericsFactory that will be adapted to a fit the {@link CodesTableFactory} interface.
	 *
	 * @param noGenericsCodesTableFactory {@link NoGenericsCodesTableFactory} to adapt to fit the
	 * {@link CodesTableFactory} interface. 
	 */
	public void setNoGenericsCodesTableFactory(NoGenericsCodesTableFactory noGenericsCodesTableFactory) {
		log.warn("DEPRECATED - Use NoGenericsCodesTableItemsFactoryAdapter instead - DEPRECATED");

		this.noGenericsCodesTableFactory = noGenericsCodesTableFactory;
	}	
}
