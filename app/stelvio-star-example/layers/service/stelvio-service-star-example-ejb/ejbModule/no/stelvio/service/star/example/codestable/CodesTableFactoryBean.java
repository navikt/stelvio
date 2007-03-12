package no.stelvio.service.star.example.codestable;

import javax.ejb.CreateException;
import javax.ejb.SessionContext;

import org.springframework.context.access.ContextSingletonBeanFactoryLocator;
import org.springframework.ejb.support.AbstractStatelessSessionBean;

import no.stelvio.common.codestable.CodesTable;
import no.stelvio.common.codestable.CodesTableItem;
import no.stelvio.common.codestable.CodesTableItemPeriodic;
import no.stelvio.common.codestable.CodesTableNotFoundException;
import no.stelvio.common.codestable.CodesTablePeriodic;
import no.stelvio.common.codestable.factory.CodesTableFactory;

/**
 * EJB implementation of the <code>CodesTableFactory</code> interface which acts as a thin facade over the POJO
 * containing the business logic.
 *
 * @author personf8e9850ed756, Accenture
 */
public class CodesTableFactoryBean extends AbstractStatelessSessionBean implements CodesTableFactory {
	private CodesTableFactory codesTableFactory;

	public <T extends CodesTableItem> CodesTable<T> createCodesTable(Class<T> codesTableClass)
			throws CodesTableNotFoundException {
		return codesTableFactory.createCodesTable(codesTableClass);
	}

	public <T extends CodesTableItemPeriodic> CodesTablePeriodic<T> createCodesTablePeriodic(Class<T> codesTableClass)
			throws CodesTableNotFoundException {
		return codesTableFactory.createCodesTablePeriodic(codesTableClass);
	}

	/** {@inheritDoc} */
	@Override
	protected void onEjbCreate() throws CreateException {
		codesTableFactory = (CodesTableFactory)
				getBeanFactory().getBean("srv.starexample.codesTableFactory", CodesTableFactory.class);
	}

	/** {@inheritDoc} */
	@Override
	public void setSessionContext(SessionContext sessionContext) {
		super.setSessionContext(sessionContext);
		setBeanFactoryLocator(ContextSingletonBeanFactoryLocator.getInstance());
		setBeanFactoryLocatorKey("star.example.beanFactory");
	}
}