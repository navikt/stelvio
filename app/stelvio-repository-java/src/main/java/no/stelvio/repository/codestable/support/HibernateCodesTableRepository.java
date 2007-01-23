package no.stelvio.repository.codestable.support;

import java.util.List;

import no.stelvio.common.codestable.CodesTableItem;
import no.stelvio.common.codestable.support.AbstractCodesTableItem;
import no.stelvio.repository.codestable.CodesTableRepository;

import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * Hibernate implementation of the CodesTableRepository. 
 * A HibernateTemplate must be injected into this class before an instance can be passed to a client.
 * 
 * @see CodesTableRepository
 * 
 * @author person983601e0e117 (Accenture)
 *
 */
public class HibernateCodesTableRepository implements CodesTableRepository {

	private HibernateTemplate hibernateTemplate;
	
	private static final String QUERY_FIND_ALL_CODESTABLEITEMS_START = "SELECT cti FROM ";
	private static final String QUERY_FIND_ALL_CODESTABLEITEMS_END = " cti";

	/**
	 * {@inheritDoc}
	 */
	public <T extends AbstractCodesTableItem> List findCodesTableItems(Class<T>  codestableItem) {
		String query = buildQuery( codestableItem);
		
		List codesTableItems = getHibernateTemplate().find(query);
		
		return codesTableItems;
	}
	
	/**
	 * Builds a query used to fetch contens of codestable 
	 * specified by the supplied codestableitem class.
	 * @param codestableItem Class the identifies the CodestableItem
	 * @return query string
	 */
	private <T extends AbstractCodesTableItem> String buildQuery(Class<T> codestableItem){
		StringBuffer sb = new StringBuffer();
		
		//Building query
		sb.append(QUERY_FIND_ALL_CODESTABLEITEMS_START);
		sb.append(codestableItem.getSimpleName());
		sb.append(QUERY_FIND_ALL_CODESTABLEITEMS_END);
		
		return sb.toString();
	}

	/**
	 * Sets the hibernateTemplate used by this implementation to retrieve codestables from the databas
	 * @return
	 */
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	/**
	 * Gets the hibernateTemplate used by this implementation to retrieve codestables from the databas
	 * @param hibernateTemplate
	 */
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
}
