  
package ejbs;	


/**
 * @generated
 */
public interface LargeGraphAsync {

  /**
   * @generated
   */		
  public com.ibm.websphere.sca.Ticket echoAsync(java.lang.String arg0);

  /**
   * @generated
   */		
  public com.ibm.websphere.sca.Ticket echoAsyncWithCallback(java.lang.String arg0);

  /**
   * @generated
   */		
  public ejbs.LGResponse echoResponse(com.ibm.websphere.sca.Ticket __ticket, long __timeout) throws java.rmi.RemoteException;
}
