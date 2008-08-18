package nav_cons_deploy_verifikasjon;

public class VerifikasjonProxy implements nav_cons_deploy_verifikasjon.Verifikasjon {
  private boolean _useJNDI = true;
  private String _endpoint = null;
  private nav_cons_deploy_verifikasjon.Verifikasjon __verifikasjon = null;
  
  public VerifikasjonProxy() {
    _initVerifikasjonProxy();
  }
  
  private void _initVerifikasjonProxy() {
  
    if (_useJNDI) {
      try {
        javax.naming.InitialContext ctx = new javax.naming.InitialContext();
        __verifikasjon = ((nav_cons_deploy_verifikasjon.VerfikasjonWSExp_VerifikasjonHttpService)ctx.lookup("java:comp/env/service/VerfikasjonWSExp_VerifikasjonHttpService")).getVerfikasjonWSExp_VerifikasjonHttpPort();
      }
      catch (javax.naming.NamingException namingException) {}
      catch (javax.xml.rpc.ServiceException serviceException) {}
    }
    if (__verifikasjon == null) {
      try {
        __verifikasjon = (new nav_cons_deploy_verifikasjon.VerfikasjonWSExp_VerifikasjonHttpServiceLocator()).getVerfikasjonWSExp_VerifikasjonHttpPort();
        
      }
      catch (javax.xml.rpc.ServiceException serviceException) {}
    }
    if (__verifikasjon != null) {
      if (_endpoint != null)
        ((javax.xml.rpc.Stub)__verifikasjon)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
      else
        _endpoint = (String)((javax.xml.rpc.Stub)__verifikasjon)._getProperty("javax.xml.rpc.service.endpoint.address");
    }
    
  }
  
  
  public void useJNDI(boolean useJNDI) {
    _useJNDI = useJNDI;
    __verifikasjon = null;
    
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (__verifikasjon != null)
      ((javax.xml.rpc.Stub)__verifikasjon)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public nav_cons_deploy_verifikasjon.Verifikasjon getVerifikasjon() {
    if (__verifikasjon == null)
      _initVerifikasjonProxy();
    return __verifikasjon;
  }
  
  public nav_cons_deploy_verifikasjon.ASBOVerifikasjonRes opWS(nav_cons_deploy_verifikasjon.ASBOVerifikasjonReq ASBOVerifikasjonReqWS) throws java.rmi.RemoteException, nav_cons_deploy_verifikasjon.FaultVerifikasjonGenerisk{
    if (__verifikasjon == null)
      _initVerifikasjonProxy();
    return __verifikasjon.opWS(ASBOVerifikasjonReqWS);
  }
  
  public nav_cons_deploy_verifikasjon.ASBOVerifikasjonRes opCEI(nav_cons_deploy_verifikasjon.ASBOVerifikasjonReq ASBOVerifikasjonReqCEI) throws java.rmi.RemoteException, nav_cons_deploy_verifikasjon.FaultVerifikasjonGenerisk{
    if (__verifikasjon == null)
      _initVerifikasjonProxy();
    return __verifikasjon.opCEI(ASBOVerifikasjonReqCEI);
  }
  
  public nav_cons_deploy_verifikasjon.ASBOVerifikasjonRes opFEM(nav_cons_deploy_verifikasjon.ASBOVerifikasjonReq ASBOVerifikasjonReqFEM) throws java.rmi.RemoteException, nav_cons_deploy_verifikasjon.FaultVerifikasjonGenerisk{
    if (__verifikasjon == null)
      _initVerifikasjonProxy();
    return __verifikasjon.opFEM(ASBOVerifikasjonReqFEM);
  }
  
  public nav_cons_deploy_verifikasjon.ASBOVerifikasjonRes opSCA(nav_cons_deploy_verifikasjon.ASBOVerifikasjonReq ASBOVerifikasjonReqSCA) throws java.rmi.RemoteException, nav_cons_deploy_verifikasjon.FaultVerifikasjonGenerisk{
    if (__verifikasjon == null)
      _initVerifikasjonProxy();
    return __verifikasjon.opSCA(ASBOVerifikasjonReqSCA);
  }
  
  
}