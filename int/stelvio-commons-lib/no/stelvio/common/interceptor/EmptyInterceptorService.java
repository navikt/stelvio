package no.stelvio.common.interceptor;

import java.util.Collection;
import java.util.Collections;

import com.ibm.websphere.sca.ServiceBusinessException;
import com.ibm.websphere.sca.ServiceCallback;
import com.ibm.websphere.sca.Ticket;
import com.ibm.websphere.sca.scdl.OperationType;

public class EmptyInterceptorService extends InterceptorService {
	@Override
	protected Collection<? extends Interceptor> buildInterceptors(InterceptorServiceContext context) {
		return Collections.emptyList();
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
