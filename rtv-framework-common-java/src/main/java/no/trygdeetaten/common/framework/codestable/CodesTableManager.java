package no.trygdeetaten.common.framework.codestable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import no.trygdeetaten.common.framework.FrameworkError;
import no.trygdeetaten.common.framework.cache.Cache;
import no.trygdeetaten.common.framework.error.SystemException;
import no.trygdeetaten.common.framework.service.LocalService;
import no.trygdeetaten.common.framework.service.ServiceFailedException;
import no.trygdeetaten.common.framework.service.ServiceRequest;
import no.trygdeetaten.common.framework.service.ServiceResponse;

/**
 * CodesTableManager is utility for accessing and caching persistent codes tables.
 * The class is not implemented as a Singleton, but should be externally managed
 * as a singleton, e.g. by using Spring Framework.
 * 
 * @author Ragnar Wisløff, Accenture
 * @author person7553f5959484, Accenture
 * @version $Id: CodesTableManager.java 2786 2006-02-28 13:24:08Z skb2930 $
 */
public class CodesTableManager {

	// The local thread safe cache of codes tables
	private Cache cachedCodesTables = null;

	// The business delegate
	private LocalService delegate = null;

	// The list of codes table names to initialize
	private List codesTableNames = null;

	// Name of this component
	private String name = "";

	/**
	 * Initializes the cache of configured codes tables.
	 */
	public void init() {
		if (null == cachedCodesTables) {
			throw new SystemException(
				FrameworkError.SERVICE_INIT_ERROR,
				new IllegalStateException("Missing cachedCodesTables. Config problem?"),
				name);
		}

		if (null == codesTableNames || 1 > codesTableNames.size()) {
			throw new SystemException(
				FrameworkError.SERVICE_INIT_ERROR,
				new IllegalStateException("The list of codes table names must contain at least one entry"),
				name);
		}

		if (null == delegate) {
			throw new SystemException(
				FrameworkError.SERVICE_INIT_ERROR,
				new IllegalStateException("No delegate has been set for retrieving codes tables."),
				name);
		}

		try {
			ServiceResponse response =
				delegate.execute(new ServiceRequest("InitCodesTables", "CodesTableNames", codesTableNames));

			for (int idx = 0; idx < codesTableNames.size(); idx++) {
				String codesTableName = (String) codesTableNames.get(idx);
				Map codesTable = (Map) response.getData(codesTableName);

				if (null == codesTable) {
					throw new SystemException(FrameworkError.CODES_TABLE_NOT_FOUND, codesTableName);
				}

				synchronized (cachedCodesTables) {
					cachedCodesTables.put(codesTableName, codesTable);
				}
			}
		} catch (ServiceFailedException sfe) {
			throw new SystemException(FrameworkError.CODES_TABLE_INIT_ERROR, sfe);
		}
	}

	/**
	 * Returns all items in the specified codes table.
	 * 
	 * @param codesTableClass the codes table class.
	 * @return a Map containing all the items in the codes table.
	 */
	public Map getCodesTable(Class codesTableClass) {
		validateCodesTableClass(codesTableClass);

		return getCachedCodesTable(codesTableClass);
	}

	/**
	 * Returns a filtered view of all items in the specified codes table.
	 *
	 * @param codesTableClass the codes table class.
	 * @param filter the filter to run the codestable through to get the filtered view.
	 * @return a Map containing a filtered view of all items in the codes table.
	 * @see Filter
	 */
	public Map getFilteredCodesTable(Class codesTableClass, Filter filter) {
		validateCodesTableClass(codesTableClass);

		final Map codesTable = getCodesTable(codesTableClass);
		final Map filteredMap = new LinkedHashMap();

		for (Iterator iter = codesTable.entrySet().iterator(); iter.hasNext();) {
			Map.Entry entry = (Map.Entry) iter.next();

			if (filter.include((CodesTableItem) entry.getValue())) {
				filteredMap.put(entry.getKey(), entry.getValue());
			}
		}

		return filteredMap;
	}

	/**
	 * Returns the item in the specified codes table that matches the specified code.
	 * 
	 * @param codesTableClass the codes table class
	 * @param code the item's code
	 * @return the codes table item or null if the code does not exist in the codes table
	 */
	public CodesTableItem getCodesTableItem(Class codesTableClass, Object code) {
		validateCodesTableClass(codesTableClass);

		return (CodesTableItem) getCachedCodesTable(codesTableClass).get(code);
	}

