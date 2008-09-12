package no.nav.bpchelper.service;

import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

import com.ibm.bpe.api.BusinessFlowManager;
import com.ibm.bpe.api.BusinessFlowManagerHome;

public class ServiceFactory {
	public static BusinessFlowManagerService getBusinessFlowManagerService() {
		try {
			InitialContext ctx= new InitialContext();
			Object result=ctx.lookup("java:comp/env/ejb/BusinessFlowManagerHome");
			BusinessFlowManagerHome processHome=(BusinessFlowManagerHome) PortableRemoteObject.narrow(result, BusinessFlowManagerHome.class);
			BusinessFlowManager businessFlowManager = processHome.create();
			 return new BusinessFlowManagerServiceImpl(businessFlowManager);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
