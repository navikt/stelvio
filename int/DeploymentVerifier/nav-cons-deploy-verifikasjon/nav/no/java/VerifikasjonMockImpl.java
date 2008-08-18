package nav.no.java;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.websphere.sca.ServiceManager;
import com.ibm.websphere.sca.ServiceRuntimeException;
import com.ibm.websphere.sca.sdo.DataFactory;
import com.ibm.ws.session.WBISessionManager;

import commonj.sdo.DataObject;

public class VerifikasjonMockImpl {
	
	private static Logger LOGGER = Logger.getLogger(VerifikasjonMockImpl.class.getName());
	private static String className = VerifikasjonMockImpl.class.getName();
	
	/**
	 * Default constructor.
	 */
	public VerifikasjonMockImpl() {
		super();
	
	}

	/**
	 * Return a reference to the component service instance for this implementation
	 * class.  This method should be used when passing this service to a partner reference
	 * or if you want to invoke this component service asynchronously.    
	 *
	 * @generated (com.ibm.wbit.java)
	 */
	@SuppressWarnings("unused")
	private Object getMyService() {
		return (Object) ServiceManager.INSTANCE.locateService("self");
	}

	/**
	 * Method generated to support implemention of operation "opWS" defined for WSDL port type 
	 * named "Verifikasjon".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject opWS(DataObject aSBOVerifikasjonReqWS) {
		DataObject resultBo = DataFactory.INSTANCE.create("http://nav-cons-deploy-verifikasjon/nav/no/deploy/asbo", "ASBOVerifikasjonRes");
		
		if (aSBOVerifikasjonReqWS.get("typeVerifikasjon") != null)
		{
			resultBo.setString("typeVerifikasjon", aSBOVerifikasjonReqWS.getString("typeVerifikasjon"));
			resultBo.setString("status", "OK");
			resultBo.setString("aksjon", "INGEN");
		}
		else
		{
			resultBo.setString("typeVerifikasjon", "WS");
			resultBo.setString("status", "OK");
			resultBo.setString("aksjon", "INGEN");
		}
		
		LOGGER.log(Level.INFO, "opWS - OK");
		return resultBo;
		
	}

	/**
	 * Method generated to support implemention of operation "opCEI" defined for WSDL port type 
	 * named "Verifikasjon".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject opCEI(DataObject aSBOVerifikasjonReqCEI) {

		String wbiSessionId = null;
		WBISessionManager sessionManager = null;
		try {
			// WBISession -> dont throw if we no get the SessionId
			sessionManager = WBISessionManager.getInstance();
			if (sessionManager.isSessionExisted()) {
				wbiSessionId = sessionManager.getSessionContext().getSessionId();
				LOGGER.log(Level.FINE, "wbiSessionId: " +  wbiSessionId);
			} else {
				wbiSessionId = null;
				LOGGER.log(Level.FINE, "wbiSessionId: ikke mulig");
			}

		} catch (Exception e) {
			LOGGER.logp(Level.SEVERE, className, "constructor()", "CatchedError: " + e.getMessage());
		}	
		
		DataObject resultBo = DataFactory.INSTANCE.create("http://nav-cons-deploy-verifikasjon/nav/no/deploy/asbo", "ASBOVerifikasjonRes");
		
		if (aSBOVerifikasjonReqCEI.get("typeVerifikasjon") != null)
		{
			resultBo.setString("typeVerifikasjon", aSBOVerifikasjonReqCEI.getString("typeVerifikasjon"));
			resultBo.setString("status", "OK");
			if (wbiSessionId != null)
				resultBo.setString("aksjon", "Sjekk CEI browser eller CEILogger logfile at den session ID " + wbiSessionId + " er rapportert");
			else
				resultBo.setString("aksjon", "Sjekk CEI browser eller CEILogger logfile at en CEI message er rapportert.");	
		}
		else
		{
			resultBo.setString("typeVerifikasjon", "CEI");
			resultBo.setString("status", "OK");
			if (wbiSessionId != null)
				resultBo.setString("aksjon", "Sjekk CEI browser eller CEILogger logfile at den session ID " + wbiSessionId + " er rapportert");
			else
				resultBo.setString("aksjon", "Sjekk CEI browser eller CEILogger logfile at en CEI message er rapportert.");	
		}
		
		LOGGER.log(Level.INFO, "opCEI - OK");
		return resultBo;
	}

	/**
	 * Method generated to support implemention of operation "opFEM" defined for WSDL port type 
	 * named "Verifikasjon".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject opFEM(DataObject aSBOVerifikasjonReqFEM) {

		String wbiSessionId = null;
		WBISessionManager sessionManager = null;
		try {
			// WBISession -> dont throw if we no get the SessionId
			sessionManager = WBISessionManager.getInstance();
			if (sessionManager.isSessionExisted()) {
				wbiSessionId = sessionManager.getSessionContext().getSessionId();
				LOGGER.log(Level.FINE, "wbiSessionId: " +  wbiSessionId);
			} else {
				wbiSessionId = null;
				LOGGER.log(Level.FINE, "wbiSessionId: ikke mulig");
			}

		} catch (Exception e) {
			LOGGER.logp(Level.SEVERE, className, "constructor()", "CatchedError: " + e.getMessage());
		}	
		
		if (wbiSessionId != null)
		{	
			LOGGER.log(Level.INFO, "opFEM - OK");
			throw new ServiceRuntimeException("Lagre failed event session ID " + wbiSessionId + " til verifikasjon av deployment som kan slettes.");
		}	
		else
		{	
			LOGGER.log(Level.INFO, "opFEM - OK");
			throw new ServiceRuntimeException("Lagre failed event til verifikasjon av deployment som kan slette.");
		}	
	}

	/**
	 * Method generated to support implemention of operation "opSCA" defined for WSDL port type 
	 * named "Verifikasjon".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject opSCA(DataObject aSBOVerifikasjonReqSCA) {

		DataObject resultBo = DataFactory.INSTANCE.create("http://nav-cons-deploy-verifikasjon/nav/no/deploy/asbo", "ASBOVerifikasjonRes");
		
		if (aSBOVerifikasjonReqSCA.get("typeVerifikasjon") != null)
		{
			resultBo.setString("typeVerifikasjon", aSBOVerifikasjonReqSCA.getString("typeVerifikasjon"));
			resultBo.setString("status", "OK");
			resultBo.setString("aksjon", "INGEN");
		}
		else
		{
			resultBo.setString("typeVerifikasjon", "SCA");
			resultBo.setString("status", "OK");
			resultBo.setString("aksjon", "INGEN");
		}
		LOGGER.log(Level.INFO, "opSCA - OK");
		return resultBo;
	}
}