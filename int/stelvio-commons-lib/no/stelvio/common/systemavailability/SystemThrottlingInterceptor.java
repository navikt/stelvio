package no.stelvio.common.systemavailability;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;

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

		// Pr�v � sl� opp JNDI binding variabel maxSimultaneousInvocations for systemet
		try {
			InitialContext ctx = new InitialContext();
			
			maxSimultaneousInvocations = Integer.parseInt((String) ctx.lookup("cell/persistent/binding/stelvio-commons-lib/maxSimultaneousInvocations_" + systemName));
			System.out.println("Getting JNDI binding variable for " + systemName + ", retrieved value " + maxSimultaneousInvocations);
		}  catch (Exception e) {
			Logger.getAnonymousLogger().logp(Level.SEVERE, systemName, "Get configuration values", "JNDI-resouce for maxSimultaneousInvocations is not set up on BPM! Using hard coded values from SystemAvailability Java code");
		}

		// Pr�v � sl� opp JNDI binding variabel defaultMaxSimultaneoutInvocations
		try {
			InitialContext ctx = new InitialContext();
			
			maxSimultaneousInvocations = Integer.parseInt((String) ctx.lookup("cell/persistent/binding/stelvio-commons-lib/defaultMaxSimultaneousInvocations"));
			System.out.println("Getting defaultMaxSimultaneous JNDI binding variable, retrieved value " + maxSimultaneousInvocations);
		}  catch (Exception e) {
			Logger.getAnonymousLogger().logp(Level.SEVERE, systemName, "Get configuration values", "JNDI-resouce for maxSimultaneousInvocations is not set up on BPM! Using hard coded values from SystemAvailability Java code");
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