package no.nav.java;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.ibm.websphere.sca.Service;
import com.ibm.websphere.sca.ServiceBusinessException;
import com.ibm.websphere.sca.ServiceManager;
import com.ibm.websphere.sca.Ticket;
import com.ibm.websphere.sca.sdo.DataFactory;
import commonj.sdo.DataObject;
import no.stelvio.common.bus.util.DataObjectUtil;

public class InvoketpsNoNSImpl {

	final static String currentModulName = "nav-prod-frg-tps";

	/**
	 * Default constructor.
	 */
	public InvoketpsNoNSImpl() {
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
	 * named "TPSNoNSPartner".  This will return an instance of 
	 * {@link com.ibm.websphere.sca.Service}.  This is the dynamic
	 * interface which is used to invoke operations on the reference service
	 * either synchronously or asynchronously.  You will need to pass the operation
	 * name in order to invoke an operation on the service.
	 *
	 * @generated (com.ibm.wbit.java)
	 *
	 * @return Service
	 */
	public Service locateService_TPSNoNSPartner() {
		return (Service) ServiceManager.INSTANCE
				.locateService("TPSNoNSPartner");
	}

	/**
	 * Method generated to support implemention of operation "A0" defined for WSDL port type 
	 * named "interface.tps".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject hentTPSData(DataObject tpsPersonDataReq) {

		DataObject personDataWithNS = null;
		
		if (tpsPersonDataReq.getDataObject("tpsServiceRutine") != null) {
			DataObject personDataWithoutNS = DataFactory.INSTANCE.create("",
					"tpsPersonDataType");

			//copy tpsPersonDataInput>tpsPersonData
			DataObjectUtil.deepCopyDataObjectNoNS(tpsPersonDataReq,
					personDataWithoutNS);
			
			DataObject tpsPersonDataRes = (DataObject) locateService_TPSNoNSPartner()
					.invoke("hentTPSData", personDataWithoutNS);

			if (tpsPersonDataRes != null) {
				tpsPersonDataRes = tpsPersonDataRes.getDataObject(0);
			}

			personDataWithNS = DataFactory.INSTANCE.create(
					"http://www.rtv.no/TPSData", "tpsPersonDataType");

			//copy tpsPersonDataRes>personDataWithNS
			DataObjectUtil.deepCopyDataObjectNoNS(tpsPersonDataRes,
					personDataWithNS);

			return personDataWithNS;
		} else {
			return personDataWithNS;
		}

	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation "interface.tpsNoNS#A0(DataObject tpsPersonDataReq)"
	 * of wsdl interface "interface.tpsNoNS"	
	 */
	public void onHentTPSDataResponse(Ticket __ticket, DataObject returnValue,
			Exception exception) {
		//TODO Needs to be implemented.
	}

	public String getSBEStackTrace(ServiceBusinessException sbe) {
		StringWriter sw = new StringWriter();
		sbe.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}

	/* TODO skal denne benyttes her?
	 public String getSRETrace(ServiceRuntimeException sre)
	 {
	 StringWriter sw = new StringWriter();
	 sre.printStackTrace(new PrintWriter(sw));
	 return sw.toString();
	 }
	 */
}