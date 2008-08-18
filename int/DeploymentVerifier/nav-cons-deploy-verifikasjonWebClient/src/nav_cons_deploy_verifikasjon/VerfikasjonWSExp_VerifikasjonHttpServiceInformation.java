/**
 * VerfikasjonWSExp_VerifikasjonHttpServiceInformation.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf170819.19 v52708210711
 */

package nav_cons_deploy_verifikasjon;

public class VerfikasjonWSExp_VerifikasjonHttpServiceInformation implements com.ibm.ws.webservices.multiprotocol.ServiceInformation {

    private static java.util.Map operationDescriptions;
    private static java.util.Map typeMappings;

    static {
         initOperationDescriptions();
         initTypeMappings();
    }

    private static void initOperationDescriptions() { 
        operationDescriptions = new java.util.HashMap();

        java.util.Map inner0 = new java.util.HashMap();

        java.util.List list0 = new java.util.ArrayList();
        inner0.put("opCEI", list0);

        com.ibm.ws.webservices.engine.description.OperationDesc opCEI0Op = _opCEI0Op();
        list0.add(opCEI0Op);

        java.util.List list1 = new java.util.ArrayList();
        inner0.put("opFEM", list1);

        com.ibm.ws.webservices.engine.description.OperationDesc opFEM1Op = _opFEM1Op();
        list1.add(opFEM1Op);

        java.util.List list2 = new java.util.ArrayList();
        inner0.put("opSCA", list2);

        com.ibm.ws.webservices.engine.description.OperationDesc opSCA2Op = _opSCA2Op();
        list2.add(opSCA2Op);

        java.util.List list3 = new java.util.ArrayList();
        inner0.put("opWS", list3);

        com.ibm.ws.webservices.engine.description.OperationDesc opWS3Op = _opWS3Op();
        list3.add(opWS3Op);

        operationDescriptions.put("VerfikasjonWSExp_VerifikasjonHttpPort",inner0);
        operationDescriptions = java.util.Collections.unmodifiableMap(operationDescriptions);
    }

