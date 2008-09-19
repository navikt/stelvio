package no.nav.bpchelper.adapters;

import com.ibm.bpe.clientmodel.BFMConnection;

public class BFMConnectionAdapter {
    private BFMConnection adaptee;

    private BFMConnectionAdapter(BFMConnection adaptee) {
	this.adaptee = adaptee;
    }

    public BFMConnection getAdaptee() {
	return adaptee;
    }

    public static BFMConnectionAdapter getInstance() {
	BFMConnection bfmConnection = new BFMConnection();
	bfmConnection.setRemote(Boolean.TRUE.toString());
	bfmConnection.setJndiName("java:comp/env/ejb/BusinessFlowManagerHome");
	return new BFMConnectionAdapter(bfmConnection);
    }

    public BusinessFlowManagerServiceAdapter getBusinessFlowManagerService() {
	try {
	    return new BusinessFlowManagerServiceAdapter(adaptee.getBusinessFlowManagerService());
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }
}
