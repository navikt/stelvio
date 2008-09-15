package no.nav.bpchelper.service;

import com.ibm.bpe.clientmodel.BFMConnection;

public class ServiceFactory {
	public static BusinessFlowManagerService getBusinessFlowManagerService() {
		try {
			BFMConnection bfmConnection = new BFMConnection();
			bfmConnection.setRemote(Boolean.TRUE.toString());
			bfmConnection.setJndiName("java:comp/env/ejb/BusinessFlowManagerHome");
			return new BusinessFlowManagerServiceAdapter(bfmConnection.getBusinessFlowManagerService());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
