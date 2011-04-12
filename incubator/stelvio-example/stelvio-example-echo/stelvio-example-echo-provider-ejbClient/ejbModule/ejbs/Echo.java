package ejbs;

import java.rmi.RemoteException;

/**
 * Remote interface for Enterprise Bean: Echo
 */
public interface Echo extends javax.ejb.EJBObject {

	public String echo(String input) throws RemoteException;

}
