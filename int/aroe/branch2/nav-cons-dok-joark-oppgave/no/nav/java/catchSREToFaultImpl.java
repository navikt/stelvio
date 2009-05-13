package no.nav.java;

import no.stelvio.common.bus.util.ErrorHelperUtil;

import com.ibm.websphere.sca.ServiceBusinessException;
import com.ibm.websphere.sca.ServiceRuntimeException;
import com.ibm.websphere.sca.Ticket;
import commonj.sdo.DataObject;
import oppgave.joark.dok.cons.nav.no.nav.inf.JOARKOppgave;
import com.ibm.websphere.sca.ServiceManager;



public class catchSREToFaultImpl {
	
	private static final String MODULE_NAME = "nav-cons-dok-joark-oppgave";
	private final static String FAULT_NAMESPACE = "http://nav-lib-cons-dok-joark/no/nav/lib/dok/joark/fault";
	private final static String FAULT_ASBO_NAME = "FaultDokGenerisk";
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
	private Object getMyService() {
		String foo = "bar";
		return (Object) ServiceManager.INSTANCE.locateService("self");
	}

	/**
	 * This method is used to locate the service for the reference
	 * named "JOARKOppgavePartner".  This will return an instance of
	 * {@link JOARKOppgave}.  If you would like to use this service 
	 * asynchronously then you will need to cast the result
	 * to {@link JOARKOppgaveAsync}.
	 *
	 * @generated (com.ibm.wbit.java)
	 *
	 * @return JOARKOppgave
	 */
	public JOARKOppgave locateService_JOARKOppgavePartner() {
		return (JOARKOppgave) ServiceManager.INSTANCE
				.locateService("JOARKOppgavePartner");
	}

	/**
	 * Method generated to support implemention of operation "hentOppgaveListe" defined for WSDL port type 
	 * named "interface.JOARKOppgave".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject hentOppgaveListe(DataObject hentOppgaveListeRequest) {
		
		DataObject hentOppgaveListeResponse = null;
		try {
			hentOppgaveListeResponse = locateService_JOARKOppgavePartner().hentOppgaveListe(hentOppgaveListeRequest);
			
		} catch (ServiceRuntimeException sre) {
		//	DataObject faultBo = ErrorHelperUtil.getRuntimeFaultBO(sre, MODULE_NAME);
			DataObject faultBo = ErrorHelperUtil.getRuntimeFaultBO(sre, MODULE_NAME,FAULT_NAMESPACE,FAULT_ASBO_NAME);		
			throw new ServiceBusinessException(faultBo);
		}
		return hentOppgaveListeResponse;
	}

	/**
	 * Method generated to support implemention of operation "opprettOppgave" defined for WSDL port type 
	 * named "interface.JOARKOppgave".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject opprettOppgave(DataObject opprettOppgaveRequest) {
		DataObject opprettOppgaveResponse = null;
		try {
			opprettOppgaveResponse = locateService_JOARKOppgavePartner().opprettOppgave(opprettOppgaveRequest);
			
		} catch (ServiceRuntimeException sre) {
		//	DataObject faultBo = ErrorHelperUtil.getRuntimeFaultBO(sre, MODULE_NAME);
			DataObject faultBo = ErrorHelperUtil.getRuntimeFaultBO(sre, MODULE_NAME,FAULT_NAMESPACE,FAULT_ASBO_NAME);			
			throw new ServiceBusinessException(faultBo);
		}
		return opprettOppgaveResponse;
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link oppgave.joark.dok.cons.nav.no.nav.inf.JOARKOppgave#hentOppgaveListe(DataObject aDataObject))
	 * of java interface (@link oppgave.joark.dok.cons.nav.no.nav.inf.JOARKOppgave)	
	 * @see oppgave.joark.dok.cons.nav.no.nav.inf.JOARKOppgave#hentOppgaveListe(DataObject aDataObject)	
	 */
	public void onHentOppgaveListeResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link oppgave.joark.dok.cons.nav.no.nav.inf.JOARKOppgave#opprettOppgave(DataObject aDataObject))
	 * of java interface (@link oppgave.joark.dok.cons.nav.no.nav.inf.JOARKOppgave)	
	 * @see oppgave.joark.dok.cons.nav.no.nav.inf.JOARKOppgave#opprettOppgave(DataObject aDataObject)	
	 */
	public void onOpprettOppgaveResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

}