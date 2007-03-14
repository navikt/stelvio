package no.stelvio.service.star.example.codestable;

import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.SessionContext;

import no.stelvio.common.codestable.CodesTableItem;
import no.stelvio.common.codestable.CodesTableNotFoundException;
import no.stelvio.common.codestable.CodesTablePeriodicItem;
import no.stelvio.common.codestable.factory.CodesTableItemsFactory;

import org.springframework.context.access.ContextSingletonBeanFactoryLocator;
import org.springframework.ejb.support.AbstractStatelessSessionBean;

/**
 * EJB implementation of the <code>CodesTableFactory</code> interface which acts as a thin facade over the POJO
 * containing the business logic.
 *
 * @author personf8e9850ed756, Accenture
 */
public class CodesTableFactoryBean extends AbstractStatelessSessionBean implements CodesTableItemsFactory {
	private static final long serialVersionUID = 234987123498L;
	private CodesTableItemsFactory codesTableItemsFactory;

	public <T extends CodesTableItem<K, V>, K extends Enum, V> 
		List<T> createCodesTableItems(Class<T> codesTableItemClass)
			throws CodesTableNotFoundException {
		return codesTableItemsFactory.createCodesTableItems(codesTableItemClass);
	}

	public <T extends CodesTablePeriodicItem<K, V>, K extends Enum, V> 
		List<T> createCodesTablePeriodicItems(Class<T> codesTableItemClass)
			throws CodesTableNotFoundException {
		return codesTableItemsFactory.createCodesTablePeriodicItems(codesTableItemClass);
	}

	/** {@inheritDoc} */
	@Override
	protected void onEjbCreate() throws CreateException {
		codesTableItemsFactory = (CodesTableItemsFactory)
				getBeanFactory().getBean("srv.starexample.codesTableFactory", CodesTableItemsFactory.class);
	}

	/** {@inheritDoc} */
	@Override
	public void setSessionContext(SessionContext sessionContext) {
		super.setSessionContext(sessionContext);
		setBeanFactoryLocator(ContextSingletonBeanFactoryLocator.getInstance());
		setBeanFactoryLocatorKey("star.example.beanFactory");
	}
}