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

import com.ibm.websphere.sca.ServiceManager;
import com.ibm.websphere.sca.ServiceRuntimeException;
import com.ibm.websphere.sca.sdo.DataFactory;
import com.ibm.wsspi.sca.scdl.InterfaceType;
import com.ibm.wsspi.sca.scdl.OperationType;
import com.ibm.wsspi.sca.scdl.Reference;
import commonj.sdo.DataObject;


/*
 * <p> This is a utility class that provides helper methods around common error handling with WPS, useful
 * for different purpose as convert ServiceBusinessException or ServiceRuntimeExcpetion to string 
 * </p>
 * 
 * @usage 
 * <p>
 * 
 * </p>
 * 
 * @author persona2c5e3b49756 Schnell, test@example.com, and Andre Rakvåg, test@example.com.
 *  
 */
public class ErrorHelperUtil {

	/**
	 * Default date pattern used within integration
	 */
	private final static String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
	private final static String FAULT_NAMESPACE = "http://nav-lib-cons-pen-psakpselv/no/nav/lib/pen/psakpselv/fault";
	private final static String FAULT_ASBO_NAME = "FaultPenGenerisk";
	
	
	/**
	 * <p>
	 * provides current timestamp based on new Date with standard default format yyyy-MM-dd HH:mm:ss
	 * </p>
	 * 
	 * @param 
	 *        
	 * 
	 * @return 
	 * 			  String
	 * 
	 */
	public static String getTimestamp()
	{
		Date date = new Date();
		SimpleDateFormat formater = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
		return formater.format(date); 
	}
	
	/**
	 * <p>
	 * provides current timestamp based on new Date with custom input format pattern
	 * </p>
	 * 
	 * @param sbe
	 *            valid java DateTime pattern as yyy-MM-dd
	 * 
	 * @return 
	 * 			  String
	 * 
	 */
	public static String getTimestamp(String datePattern)
	{
		Date date = null;
		SimpleDateFormat formater = null;
		
		try {
			date = new Date();
			formater = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
			return formater.format(date); 
		} catch (Exception e) {
			date = new Date();
			formater = new SimpleDateFormat(datePattern);
			return formater.format(date); 
		}
		
	}
	
	/**
	 * 
	 * Method used to create a fault object. 
	 * 
	 * @param sre					the catched ServiceRuntimeException
	 * @param module				the name of the module that initiated the fault generation
	 * @param faultBONamespace		the namespace of the fault object 
	 * @param faultBOName			the name of the fault object 
	 * @param errorType				indicates whether the fault is a ServiceBusinessException or ServiceRuntimeException
	 * @param errorMessage			enables the user to provide an errorMessage in case a SRE is not available
	 * @param rootCause				enables the user to provide a rootCause in case a SRE is not available
	 * @return						the fault business object created from the ServiceRuntimeException
	 */
	private static DataObject getFaultBO(Exception sre, String module, String faultBONamespace, String faultBOName, String errorType, String errorMessage, String rootCause) {
		
		DataObject faultBo = DataFactory.INSTANCE.create(faultBONamespace, faultBOName);
		String datePattern = "yyyy-MM-dd HH:mm:ss";
		Date date = new Date();
		SimpleDateFormat formater = new SimpleDateFormat(datePattern);
		
		faultBo.setString("errorSource", getSCAContext(module));
		faultBo.setString("errorType", errorType);
		faultBo.setString("dateTimeStamp", formater.format(date));
		if(errorMessage!=null)
		{
			faultBo.setString("errorMessage", errorMessage);
		}
		else if (sre!=null)
		{
			faultBo.setString("errorMessage", sre.getMessage());
		}
		
		if(rootCause!=null)
		{
			faultBo.setString("rootCause", rootCause);
		}
		else if(sre!=null)
		{
			faultBo.setString("rootCause", getRootCause(sre).toString());
		}
		

		return faultBo;
	}
	
	/**
	 * 
	 * Method used to create a fault object when a ServiceRuntimeException does exist. 
	 * 
	 * @param sre					the ServiceRuntimeException
	 * @param module				the name of the module that caught the sre
	 * @return						the fault business object created
	 */
	public static DataObject getRuntimeFaultBO(ServiceRuntimeException sre, String module) {
		
		DataObject fault = getFaultBO(sre, module, FAULT_NAMESPACE, FAULT_ASBO_NAME, "Runtime", null, null);
		return fault;
	}
	
	/**
	 * 
	 * Method used to create a fault object when a ServiceRuntimeException does exist. 
	 * 
	 * @param sre					the ServiceRuntimeException
	 * @param module				the name of the module that caught the sre
	 * @param faultBONamespace		the namespace of the fault object 
	 * @param faultBOName			the name of the fault object 
	 * @return						the fault business object created
	 */
	public static DataObject getRuntimeFaultBO(ServiceRuntimeException sre, String module, String faultBONamespace, String faultBOName) {
		
		DataObject fault = getFaultBO(sre, module, faultBONamespace, faultBOName, "Runtime", null, null);
		return fault;
	}

