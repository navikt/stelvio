/**
 * VerfikasjonWSExp_VerifikasjonHttpBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf170819.19 v52708210711
 */

package nav_cons_deploy_verifikasjon;

public class VerfikasjonWSExp_VerifikasjonHttpBindingStub extends com.ibm.ws.webservices.engine.client.Stub implements nav_cons_deploy_verifikasjon.Verifikasjon {
    public VerfikasjonWSExp_VerifikasjonHttpBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws com.ibm.ws.webservices.engine.WebServicesFault {
        if (service == null) {
            super.service = new com.ibm.ws.webservices.engine.client.Service();
        }
        else {
            super.service = service;
        }
        super.engine = ((com.ibm.ws.webservices.engine.client.Service) super.service).getEngine();
        initTypeMapping();
        super.cachedEndpoint = endpointURL;
        super.connection = ((com.ibm.ws.webservices.engine.client.Service) super.service).getConnection(endpointURL);
        super.messageContexts = new com.ibm.ws.webservices.engine.MessageContext[4];
    }

    private void initTypeMapping() {
        javax.xml.rpc.encoding.TypeMapping tm = super.getTypeMapping(com.ibm.ws.webservices.engine.Constants.URI_LITERAL_ENC);
        java.lang.Class javaType = null;
        javax.xml.namespace.QName xmlType = null;
        javax.xml.namespace.QName compQName = null;
        javax.xml.namespace.QName compTypeQName = null;
        com.ibm.ws.webservices.engine.encoding.SerializerFactory sf = null;
        com.ibm.ws.webservices.engine.encoding.DeserializerFactory df = null;
        javaType = nav_cons_deploy_verifikasjon.ASBOVerifikasjonReq.class;
        xmlType = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/asbo", "ASBOVerifikasjonReq");
        sf = com.ibm.ws.webservices.engine.encoding.ser.BaseSerializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.BeanSerializerFactory.class, javaType, xmlType);
        df = com.ibm.ws.webservices.engine.encoding.ser.BaseDeserializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.BeanDeserializerFactory.class, javaType, xmlType);
        if (sf != null || df != null) {
            tm.register(javaType, xmlType, sf, df);
        }

        javaType = nav_cons_deploy_verifikasjon.ASBOVerifikasjonRes.class;
        xmlType = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/asbo", "ASBOVerifikasjonRes");
        sf = com.ibm.ws.webservices.engine.encoding.ser.BaseSerializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.BeanSerializerFactory.class, javaType, xmlType);
        df = com.ibm.ws.webservices.engine.encoding.ser.BaseDeserializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.BeanDeserializerFactory.class, javaType, xmlType);
        if (sf != null || df != null) {
            tm.register(javaType, xmlType, sf, df);
        }

        javaType = nav_cons_deploy_verifikasjon.FaultVerifikasjonGenerisk.class;
        xmlType = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/fault", "FaultVerifikasjonGenerisk");
        sf = com.ibm.ws.webservices.engine.encoding.ser.BaseSerializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.BeanSerializerFactory.class, javaType, xmlType);
        df = com.ibm.ws.webservices.engine.encoding.ser.BaseDeserializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.BeanDeserializerFactory.class, javaType, xmlType);
        if (sf != null || df != null) {
            tm.register(javaType, xmlType, sf, df);
        }

        javaType = nav_cons_deploy_verifikasjon.FaultVerifikasjon.class;
        xmlType = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/fault", "FaultVerifikasjon");
        sf = com.ibm.ws.webservices.engine.encoding.ser.BaseSerializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.BeanSerializerFactory.class, javaType, xmlType);
        df = com.ibm.ws.webservices.engine.encoding.ser.BaseDeserializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.BeanDeserializerFactory.class, javaType, xmlType);
        if (sf != null || df != null) {
            tm.register(javaType, xmlType, sf, df);
        }

    }

    private static com.ibm.ws.webservices.engine.description.OperationDesc _opWSOperation0 = null;
    private static com.ibm.ws.webservices.engine.description.OperationDesc _getopWSOperation0() {
        com.ibm.ws.webservices.engine.description.ParameterDesc[]  _params0 = new com.ibm.ws.webservices.engine.description.ParameterDesc[] {
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "ASBOVerifikasjonReqWS"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/asbo", "ASBOVerifikasjonReq"), nav_cons_deploy_verifikasjon.ASBOVerifikasjonReq.class, false, false, false, false, true, false), 
          };
        _params0[0].setOption("inputPosition","0");
        _params0[0].setOption("partQNameString","{http://nav-cons-deploy-verifikasjon/nav/no/deploy/asbo}ASBOVerifikasjonReq");
        _params0[0].setOption("partName","ASBOVerifikasjonReq");
        com.ibm.ws.webservices.engine.description.ParameterDesc  _returnDesc0 = new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "ASBOVerifikasjonResWS"), com.ibm.ws.webservices.engine.description.ParameterDesc.OUT, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/asbo", "ASBOVerifikasjonRes"), nav_cons_deploy_verifikasjon.ASBOVerifikasjonRes.class, true, false, false, false, true, false); 
        _returnDesc0.setOption("outputPosition","0");
        _returnDesc0.setOption("partQNameString","{http://nav-cons-deploy-verifikasjon/nav/no/deploy/asbo}ASBOVerifikasjonRes");
        _returnDesc0.setOption("partName","ASBOVerifikasjonRes");
        com.ibm.ws.webservices.engine.description.FaultDesc[]  _faults0 = new com.ibm.ws.webservices.engine.description.FaultDesc[] {
         new com.ibm.ws.webservices.engine.description.FaultDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/fault", "FaultVerifikasjonGenerisk"), com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon", "opWS_faultVerfikasjonGeneriskWSMsg"), "nav_cons_deploy_verifikasjon.FaultVerifikasjonGenerisk", com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon", "FaultVerifikasjonGenerisk_element"), com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/fault", "FaultVerifikasjonGenerisk")), 
          };
        _opWSOperation0 = new com.ibm.ws.webservices.engine.description.OperationDesc("opWS", com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon", "opWS"), _params0, _returnDesc0, _faults0, "");
        _opWSOperation0.setOption("inputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon", "opWSRequestMsg"));
        _opWSOperation0.setOption("ServiceQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon/Binding", "VerfikasjonWSExp_VerifikasjonHttpService"));
        _opWSOperation0.setOption("inputName","opWSRequest");
        _opWSOperation0.setOption("ResponseNamespace","http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon");
        _opWSOperation0.setOption("buildNum","cf170819.19");
        _opWSOperation0.setOption("outputName","opWSResponse");
        _opWSOperation0.setOption("ResponseLocalPart","opWSResponse");
        _opWSOperation0.setOption("targetNamespace","http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon/Binding");
        _opWSOperation0.setOption("outputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon", "opWSResponseMsg"));
        _opWSOperation0.setOption("portTypeQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon", "Verifikasjon"));
        _opWSOperation0.setUse(com.ibm.ws.webservices.engine.enumtype.Use.LITERAL);
        _opWSOperation0.setStyle(com.ibm.ws.webservices.engine.enumtype.Style.WRAPPED);
        return _opWSOperation0;

    }

    private int _opWSIndex0 = 0;
    private synchronized com.ibm.ws.webservices.engine.client.Stub.Invoke _getopWSInvoke0(Object[] parameters) throws com.ibm.ws.webservices.engine.WebServicesFault  {
        com.ibm.ws.webservices.engine.MessageContext mc = super.messageContexts[_opWSIndex0];
        if (mc == null) {
            mc = new com.ibm.ws.webservices.engine.MessageContext(super.engine);
            mc.setOperation(VerfikasjonWSExp_VerifikasjonHttpBindingStub._opWSOperation0);
            mc.setUseSOAPAction(true);
            mc.setSOAPActionURI("");
            mc.setEncodingStyle(com.ibm.ws.webservices.engine.Constants.URI_LITERAL_ENC);
            mc.setProperty(com.ibm.wsspi.webservices.Constants.SEND_TYPE_ATTR_PROPERTY, Boolean.FALSE);
            mc.setProperty(com.ibm.wsspi.webservices.Constants.ENGINE_DO_MULTI_REFS_PROPERTY, Boolean.FALSE);
            super.primeMessageContext(mc);
            super.messageContexts[_opWSIndex0] = mc;
        }
        try {
            mc = (com.ibm.ws.webservices.engine.MessageContext) mc.clone();
        }
        catch (CloneNotSupportedException cnse) {
            throw com.ibm.ws.webservices.engine.WebServicesFault.makeFault(cnse);
        }
        return new com.ibm.ws.webservices.engine.client.Stub.Invoke(connection, mc, parameters);
    }

    public nav_cons_deploy_verifikasjon.ASBOVerifikasjonRes opWS(nav_cons_deploy_verifikasjon.ASBOVerifikasjonReq ASBOVerifikasjonReqWS) throws java.rmi.RemoteException, nav_cons_deploy_verifikasjon.FaultVerifikasjonGenerisk {
        if (super.cachedEndpoint == null) {
            throw new com.ibm.ws.webservices.engine.NoEndPointException();
        }
        java.util.Vector _resp = null;
        try {
            _resp = _getopWSInvoke0(new java.lang.Object[] {ASBOVerifikasjonReqWS}).invoke();

        } catch (com.ibm.ws.webservices.engine.WebServicesFault wsf) {
            Exception e = wsf.getUserException();
            if (e != null) {
                if (e instanceof nav_cons_deploy_verifikasjon.FaultVerifikasjonGenerisk) {
                    throw (nav_cons_deploy_verifikasjon.FaultVerifikasjonGenerisk) e;
                }
            }
            throw wsf;
        } 
        try {
            return (nav_cons_deploy_verifikasjon.ASBOVerifikasjonRes) ((com.ibm.ws.webservices.engine.xmlsoap.ext.ParamValue) _resp.get(0)).getValue();
        } catch (java.lang.Exception _exception) {
            return (nav_cons_deploy_verifikasjon.ASBOVerifikasjonRes) super.convert(((com.ibm.ws.webservices.engine.xmlsoap.ext.ParamValue) _resp.get(0)).getValue(), nav_cons_deploy_verifikasjon.ASBOVerifikasjonRes.class);
        }
    }

    private static com.ibm.ws.webservices.engine.description.OperationDesc _opCEIOperation1 = null;
    private static com.ibm.ws.webservices.engine.description.OperationDesc _getopCEIOperation1() {
        com.ibm.ws.webservices.engine.description.ParameterDesc[]  _params1 = new com.ibm.ws.webservices.engine.description.ParameterDesc[] {
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "ASBOVerifikasjonReqCEI"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/asbo", "ASBOVerifikasjonReq"), nav_cons_deploy_verifikasjon.ASBOVerifikasjonReq.class, false, false, false, false, true, false), 
          };
        _params1[0].setOption("inputPosition","0");
        _params1[0].setOption("partQNameString","{http://nav-cons-deploy-verifikasjon/nav/no/deploy/asbo}ASBOVerifikasjonReq");
        _params1[0].setOption("partName","ASBOVerifikasjonReq");
        com.ibm.ws.webservices.engine.description.ParameterDesc  _returnDesc1 = new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "ASBOVerifikasjonResCEI"), com.ibm.ws.webservices.engine.description.ParameterDesc.OUT, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/asbo", "ASBOVerifikasjonRes"), nav_cons_deploy_verifikasjon.ASBOVerifikasjonRes.class, true, false, false, false, true, false); 
        _returnDesc1.setOption("outputPosition","0");
        _returnDesc1.setOption("partQNameString","{http://nav-cons-deploy-verifikasjon/nav/no/deploy/asbo}ASBOVerifikasjonRes");
        _returnDesc1.setOption("partName","ASBOVerifikasjonRes");
        com.ibm.ws.webservices.engine.description.FaultDesc[]  _faults1 = new com.ibm.ws.webservices.engine.description.FaultDesc[] {
         new com.ibm.ws.webservices.engine.description.FaultDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/fault", "FaultVerifikasjonGenerisk"), com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon", "opCEI_faultVerfikasjonGeneriskCEIMsg"), "nav_cons_deploy_verifikasjon.FaultVerifikasjonGenerisk", com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon", "FaultVerifikasjonGenerisk_element2"), com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/fault", "FaultVerifikasjonGenerisk")), 
          };
        _opCEIOperation1 = new com.ibm.ws.webservices.engine.description.OperationDesc("opCEI", com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon", "opCEI"), _params1, _returnDesc1, _faults1, "");
        _opCEIOperation1.setOption("inputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon", "opCEIRequestMsg"));
        _opCEIOperation1.setOption("ServiceQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon/Binding", "VerfikasjonWSExp_VerifikasjonHttpService"));
        _opCEIOperation1.setOption("inputName","opCEIRequest");
        _opCEIOperation1.setOption("ResponseNamespace","http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon");
        _opCEIOperation1.setOption("buildNum","cf170819.19");
        _opCEIOperation1.setOption("outputName","opCEIResponse");
        _opCEIOperation1.setOption("ResponseLocalPart","opCEIResponse");
        _opCEIOperation1.setOption("targetNamespace","http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon/Binding");
        _opCEIOperation1.setOption("outputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon", "opCEIResponseMsg"));
        _opCEIOperation1.setOption("portTypeQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon", "Verifikasjon"));
        _opCEIOperation1.setUse(com.ibm.ws.webservices.engine.enumtype.Use.LITERAL);
        _opCEIOperation1.setStyle(com.ibm.ws.webservices.engine.enumtype.Style.WRAPPED);
        return _opCEIOperation1;

    }

    private int _opCEIIndex1 = 1;
    private synchronized com.ibm.ws.webservices.engine.client.Stub.Invoke _getopCEIInvoke1(Object[] parameters) throws com.ibm.ws.webservices.engine.WebServicesFault  {
        com.ibm.ws.webservices.engine.MessageContext mc = super.messageContexts[_opCEIIndex1];
        if (mc == null) {
            mc = new com.ibm.ws.webservices.engine.MessageContext(super.engine);
            mc.setOperation(VerfikasjonWSExp_VerifikasjonHttpBindingStub._opCEIOperation1);
            mc.setUseSOAPAction(true);
            mc.setSOAPActionURI("");
            mc.setEncodingStyle(com.ibm.ws.webservices.engine.Constants.URI_LITERAL_ENC);
            mc.setProperty(com.ibm.wsspi.webservices.Constants.SEND_TYPE_ATTR_PROPERTY, Boolean.FALSE);
            mc.setProperty(com.ibm.wsspi.webservices.Constants.ENGINE_DO_MULTI_REFS_PROPERTY, Boolean.FALSE);
            super.primeMessageContext(mc);
            super.messageContexts[_opCEIIndex1] = mc;
        }
        try {
            mc = (com.ibm.ws.webservices.engine.MessageContext) mc.clone();
        }
        catch (CloneNotSupportedException cnse) {
            throw com.ibm.ws.webservices.engine.WebServicesFault.makeFault(cnse);
        }
        return new com.ibm.ws.webservices.engine.client.Stub.Invoke(connection, mc, parameters);
    }

    public nav_cons_deploy_verifikasjon.ASBOVerifikasjonRes opCEI(nav_cons_deploy_verifikasjon.ASBOVerifikasjonReq ASBOVerifikasjonReqCEI) throws java.rmi.RemoteException, nav_cons_deploy_verifikasjon.FaultVerifikasjonGenerisk {
        if (super.cachedEndpoint == null) {
            throw new com.ibm.ws.webservices.engine.NoEndPointException();
        }
        java.util.Vector _resp = null;
        try {
            _resp = _getopCEIInvoke1(new java.lang.Object[] {ASBOVerifikasjonReqCEI}).invoke();

        } catch (com.ibm.ws.webservices.engine.WebServicesFault wsf) {
            Exception e = wsf.getUserException();
            if (e != null) {
                if (e instanceof nav_cons_deploy_verifikasjon.FaultVerifikasjonGenerisk) {
                    throw (nav_cons_deploy_verifikasjon.FaultVerifikasjonGenerisk) e;
                }
            }
            throw wsf;
        } 
        try {
            return (nav_cons_deploy_verifikasjon.ASBOVerifikasjonRes) ((com.ibm.ws.webservices.engine.xmlsoap.ext.ParamValue) _resp.get(0)).getValue();
        } catch (java.lang.Exception _exception) {
            return (nav_cons_deploy_verifikasjon.ASBOVerifikasjonRes) super.convert(((com.ibm.ws.webservices.engine.xmlsoap.ext.ParamValue) _resp.get(0)).getValue(), nav_cons_deploy_verifikasjon.ASBOVerifikasjonRes.class);
        }
    }

    private static com.ibm.ws.webservices.engine.description.OperationDesc _opFEMOperation2 = null;
    private static com.ibm.ws.webservices.engine.description.OperationDesc _getopFEMOperation2() {
        com.ibm.ws.webservices.engine.description.ParameterDesc[]  _params2 = new com.ibm.ws.webservices.engine.description.ParameterDesc[] {
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "ASBOVerifikasjonReqFEM"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/asbo", "ASBOVerifikasjonReq"), nav_cons_deploy_verifikasjon.ASBOVerifikasjonReq.class, false, false, false, false, true, false), 
          };
        _params2[0].setOption("inputPosition","0");
        _params2[0].setOption("partQNameString","{http://nav-cons-deploy-verifikasjon/nav/no/deploy/asbo}ASBOVerifikasjonReq");
        _params2[0].setOption("partName","ASBOVerifikasjonReq");
        com.ibm.ws.webservices.engine.description.ParameterDesc  _returnDesc2 = new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "ASBOVerifikasjonResFEM"), com.ibm.ws.webservices.engine.description.ParameterDesc.OUT, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/asbo", "ASBOVerifikasjonRes"), nav_cons_deploy_verifikasjon.ASBOVerifikasjonRes.class, true, false, false, false, true, false); 
        _returnDesc2.setOption("outputPosition","0");
        _returnDesc2.setOption("partQNameString","{http://nav-cons-deploy-verifikasjon/nav/no/deploy/asbo}ASBOVerifikasjonRes");
        _returnDesc2.setOption("partName","ASBOVerifikasjonRes");
        com.ibm.ws.webservices.engine.description.FaultDesc[]  _faults2 = new com.ibm.ws.webservices.engine.description.FaultDesc[] {
         new com.ibm.ws.webservices.engine.description.FaultDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/fault", "FaultVerifikasjonGenerisk"), com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon", "opFEM_faultVerfikasjonGeneriskFEMMsg"), "nav_cons_deploy_verifikasjon.FaultVerifikasjonGenerisk", com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon", "FaultVerifikasjonGenerisk_element4"), com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/fault", "FaultVerifikasjonGenerisk")), 
          };
        _opFEMOperation2 = new com.ibm.ws.webservices.engine.description.OperationDesc("opFEM", com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon", "opFEM"), _params2, _returnDesc2, _faults2, "");
        _opFEMOperation2.setOption("inputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon", "opFEMRequestMsg"));
        _opFEMOperation2.setOption("ServiceQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon/Binding", "VerfikasjonWSExp_VerifikasjonHttpService"));
        _opFEMOperation2.setOption("inputName","opFEMRequest");
        _opFEMOperation2.setOption("ResponseNamespace","http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon");
        _opFEMOperation2.setOption("buildNum","cf170819.19");
        _opFEMOperation2.setOption("outputName","opFEMResponse");
        _opFEMOperation2.setOption("ResponseLocalPart","opFEMResponse");
        _opFEMOperation2.setOption("targetNamespace","http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon/Binding");
        _opFEMOperation2.setOption("outputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon", "opFEMResponseMsg"));
        _opFEMOperation2.setOption("portTypeQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon", "Verifikasjon"));
        _opFEMOperation2.setUse(com.ibm.ws.webservices.engine.enumtype.Use.LITERAL);
        _opFEMOperation2.setStyle(com.ibm.ws.webservices.engine.enumtype.Style.WRAPPED);
        return _opFEMOperation2;

    }

    private int _opFEMIndex2 = 2;
    private synchronized com.ibm.ws.webservices.engine.client.Stub.Invoke _getopFEMInvoke2(Object[] parameters) throws com.ibm.ws.webservices.engine.WebServicesFault  {
        com.ibm.ws.webservices.engine.MessageContext mc = super.messageContexts[_opFEMIndex2];
        if (mc == null) {
            mc = new com.ibm.ws.webservices.engine.MessageContext(super.engine);
            mc.setOperation(VerfikasjonWSExp_VerifikasjonHttpBindingStub._opFEMOperation2);
            mc.setUseSOAPAction(true);
            mc.setSOAPActionURI("");
            mc.setEncodingStyle(com.ibm.ws.webservices.engine.Constants.URI_LITERAL_ENC);
            mc.setProperty(com.ibm.wsspi.webservices.Constants.SEND_TYPE_ATTR_PROPERTY, Boolean.FALSE);
            mc.setProperty(com.ibm.wsspi.webservices.Constants.ENGINE_DO_MULTI_REFS_PROPERTY, Boolean.FALSE);
            super.primeMessageContext(mc);
            super.messageContexts[_opFEMIndex2] = mc;
        }
        try {
            mc = (com.ibm.ws.webservices.engine.MessageContext) mc.clone();
        }
        catch (CloneNotSupportedException cnse) {
            throw com.ibm.ws.webservices.engine.WebServicesFault.makeFault(cnse);
        }
        return new com.ibm.ws.webservices.engine.client.Stub.Invoke(connection, mc, parameters);
    }

    public nav_cons_deploy_verifikasjon.ASBOVerifikasjonRes opFEM(nav_cons_deploy_verifikasjon.ASBOVerifikasjonReq ASBOVerifikasjonReqFEM) throws java.rmi.RemoteException, nav_cons_deploy_verifikasjon.FaultVerifikasjonGenerisk {
        if (super.cachedEndpoint == null) {
            throw new com.ibm.ws.webservices.engine.NoEndPointException();
        }
        java.util.Vector _resp = null;
        try {
            _resp = _getopFEMInvoke2(new java.lang.Object[] {ASBOVerifikasjonReqFEM}).invoke();

        } catch (com.ibm.ws.webservices.engine.WebServicesFault wsf) {
            Exception e = wsf.getUserException();
            if (e != null) {
                if (e instanceof nav_cons_deploy_verifikasjon.FaultVerifikasjonGenerisk) {
                    throw (nav_cons_deploy_verifikasjon.FaultVerifikasjonGenerisk) e;
                }
            }
            throw wsf;
        } 
        try {
            return (nav_cons_deploy_verifikasjon.ASBOVerifikasjonRes) ((com.ibm.ws.webservices.engine.xmlsoap.ext.ParamValue) _resp.get(0)).getValue();
        } catch (java.lang.Exception _exception) {
            return (nav_cons_deploy_verifikasjon.ASBOVerifikasjonRes) super.convert(((com.ibm.ws.webservices.engine.xmlsoap.ext.ParamValue) _resp.get(0)).getValue(), nav_cons_deploy_verifikasjon.ASBOVerifikasjonRes.class);
        }
    }

    private static com.ibm.ws.webservices.engine.description.OperationDesc _opSCAOperation3 = null;
    private static com.ibm.ws.webservices.engine.description.OperationDesc _getopSCAOperation3() {
        com.ibm.ws.webservices.engine.description.ParameterDesc[]  _params3 = new com.ibm.ws.webservices.engine.description.ParameterDesc[] {
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "ASBOVerifikasjonReqSCA"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/asbo", "ASBOVerifikasjonReq"), nav_cons_deploy_verifikasjon.ASBOVerifikasjonReq.class, false, false, false, false, true, false), 
          };
        _params3[0].setOption("inputPosition","0");
        _params3[0].setOption("partQNameString","{http://nav-cons-deploy-verifikasjon/nav/no/deploy/asbo}ASBOVerifikasjonReq");
        _params3[0].setOption("partName","ASBOVerifikasjonReq");
        com.ibm.ws.webservices.engine.description.ParameterDesc  _returnDesc3 = new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "ASBOVerifikasjonReqSCA"), com.ibm.ws.webservices.engine.description.ParameterDesc.OUT, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/asbo", "ASBOVerifikasjonRes"), nav_cons_deploy_verifikasjon.ASBOVerifikasjonRes.class, true, false, false, false, true, false); 
        _returnDesc3.setOption("outputPosition","0");
        _returnDesc3.setOption("partQNameString","{http://nav-cons-deploy-verifikasjon/nav/no/deploy/asbo}ASBOVerifikasjonRes");
        _returnDesc3.setOption("partName","ASBOVerifikasjonRes");
        com.ibm.ws.webservices.engine.description.FaultDesc[]  _faults3 = new com.ibm.ws.webservices.engine.description.FaultDesc[] {
         new com.ibm.ws.webservices.engine.description.FaultDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/fault", "FaultVerifikasjonGenerisk"), com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon", "opSCA_faultVerfikasjonGeneriskSCAMsg"), "nav_cons_deploy_verifikasjon.FaultVerifikasjonGenerisk", com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon", "FaultVerifikasjonGenerisk_element3"), com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/fault", "FaultVerifikasjonGenerisk")), 
          };
        _opSCAOperation3 = new com.ibm.ws.webservices.engine.description.OperationDesc("opSCA", com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon", "opSCA"), _params3, _returnDesc3, _faults3, "");
        _opSCAOperation3.setOption("inputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon", "opSCARequestMsg"));
        _opSCAOperation3.setOption("ServiceQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon/Binding", "VerfikasjonWSExp_VerifikasjonHttpService"));
        _opSCAOperation3.setOption("inputName","opSCARequest");
        _opSCAOperation3.setOption("ResponseNamespace","http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon");
        _opSCAOperation3.setOption("buildNum","cf170819.19");
        _opSCAOperation3.setOption("outputName","opSCAResponse");
        _opSCAOperation3.setOption("ResponseLocalPart","opSCAResponse");
        _opSCAOperation3.setOption("targetNamespace","http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon/Binding");
        _opSCAOperation3.setOption("outputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon", "opSCAResponseMsg"));
        _opSCAOperation3.setOption("portTypeQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon", "Verifikasjon"));
        _opSCAOperation3.setUse(com.ibm.ws.webservices.engine.enumtype.Use.LITERAL);
        _opSCAOperation3.setStyle(com.ibm.ws.webservices.engine.enumtype.Style.WRAPPED);
        return _opSCAOperation3;

    }

    private int _opSCAIndex3 = 3;
    private synchronized com.ibm.ws.webservices.engine.client.Stub.Invoke _getopSCAInvoke3(Object[] parameters) throws com.ibm.ws.webservices.engine.WebServicesFault  {
        com.ibm.ws.webservices.engine.MessageContext mc = super.messageContexts[_opSCAIndex3];
        if (mc == null) {
            mc = new com.ibm.ws.webservices.engine.MessageContext(super.engine);
            mc.setOperation(VerfikasjonWSExp_VerifikasjonHttpBindingStub._opSCAOperation3);
            mc.setUseSOAPAction(true);
            mc.setSOAPActionURI("");
            mc.setEncodingStyle(com.ibm.ws.webservices.engine.Constants.URI_LITERAL_ENC);
            mc.setProperty(com.ibm.wsspi.webservices.Constants.SEND_TYPE_ATTR_PROPERTY, Boolean.FALSE);
            mc.setProperty(com.ibm.wsspi.webservices.Constants.ENGINE_DO_MULTI_REFS_PROPERTY, Boolean.FALSE);
            super.primeMessageContext(mc);
            super.messageContexts[_opSCAIndex3] = mc;
        }
        try {
            mc = (com.ibm.ws.webservices.engine.MessageContext) mc.clone();
        }
        catch (CloneNotSupportedException cnse) {
            throw com.ibm.ws.webservices.engine.WebServicesFault.makeFault(cnse);
        }
        return new com.ibm.ws.webservices.engine.client.Stub.Invoke(connection, mc, parameters);
    }

    public nav_cons_deploy_verifikasjon.ASBOVerifikasjonRes opSCA(nav_cons_deploy_verifikasjon.ASBOVerifikasjonReq ASBOVerifikasjonReqSCA) throws java.rmi.RemoteException, nav_cons_deploy_verifikasjon.FaultVerifikasjonGenerisk {
        if (super.cachedEndpoint == null) {
            throw new com.ibm.ws.webservices.engine.NoEndPointException();
        }
        java.util.Vector _resp = null;
        try {
            _resp = _getopSCAInvoke3(new java.lang.Object[] {ASBOVerifikasjonReqSCA}).invoke();

        } catch (com.ibm.ws.webservices.engine.WebServicesFault wsf) {
            Exception e = wsf.getUserException();
            if (e != null) {
                if (e instanceof nav_cons_deploy_verifikasjon.FaultVerifikasjonGenerisk) {
                    throw (nav_cons_deploy_verifikasjon.FaultVerifikasjonGenerisk) e;
                }
            }
            throw wsf;
        } 
        try {
            return (nav_cons_deploy_verifikasjon.ASBOVerifikasjonRes) ((com.ibm.ws.webservices.engine.xmlsoap.ext.ParamValue) _resp.get(0)).getValue();
        } catch (java.lang.Exception _exception) {
            return (nav_cons_deploy_verifikasjon.ASBOVerifikasjonRes) super.convert(((com.ibm.ws.webservices.engine.xmlsoap.ext.ParamValue) _resp.get(0)).getValue(), nav_cons_deploy_verifikasjon.ASBOVerifikasjonRes.class);
        }
    }

    private static void _staticInit() {
        _opFEMOperation2 = _getopFEMOperation2();
        _opWSOperation0 = _getopWSOperation0();
        _opCEIOperation1 = _getopCEIOperation1();
        _opSCAOperation3 = _getopSCAOperation3();
    }

    static {
       _staticInit();
    }
}
