package no.stelvio.common.context;

import java.util.Collection;
import java.util.Collections;

import no.stelvio.common.bus.util.StelvioContextHelper;
import no.stelvio.common.interceptor.Interceptor;
import no.stelvio.common.interceptor.InterceptorChain;
import no.stelvio.common.interceptor.InterceptorService;
import no.stelvio.common.interceptor.InterceptorServiceContext;

import com.ibm.websphere.sca.ServiceBusinessException;
import com.ibm.websphere.sca.ServiceCallback;
import com.ibm.websphere.sca.Ticket;
import com.ibm.websphere.sca.scdl.OperationType;
import commonj.sdo.DataObject;

public class SetRequestContextComponent extends InterceptorService implements Interceptor {
	@Override
	public Object doIntercept(OperationType operationType, Object input, InterceptorChain interceptorChain) {
		DataObject inputDataObject = (DataObject) input;
		DataObject serviceRequest = inputDataObject.getDataObject("arg0");
		// TODO: Consider to check if requestContext already set
		DataObject requestContext = serviceRequest.createDataObject("requestContextDto");
		StelvioContext stelvioContext = new StelvioContextHelper().getContext();
		// TODO: Review mapping of values - is this correct and sufficient?
		requestContext.set("componentId", stelvioContext.getApplicationId());
		requestContext.set("moduleId", stelvioContext.getApplicationId());
		requestContext.set("transactionId", stelvioContext.getCorrelationId());
		requestContext.set("userId", stelvioContext.getUserId());
		return interceptorChain.doIntercept(operationType, input);
	}

	@Override
	protected Collection<? extends Interceptor> buildInterceptors(InterceptorServiceContext context) {
		return Collections.singletonList(this);
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