package no.nav.java;

import java.util.logging.Level;
import java.util.logging.Logger;

import tps.frg.cons.nav.no.nav.inf.tps.person.hendelse.TPSPersonHendelse;
import tps.frg.cons.nav.no.nav.inf.tps.person.hendelse.TPSPersonHendelseAsync;

import com.ibm.websphere.sca.ServiceManager;
import commonj.sdo.DataObject;

public class QuickFixImpl {
	
	
	// PoJoLogger - log.logp(Level.FINEST, className, <yourMethod>, <yourText>);
	private final static String className = QuickFixImpl.class.getName();

	private final Logger log = Logger.getLogger(className);
	
	/**
	 * Default constructor.
	 */
	public QuickFixImpl() {
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
	 * named "TPSPersonHendelsePartner".  This will return an instance of
	 * {@link TPSPersonHendelse}.  If you would like to use this service 
	 * asynchronously then you will need to cast the result
	 * to {@link TPSPersonHendelseAsync}.
	 *
	 * @generated (com.ibm.wbit.java)
	 *
	 * @return TPSPersonHendelse
	 */
	public TPSPersonHendelse locateService_TPSPersonHendelsePartner() {
		return (TPSPersonHendelse) ServiceManager.INSTANCE
				.locateService("TPSPersonHendelsePartner");
	}

	/**
	 * Method generated to support implemention of operation "TPSEndringsmelding" defined for WSDL port type 
	 * named "TPSPersonHendelse".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public void TPSEndringsmelding(DataObject distribusjonsMelding) {
		
		log.logp(Level.INFO, className, "TPSEndringsmelding", "QuickFixPOJO: Received message and call InterfaceMap.");
		locateService_TPSPersonHendelsePartner().TPSEndringsmelding(distribusjonsMelding);
		log.logp(Level.INFO, className, "TPSEndringsmelding", "QuickFixPOJO: InterfaceMap called.");
		
	}

}