package no.stelvio.repository.codestable.support;

import java.util.List;
import javax.persistence.Entity;

import org.springframework.orm.hibernate3.HibernateTemplate;

import no.stelvio.common.codestable.support.AbstractCodesTableItem;
import no.stelvio.repository.codestable.CodesTableRepository;

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
	@SuppressWarnings("unchecked")
	public <T extends AbstractCodesTableItem<? extends Enum, V>, V> List<T> findCodesTableItems(Class<T>  codestableItem) {
		String query = buildQuery( codestableItem);
		
		return getHibernateTemplate().find(query);
	}
	
	/**
	 * Builds a query used to fetch contens of codestable 
	 * specified by the supplied codestableitem class.
	 * @param codestableItem Class the identifies the CodestableItem
	 * @return query string
	 */
	private <T extends AbstractCodesTableItem<? extends Enum, V>, V> String buildQuery(Class<T> codestableItem){
		StringBuffer sb = new StringBuffer();
		String entityName = codestableItem.getSimpleName();
		
		//Check if default entity name has been overridden by setting name in Entity-annotation
		if(codestableItem.isAnnotationPresent(Entity.class)){
			Entity entityAnnotation = codestableItem.getAnnotation(Entity.class);
			entityName = entityAnnotation.name().equals("") ? entityName : entityAnnotation.name();
		}
		
		//Building query
		sb.append(QUERY_FIND_ALL_CODESTABLEITEMS_START);
		sb.append(entityName);
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
