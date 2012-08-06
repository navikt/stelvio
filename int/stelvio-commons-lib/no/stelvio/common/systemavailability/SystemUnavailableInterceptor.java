package no.stelvio.common.systemavailability;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;
import java.util.logging.Level;

import no.stelvio.common.interceptor.GenericInterceptor;
import no.stelvio.common.interceptor.InterceptorChain;
import no.stelvio.common.util.IOUtils;

import com.ibm.websphere.sca.ServiceRuntimeException;
import com.ibm.websphere.sca.ServiceUnavailableException;
import com.ibm.websphere.sca.scdl.OperationType;

/**
 * Interceptor that analyzes runtime exceptions thrown by the intercepting call. Criteria can be defined that defines the system
 * as unavailable if criteria are met.
 * 
 * @author person73874c7d71f8
 * @author test@example.com
 */
public class SystemUnavailableInterceptor extends GenericInterceptor {
	private static final String PROPERTIES_FILENAME = "SystemUnavailableExceptionList.properties";

	private final String systemName;
	private final Set<String> systemUnavailableExceptionClassNames = new HashSet<String>();
	private final Set<String> systemUnavailableExceptionSubstrings = new HashSet<String>();

	public SystemUnavailableInterceptor(String systemName) {
		this.systemName = systemName;

		Properties serviceUnavailableExceptionProperties = getServiceUnavailableProperties();
		if (serviceUnavailableExceptionProperties.isEmpty()) {
			String logMessage = systemName + ": No service unavailable properties found. Disabling interceptor";
			logger.logp(Level.INFO, className, "SystemUnavailableInterceptor", logMessage);
			setEnabled(false);
		} else {
			for (Entry<Object, Object> entry : serviceUnavailableExceptionProperties.entrySet()) {
				String propertyName = (String) entry.getKey();
				String propertyValue = (String) entry.getValue();
				if (propertyName.startsWith("EXCEPTION")) {
					systemUnavailableExceptionClassNames.add(propertyValue);
				} else if (propertyName.startsWith("SUBSTRING")) {
					systemUnavailableExceptionSubstrings.add(propertyValue);
				} else {
					String logMessage = systemName + ": Ignoring property with name " + propertyName
							+ ". Prefix is currently unsupported.";
					logger.logp(Level.WARNING, className, "SystemUnavailableInterceptor", logMessage);
				}
			}
		}
	}

	private Properties getServiceUnavailableProperties() {
		Properties serviceUnavailableExceptionProperties = new Properties();
		InputStream is = null;
		try {
			is = getClass().getResourceAsStream(PROPERTIES_FILENAME);
			if (is != null) {
				serviceUnavailableExceptionProperties.load(is);
				return serviceUnavailableExceptionProperties;
			} else {
				String logMessage = systemName + ": " + PROPERTIES_FILENAME
						+ " not found in classpath. Will continue with no automatic resubmit flagging for this component.";
				logger.logp(Level.WARNING, className, "getServiceUnavailableProperties", logMessage);
			}
		} catch (IOException ioe) {
			String logMessage = systemName + ": Problems reading " + PROPERTIES_FILENAME
					+ " from classpath. Will continue with no automatic resubmit flagging for this component.";
			logger.logp(Level.WARNING, className, "getServiceUnavailableProperties", logMessage, ioe);
		} finally{
			IOUtils.closeQuietly(is);
		}
		return serviceUnavailableExceptionProperties;
	}

	@Override
	protected Object doInterceptInternal(OperationType operationType, Object input, InterceptorChain interceptorChain) {
		try {
			return interceptorChain.doIntercept(operationType, input);
		} catch (ServiceRuntimeException sre) {
			// Check if exception already is SUE
			if (isServiceUnavailableException(sre)) {
				throw sre;
			}

			// Check if exception (root or nested) class name is flagged as SUE
			String systemUnavailableExceptionClassName = findMathcingSystemUnavailableExceptionClassName(sre);
			if (systemUnavailableExceptionClassName != null) {
				throw new ServiceUnavailableException("System " + systemName + " received an exception: " + sre.getMessage()
						+ ". This was automatically flagged as a temporary unavailability due to the presence of the token '"
						+ systemUnavailableExceptionClassName + "' :", sre);
			}

			// Check if exception matches any substrings flagged as SUE
			String systemUnavailableSubstring = findMathcingSystemUnavailableSubstring(sre);
			if (systemUnavailableSubstring != null) {
				throw new ServiceUnavailableException("System " + systemName + " received an exception: " + sre.getMessage()
						+ ". This was automatically flagged as a temporary unavailability due to the presence of the token '"
						+ systemUnavailableSubstring + "' :", sre);
			}

			// No reason to flag exception as temporary unavailability - rethrow
			throw sre;
		}
	}

	/**
	 * Method that recursively checks if a chain of nested exceptions contains (one or more) ServiceUnavailableException(s).
	 * 
	 * @param t
	 * @return
	 */
	private boolean isServiceUnavailableException(Throwable t) {
		if (t instanceof ServiceUnavailableException) {
			return true;
		} else {
			Throwable cause = t.getCause();
			if (cause != null) {
				// Recursive call
				return isServiceUnavailableException(cause);
			} else {
				return false;
			}
		}
	}

	private String findMathcingSystemUnavailableExceptionClassName(Throwable t) {
		if (!systemUnavailableExceptionClassNames.isEmpty()) {
			String className = t.getClass().getName();
			if (systemUnavailableExceptionClassNames.contains(className)) {
				return className;
			} else {
				Throwable cause = t.getCause();
				if (cause != null) {
					// Recursive call
					return findMathcingSystemUnavailableExceptionClassName(cause);
				}
			}
		}
		return null;
	}

	private String findMathcingSystemUnavailableSubstring(Throwable t) {
		if (!systemUnavailableExceptionSubstrings.isEmpty()) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw, true);
			t.printStackTrace(pw);
			String stackTrace = sw.toString();
			for (String substring : systemUnavailableExceptionSubstrings) {
				if (stackTrace.indexOf(substring) != -1) {
					return substring;
				}
			}
		}
		return null;
	}
}
