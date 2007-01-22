package no.stelvio.common.codestable.factory;

import no.stelvio.common.codestable.CodesTable;
import no.stelvio.common.codestable.CodesTableItem;
import no.stelvio.common.codestable.CodesTableItemPeriodic;
import no.stelvio.common.codestable.CodesTableNotFoundException;
import no.stelvio.common.codestable.CodesTablePeriodic;

import org.springmodules.cache.annotations.Cacheable;

import java.util.List;

/**
 * Interface defining functionality for retrieving a <code>CodesTable</code> from the database.
 * Must be implemented by the EJB that has responsibility for fetching the codestables
 * from the database.
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
public interface CodesTableFactory{
	static final String name = "sdf";
	/**
	 * Retrieves a list of <code>CodesTableItem</code>s belonging to a <code>CodesTableItem</code>s.
	 * 
	 * @param <T> <code>CodesTableItem</code>'s or subclasses of <code>CodesTableItem</code> that are valid input 
	 * and output parameters. 
	 * @param codesTable the class of type <code>CodesTableItem</code> that represents the <code>CodesTable</code> 
	 * the items shall be retrieved from.
	 * @return A list of <code>CodesTableItem</code>s belonging to a <code>CodesTable</code>.
	 * @throws no.stelvio.common.codestable.CodesTableNotFoundException - exception thrown when a <code>CodesTable</code> couldn't be retrieved from the database.
	 * @todo fix javadoc and cache model id
	 */
	@Cacheable(modelId = name)
	<T extends CodesTableItem> CodesTable<T> createCodesTable(Class<T> codesTable)
		throws CodesTableNotFoundException;
	
	/**
	 * Retrieves a list of <code>CodesTableItemPeriodic</code>s belonging to a <code>CodesTableItemPeriodic</code>s.
	 * 
	 * @param <T> <code>CodesTableItemPeriodic</code>'s or subclasses of <code>CodesTableItemPeriodic</code> that are valid input 
	 * and output parameters.
	 * @param codesTablePeriodic the class of type <code>CodesTableItemPeriodic</code> that represents the <code>CodesTablePeriodic</code> 
	 * the items shall be retrieved from.
	 * @return A list of <code>CodesTableItemPeriodic</code>s belonging to a <code>CodesTablePeriodic</code>.
	 * @throws CodesTableNotFoundException - exception thrown when a codestable couldn't be retrieved from the database.
	 * @todo fix javadoc
	 */
	@Cacheable(modelId = "persistent")
	<T extends CodesTableItemPeriodic> CodesTablePeriodic<T> createCodesTablePeriodic(Class<T> codesTablePeriodic)
		throws CodesTableNotFoundException;
}