package no.stelvio.common.interceptor;

import java.util.Collection;
import java.util.List;

import com.ibm.websphere.sca.Service;
import com.ibm.websphere.sca.ServiceBusinessException;
import com.ibm.websphere.sca.ServiceCallback;
import com.ibm.websphere.sca.ServiceImplAsync;
import com.ibm.websphere.sca.ServiceImplSync;
import com.ibm.websphere.sca.ServiceManager;
import com.ibm.websphere.sca.Ticket;
import com.ibm.websphere.sca.scdl.Component;
import com.ibm.websphere.sca.scdl.OperationType;
import com.ibm.websphere.sca.scdl.Reference;

/**
 * Base class that can be extended to implement an interceptor service (a dynamic service implementation purely based on
 * interceptors).
 * 
 * @author test@example.com
 */
public abstract class InterceptorService implements ServiceImplSync, ServiceImplAsync, ServiceCallback {
	private final Collection<? extends Interceptor> interceptors;
	private final Service partnerService;

	public InterceptorService() {
		InterceptorServiceContext context = buildContext();
		partnerService = context.getPartnerService();
		interceptors = buildInterceptors(context);
		if (interceptors == null) {
			throw new RuntimeException(context.getComponent().getName() + ": interceptors cannot be null");
		}
	}

	/**
	 * Template method that builds the interceptors that applies to this service.
	 * 
	 * @param context
	 * @return interceptors that apply to this service (never <code>null</code>)
	 */
	protected abstract Collection<? extends Interceptor> buildInterceptors(InterceptorServiceContext context);

	private static InterceptorServiceContext buildContext() {
		ServiceManager serviceManager = ServiceManager.INSTANCE;

		Component component = serviceManager.getComponent();

		Service partnerService = null;
		List references = component.getReferences();
		if (references.size() != 2) {
			throw new RuntimeException("Component " + component.getName()
					+ " should have 1 reference (in addition to self). Actual: " + references.size());
		}
		for (Object ref : references) {
			Reference reference = (Reference) ref;
			if (!reference.getName().equals("self")) {
				partnerService = (Service) serviceManager.locateService(reference);
				break;
			}
		}
		return new InterceptorServiceContext(component, partnerService);
	}

	public Object invoke(OperationType operationType, Object input) throws ServiceBusinessException {
		return new InterceptorChainImpl(interceptors, partnerService).doIntercept(operationType, input);
	}

	public void invokeAsync(OperationType operationType, Object input, ServiceCallback callback, Ticket ticket) {
		throw new UnsupportedOperationException("Async invokation is currently unsupported");
	}

	public void onInvokeResponse(Ticket ticket, Object output, Exception exception) {
		throw new UnsupportedOperationException("Async invokation is currently unsupported");
	}
}
