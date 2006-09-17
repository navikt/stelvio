package no.stelvio.integration.framework.jms;

import java.util.Hashtable;

import no.stelvio.common.framework.FrameworkError;
import no.stelvio.common.framework.ejb.LookupHelper;
import no.stelvio.common.framework.service.ServiceNotFoundException;

/**
 * 
 * @author person356941106810, Accenture
 */
public class DummyJndiHelper implements LookupHelper {
	
	private DummyQueueConnectionFactory factory = new DummyQueueConnectionFactory();

	/* (non-Javadoc)
	 * @see no.stelvio.integration.framework.jndi.JNDIHelper#lookup(java.lang.String)
	 */
	public Object lookup(String jndiName, Hashtable env) throws ServiceNotFoundException {
		
		if( "jms/qcf/dummyQcf".equals(jndiName) ) {
			throw new ServiceNotFoundException(FrameworkError.UNSPECIFIED_ERROR);
		} else if("jms/queue/dummyQueue".equals(jndiName) ) {
			throw new ServiceNotFoundException(FrameworkError.UNSPECIFIED_ERROR);
		} else if( "jms/qcf/testQcf".equals(jndiName)) {
			return factory;
		}
		
		
		return null;
	}

	/**
	 * @return
	 */
	public DummyQueueConnectionFactory getFactory() {
		return factory;
	}

	/**
	 * @param factory
	 */
	public void setFactory(DummyQueueConnectionFactory factory) {
		this.factory = factory;
	}

}
