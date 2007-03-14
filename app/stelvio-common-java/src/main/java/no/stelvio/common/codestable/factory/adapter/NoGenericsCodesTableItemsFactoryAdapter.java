package no.stelvio.common.codestable.factory.adapter;

import java.util.List;

import no.stelvio.common.codestable.CodesTableItem;
import no.stelvio.common.codestable.CodesTableNotFoundException;
import no.stelvio.common.codestable.CodesTablePeriodicItem;
import no.stelvio.common.codestable.factory.CodesTableItemsFactory;
import no.stelvio.common.codestable.factory.NoGenericsCodesTableItemsFactory;

/**
 * Adapter to bridge the Generics/NoGenerics ({@link CodesTableItemsFactory}/{@link NoGenericsCodesTableItemsFactory})
 * gap.
 *
 * This is an implementation of the Object Adapter pattern. It becomes obsolete once EJB handles generics in method
 * signatures.
 *
 * @author person983601e0e117 (Accenture)
 */
public class NoGenericsCodesTableItemsFactoryAdapter implements CodesTableItemsFactory {
	private NoGenericsCodesTableItemsFactory noGenericsCodesTableItemsFactory;

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public <T extends CodesTableItem<K, V>, K extends Enum, V>
		List<T> createCodesTableItems(Class<T> codesTableItemClass) throws CodesTableNotFoundException {

		return noGenericsCodesTableItemsFactory.createCodesTableItems(codesTableItemClass);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public <T extends CodesTablePeriodicItem<K, V>, K extends Enum, V>
		List<T> createCodesTablePeriodicItems(Class<T> codesTableItemPeriodicClass)
			throws CodesTableNotFoundException {

		return noGenericsCodesTableItemsFactory.createCodesTablePeriodicItems(codesTableItemPeriodicClass);
	}

	/**
	 * Sets the noGenericsFactory that will be adapted to a fit the {@link CodesTableItemsFactory} interface.
	 *
	 * @param genericsCodesTableItemsFactory {@link NoGenericsCodesTableItemsFactory} to adapt to fit the
	 * {@link CodesTableItemsFactory} interface.
	 */
	public void setNoGenericsCodesTableItemsFactory(NoGenericsCodesTableItemsFactory genericsCodesTableItemsFactory) {
		this.noGenericsCodesTableItemsFactory = genericsCodesTableItemsFactory;
	}
}
