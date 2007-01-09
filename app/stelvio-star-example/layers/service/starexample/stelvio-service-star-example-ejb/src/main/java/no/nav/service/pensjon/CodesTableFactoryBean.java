 
package no.nav.service.pensjon;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

/**
 * Bean implementation class for Session Bean: JpaCodesTableFactory
 *
 * @ejb.bean
 *	name="JpaCodesTableFactory"
 *	type="Stateless"
 *	local-jndi-name="ejb/no/nav/service/pensjon/CodesTableFactoryLocalHome"
 *	view-type="local"
 *	transaction-type="Container"
 *
 * @ejb.home
 *	local-class="no.nav.service.pensjon.CodesTableFactoryLocalHome"
 *
 * @ejb.interface
 *	local-class="no.nav.service.pensjon.CodesTableFactoryLocal"
 *  local-extends="no.stelvio.common.codestable.factory.CodesTableFactory"
 *
 */
public class CodesTableFactoryBean implements javax.ejb.SessionBean {
	private SessionContext mySessionCtx;
	/**
	 * getSessionContext
	 */
	public SessionContext getSessionContext() {
		return mySessionCtx;
	}
	/**
	 * setSessionContext
	 */
	public void setSessionContext(SessionContext ctx) {
		mySessionCtx = ctx;
	}
	/**
	 * ejbCreate
	 */
	public void ejbCreate() throws CreateException {
	}
	/**
	 * ejbActivate
	 */
	public void ejbActivate() {
	}
	/**
	 * ejbPassivate
	 */
	public void ejbPassivate() {
	}
	/**
	 * ejbRemove
	 */
	public void ejbRemove() {
	}
}