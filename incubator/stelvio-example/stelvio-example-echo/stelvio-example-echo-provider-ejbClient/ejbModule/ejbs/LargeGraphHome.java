package ejbs;

/**
 * Home interface for Enterprise Bean: LargeGraph
 */
public interface LargeGraphHome extends javax.ejb.EJBHome {

	/**
	 * Creates a default instance of Session Bean: LargeGraph
	 */
	public ejbs.LargeGraph create()
		throws javax.ejb.CreateException,
		java.rmi.RemoteException;
}
