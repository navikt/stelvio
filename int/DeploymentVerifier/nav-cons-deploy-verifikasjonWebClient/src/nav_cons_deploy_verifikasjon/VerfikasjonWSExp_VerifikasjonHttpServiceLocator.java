/**
 * VerfikasjonWSExp_VerifikasjonHttpServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf170819.19 v52708210711
 */

package nav_cons_deploy_verifikasjon;

public class VerfikasjonWSExp_VerifikasjonHttpServiceLocator extends com.ibm.ws.webservices.multiprotocol.AgnosticService implements com.ibm.ws.webservices.multiprotocol.GeneratedService, nav_cons_deploy_verifikasjon.VerfikasjonWSExp_VerifikasjonHttpService {

    public VerfikasjonWSExp_VerifikasjonHttpServiceLocator() {
        super(com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
           "http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon/Binding",
           "VerfikasjonWSExp_VerifikasjonHttpService"));

        context.setLocatorName("nav_cons_deploy_verifikasjon.VerfikasjonWSExp_VerifikasjonHttpServiceLocator");
    }

    public VerfikasjonWSExp_VerifikasjonHttpServiceLocator(com.ibm.ws.webservices.multiprotocol.ServiceContext ctx) {
        super(ctx);
        context.setLocatorName("nav_cons_deploy_verifikasjon.VerfikasjonWSExp_VerifikasjonHttpServiceLocator");
    }

    // Use to get a proxy class for verfikasjonWSExp_VerifikasjonHttpPort
    private final java.lang.String verfikasjonWSExp_VerifikasjonHttpPort_address = "http://localhost:9080/nav-cons-deploy-verifikasjonWeb/sca/VerfikasjonWSExp";

    public java.lang.String getVerfikasjonWSExp_VerifikasjonHttpPortAddress() {
        if (context.getOverriddingEndpointURIs() == null) {
            return verfikasjonWSExp_VerifikasjonHttpPort_address;
        }
        String overriddingEndpoint = (String) context.getOverriddingEndpointURIs().get("VerfikasjonWSExp_VerifikasjonHttpPort");
        if (overriddingEndpoint != null) {
            return overriddingEndpoint;
        }
        else {
            return verfikasjonWSExp_VerifikasjonHttpPort_address;
        }
    }

    private java.lang.String verfikasjonWSExp_VerifikasjonHttpPortPortName = "VerfikasjonWSExp_VerifikasjonHttpPort";

    // The WSDD port name defaults to the port name.
    private java.lang.String verfikasjonWSExp_VerifikasjonHttpPortWSDDPortName = "VerfikasjonWSExp_VerifikasjonHttpPort";

    public java.lang.String getVerfikasjonWSExp_VerifikasjonHttpPortWSDDPortName() {
        return verfikasjonWSExp_VerifikasjonHttpPortWSDDPortName;
    }

    public void setVerfikasjonWSExp_VerifikasjonHttpPortWSDDPortName(java.lang.String name) {
        verfikasjonWSExp_VerifikasjonHttpPortWSDDPortName = name;
    }

    public nav_cons_deploy_verifikasjon.Verifikasjon getVerfikasjonWSExp_VerifikasjonHttpPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(getVerfikasjonWSExp_VerifikasjonHttpPortAddress());
        }
        catch (java.net.MalformedURLException e) {
            return null; // unlikely as URL was validated in WSDL2Java
        }
        return getVerfikasjonWSExp_VerifikasjonHttpPort(endpoint);
    }

    public nav_cons_deploy_verifikasjon.Verifikasjon getVerfikasjonWSExp_VerifikasjonHttpPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        nav_cons_deploy_verifikasjon.Verifikasjon _stub =
            (nav_cons_deploy_verifikasjon.Verifikasjon) getStub(
                verfikasjonWSExp_VerifikasjonHttpPortPortName,
                (String) getPort2NamespaceMap().get(verfikasjonWSExp_VerifikasjonHttpPortPortName),
                nav_cons_deploy_verifikasjon.Verifikasjon.class,
                "nav_cons_deploy_verifikasjon.VerfikasjonWSExp_VerifikasjonHttpBindingStub",
                portAddress.toString());
        if (_stub instanceof com.ibm.ws.webservices.engine.client.Stub) {
            ((com.ibm.ws.webservices.engine.client.Stub) _stub).setPortName(verfikasjonWSExp_VerifikasjonHttpPortWSDDPortName);
        }
        return _stub;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (nav_cons_deploy_verifikasjon.Verifikasjon.class.isAssignableFrom(serviceEndpointInterface)) {
                return getVerfikasjonWSExp_VerifikasjonHttpPort();
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("WSWS3273E: Error: There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        String inputPortName = portName.getLocalPart();
        if ("VerfikasjonWSExp_VerifikasjonHttpPort".equals(inputPortName)) {
            return getVerfikasjonWSExp_VerifikasjonHttpPort();
        }
        else  {
            throw new javax.xml.rpc.ServiceException();
        }
    }

    public void setPortNamePrefix(java.lang.String prefix) {
        verfikasjonWSExp_VerifikasjonHttpPortWSDDPortName = prefix + "/" + verfikasjonWSExp_VerifikasjonHttpPortPortName;
    }

    public javax.xml.namespace.QName getServiceName() {
        return com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon/Binding", "VerfikasjonWSExp_VerifikasjonHttpService");
    }

    private java.util.Map port2NamespaceMap = null;

    protected synchronized java.util.Map getPort2NamespaceMap() {
        if (port2NamespaceMap == null) {
            port2NamespaceMap = new java.util.HashMap();
            port2NamespaceMap.put(
               "VerfikasjonWSExp_VerifikasjonHttpPort",
               "http://schemas.xmlsoap.org/wsdl/soap/");
        }
        return port2NamespaceMap;
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            String serviceNamespace = getServiceName().getNamespaceURI();
            for (java.util.Iterator i = getPort2NamespaceMap().keySet().iterator(); i.hasNext(); ) {
                ports.add(
                    com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                        serviceNamespace,
                        (String) i.next()));
            }
        }
        return ports.iterator();
    }

    public javax.xml.rpc.Call[] getCalls(javax.xml.namespace.QName portName) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            throw new javax.xml.rpc.ServiceException("WSWS3062E: Error: portName should not be null.");
        }
        if  (portName.getLocalPart().equals("VerfikasjonWSExp_VerifikasjonHttpPort")) {
            return new javax.xml.rpc.Call[] {
                createCall(portName, "opWS", "opWSRequest"),
                createCall(portName, "opCEI", "opCEIRequest"),
                createCall(portName, "opFEM", "opFEMRequest"),
                createCall(portName, "opSCA", "opSCARequest"),
            };
        }
        else {
            throw new javax.xml.rpc.ServiceException("WSWS3062E: Error: portName should not be null.");
        }
    }
}
