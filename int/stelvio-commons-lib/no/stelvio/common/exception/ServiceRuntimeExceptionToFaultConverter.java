package no.stelvio.common.exception;

import java.util.Date;
import java.util.List;

import com.ibm.websphere.sca.ServiceRuntimeException;
import com.ibm.websphere.sca.sdo.DataFactory;
import commonj.sdo.DataObject;

/**
 * Simple utility class that converts ServiceRuntimeException to fault business object. This class is typically used in the
 * consumer integration layer to avoid throwing runtime exception out of the integration platform.
 * 
 * @author test@example.com
 */
public class ServiceRuntimeExceptionToFaultConverter {
	private ServiceRuntimeExceptionToFaultConverter() {
	}

	public static DataObject convert(ServiceRuntimeException sre, String faultBONamespace, String faultBOName) {
		DataObject faultBo = DataFactory.INSTANCE.create(faultBONamespace, faultBOName);
		faultBo.setString("errorSource", buildErrorSource(sre));
		faultBo.setString("errorType", ErrorTypes.RUNTIME);
		faultBo.setDate("dateTimeStamp", new Date());
		faultBo.setString("errorMessage", buildErrorMessage(sre));
		faultBo.setString("rootCause", buildRootCause(sre));
		return faultBo;
	}

	private static String buildErrorSource(ServiceRuntimeException sre) {
		List serviceContext = sre.getServiceContext();
		if (serviceContext.isEmpty()) {
			return "";
		} else {
			return serviceContext.get(0).toString();
		}
	}

	private static String buildErrorMessage(ServiceRuntimeException sre) {
		Throwable cause = sre.getCause();
		if (cause != null) {
			if (cause instanceof ServiceRuntimeException) {
				return buildErrorMessage((ServiceRuntimeException) cause);
			} else if (cause instanceof RuntimeFault) {
				ThrowableInfo throwableInfo = ((RuntimeFault) cause).getExceptions()[0];
				return throwableInfo.toString();
			} else {
				return cause.toString();
			}
		} else {
			return sre.toString();
		}
	}

	private static String buildRootCause(ServiceRuntimeException sre) {
		Throwable rootCause = getRootCause(sre);
		if (rootCause instanceof RuntimeFault) {
			ThrowableInfo[] exceptions = ((RuntimeFault) rootCause).getExceptions();
			return exceptions[exceptions.length - 1].toString();
		} else {
			return rootCause.toString();
		}
	}

	private static Throwable getRootCause(Throwable t) {
		Throwable cause = t.getCause();
		if (cause != null) {
			return getRootCause(cause);
		} else {
			return t;
		}
	}
}
