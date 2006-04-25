package no.trygdeetaten.business.framework.service;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.springframework.orm.hibernate3.HibernateTemplate;

import no.trygdeetaten.common.framework.FrameworkError;
import no.trygdeetaten.common.framework.error.SystemException;

/**
 * Base class that adds support for using Hibernate from a BusinessService implementation.
 * 
 * @author person7553f5959484, Accenture
 * @version $Id: BusinessHibernateService.java 2370 2005-06-23 14:39:51Z skb2930 $
 */
public abstract class BusinessHibernateService extends BusinessService {

	/** Helper class that makes working with Hibernate a lot easier. */
	private HibernateTemplate hibernateTemplate = null;

	/**
	 * Sets the HibernateTemplate to use for accessing the database.
	 *
	 * @param hibernateTemplate HibernateTemplate to use for accessing the database.
	 * @see HibernateTemplate
	 */
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	/**
	 * Returns the HibernateTemplate to use for accessing the database.
	 *
	 * @return the HibernateTemplate to use for accessing the database.
	 * @see HibernateTemplate
	 */
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	/**
	 * Force initialization of a proxy or persistent collection..
	 * 
	 * @param proxy - a persistable object, proxy, persistent collection or null
	 * @throws SystemException if the proxy can't be initialized, e.g. the session was closed
	 */
	protected void initialize(Object proxy) throws SystemException {
		try {
			Hibernate.initialize(proxy);
		} catch (HibernateException e) {
			throw new SystemException(FrameworkError.HIBERNATE_INITIALIZATION_ERROR, e);
		}
	}
}
