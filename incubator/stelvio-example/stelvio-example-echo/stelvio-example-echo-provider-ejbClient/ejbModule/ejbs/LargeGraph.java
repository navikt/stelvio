package ejbs;

import java.rmi.RemoteException;

/**
 * Remote interface for Enterprise Bean: LargeGraph
 */
public interface LargeGraph extends javax.ejb.EJBObject {
	
	public LGResponse echo(String input) throws RemoteException;
}
