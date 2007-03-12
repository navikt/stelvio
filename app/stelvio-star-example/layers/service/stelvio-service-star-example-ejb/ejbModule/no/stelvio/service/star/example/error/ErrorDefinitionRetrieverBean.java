/**
 * @author personf8e9850ed756, Accenture
 * @todo write javadoc
 */
package no.stelvio.service.star.example.error;

import java.util.Set;
import javax.ejb.CreateException;
import javax.ejb.SessionContext;

import org.springframework.context.access.ContextSingletonBeanFactoryLocator;
import org.springframework.ejb.support.AbstractStatelessSessionBean;

import no.stelvio.common.error.retriever.ErrorDefinitionRetriever;
import no.stelvio.common.error.support.ErrorDefinition;
import no.stelvio.service.star.example.saksbehandling.SaksbehandlerNotFoundException;

/**
 * EJB implementation of the <code>ErrorDefinitionRetriever</code> interface which acts as a thin facade over the POJO
 * containing the business logic.
 *
 * @author personf8e9850ed756, Accenture
 */
public class ErrorDefinitionRetrieverBean extends AbstractStatelessSessionBean implements ErrorDefinitionRetriever {
	private ErrorDefinitionRetriever errorDefinitionRetriever;

	/** {@inheritDoc} */
	public Set<ErrorDefinition> retrieveAll() throws SaksbehandlerNotFoundException {
		return errorDefinitionRetriever.retrieveAll();
	}

	/** {@inheritDoc} */
	@Override
	protected void onEjbCreate() throws CreateException {
		errorDefinitionRetriever = (ErrorDefinitionRetriever) getBeanFactory().getBean("srv.starexample.errorDefinitionRetriever");
	}

	/** {@inheritDoc} */
	@Override
	public void setSessionContext(SessionContext sessionContext) {
		super.setSessionContext(sessionContext);
		setBeanFactoryLocator(ContextSingletonBeanFactoryLocator.getInstance());
		setBeanFactoryLocatorKey("star.example.beanFactory");
	}
}
