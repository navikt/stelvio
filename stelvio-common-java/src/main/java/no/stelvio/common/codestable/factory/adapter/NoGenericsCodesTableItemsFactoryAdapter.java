package no.stelvio.common.codestable.factory.adapter;

import static no.stelvio.common.util.Internal.cast;

import java.util.List;

import no.stelvio.common.codestable.CodesTableNotFoundException;
import no.stelvio.common.codestable.factory.CodesTableItemsFactory;
import no.stelvio.common.codestable.factory.NoGenericsCodesTableItemsFactory;
import no.stelvio.common.codestable.support.AbstractCodesTableItem;
import no.stelvio.common.codestable.support.AbstractCodesTablePeriodicItem;

/**
 * Adapter to bridge the Generics/NoGenerics ({@link CodesTableItemsFactory}/{@link NoGenericsCodesTableItemsFactory})
 * gap.
 *
 * This is an implementation of the Object Adapter pattern. It becomes obsolete once EJB handles generics in method
 * signatures.
 *
 */
public class NoGenericsCodesTableItemsFactoryAdapter implements CodesTableItemsFactory {
	private NoGenericsCodesTableItemsFactory noGenericsCodesTableItemsFactory;

	@Override
	public <T extends AbstractCodesTableItem<K, V>, K extends Enum, V>
		List<T> createCodesTableItems(Class<T> codesTableItemClass) throws CodesTableNotFoundException {

		return cast(noGenericsCodesTableItemsFactory.createCodesTableItems(codesTableItemClass));
	}

	@Override
	public <T extends AbstractCodesTablePeriodicItem<K, V>, K extends Enum, V>
		List<T> createCodesTablePeriodicItems(Class<T> codesTableItemPeriodicClass)
			throws CodesTableNotFoundException {

		return cast(noGenericsCodesTableItemsFactory.createCodesTablePeriodicItems(codesTableItemPeriodicClass));
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