	/**
	 * Returns the decode for the specified code in specified codes table.
	 * 
	 * @param codesTableClass the codes table class
	 * @param code the item's code
	 * @return the decode or null if the code does not exist in the codes table
	 */
	public String getDecode(Class codesTableClass, Object code) {
		validateCodesTableClass(codesTableClass);
		CodesTableItem codesTableItem = getCodesTableItem(codesTableClass, code);

		return null == codesTableItem ? null : codesTableItem.getDekode();
	}

	/**
	 * Returns the code for the specified decode in specified codes table.
	 * 
	 * @param codesTableClass the codes table class
	 * @param decode the item's decode
	 * @return the code
	 */
	public String getCode(Class codesTableClass, Object decode) {
		validateCodesTableClass(codesTableClass);

		if (null != decode) {
			Set entries = getCachedCodesTable(codesTableClass).entrySet();

			if (null != entries) {
				for (Iterator iterator = entries.iterator(); iterator.hasNext();) {
					Map.Entry entry = (Map.Entry) iterator.next();
					CodesTableItem value = (CodesTableItem) entry.getValue();

					if (decode.equals(value.getDekode())) {
						return value.getKode();
					}
				}
			}
		}

		return null;
	}

	/**
	 * Initializes the specified codes table.
	 *
	 * @param codesTableClass the codes table class
	 */
	private Map retrieveCodesTable(Class codesTableClass) {
		List list = new ArrayList();
		list.add(codesTableClass.getName());

		try {
			ServiceResponse response = delegate.execute(new ServiceRequest("InitCodesTables", "CodesTableNames", list));
			Map ct = (Map) response.getData(codesTableClass.getName());

			if (null == ct) {
				throw new SystemException(FrameworkError.CODES_TABLE_NOT_FOUND, codesTableClass.getName());
			}

			return ct;
		} catch (ServiceFailedException sfe) {
			throw new SystemException(FrameworkError.CODES_TABLE_INIT_ERROR, sfe);
		}
	}

	/**
	 * Accesses the codestable from the local cache. If the specified codes table is not present,
	 * an attempt to initialize it using the delegae is made.
	 * 
	 * @param codesTableClass the codes table class
	 * @return a Map containing all the items in the codes table
	 */
	private Map getCachedCodesTable(Class codesTableClass) {
		final String codesTableClassName = codesTableClass.getName();
		Map codesTable;

		// TODOS double check idiom??
		synchronized (cachedCodesTables) {
			codesTable = (Map) cachedCodesTables.get(codesTableClassName);
		}

		if (null == codesTable) {
			codesTable = retrieveCodesTable(codesTableClass);

			synchronized (cachedCodesTables) {
				cachedCodesTables.put(codesTableClassName, codesTable);
			}

			if (null == codesTable) {
				throw new SystemException(FrameworkError.CODES_TABLE_NOT_FOUND, codesTableClass);
			}
		}

		return codesTable;
	}

	/**
	 * Checks that the class to load a codes table for is a subclass of <code>CodesTableItem</code>. If not, an exception will
	 * be thrown.
	 *
	 * @param codesTableClass the class to load a codes table for.
	 * @throws SystemException if the class to load a codes table for is not a subclass of <code>CodesTableItem</code>.
	 * @see CodesTableItem
	 */
	private void validateCodesTableClass(final Class codesTableClass) {
		if (null != codesTableClass && !CodesTableItem.class.isAssignableFrom(codesTableClass)) {
			throw new SystemException(FrameworkError.CODES_TABLE_INIT_ERROR, codesTableClass + " is not a codes table");
		}
	}

	/**
	 * Assigns a list of codes table class names to be initialized.
	 * 
	 * @param list the list of codes table class names
	 */
	public void setCodesTableNames(List list) {
		codesTableNames = list;
	}

	/**
	 * Assigns an implementation of a LocalService as the delegate.
	 * 
	 * @param service the local service
	 */
	public void setDelegate(LocalService service) {
		delegate = service;
	}

	/**
	 * Assigns a cache to be used for caching codes tables.
	 * 
	 * @param cache the codes tables cache.
	 */
	public void setCache(Cache cache) {
		cachedCodesTables = cache;
	}

	/**
	 * Assigns the name of this component. The name is only used in error reporting.
	 * 
	 * @param name the name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Filter for creating a filtered view of a codes table
	 */
	public interface Filter {

		/**
		 * Returns whether the item should be included or not in the filtered view of the codes table.
		 *
		 * @param item the item to check whether should be included or not.
		 * @return true if the item should be included, false otherwise.
		 */
		boolean include(CodesTableItem item);
	}
}
