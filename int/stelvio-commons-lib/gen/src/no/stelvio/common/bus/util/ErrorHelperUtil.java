/*
 * ErrorHelperUtil.java Created on Dec 07, 2006 Author: persona2c5e3b49756 Schnell (test@example.com)
 * 
 * This is a utility class that provides pretty-print capability for SDOs,
 * useful for debug and trace statements in SCA/SDO code.
 */

package no.stelvio.common.bus.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import no.stelvio.common.exception.ErrorTypes;
import no.stelvio.common.exception.ServiceRuntimeExceptionToFaultConverter;

import com.ibm.websphere.sca.ServiceManager;
import com.ibm.websphere.sca.ServiceRuntimeException;
import com.ibm.websphere.sca.sdo.DataFactory;
import com.ibm.wsspi.sca.scdl.InterfaceType;
import com.ibm.wsspi.sca.scdl.OperationType;
import com.ibm.wsspi.sca.scdl.Reference;
import commonj.sdo.DataObject;

/**
 * <p>
 * This is a utility class that provides helper methods around common error
 * handling with WPS, useful for different purpose as convert
 * ServiceBusinessException or ServiceRuntimeExcpetion to faults.
 * </p>
 * 
 * @author persona2c5e3b49756 Schnell, test@example.com
 * @author Andre Rakv�g, test@example.com
 * @author test@example.com
 * 
 */
public class ErrorHelperUtil {
	/**
	 * Default date pattern used within integration
	 */
	public final static String DEFAULT_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

