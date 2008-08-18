package nav.no.java;


import com.ibm.websphere.sca.Service;
import com.ibm.websphere.sca.ServiceBusinessException;
import com.ibm.websphere.sca.ServiceRuntimeException;
import com.ibm.websphere.sca.Ticket;
import commonj.sdo.DataObject;
import com.ibm.websphere.sca.ServiceManager;
import com.ibm.ws.sca.internal.multipart.impl.ManagedMultipartImpl;
import nav.no.java.ErrorHelperUtil;


public class catchSREToFaultImpl {
	
	final static String CURRENTMODULENAME = "nav-cons-deploy-verifikasjon";
	final static String FAULTASBONS = "http://nav-cons-deploy-verifikasjon/nav/no/deploy/fault";
	final static String FAULTASBO = "FaultVerifikasjonGenerisk";
	
	/**
	 * Default constructor.
	 */
	public catchSREToFaultImpl() {
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

		DataObject resultBo = null;
		
		try {
			resultBo = (DataObject) locateService_VerifikasjonPartner().invoke("opWS", aSBOVerifikasjonReqWS);
			
			if (resultBo != null)
			{
				if (resultBo instanceof ManagedMultipartImpl) {
					return resultBo.getDataObject(0);
				}
				else
				 return resultBo;
			}
			else
			{	
				resultBo.setString("typeVerifikasjon", "WS");
				resultBo.setString("status", "FEIL");
				resultBo.setString("aksjon", "Fikk tom response tilbake som er ikke forventet - Sjekk WPS loggen.");
				return resultBo;
			} 
			
		} catch (ServiceRuntimeException sre) {
			DataObject faultBo = ErrorHelperUtil.getRuntimeFaultBO(sre, CURRENTMODULENAME, FAULTASBONS, FAULTASBO);
			throw new ServiceBusinessException(faultBo);
		}
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
		DataObject resultBo = null;

		try {
			resultBo = (DataObject) locateService_VerifikasjonPartner().invoke("opCEI", aSBOVerifikasjonReqCEI);
			
			if (resultBo != null)
			{
				if (resultBo instanceof ManagedMultipartImpl) {
					return resultBo.getDataObject(0);
				}
				else
				 return resultBo;
			}
			else
			{	
				resultBo.setString("typeVerifikasjon", "CEI");
				resultBo.setString("status", "FEIL");
				resultBo.setString("aksjon", "Fikk tom response tilbake som er ikke forventet - Sjekk WPS loggen.");
				return resultBo;
			} 
		
		} catch (ServiceRuntimeException sre) {
			DataObject faultBo = ErrorHelperUtil.getRuntimeFaultBO(sre, CURRENTMODULENAME, FAULTASBONS, FAULTASBO);
			throw new ServiceBusinessException(faultBo);
		}
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
		DataObject resultBo = null;

		try {
			resultBo = (DataObject) locateService_VerifikasjonPartner().invoke("opFEM", aSBOVerifikasjonReqFEM);
			
			if (resultBo != null)
			{
				if (resultBo instanceof ManagedMultipartImpl) {
	
					return resultBo.getDataObject(0);
				}
				else
				 return resultBo;
			}
			else
			{	
				resultBo.setString("typeVerifikasjon", "FEM");
				resultBo.setString("status", "FEIL");
				resultBo.setString("aksjon", "Fikk tom response tilbake som er ikke forventet - Sjekk WPS loggen.");
				return resultBo;
			} 
		
		} catch (ServiceRuntimeException sre) {
			DataObject faultBo = ErrorHelperUtil.getRuntimeFaultBO(sre, CURRENTMODULENAME, FAULTASBONS, FAULTASBO);
			throw new ServiceBusinessException(faultBo);
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
		DataObject resultBo = null;

		try {
			resultBo = (DataObject) locateService_VerifikasjonPartner().invoke("opSCA", aSBOVerifikasjonReqSCA);
			
			if (resultBo != null)
			{
				if (resultBo instanceof ManagedMultipartImpl) {
					return resultBo.getDataObject(0);
				}
				else
				 return resultBo;
			}
			else
			{	
				resultBo.setString("typeVerifikasjon", "SCA");
				resultBo.setString("status", "FEIL");
				resultBo.setString("aksjon", "Fikk tom response tilbake som er ikke forventet - Sjekk WPS loggen.");
				return resultBo;
			} 
		
		} catch (ServiceRuntimeException sre) {
			DataObject faultBo = ErrorHelperUtil.getRuntimeFaultBO(sre, CURRENTMODULENAME, FAULTASBONS, FAULTASBO);
			throw new ServiceBusinessException(faultBo);
		}
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