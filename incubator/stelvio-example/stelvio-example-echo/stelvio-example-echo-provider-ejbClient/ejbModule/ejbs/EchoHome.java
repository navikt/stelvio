package ejbs;

/**
 * Home interface for Enterprise Bean: Echo
 */
public interface EchoHome extends javax.ejb.EJBHome {

	/**
	 * Creates a default instance of Session Bean: Echo
	 */
	public ejbs.Echo create()
		throws javax.ejb.CreateException,
		java.rmi.RemoteException;
}
