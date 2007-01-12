package test;

/**
 * Home interface for Enterprise Bean: TestBb
 */
public interface TestBbHome extends javax.ejb.EJBHome {

	/**
	 * Creates a default instance of Session Bean: TestBb
	 */
	public test.TestBb create()
		throws javax.ejb.CreateException,
		java.rmi.RemoteException;
}
