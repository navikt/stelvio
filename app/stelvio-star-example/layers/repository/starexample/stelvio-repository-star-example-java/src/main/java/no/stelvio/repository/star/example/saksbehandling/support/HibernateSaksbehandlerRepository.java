package no.stelvio.repository.star.example.saksbehandling.support;

import org.springframework.orm.hibernate3.HibernateTemplate;

import no.stelvio.domain.star.example.saksbehandling.Saksbehandler;
import no.stelvio.repository.star.example.saksbehandling.SaksbehandlerRepository;

/**
 * Using Hibernate to work with saksbehandler against a database.
 *
 * @author personf8e9850ed756, Accenture
 */
public class HibernateSaksbehandlerRepository implements SaksbehandlerRepository {
	HibernateTemplate hibernateTemplate;

	/** {@inheritDoc} */
	public Saksbehandler findSaksbehandlerById(long saksbehandlerId) {
		return (Saksbehandler) hibernateTemplate.get(Saksbehandler.class, saksbehandlerId);
	}

	/**
	 * Sets the value of hibernateTemplate.
	 *
	 * @param hibernateTemplate value to set on hibernateTemplate.
	 */
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
}
