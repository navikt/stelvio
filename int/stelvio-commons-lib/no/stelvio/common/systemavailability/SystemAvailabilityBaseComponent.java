package no.stelvio.common.systemavailability;

import java.util.Arrays;
import java.util.Collection;

import no.stelvio.common.exception.CatchServiceRuntimeExceptionInterceptor;
import no.stelvio.common.interceptor.Interceptor;
import no.stelvio.common.interceptor.InterceptorService;
import no.stelvio.common.interceptor.InterceptorServiceContext;

import com.ibm.websphere.sca.ServiceBusinessException;
import com.ibm.websphere.sca.ServiceCallback;
import com.ibm.websphere.sca.Ticket;
import com.ibm.websphere.sca.scdl.OperationType;

/**
 * @author person73874c7d71f8
 * 
 */
public class SystemAvailabilityBaseComponent extends InterceptorService {
	@Override
	protected Collection<? extends Interceptor> buildInterceptors(InterceptorServiceContext context) {
		String componentName = context.getComponent().getName();
		if (componentName.indexOf("Avail") == -1) {
			throw new RuntimeException(
					"AvailabilityCheck: Unable to extract the backend system name from the component name \"" + componentName
							+ "\". Make sure that the component name is on the form \"<SystemName>AvailabilityCheck\"");
		}
		String systemName = componentName.substring(0, componentName.indexOf("Avail"));
		return Arrays.asList(new CatchServiceRuntimeExceptionInterceptor(), new SystemThrottlingInterceptor(systemName),
				new SystemStubbingInterceptor(systemName), new SystemUnavailableInterceptor(systemName));
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
