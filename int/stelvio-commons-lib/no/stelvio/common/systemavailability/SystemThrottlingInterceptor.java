package no.stelvio.common.systemavailability;

import java.util.HashMap;
import java.util.Map;

import no.stelvio.common.interceptor.GenericInterceptor;
import no.stelvio.common.interceptor.InterceptorChain;

import com.ibm.websphere.sca.ServiceUnavailableException;
import com.ibm.websphere.sca.scdl.OperationType;

public class SystemThrottlingInterceptor extends GenericInterceptor {
	private static Map<String, Integer> currentExecutionMap = new HashMap<String, Integer>();

	private String systemName;

	public SystemThrottlingInterceptor(String systemName) {
		this.systemName = systemName;
	}

	@Override
	protected Object doInterceptInternal(OperationType operationType, Object input, InterceptorChain interceptorChain) {
		SystemAvailabilityStorage storage = new SystemAvailabilityStorage();
		AvailabilityRecord availRecord = storage.getAvailabilityRecord(systemName);
		int maxSimultaneousInvocations = AvailabilityRecord.DEFAULT_MAX_SIMULTANEOUS_INVOCATIONS;
		if (availRecord != null && availRecord.maxSimultaneousInvocations > 0) {
			maxSimultaneousInvocations = availRecord.maxSimultaneousInvocations;
		}
		synchronized (currentExecutionMap) {
			Integer currentNumber = currentExecutionMap.get(systemName);
			if (currentNumber == null) {
				currentNumber = 0;
				currentExecutionMap.put(systemName, 0);
			}
			if (currentNumber >= maxSimultaneousInvocations) {
				throw new ServiceUnavailableException("System " + systemName
						+ " is currently considered unavailable. A total of " + currentNumber
						+ " clients are currently waiting for results from this component");
			} else {
				currentExecutionMap.put(systemName, currentNumber + 1);
			}
		}
		try {
			return interceptorChain.doIntercept(operationType, input);
		} finally {
			synchronized (currentExecutionMap) {
				Integer currentNumber = currentExecutionMap.get(systemName);
				currentExecutionMap.put(systemName, currentNumber - 1);
			}
		}

	}

}