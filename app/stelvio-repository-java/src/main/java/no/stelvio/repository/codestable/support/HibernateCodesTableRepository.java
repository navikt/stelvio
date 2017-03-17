package no.stelvio.repository.codestable.support;

import java.util.List;

import javax.persistence.Entity;

import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate4.HibernateTemplate;

import no.stelvio.common.codestable.support.AbstractCodesTableItem;
import no.stelvio.repository.codestable.CodesTableRepository;

/**
 * Hibernate implementation of the CodesTableRepository. A HibernateTemplate must be injected into this class before an instance
 * can be passed to a client.
 * 
 * @author person983601e0e117 (Accenture)
 * @see CodesTableRepository
 */
public class HibernateCodesTableRepository implements CodesTableRepository {

	private HibernateTemplate hibernateTemplate;

	private static final String QUERY_FIND_ALL_CODESTABLEITEMS_START = "SELECT cti FROM ";
	private static final String QUERY_FIND_ALL_CODESTABLEITEMS_END = " cti";

	/** {@inheritDoc} */
	public <T extends AbstractCodesTableItem<? extends Enum, V>, V> List<T> findCodesTableItems(Class<T> codestableItem) {
		final String queryString = buildQuery(codestableItem);

		// Uses HibernateCallback to be able to set the FlushMode
		// Without effecting the code using the same HibernateTemplate
		// as would be the case with HibernateTemplate.setFlushMode)
		return hibernateTemplate.execute(new HibernateCallback<List<T>>() {
			public List<T> doInHibernate(Session session) throws HibernateException {
				Query queryObject = session.createQuery(queryString);
				queryObject.setFlushMode(FlushMode.MANUAL);
				return queryObject.list();
			}
		});

	}

	/**
	 * Builds a query used to fetch contens of codestable specified by the supplied codestableitem class.
	 * 
	 * @param codestableItem
	 *            Class the identifies the CodestableItem
	 * @param <V>
	 *            an object type
	 * @param <T>
	 *            item tyep
	 * @return query string
	 */
	private <T extends AbstractCodesTableItem<? extends Enum, V>, V> String buildQuery(Class<T> codestableItem) {
		StringBuffer sb = new StringBuffer();
		String entityName = codestableItem.getSimpleName();

		// Check if default entity name has been overridden by setting name in Entity-annotation
		if (codestableItem.isAnnotationPresent(Entity.class)) {
			Entity entityAnnotation = codestableItem.getAnnotation(Entity.class);
			entityName = entityAnnotation.name().equals("") ? entityName : entityAnnotation.name();
		}

		// Building query
		sb.append(QUERY_FIND_ALL_CODESTABLEITEMS_START);
		sb.append(entityName);
		sb.append(QUERY_FIND_ALL_CODESTABLEITEMS_END);

		return sb.toString();
	}

	/**
	 * Gets the hibernateTemplate used by this implementation to retrieve codestables from the database.
	 * 
	 * @return the hibernateTemplate
	 */
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	/**
	 * Sets the hibernateTemplate used by this implementation to retrieve codestables from the database.
	 * 
	 * @param hibernateTemplate
	 *            a hibernateTemplate
	 */
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
}
