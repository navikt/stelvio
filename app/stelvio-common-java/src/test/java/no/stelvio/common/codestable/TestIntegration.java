package no.stelvio.common.codestable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * @author personf8e9850ed756, Accenture
 * @todo write javadoc
 */
public class TestIntegration {
	HibernateTemplate hibernateTemplate;

	public Set<TestCti> loadData() {
		List list = hibernateTemplate.loadAll(TestCti.class);
		
		return new HashSet<TestCti>(list);
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
}
