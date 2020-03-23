package no.stelvio.repository.error.support;

import java.util.HashSet;
import java.util.Set;

import org.springframework.orm.hibernate4.HibernateTemplate;

import no.stelvio.common.error.support.ErrorDefinition;
import no.stelvio.repository.error.ErrorDefinitionRepository;

/**
 * Hibernate implementation of repository for retrieving all error definitions from the error definition table.
 *
 */
public class HibernateErrorDefinitionRepository implements ErrorDefinitionRepository {
	private HibernateTemplate hibernateTemplate;

	@Override
	public Set<ErrorDefinition> findErrorDefinitions() {
		return new HashSet<>(hibernateTemplate.loadAll(ErrorDefinition.class));
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
