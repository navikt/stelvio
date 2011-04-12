  
package ejbs;	


/**
 * @generated
 */
public interface EchoAsync {

  /**
   * @generated
   */		
  public com.ibm.websphere.sca.Ticket echoAsync(java.lang.String input);

  /**
   * @generated
   */		
  public com.ibm.websphere.sca.Ticket echoAsyncWithCallback(java.lang.String input);

  /**
   * @generated
   */		
  public java.lang.String echoResponse(com.ibm.websphere.sca.Ticket __ticket, long __timeout) throws java.rmi.RemoteException;
}
