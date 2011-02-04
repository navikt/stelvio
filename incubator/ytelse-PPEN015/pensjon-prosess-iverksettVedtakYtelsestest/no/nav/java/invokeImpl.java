package no.nav.java;

import com.ibm.websphere.sca.Service;
import commonj.sdo.DataObject;
import com.ibm.websphere.sca.ServiceManager;

public class invokeImpl {
	/**
	 * Default constructor.
	 */
	public invokeImpl() {
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
	 * named "IverksettVedtakPartner".  This will return an instance of 
	 * {@link com.ibm.websphere.sca.Service}.  This is the dynamic
	 * interface which is used to invoke operations on the reference service
	 * either synchronously or asynchronously.  You will need to pass the operation
	 * name in order to invoke an operation on the service.
	 *
	 * @generated (com.ibm.wbit.java)
	 *
	 * @return Service
	 */
	private Service _IverksettVedtakPartner = null;

	public Service locateService_IverksettVedtakPartner() {
		if (_IverksettVedtakPartner == null) {
			_IverksettVedtakPartner = (Service) ServiceManager.INSTANCE
					.locateService("IverksettVedtakPartner");
		}
		return _IverksettVedtakPartner;
	}

	/**
	 * Method generated to support implementation of operation "iverksettVedtak" defined for WSDL port type 
	 * named "IverksettVedtak".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that it is a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public void iverksettVedtak(DataObject request) {
		locateService_IverksettVedtakPartner().invokeAsync("iverksettVedtak", request);
	}

	/**
	 * Method generated to support implementation of operation "mottaSamhandlerSvar" defined for WSDL port type 
	 * named "IverksettVedtak".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that it is a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public void mottaSamhandlerSvar(DataObject request) {
		locateService_IverksettVedtakPartner().invokeAsync("mottaSamhandlerSvar", request);
	}

}