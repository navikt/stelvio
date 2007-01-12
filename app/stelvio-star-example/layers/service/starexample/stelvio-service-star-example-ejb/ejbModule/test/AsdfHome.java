package test;

/**
 * Home interface for Enterprise Bean: Asdf
 */
public interface AsdfHome extends javax.ejb.EJBHome {

	/**
	 * Creates a default instance of Session Bean: Asdf
	 */
	public test.Asdf create()
		throws javax.ejb.CreateException,
		java.rmi.RemoteException;
}
