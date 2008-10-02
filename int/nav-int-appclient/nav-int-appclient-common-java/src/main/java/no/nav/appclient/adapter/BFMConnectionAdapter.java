package no.nav.appclient.adapter;

import java.util.Properties;


import com.ibm.bpe.clientmodel.BFMConnection;

public class BFMConnectionAdapter {
	private BFMConnection adaptee;

	private BusinessFlowManagerServiceAdapter businessFlowManagerService;

	private BFMConnectionAdapter(BFMConnection adaptee) {
		this.adaptee = adaptee;
	}

	public BFMConnection getAdaptee() {
		return adaptee;
	}

	public static BFMConnectionAdapter getInstance(Properties properties) {
		BFMConnection bfmConnection = new SecuredBFMConnection(properties);
		bfmConnection.setRemote(Boolean.TRUE.toString());
		bfmConnection.setJndiName("java:comp/env/ejb/BusinessFlowManagerHome");
		BFMConnectionAdapter adapter = new BFMConnectionAdapter(bfmConnection);
		return adapter;
	}

	public BusinessFlowManagerServiceAdapter getBusinessFlowManagerService() {
		if (businessFlowManagerService == null) {
			try {
				businessFlowManagerService = new BusinessFlowManagerServiceAdapter(adaptee.getBusinessFlowManagerService());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return businessFlowManagerService;
	}
}
