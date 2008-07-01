package no.nav.java;

import com.ibm.websphere.sca.Service;

import no.nav.dto.fellesreg.opptjening.to.OppdaterFodselsnummerRequestDto;
import no.stelvio.common.bus.util.StelvioContextHelper;
import no.stelvio.dto.context.RequestContextDto;

import com.ibm.websphere.sca.ServiceManager;
import com.ibm.websphere.sca.sdo.DataFactory;
import commonj.sdo.DataObject;

public class SetContextData {
	/**
	 * Default constructor.
	 */
	public SetContextData() {
		super();
	}

	/**
	 * Return a reference to the component service instance for this implementation
	 * class.  This method should be used when passing this service to a partner reference
	 * or if you want to invoke this component service asynchronously.    
	 *
	 * @generated (com.ibm.wbit.java)
	 */
	private Object getMyService() {
		return (Object) ServiceManager.INSTANCE.locateService("self");
	}

	/**
	 * This method is used to locate the service for the reference
	 * named "VedlikeholdServicePartner".  This will return an instance of 
	 * {@link com.ibm.websphere.sca.Service}.  This is the dynamic
	 * interface which is used to invoke operations on the reference service
	 * either synchronously or asynchronously.  You will need to pass the operation
	 * name in order to invoke an operation on the service.
	 *
	 * @generated (com.ibm.wbit.java)
	 *
	 * @return Service
	 */
	public Service locateService_VedlikeholdServicePartner() {
		return (Service) ServiceManager.INSTANCE
				.locateService("VedlikeholdServicePartner");
	}

	/**
	 * Method generated to support implemention of operation "oppdaterFodselsnummer" defined for WSDL port type 
	 * named "interface.VedlikeholdService".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public void oppdaterFodselsnummer(DataObject oppdaterfodselsnummerrequestdto) {

			
		//Need to set request context manually
		DataObject ctx = DataFactory.INSTANCE.create(
				"http://context.dto.stelvio.no", "RequestContextDto");
		StelvioContextHelper stelvioContext = new StelvioContextHelper();

		ctx.setString("moduleId", stelvioContext.getApplicationId());
		ctx.setString("transactionId", stelvioContext.getCorrelationId());
		ctx.setString("userId", stelvioContext.getUserId());

		oppdaterfodselsnummerrequestdto.setDataObject("requestContextDto", ctx);
				
		locateService_VedlikeholdServicePartner().invoke("oppdaterFodselsnummer", oppdaterfodselsnummerrequestdto);

	}
}