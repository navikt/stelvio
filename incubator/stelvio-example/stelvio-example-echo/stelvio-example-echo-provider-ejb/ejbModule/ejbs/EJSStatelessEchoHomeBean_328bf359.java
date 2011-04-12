package ejbs;

import com.ibm.ejs.container.*;

/**
 * EJSStatelessEchoHomeBean_328bf359
 */
public class EJSStatelessEchoHomeBean_328bf359 extends EJSHome {
	static final long serialVersionUID = 61;
	/**
	 * EJSStatelessEchoHomeBean_328bf359
	 */
	public EJSStatelessEchoHomeBean_328bf359() throws java.rmi.RemoteException {
		super();	}
	/**
	 * create
	 */
	public ejbs.Echo create() throws javax.ejb.CreateException, java.rmi.RemoteException {
BeanO beanO = null;
ejbs.Echo result = null;
boolean createFailed = false;
try {
	result = (ejbs.Echo) super.createWrapper(null);
}
catch (javax.ejb.CreateException ex) {
	createFailed = true;
	throw ex;
} catch (java.rmi.RemoteException ex) {
	createFailed = true;
	throw ex;
} catch (Throwable ex) {
	createFailed = true;
	throw new CreateFailureException(ex);
} finally {
	if (createFailed) {
		super.createFailure(beanO);
	}
}
return result;	}
}
