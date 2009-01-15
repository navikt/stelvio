package no.stelvio.common.systemavailability;

import java.util.Collection;
import java.util.Collections;

import no.stelvio.common.interceptor.Interceptor;
import no.stelvio.common.interceptor.InterceptorService;
import no.stelvio.common.interceptor.InterceptorServiceContext;

import com.ibm.websphere.sca.ServiceBusinessException;
import com.ibm.websphere.sca.ServiceCallback;
import com.ibm.websphere.sca.Ticket;
import com.ibm.websphere.sca.scdl.OperationType;

/**
 * @author test@example.com
 */
public class SystemStubbingComponent extends InterceptorService {
	@Override
	protected Collection<? extends Interceptor> buildInterceptors(InterceptorServiceContext context) {
		String componentName = context.getComponent().getName();
		if (componentName.indexOf("Stubbing") == -1) {
			throw new RuntimeException("SystemStubbing: Unable to extract the backend system name from the component name \""
					+ componentName + "\". Make sure that the component name is on the form \"<SystemName>Stubbing\"");
		}
		String systemName = componentName.substring(0, componentName.indexOf("Stubbing"));
		return Collections.singletonList(new SystemStubbingInterceptor(systemName));
	}

	@Override
	public Object invoke(OperationType operationType, Object input) throws ServiceBusinessException {
		return super.invoke(operationType, input);
	}

	@Override
	public void invokeAsync(OperationType operationType, Object input, ServiceCallback callback, Ticket ticket) {
		super.invokeAsync(operationType, input, callback, ticket);
	}

	@Override
	public void onInvokeResponse(Ticket ticket, Object output, Exception exception) {
		super.onInvokeResponse(ticket, output, exception);
	}
}
