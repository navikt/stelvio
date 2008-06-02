package no.nav.java;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.websphere.sca.Service;
import com.ibm.websphere.sca.ServiceCallback;
import com.ibm.websphere.sca.Ticket;

import commonj.sdo.DataObject;
import com.ibm.websphere.sca.ServiceManager;
import com.ibm.websphere.sca.scdl.OperationType;
import com.ibm.wsspi.sca.multipart.impl.MultipartImpl;

public class ReliableServiceInvocationImpl {

	// PoJoLogger - log.logp(Level.FINEST, className, <yourMethod>, <yourText>);
	private final static String className = ReliableServiceInvocationImpl.class
			.getName();

	private final Logger log = Logger.getLogger(className);

	// Helpers for AsynchWithCallBack
	ServiceCallback caller;

	Ticket ticket;

	/**
	 * Default constructor.
	 */
	public ReliableServiceInvocationImpl() {
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
	 * named "PersonHendelsePartnerMEDL".  This will return an instance of 
	 * {@link com.ibm.websphere.sca.Service}.  This is the dynamic
	 * interface which is used to invoke operations on the reference service
	 * either synchronously or asynchronously.  You will need to pass the operation
	 * name in order to invoke an operation on the service.
	 *
	 * @generated (com.ibm.wbit.java)
	 *
	 * @return Service
	 */
	public Service locateService_PersonHendelsePartnerMEDL() {
		return (Service) ServiceManager.INSTANCE
				.locateService("PersonHendelsePartnerMEDL");
	}

	/**
	 * This method is used to locate the service for the reference
	 * named "PersonHendelsePartnerPROF".  This will return an instance of 
	 * {@link com.ibm.websphere.sca.Service}.  This is the dynamic
	 * interface which is used to invoke operations on the reference service
	 * either synchronously or asynchronously.  You will need to pass the operation
	 * name in order to invoke an operation on the service.
	 *
	 * @generated (com.ibm.wbit.java)
	 *
	 * @return Service
	 */
	/*public Service locateService_PersonHendelsePartnerPROF() {
		return (Service) ServiceManager.INSTANCE
				.locateService("PersonHendelsePartnerPROF");
	}*/

	/**
	 * This method is used to locate the service for the reference
	 * named "PersonHendelsePartnerPOP".  This will return an instance of 
	 * {@link com.ibm.websphere.sca.Service}.  This is the dynamic
	 * interface which is used to invoke operations on the reference service
	 * either synchronously or asynchronously.  You will need to pass the operation
	 * name in order to invoke an operation on the service.
	 *
	 * @generated (com.ibm.wbit.java)
	 *
	 * @return Service
	 */
	public Service locateService_PersonHendelsePartnerPOP() {
		return (Service) ServiceManager.INSTANCE
				.locateService("PersonHendelsePartnerPOP");
	}

	/**
	 * This method is used to locate the service for the reference
	 * named "PersonHendelsePartnerGSAK".  This will return an instance of 
	 * {@link com.ibm.websphere.sca.Service}.  This is the dynamic
	 * interface which is used to invoke operations on the reference service
	 * either synchronously or asynchronously.  You will need to pass the operation
	 * name in order to invoke an operation on the service.
	 *
	 * @generated (com.ibm.wbit.java)
	 *
	 * @return Service
	 */
	/*public Service locateService_PersonHendelsePartnerGSAK() {
		return (Service) ServiceManager.INSTANCE
				.locateService("PersonHendelsePartnerGSAK");
	}*/

	/**
	 * This method is used to locate the service for the reference
	 * named "PersonHendelsePartnerPEN".  This will return an instance of 
	 * {@link com.ibm.websphere.sca.Service}.  This is the dynamic
	 * interface which is used to invoke operations on the reference service
	 * either synchronously or asynchronously.  You will need to pass the operation
	 * name in order to invoke an operation on the service.
	 *
	 * @generated (com.ibm.wbit.java)
	 *
	 * @return Service
	 */
	public Service locateService_PersonHendelsePartnerPEN() {
		return (Service) ServiceManager.INSTANCE
				.locateService("PersonHendelsePartnerPEN");
	}

	/**
	 * Method generated to support implemention of operation "fodselsnummerOppdatert" defined for WSDL port type 
	 * named "interface.PersonHendelse".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public void fodselsnummerOppdatert(DataObject fodselsnummerOppdatertRequest) {

		//ONE-WAY no response object
		locateService_PersonHendelsePartnerMEDL().invokeAsync(
				"fodselsnummerOppdatert", fodselsnummerOppdatertRequest);
		log.logp(Level.FINE, className, "fodselsnummerOppdatert(DataObject)",
				"PersonHendelsePartnerMEDL done.");

		//ONE-WAY no response object
	/*	locateService_PersonHendelsePartnerPROF().invokeAsync(
				"fodselsnummerOppdatert", fodselsnummerOppdatertRequest);
		log.logp(Level.INFO, className, "fodselsnummerOppdatert(DataObject)",
				"PersonHendelsePartnerPROF done");*/

		//ONE-WAY no response object
		locateService_PersonHendelsePartnerPOP().invokeAsync(
				"fodselsnummerOppdatert", fodselsnummerOppdatertRequest);
		log.logp(Level.FINE, className, "fodselsnummerOppdatert(DataObject)",
				"PersonHendelsePartnerPOP done");

		//ONE-WAY no response object
	/*	locateService_PersonHendelsePartnerGSAK().invokeAsync(
				"fodselsnummerOppdatert", fodselsnummerOppdatertRequest);
		log.logp(Level.INFO, className, "fodselsnummerOppdatert(DataObject)",
				"PersonHendelsePartnerGSAK done");*/

		//ONE-WAY no response object
		locateService_PersonHendelsePartnerPEN().invokeAsync(
				"fodselsnummerOppdatert", fodselsnummerOppdatertRequest);
		log.logp(Level.FINE, className, "fodselsnummerOppdatert(DataObject)",
				"PersonHendelsePartnerPEN done");

		return;

	}

	/**
	 * Method generated to support implemention of operation "adresseOppdatert" defined for WSDL port type 
	 * named "interface.PersonHendelse".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public void adresseOppdatert(DataObject adresseOppdatertRequest) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support implemention of operation "fodselsnummerOpprettet" defined for WSDL port type 
	 * named "interface.PersonHendelse".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public void fodselsnummerOpprettet(DataObject fodselsnummerOpprettetRequest) {

		//STUBBING because no subscribers have defined the operation on their interface
		//log.logp(Level.INFO,className,"fodselsnummerOpprettet","fodselsnummerOpprettet stub kallet");
		
		//ONE-WAY no response object
		locateService_PersonHendelsePartnerMEDL().invokeAsync(
				"fodselsnummerOpprettet", fodselsnummerOpprettetRequest);
		log.logp(Level.FINE, className, "fodselsnummerOpprettet(DataObject)",
				"PersonHendelsePartnerMEDL done.");
/*
		//ONE-WAY no response object
		locateService_PersonHendelsePartnerPOP().invokeAsync(
				"fodselsnummerOppdatert", fodselsnummerOpprettetRequest);
		log.logp(Level.INFO, className, "fodselsnummerOpprettet(DataObject)",
				"PersonHendelsePartnerPOP done");

		//ONE-WAY no response object
		locateService_PersonHendelsePartnerPEN().invokeAsync(
				"fodselsnummerOppdatert", fodselsnummerOpprettetRequest);
		log.logp(Level.INFO, className, "fodselsnummerOpprettet(DataObject)",
				"PersonHendelsePartnerPEN done");*/

		return;
	}
}