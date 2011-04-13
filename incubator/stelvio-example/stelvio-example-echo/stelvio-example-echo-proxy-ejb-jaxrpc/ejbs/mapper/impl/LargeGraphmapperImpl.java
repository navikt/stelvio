package ejbs.mapper.impl;

public class LargeGraphmapperImpl {

public LargeGraphmapperImpl() {
	super();
}

public ejbs.LargeGraph locateService_LargeGraphPartner() {
	return (ejbs.LargeGraph) com.ibm.websphere.sca.ServiceManager.INSTANCE.locateService("LargeGraphPartner");
}

public commonj.sdo.DataObject echo(java.lang.String input) {
	try {
		ejbs.LargeGraph partner = locateService_LargeGraphPartner();
		ejbs.LGResponse java_result;
		java_result = partner.echo(input);
		commonj.sdo.DataObject result = null;
		result = javatodata_echo_output(java_result, new javax.xml.namespace.QName("http://ejbs","LGResponse"));
		return result;
	} catch (com.ibm.websphere.sca.ServiceRuntimeException sre) {
		throw sre;
	} catch (com.ibm.websphere.sca.ServiceBusinessException java_com_ibm_websphere_sca_ServiceBusinessException) {
		commonj.sdo.DataObject result = javatodata_echo_fault(java_com_ibm_websphere_sca_ServiceBusinessException.getData(), new javax.xml.namespace.QName("http://ejbs","echoResponse"));
		throw new com.ibm.websphere.sca.ServiceBusinessException(result);
	} catch (com.ibm.wbiserver.mediation.jtow.MediationException mediationException) {
		throw new com.ibm.websphere.sca.ServiceRuntimeException(mediationException);
	} catch (java.lang.Exception java_java_lang_Exception) {
		commonj.sdo.DataObject result = javatodata_echo_fault(java_java_lang_Exception, new javax.xml.namespace.QName("http://ejbs","echoResponse"));
		throw new com.ibm.websphere.sca.ServiceBusinessException(result);
	}
}

public void onEchoResponse(com.ibm.websphere.sca.Ticket __ticket, commonj.sdo.DataObject returnValue, java.lang.Exception exception) {
	//TODO Needs to be implemented.
}

private commonj.sdo.DataObject javatodata_echo_output(java.lang.Object result, javax.xml.namespace.QName primitiveNameSpace) {
	// User can override this code to do custom mapping.
	// Just comment out the existing code and write your custom code.
	return com.ibm.wbiserver.mediation.jtow.SDOJavaObjectMediator.java2Data(result, primitiveNameSpace);
}

private commonj.sdo.DataObject javatodata_echo_fault(java.lang.Object result, javax.xml.namespace.QName primitiveNameSpace) {
	// User can override this code to do custom mapping.
	// Just comment out the existing code and write your custom code.
	return com.ibm.wbiserver.mediation.jtow.SDOJavaObjectMediator.java2Data(result, primitiveNameSpace);
}
}