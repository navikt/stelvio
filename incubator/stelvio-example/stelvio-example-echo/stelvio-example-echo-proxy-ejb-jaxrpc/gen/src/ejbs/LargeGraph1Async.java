  
package ejbs;	


/**
 * @generated
 */
public interface LargeGraph1Async {

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
  public commonj.sdo.DataObject echoResponse(com.ibm.websphere.sca.Ticket __ticket, long __timeout) throws com.ibm.websphere.sca.ServiceBusinessException;
}
