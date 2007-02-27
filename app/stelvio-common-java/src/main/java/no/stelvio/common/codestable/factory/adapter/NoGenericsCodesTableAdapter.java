package no.stelvio.common.codestable.factory.adapter;

import no.stelvio.common.codestable.CodesTable;
import no.stelvio.common.codestable.CodesTableItem;
import no.stelvio.common.codestable.CodesTableItemPeriodic;
import no.stelvio.common.codestable.CodesTableNotFoundException;
import no.stelvio.common.codestable.CodesTablePeriodic;
import no.stelvio.common.codestable.factory.CodesTableFactory;
import no.stelvio.common.codestable.factory.NoGenericsCodesTableFactory;

/**
 * Adapter to bridge the Generics/NoGenerics ({@link CodesTableFactory}/{@link NoGenericsCodesTableFactory}) gap
 * 
 * This is an implementation of the Object Adapter pattern. It becomes obsolete once EJB handles generics in method signatures
 * 
 * @author person983601e0e117 (Accenture)
 */
public class NoGenericsCodesTableAdapter implements CodesTableFactory{
	
	
	private NoGenericsCodesTableFactory noGenenericsFactory;
	
	/**
	 * {@inheritDoc}
	 */	
	@SuppressWarnings("unchecked")
	public <T extends CodesTableItem> CodesTable<T> createCodesTable(
			Class<T> codesTableItemClass) throws CodesTableNotFoundException {
		
		Object codesTableObject = noGenenericsFactory.createCodesTable(codesTableItemClass);
		CodesTable<T> codeTable = (CodesTable<T>) codesTableObject;
		
		return codeTable;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public <T extends CodesTableItemPeriodic> CodesTablePeriodic<T> createCodesTablePeriodic(
			Class<T> codesTableItemPeriodicClass)
			throws CodesTableNotFoundException {
		
		Object codesTableObject = noGenenericsFactory.createCodesTablePeriodic(codesTableItemPeriodicClass);
		CodesTablePeriodic<T> codeTable = (CodesTablePeriodic<T>) codesTableObject;
		
		return codeTable;
	}

	/**
	 * Gets the noGenericsFactory that will be adapted to a fit the {@link CodesTableFactory} interface
	 * @return NoGenericsCodesTableFactory instance
	 */
	public NoGenericsCodesTableFactory getNoGenenericsFactory() {
			
		return noGenenericsFactory;
	}

	/**
	 * Gets the noGenericsFactory that will be adapted to a fit the {@link CodesTableFactory} interface
	 * @param noGenenericsFactory {@link NoGenericsCodesTableFactory} to adapt to fit the {@link CodesTableFactory} interface 
	 */
	public void setNoGenenericsFactory(
			NoGenericsCodesTableFactory noGenenericsFactory) {
				
		this.noGenenericsFactory = noGenenericsFactory;
	}	
	
}
