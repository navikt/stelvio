package no.stelvio.common.interceptor;

import com.ibm.websphere.sca.Service;
import com.ibm.websphere.sca.scdl.Component;

public class InterceptorServiceContext {
	private final Component component;
	private final Service partnerService;

	public InterceptorServiceContext(Component component, Service partnerService) {
		this.component = component;
		this.partnerService = partnerService;
	}

	public Component getComponent() {
		return component;
	}

	public Service getPartnerService() {
		return partnerService;
	}
}