    private static com.ibm.ws.webservices.engine.description.OperationDesc _opCEI0Op() {
        com.ibm.ws.webservices.engine.description.OperationDesc opCEI0Op = null;
        com.ibm.ws.webservices.engine.description.ParameterDesc[]  _params0 = new com.ibm.ws.webservices.engine.description.ParameterDesc[] {
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "ASBOVerifikasjonReqCEI"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/asbo", "ASBOVerifikasjonReq"), nav_cons_deploy_verifikasjon.ASBOVerifikasjonReq.class, false, false, false, false, true, false), 
          };
        _params0[0].setOption("inputPosition","0");
        _params0[0].setOption("partQNameString","{http://nav-cons-deploy-verifikasjon/nav/no/deploy/asbo}ASBOVerifikasjonReq");
        _params0[0].setOption("partName","ASBOVerifikasjonReq");
        com.ibm.ws.webservices.engine.description.ParameterDesc  _returnDesc0 = new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "ASBOVerifikasjonResCEI"), com.ibm.ws.webservices.engine.description.ParameterDesc.OUT, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/asbo", "ASBOVerifikasjonRes"), nav_cons_deploy_verifikasjon.ASBOVerifikasjonRes.class, true, false, false, false, true, false); 
        _returnDesc0.setOption("outputPosition","0");
        _returnDesc0.setOption("partQNameString","{http://nav-cons-deploy-verifikasjon/nav/no/deploy/asbo}ASBOVerifikasjonRes");
        _returnDesc0.setOption("partName","ASBOVerifikasjonRes");
        com.ibm.ws.webservices.engine.description.FaultDesc[]  _faults0 = new com.ibm.ws.webservices.engine.description.FaultDesc[] {
         new com.ibm.ws.webservices.engine.description.FaultDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/fault", "FaultVerifikasjonGenerisk"), com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon", "opCEI_faultVerfikasjonGeneriskCEIMsg"), "nav_cons_deploy_verifikasjon.FaultVerifikasjonGenerisk", com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon", "FaultVerifikasjonGenerisk_element2"), com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/fault", "FaultVerifikasjonGenerisk")), 
          };
        opCEI0Op = new com.ibm.ws.webservices.engine.description.OperationDesc("opCEI", com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon", "opCEI"), _params0, _returnDesc0, _faults0, null);
        opCEI0Op.setOption("inputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon", "opCEIRequestMsg"));
        opCEI0Op.setOption("ServiceQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon/Binding", "VerfikasjonWSExp_VerifikasjonHttpService"));
        opCEI0Op.setOption("inputName","opCEIRequest");
        opCEI0Op.setOption("ResponseNamespace","http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon");
        opCEI0Op.setOption("buildNum","cf170819.19");
        opCEI0Op.setOption("outputName","opCEIResponse");
        opCEI0Op.setOption("ResponseLocalPart","opCEIResponse");
        opCEI0Op.setOption("targetNamespace","http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon/Binding");
        opCEI0Op.setOption("outputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon", "opCEIResponseMsg"));
        opCEI0Op.setOption("portTypeQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon", "Verifikasjon"));
        opCEI0Op.setStyle(com.ibm.ws.webservices.engine.enumtype.Style.WRAPPED);
        return opCEI0Op;

    }

    private static com.ibm.ws.webservices.engine.description.OperationDesc _opFEM1Op() {
        com.ibm.ws.webservices.engine.description.OperationDesc opFEM1Op = null;
        com.ibm.ws.webservices.engine.description.ParameterDesc[]  _params0 = new com.ibm.ws.webservices.engine.description.ParameterDesc[] {
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "ASBOVerifikasjonReqFEM"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/asbo", "ASBOVerifikasjonReq"), nav_cons_deploy_verifikasjon.ASBOVerifikasjonReq.class, false, false, false, false, true, false), 
          };
        _params0[0].setOption("inputPosition","0");
        _params0[0].setOption("partQNameString","{http://nav-cons-deploy-verifikasjon/nav/no/deploy/asbo}ASBOVerifikasjonReq");
        _params0[0].setOption("partName","ASBOVerifikasjonReq");
        com.ibm.ws.webservices.engine.description.ParameterDesc  _returnDesc0 = new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "ASBOVerifikasjonResFEM"), com.ibm.ws.webservices.engine.description.ParameterDesc.OUT, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/asbo", "ASBOVerifikasjonRes"), nav_cons_deploy_verifikasjon.ASBOVerifikasjonRes.class, true, false, false, false, true, false); 
        _returnDesc0.setOption("outputPosition","0");
        _returnDesc0.setOption("partQNameString","{http://nav-cons-deploy-verifikasjon/nav/no/deploy/asbo}ASBOVerifikasjonRes");
        _returnDesc0.setOption("partName","ASBOVerifikasjonRes");
        com.ibm.ws.webservices.engine.description.FaultDesc[]  _faults0 = new com.ibm.ws.webservices.engine.description.FaultDesc[] {
         new com.ibm.ws.webservices.engine.description.FaultDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/fault", "FaultVerifikasjonGenerisk"), com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon", "opFEM_faultVerfikasjonGeneriskFEMMsg"), "nav_cons_deploy_verifikasjon.FaultVerifikasjonGenerisk", com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon", "FaultVerifikasjonGenerisk_element4"), com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/fault", "FaultVerifikasjonGenerisk")), 
          };
        opFEM1Op = new com.ibm.ws.webservices.engine.description.OperationDesc("opFEM", com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon", "opFEM"), _params0, _returnDesc0, _faults0, null);
        opFEM1Op.setOption("inputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon", "opFEMRequestMsg"));
        opFEM1Op.setOption("ServiceQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon/Binding", "VerfikasjonWSExp_VerifikasjonHttpService"));
        opFEM1Op.setOption("inputName","opFEMRequest");
        opFEM1Op.setOption("ResponseNamespace","http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon");
        opFEM1Op.setOption("buildNum","cf170819.19");
        opFEM1Op.setOption("outputName","opFEMResponse");
        opFEM1Op.setOption("ResponseLocalPart","opFEMResponse");
        opFEM1Op.setOption("targetNamespace","http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon/Binding");
        opFEM1Op.setOption("outputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon", "opFEMResponseMsg"));
        opFEM1Op.setOption("portTypeQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon", "Verifikasjon"));
        opFEM1Op.setStyle(com.ibm.ws.webservices.engine.enumtype.Style.WRAPPED);
        return opFEM1Op;

    }

    private static com.ibm.ws.webservices.engine.description.OperationDesc _opSCA2Op() {
        com.ibm.ws.webservices.engine.description.OperationDesc opSCA2Op = null;
        com.ibm.ws.webservices.engine.description.ParameterDesc[]  _params0 = new com.ibm.ws.webservices.engine.description.ParameterDesc[] {
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "ASBOVerifikasjonReqSCA"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/asbo", "ASBOVerifikasjonReq"), nav_cons_deploy_verifikasjon.ASBOVerifikasjonReq.class, false, false, false, false, true, false), 
          };
        _params0[0].setOption("inputPosition","0");
        _params0[0].setOption("partQNameString","{http://nav-cons-deploy-verifikasjon/nav/no/deploy/asbo}ASBOVerifikasjonReq");
        _params0[0].setOption("partName","ASBOVerifikasjonReq");
        com.ibm.ws.webservices.engine.description.ParameterDesc  _returnDesc0 = new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "ASBOVerifikasjonReqSCA"), com.ibm.ws.webservices.engine.description.ParameterDesc.OUT, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/asbo", "ASBOVerifikasjonRes"), nav_cons_deploy_verifikasjon.ASBOVerifikasjonRes.class, true, false, false, false, true, false); 
        _returnDesc0.setOption("outputPosition","0");
        _returnDesc0.setOption("partQNameString","{http://nav-cons-deploy-verifikasjon/nav/no/deploy/asbo}ASBOVerifikasjonRes");
        _returnDesc0.setOption("partName","ASBOVerifikasjonRes");
        com.ibm.ws.webservices.engine.description.FaultDesc[]  _faults0 = new com.ibm.ws.webservices.engine.description.FaultDesc[] {
         new com.ibm.ws.webservices.engine.description.FaultDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/fault", "FaultVerifikasjonGenerisk"), com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon", "opSCA_faultVerfikasjonGeneriskSCAMsg"), "nav_cons_deploy_verifikasjon.FaultVerifikasjonGenerisk", com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon", "FaultVerifikasjonGenerisk_element3"), com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/fault", "FaultVerifikasjonGenerisk")), 
          };
        opSCA2Op = new com.ibm.ws.webservices.engine.description.OperationDesc("opSCA", com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon", "opSCA"), _params0, _returnDesc0, _faults0, null);
        opSCA2Op.setOption("inputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon", "opSCARequestMsg"));
        opSCA2Op.setOption("ServiceQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon/Binding", "VerfikasjonWSExp_VerifikasjonHttpService"));
        opSCA2Op.setOption("inputName","opSCARequest");
        opSCA2Op.setOption("ResponseNamespace","http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon");
        opSCA2Op.setOption("buildNum","cf170819.19");
        opSCA2Op.setOption("outputName","opSCAResponse");
        opSCA2Op.setOption("ResponseLocalPart","opSCAResponse");
        opSCA2Op.setOption("targetNamespace","http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon/Binding");
        opSCA2Op.setOption("outputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon", "opSCAResponseMsg"));
        opSCA2Op.setOption("portTypeQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon", "Verifikasjon"));
        opSCA2Op.setStyle(com.ibm.ws.webservices.engine.enumtype.Style.WRAPPED);
        return opSCA2Op;

    }

    private static com.ibm.ws.webservices.engine.description.OperationDesc _opWS3Op() {
        com.ibm.ws.webservices.engine.description.OperationDesc opWS3Op = null;
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
        opWS3Op = new com.ibm.ws.webservices.engine.description.OperationDesc("opWS", com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon", "opWS"), _params0, _returnDesc0, _faults0, null);
        opWS3Op.setOption("inputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon", "opWSRequestMsg"));
        opWS3Op.setOption("ServiceQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon/Binding", "VerfikasjonWSExp_VerifikasjonHttpService"));
        opWS3Op.setOption("inputName","opWSRequest");
        opWS3Op.setOption("ResponseNamespace","http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon");
        opWS3Op.setOption("buildNum","cf170819.19");
        opWS3Op.setOption("outputName","opWSResponse");
        opWS3Op.setOption("ResponseLocalPart","opWSResponse");
        opWS3Op.setOption("targetNamespace","http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon/Binding");
        opWS3Op.setOption("outputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon", "opWSResponseMsg"));
        opWS3Op.setOption("portTypeQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/inf/Verifikasjon", "Verifikasjon"));
        opWS3Op.setStyle(com.ibm.ws.webservices.engine.enumtype.Style.WRAPPED);
        return opWS3Op;

    }


    private static void initTypeMappings() {
        typeMappings = new java.util.HashMap();
        typeMappings.put(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/asbo", "ASBOVerifikasjonReq"),
                         nav_cons_deploy_verifikasjon.ASBOVerifikasjonReq.class);

        typeMappings.put(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/asbo", "ASBOVerifikasjonRes"),
                         nav_cons_deploy_verifikasjon.ASBOVerifikasjonRes.class);

        typeMappings.put(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/fault", "FaultVerifikasjonGenerisk"),
                         nav_cons_deploy_verifikasjon.FaultVerifikasjonGenerisk.class);

        typeMappings.put(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://nav-cons-deploy-verifikasjon/nav/no/deploy/fault", "FaultVerifikasjon"),
                         nav_cons_deploy_verifikasjon.FaultVerifikasjon.class);

        typeMappings = java.util.Collections.unmodifiableMap(typeMappings);
    }

    public java.util.Map getTypeMappings() {
        return typeMappings;
    }

    public Class getJavaType(javax.xml.namespace.QName xmlName) {
        return (Class) typeMappings.get(xmlName);
    }

    public java.util.Map getOperationDescriptions(String portName) {
        return (java.util.Map) operationDescriptions.get(portName);
    }

    public java.util.List getOperationDescriptions(String portName, String operationName) {
        java.util.Map map = (java.util.Map) operationDescriptions.get(portName);
        if (map != null) {
            return (java.util.List) map.get(operationName);
        }
        return null;
    }

}