	/**
	 * <p>
	 * Provides current timestamp based on new Date with standard default format
	 * </p>
	 * 
	 * @return String
	 * @see ErrorHelperUtil#DEFAULT_DATE_PATTERN
	 */
	public static String getTimestamp() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
		return dateFormat.format(date);
	}

	/**
	 * 
	 * Method used to create a fault object.
	 * 
	 * @param e
	 *            the caught exception
	 * @param module
	 *            the name of the module that caught the exception
	 * @param faultBONamespace
	 *            the namespace of the fault object
	 * @param faultBOName
	 *            the name of the fault object
	 * @param errorType
	 *            indicates whether the fault is a ServiceBusinessException
	 *            (Business) or ServiceRuntimeException (Runtime)
	 * @param errorMessage
	 *            enables the user to provide an errorMessage in case an
	 *            exception is not available
	 * @param rootCause
	 *            enables the user to provide a rootCause in case an exception
	 *            is not available
	 * @return the fault business object
	 */
	private static DataObject getFaultBO(Exception e, String module, String faultBONamespace, String faultBOName,
			String errorType, String errorMessage, String rootCause) {
		DataObject faultBo = DataFactory.INSTANCE.create(faultBONamespace, faultBOName);
		faultBo.setString("errorSource", getSCAContext(module));
		faultBo.setString("errorType", errorType);
		faultBo.setString("dateTimeStamp", getTimestamp());
		if (errorMessage != null) {
			faultBo.setString("errorMessage", errorMessage);
		} else if (e != null) {
			faultBo.setString("errorMessage", e.getMessage());
		}
		if (rootCause != null) {
			faultBo.setString("rootCause", rootCause);
		} else if (e != null) {
			faultBo.setString("rootCause", getRootCause(e).toString());
		}
		return faultBo;
	}

	/**
	 * 
	 * Method used to create a fault object.
	 * 
	 * @param sre
	 *            the caught ServiceRuntimeException
	 * @param module
	 *            the name of the module that caught the exception
	 * @param faultBONamespace
	 *            the namespace of the fault object
	 * @param faultBOName
	 *            the name of the fault object
	 * @param errorType
	 *            indicates whether the fault is a ServiceBusinessException or
	 *            ServiceRuntimeException
	 * @param errorMessage
	 *            enables the user to provide an errorMessage in case a SRE is
	 *            not available
	 * @param rootCause
	 *            enables the user to provide a rootCause in case a SRE is not
	 *            available
	 * @return the fault business object
	 */
	public static DataObject getSAMFaultBO(Exception sre, String module, String faultBONamespace, String faultBOName,
			String errorType, String errorMessage, String rootCause) {

		DataObject faultBo = DataFactory.INSTANCE.create(faultBONamespace, faultBOName);

		faultBo.setString("errorSource", getSCAContext(module));
		faultBo.setString("errorType", errorType);
		faultBo.setDate("dateTimeStamp", new Date());
		if (errorMessage != null) {
			faultBo.setString("errorMessage", errorMessage);
		} else if (sre != null) {
			faultBo.setString("errorMessage", sre.getMessage());
		}

		if (rootCause != null) {
			faultBo.setString("rootCause", rootCause);
		} else if (sre != null) {
			faultBo.setString("rootCause", getRootCause(sre).toString());
		}

		return faultBo;
	}

	/**
	 * 
	 * Method used to create a fault object when a ServiceRuntimeException does
	 * exist.
	 * 
	 * @param sre
	 *            the caught ServiceRuntimeException
	 * @param module
	 *            the name of the module that caught the exception
	 * @param faultBONamespace
	 *            the namespace of the fault object
	 * @param faultBOName
	 *            the name of the fault object
	 * @return the fault business object
	 */
	public static DataObject getRuntimeFaultBO(ServiceRuntimeException sre, String module, String faultBONamespace,
			String faultBOName) {
		return getRuntimeFaultBO(sre, module, faultBONamespace, faultBOName, null, null);
	}

	/**
	 * 
	 * Method used to create a fault object when a ServiceRuntimeException does
	 * exist.
	 * 
	 * @param sre
	 *            the caught ServiceRuntimeException
	 * @param module
	 *            the name of the module that caught the exception
	 * @param faultBONamespace
	 *            the namespace of the fault object
	 * @param faultBOName
	 *            the name of the fault object
	 * @param errorMessage
	 *            enables the user to provide an errorMessage as there is no SRE
	 *            available
	 * @param rootCause
	 *            enables the user to provide an rootCause as there is no SRE
	 *            available
	 * @return the fault business object
	 */
	public static DataObject getRuntimeFaultBO(ServiceRuntimeException sre, String module, String faultBONamespace,
			String faultBOName, String errorMessage, String rootCause) {
		return ServiceRuntimeExceptionToFaultConverter.convert(sre, faultBONamespace, faultBOName);
	}

	/**
	 * Method used to create a fault object when an exception does exist.
	 * 
	 * @param e
	 *            the exception
	 * @param module
	 *            the name of the module that caught the exception
	 * @param faultBONamespace
	 *            the namespace of the fault object
	 * @param faultBOName
	 *            the name of the fault object
	 * @return the fault business object
	 */
	public static DataObject getBusinessFaultBO(Exception e, String module, String faultBONamespace, String faultBOName) {
		return getFaultBO(e, module, faultBONamespace, faultBOName, ErrorTypes.BUSINESS, null, null);
	}

	/**
	 * 
	 * Method used to create a fault object when an exception does not exist.
	 * 
	 * @param module
	 *            the name of the module that creates the fault
	 * @param faultBONamespace
	 *            the namespace of the fault object
	 * @param faultBOName
	 *            the name of the fault object
	 * @param errorMessage
	 *            enables the user to provide an errorMessage as there is no SRE
	 *            available
	 * @param rootCause
	 *            enables the user to provide an rootCause as there is no SRE
	 *            available
	 * @return the fault business object
	 */
	public static DataObject getBusinessFaultBO(String module, String faultBONamespace, String faultBOName,
			String errorMessage, String rootCause) {
		return getFaultBO(null, module, faultBONamespace, faultBOName, ErrorTypes.BUSINESS, errorMessage, rootCause);
	}

	/**
	 * @param sre
	 * @return returns the lowest Throwable in the hierarchy
	 */
	public static Throwable getRootCause(Exception sre) {
		boolean done = false;
		Throwable e = sre;
		while (!done) {
			if (e.getCause() != null)
				e = e.getCause();
			else
				done = true;
		}
		return e;
	}

	/**
	 * 
	 * Provides the SCAContext as a string reprensentation of a component
	 * 
	 * @param module
	 *            the name of the module to get the context information from
	 * @return the context information of the specified module
	 */
	public static String getSCAContext(String module) {

		String SCAContext = null;

		// MOD
		String moduleName = "MODULE: " + module;

		// COMP
		com.ibm.websphere.sca.scdl.Component component = ServiceManager.INSTANCE.getComponent();
		String componentName = "COMPONENT: " + component.getName();

		// IF
		InterfaceType ifType = (InterfaceType) component.getInterfaceTypes().get(0);
		OperationType opType = (OperationType) ifType.getOperationTypes().get(0);
		String componentInterface = "IF(OP): " + ifType.getName().substring(10) + "(" + opType.getName() + ")";

		// REF
		Reference ref = (Reference) component.getReferences().get(0);
		ifType = (InterfaceType) ref.getInterfaceTypes().get(0);
		opType = (OperationType) ifType.getOperationTypes().get(0);
		String componentReference = "REF: " + ref.getName() + " IF(OP): " + ifType.getName().substring(10) + "("
				+ opType.getName() + ")";

		SCAContext = moduleName + " / " + componentName + " / " + componentInterface + " / " + componentReference;

		return SCAContext;
	}

	/**
	 * Convert SRE exception stacktrace to string
	 * 
	 * @param sre
	 *            the ServiceRuntimeException to convert
	 * @return the string representation of the ServiceRunTimeException
	 */
	public static String getSRETrace(ServiceRuntimeException sre) {
		return convertSBEStackTrace(sre);
	}

	/**
	 * <p>
	 * Convert exception stacktrace to string
	 * </p>
	 * 
	 * @param e
	 *            The exception
	 * 
	 * @return String
	 * 
	 */
	public static String convertSBEStackTrace(Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}
}