	/**
	 * 
	 * Method used to create a fault object when a ServiceRuntimeException does not exist. 
	 * 
	 * @param module				the name of the module that caught the sre
	 * @param faultBONamespace		the namespace of the fault object 
	 * @param faultBOName			the name of the fault object 
	 * @param errorMessage			enables the user to provide an errorMessage as there is no SRE available
	 * @param rootCause				enables the user to provide an rootCause as there is no SRE available
	 * @return						the fault business object created
	 */
	public static DataObject getRuntimeFaultBO(String module, String errorMessage, String rootCause) {
		
		DataObject fault = getFaultBO(null, module, FAULT_NAMESPACE, FAULT_ASBO_NAME, "Runtime", errorMessage, rootCause);
		return fault;
	}
	
	/**
	 * 
	 * Method used to create a fault object when a ServiceRuntimeException does exist. 
	 * 
	 * @param sre					the ServiceRuntimeException
	 * @param module				the name of the module that caught the sre
	 * @param faultBONamespace		the namespace of the fault object 
	 * @param faultBOName			the name of the fault object 
	 * @return						the fault business object created
	 */
	public static DataObject getBusinessFaultBO(Exception sre, String module, String faultBONamespace, String faultBOName) {
		
		DataObject fault = getFaultBO(sre, module, faultBONamespace, faultBOName, "Business", null, null);
		return fault;
	}
	
	/**
	 * 
	 * Method used to create a fault object when a ServiceRuntimeException does not exist. 
	 * 
	 * @param module				the name of the module that caught the sre
	 * @param faultBONamespace		the namespace of the fault object 
	 * @param faultBOName			the name of the fault object 
	 * @param errorMessage			enables the user to provide an errorMessage as there is no SRE available
	 * @param rootCause				enables the user to provide an rootCause as there is no SRE available
	 * @return						the fault business object created
	 */
	public static DataObject getBusinessFaultBO(String module, String faultBONamespace, String faultBOName, String errorMessage, String rootCause) {
		
		DataObject fault = getFaultBO(null, module, faultBONamespace, faultBOName, "Business", errorMessage, rootCause);
		return fault;
	}
	
    /**
     * @param sre
     * @return
     * returns the lowest Throwable in the hierarchy
     */
    public static Throwable getRootCause(Exception sre) {
        boolean done = false;
        Throwable e = sre;
        while(!done){
            if(e.getCause() != null)
                e = e.getCause();
            else done = true;  
        }
        return e;
    }

	/**
	 * 
	 * Provides the SCAContext as a string reprensentation of a component
	 * 
	 * @param module	the name of the module to get the context information from
	 * @return			the context information of the specified module
	 */
	public static String getSCAContext(String module) {
		
		String SCAContext = null;
		
		//MOD
		String moduleName = "MODULE: " + module;
		
		//COMP
		com.ibm.websphere.sca.scdl.Component  component = ServiceManager.INSTANCE.getComponent();
		String componentName = "COMPONENT: " + component.getName();
		
		//IF
		InterfaceType ifType = (InterfaceType) component.getInterfaceTypes().get(0);
		OperationType opType = (OperationType) ifType.getOperationTypes().get(0);
		String componentInterface = "IF(OP): " + ifType.getName().substring(10) + "(" + opType.getName() + ")";

		//REF
		Reference ref = (Reference) component.getReferences().get(0);
		ifType = (InterfaceType) ref.getInterfaceTypes().get(0);
		opType = (OperationType) ifType.getOperationTypes().get(0);
		String componentReference = "REF: " + ref.getName() + " IF(OP): "+ ifType.getName().substring(10) + "(" + opType.getName() + ")";

		SCAContext = moduleName + " / " + componentName + " / " +  componentInterface + " / " + componentReference;
	
		return SCAContext;
	}

	/**
	 * Convert SRE exception stacktrace to string
	 * 
	 * @param sre	the ServiceRuntimeException to convert
	 * @return		the string representation of the ServiceRunTimeException
	 */
	public static String getSRETrace(ServiceRuntimeException sre) {
		StringWriter sw = new StringWriter();
		sre.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}
	
	/**
	 * <p>
	 * Convert SBE exception stacktrace to string
	 * </p>
	 * 
	 * @param sbe
	 *            The SBE exception
	 * 
	 * @return 
	 * 			  String
	 * 
	 */
	public static String convertSBEStackTrace(Exception sbe)
	{
		StringWriter sw = new StringWriter();
		sbe.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}

}
