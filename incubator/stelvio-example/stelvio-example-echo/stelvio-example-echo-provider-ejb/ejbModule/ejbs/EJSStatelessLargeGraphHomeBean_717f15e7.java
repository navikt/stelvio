package ejbs;

import com.ibm.ejs.container.*;

/**
 * EJSStatelessLargeGraphHomeBean_717f15e7
 */
public class EJSStatelessLargeGraphHomeBean_717f15e7 extends EJSHome {
	static final long serialVersionUID = 61;
	/**
	 * EJSStatelessLargeGraphHomeBean_717f15e7
	 */
	public EJSStatelessLargeGraphHomeBean_717f15e7() throws java.rmi.RemoteException {
		super();	}
	/**
	 * create
	 */
	public ejbs.LargeGraph create() throws javax.ejb.CreateException, java.rmi.RemoteException {
BeanO beanO = null;
ejbs.LargeGraph result = null;
boolean createFailed = false;
try {
	result = (ejbs.LargeGraph) super.createWrapper(null);
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
