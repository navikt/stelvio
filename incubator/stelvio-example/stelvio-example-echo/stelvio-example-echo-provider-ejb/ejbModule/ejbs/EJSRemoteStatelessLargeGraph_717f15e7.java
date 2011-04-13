package ejbs;

import com.ibm.ejs.container.*;

/**
 * EJSRemoteStatelessLargeGraph_717f15e7
 */
public class EJSRemoteStatelessLargeGraph_717f15e7 extends EJSWrapper implements LargeGraph {
	/**
	 * EJSRemoteStatelessLargeGraph_717f15e7
	 */
	public EJSRemoteStatelessLargeGraph_717f15e7() throws java.rmi.RemoteException {
		super();	}
	/**
	 * echo
	 */
	public ejbs.LGResponse echo(java.lang.String input) throws java.rmi.RemoteException {
		EJSDeployedSupport _EJS_s = container.getEJSDeployedSupport(this);
		Object[] _jacc_parms = null;
		ejbs.LGResponse _EJS_result = null;
		try {
			if (container.doesJaccNeedsEJBArguments( this ))
			{
				_jacc_parms = new Object[1];
				_jacc_parms[0] = input;
			}
	ejbs.LargeGraphBean beanRef = (ejbs.LargeGraphBean)container.preInvoke(this, 0, _EJS_s, _jacc_parms);
			_EJS_result = beanRef.echo(input);
		}
		catch (java.rmi.RemoteException ex) {
			_EJS_s.setUncheckedException(ex);
		}
		catch (Throwable ex) {
			_EJS_s.setUncheckedException(ex);
			throw new java.rmi.RemoteException("bean method raised unchecked exception", ex);
		}

		finally {
			try{
				container.postInvoke(this, 0, _EJS_s);
			} finally {
				container.putEJSDeployedSupport(_EJS_s);
			}
		}
		return _EJS_result;
	}
}
