package no.nav.common.framework.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * SessionAdapter is a convenient class that a Stateless Session EJB 
 * should extend without having to implement the mandatory 
 * methods dictated by the <i>SessionBean</i> interface.
 * 
 * @author person7553f5959484
 * @version $Revision: 1130 $ $Author: psa2920 $ $Date: 2004-08-19 17:39:09 +0200 (Thu, 19 Aug 2004) $
 */
public abstract class SessionAdapter implements SessionBean {
	
	protected Log log = LogFactory.getLog(this.getClass());

	private SessionContext ctx = null;

	/** 
	 * Assign SessionContext provided by the EJB container to 
	 * current instance of the sub class.
	 * 
	 * {@inheritDoc}
	 * @see javax.ejb.SessionBean#setSessionContext(javax.ejb.SessionContext)
	 */
	public final void setSessionContext(SessionContext ctx) throws EJBException, RemoteException {
		this.ctx = ctx;
	}

	/**
	 * Access the SessionContext assigned to this EJB instance.
	 * 
	 * @return the current SessionContext
	 */
	protected final SessionContext getSessionContext() {
		return ctx;
	}

	/** 
	 * Not implemented. Should be overrided by sub classes when needed.
	 * 
	 * {@inheritDoc}
	 * @see javax.ejb.SessionBean#ejbRemove()
	 */
	public void ejbRemove() throws EJBException, RemoteException {
	}

	/** 
	 * Not implemented. Should be overrided by sub classes when needed.
	 * 
	 * {@inheritDoc}
	 * @see javax.ejb.SessionBean#ejbActivate()
	 */
	public void ejbActivate() throws EJBException, RemoteException {
	}

	/** 
	 * Not implemented. Should be overrided by sub classes when needed.
	 * 
	 * {@inheritDoc}
	 * @see javax.ejb.SessionBean#ejbPassivate()
	 */
	public void ejbPassivate() throws EJBException, RemoteException {
	}
}
