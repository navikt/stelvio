package nav.no.java;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.websphere.sca.Service;
import com.ibm.websphere.sca.ServiceManager;
import com.ibm.websphere.sca.Ticket;
import com.ibm.websphere.sca.sdo.DataFactory;
import com.ibm.ws.sca.internal.multipart.impl.ManagedMultipartImpl;
import com.ibm.ws.session.WBISessionManager;
import commonj.sdo.DataObject;

public class VerifikasjonPoJoImpl {
	
	private static Logger LOGGER = Logger.getLogger(VerifikasjonPoJoImpl.class.getName());
	private static String className = VerifikasjonPoJoImpl.class.getName();
	final static String CURRENTMODULENAME = "nav-cons-deploy-verifikasjon";
	
	/**
	 * Default constructor.
	 */
	public VerifikasjonPoJoImpl() {
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
	 * This method is used to locate the service for the reference
	 * named "VerifikasjonPartner".  This will return an instance of 
	 * {@link com.ibm.websphere.sca.Service}.  This is the dynamic
	 * interface which is used to invoke operations on the reference service
	 * either synchronously or asynchronously.  You will need to pass the operation
	 * name in order to invoke an operation on the service.
	 *
	 * @generated (com.ibm.wbit.java)
	 *
	 * @return Service
	 */
	public Service locateService_VerifikasjonPartner() {
		return (Service) ServiceManager.INSTANCE
				.locateService("VerifikasjonPartner");
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
		
		LOGGER.log(Level.INFO, "opWS - CALLING");
		DataObject resultBo = (DataObject) locateService_VerifikasjonPartner().invoke("opWS", aSBOVerifikasjonReqWS);
		if (resultBo instanceof ManagedMultipartImpl) {
			return resultBo.getDataObject(0);
		}
		else
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
		LOGGER.log(Level.INFO, "opCEI - CALLING");
		DataObject resultBo = (DataObject) locateService_VerifikasjonPartner().invoke("opCEI", aSBOVerifikasjonReqCEI); 
		if (resultBo instanceof ManagedMultipartImpl) {
			return resultBo.getDataObject(0);
		}
		else
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
		LOGGER.log(Level.INFO, "opFEM - CALLING");
		
		Ticket ticket =(Ticket) locateService_VerifikasjonPartner().invokeAsync("opFEM", aSBOVerifikasjonReqFEM);
		if (ticket != null)
		{	
			String wbiSessionId = null;
			try
			{
				WBISessionManager sessionManager = null;
				sessionManager = WBISessionManager.getInstance();
				if (sessionManager.isSessionExisted()) {
					wbiSessionId = sessionManager.getSessionContext().getSessionId();
				} else {
					wbiSessionId = null;
				}
			} catch (Exception e) {
				LOGGER.logp(Level.SEVERE, className, "constructor()", "CatchedError: " + e.getMessage());
			}	
			
			DataObject resultBo = DataFactory.INSTANCE.create("http://nav-cons-deploy-verifikasjon/nav/no/deploy/asbo", "ASBOVerifikasjonRes");
			resultBo.setString("typeVerifikasjon", "FEM");
			resultBo.setString("status", "OK");
			if (wbiSessionId != null)
				resultBo.setString("aksjon", "Slett failed event med session ID "+ wbiSessionId + " med FEM kommandolinje verktoy eller med FEM GUI.");
			else
				resultBo.setString("aksjon", "Slett failed event med Src. module navn " + CURRENTMODULENAME);
			return resultBo;
		}
		else
		{	
			DataObject resultBo = DataFactory.INSTANCE.create("http://nav-cons-deploy-verifikasjon/nav/no/deploy/asbo", "ASBOVerifikasjonRes");
			resultBo.setString("typeVerifikasjon", "FEM");
			resultBo.setString("status", "FEIL");
			resultBo.setString("aksjon", "Fikk ikke response (ticket) fra FEM kall tilbake som er ikke forventet - Sjekk WPS loggen at failed evet er lagret.");
			return resultBo;
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
		LOGGER.log(Level.INFO, "opSCA - CALLING");
		Ticket ticket = (Ticket) locateService_VerifikasjonPartner().invokeAsync("opSCA", aSBOVerifikasjonReqSCA);
		DataObject resultBo = (DataObject) locateService_VerifikasjonPartner().invokeResponse(ticket, 5000);
		if (resultBo instanceof ManagedMultipartImpl) {
			return resultBo.getDataObject(0);
		}
		else
			return resultBo;
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation "Verifikasjon#opWS(DataObject aSBOVerifikasjonReqWS)"
	 * of wsdl interface "Verifikasjon"	
	 */
	public void onOpWSResponse(Ticket __ticket, DataObject returnValue,
			Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation "Verifikasjon#opCEI(DataObject aSBOVerifikasjonReqCEI)"
	 * of wsdl interface "Verifikasjon"	
	 */
	public void onOpCEIResponse(Ticket __ticket, DataObject returnValue,
			Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation "Verifikasjon#opFEM(DataObject aSBOVerifikasjonReqFEM)"
	 * of wsdl interface "Verifikasjon"	
	 */
	public void onOpFEMResponse(Ticket __ticket, DataObject returnValue,
			Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation "Verifikasjon#opSCA(DataObject aSBOVerifikasjonReqSCA)"
	 * of wsdl interface "Verifikasjon"	
	 */
	public void onOpSCAResponse(Ticket __ticket, DataObject returnValue,
			Exception exception) {
		//TODO Needs to be implemented.
	}

}