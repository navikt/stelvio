package no.nav.java;

import com.ibm.websphere.sca.Service;
import java.util.ArrayList;
import com.ibm.websphere.bo.BOFactory;
import com.ibm.websphere.sca.ServiceManager;
import com.ibm.websphere.sca.Ticket;
import commonj.sdo.DataObject;

public class OppgaveStubImpl {

	private static final String GBONAMESPACE = "http://nav-lib-sak/no/nav/lib/sak/gbo";

	/**
	 * Default constructor.
	 */
	public OppgaveStubImpl() {
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
	 * named "OppgavePartner".  This will return an instance of 
	 * {@link com.ibm.websphere.sca.Service}.  This is the dynamic
	 * interface which is used to invoke operations on the reference service
	 * either synchronously or asynchronously.  You will need to pass the operation
	 * name in order to invoke an operation on the service.
	 *
	 * @generated (com.ibm.wbit.java)
	 *
	 * @return Service
	 */
	public Service locateService_OppgavePartner() {
		return (Service) ServiceManager.INSTANCE
				.locateService("OppgavePartner");
	}

	/**
	 * Method generated to support implemention of operation "hentOppgave" defined for WSDL port type 
	 * named "Oppgave".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject hentOppgave(DataObject hentOppgaveRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "opprettOppgave" defined for WSDL port type 
	 * named "Oppgave".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject opprettOppgave(DataObject opprettOppgaveRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "lagreOppgave" defined for WSDL port type 
	 * named "Oppgave".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject lagreOppgave(DataObject lagreOppgaveRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "lagreOppgaveListe" defined for WSDL port type 
	 * named "Oppgave".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject lagreOppgaveListe(DataObject lagreOppgaveListeRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "slettOppgaveListe" defined for WSDL port type 
	 * named "Oppgave".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public String slettOppgaveListe(DataObject slettOppgaveListeRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "hentOppgaveListe" defined for WSDL port type 
	 * named "Oppgave".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject hentOppgaveListe(DataObject hentOppgaveListeRequest) {
		BOFactory bof = (BOFactory) ServiceManager.INSTANCE
				.locateService("com/ibm/websphere/bo/BOFactory");

		DataObject oppgaveListe = bof.create(GBONAMESPACE, "GBOOppgaveListe");
		oppgaveListe.setList("oppgaver", new ArrayList());

		return oppgaveListe;
	}

	/**
	 * Method generated to support implemention of operation "hentFerdigstiltOppgave" defined for WSDL port type 
	 * named "Oppgave".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject hentFerdigstiltOppgave(
			DataObject hentFerdigstiltOppgaveRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "finnOppgaveListe" defined for WSDL port type 
	 * named "Oppgave".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject finnOppgaveListe(DataObject finnOppgaveListeRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "bestillOppgave" defined for WSDL port type 
	 * named "Oppgave".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject bestillOppgave(DataObject bestillOppgaveRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "opprettOppgaveListe" defined for WSDL port type 
	 * named "Oppgave".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public String opprettOppgaveListe(DataObject opprettOppgaveListeRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "hentFerdigstiltOppgaveListe" defined for WSDL port type 
	 * named "Oppgave".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject hentFerdigstiltOppgaveListe(
			DataObject hentFerdigstiltOppgaveListeRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link sak.lib.nav.no.nav.lib.sak.inf.Oppgave#hentOppgave(DataObject aDataObject))
	 * of java interface (@link sak.lib.nav.no.nav.lib.sak.inf.Oppgave)	
	 * @see sak.lib.nav.no.nav.lib.sak.inf.Oppgave#hentOppgave(DataObject aDataObject)	
	 */
	public void onHentOppgaveResponse(Ticket __ticket, DataObject returnValue,
			Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link sak.lib.nav.no.nav.lib.sak.inf.Oppgave#opprettOppgave(DataObject aDataObject))
	 * of java interface (@link sak.lib.nav.no.nav.lib.sak.inf.Oppgave)	
	 * @see sak.lib.nav.no.nav.lib.sak.inf.Oppgave#opprettOppgave(DataObject aDataObject)	
	 */
	public void onOpprettOppgaveResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link sak.lib.nav.no.nav.lib.sak.inf.Oppgave#lagreOppgave(DataObject aDataObject))
	 * of java interface (@link sak.lib.nav.no.nav.lib.sak.inf.Oppgave)	
	 * @see sak.lib.nav.no.nav.lib.sak.inf.Oppgave#lagreOppgave(DataObject aDataObject)	
	 */
	public void onLagreOppgaveResponse(Ticket __ticket, DataObject returnValue,
			Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link sak.lib.nav.no.nav.lib.sak.inf.Oppgave#lagreOppgaveListe(DataObject aDataObject))
	 * of java interface (@link sak.lib.nav.no.nav.lib.sak.inf.Oppgave)	
	 * @see sak.lib.nav.no.nav.lib.sak.inf.Oppgave#lagreOppgaveListe(DataObject aDataObject)	
	 */
	public void onLagreOppgaveListeResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link sak.lib.nav.no.nav.lib.sak.inf.Oppgave#slettOppgaveListe(DataObject aDataObject))
	 * of java interface (@link sak.lib.nav.no.nav.lib.sak.inf.Oppgave)	
	 * @see sak.lib.nav.no.nav.lib.sak.inf.Oppgave#slettOppgaveListe(DataObject aDataObject)	
	 */
	public void onSlettOppgaveListeResponse(Ticket __ticket,
			String returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link sak.lib.nav.no.nav.lib.sak.inf.Oppgave#hentOppgaveListe(DataObject aDataObject))
	 * of java interface (@link sak.lib.nav.no.nav.lib.sak.inf.Oppgave)	
	 * @see sak.lib.nav.no.nav.lib.sak.inf.Oppgave#hentOppgaveListe(DataObject aDataObject)	
	 */
	public void onHentOppgaveListeResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link sak.lib.nav.no.nav.lib.sak.inf.Oppgave#hentFerdigstiltOppgave(DataObject aDataObject))
	 * of java interface (@link sak.lib.nav.no.nav.lib.sak.inf.Oppgave)	
	 * @see sak.lib.nav.no.nav.lib.sak.inf.Oppgave#hentFerdigstiltOppgave(DataObject aDataObject)	
	 */
	public void onHentFerdigstiltOppgaveResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link sak.lib.nav.no.nav.lib.sak.inf.Oppgave#finnOppgaveListe(DataObject aDataObject))
	 * of java interface (@link sak.lib.nav.no.nav.lib.sak.inf.Oppgave)	
	 * @see sak.lib.nav.no.nav.lib.sak.inf.Oppgave#finnOppgaveListe(DataObject aDataObject)	
	 */
	public void onFinnOppgaveListeResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link sak.lib.nav.no.nav.lib.sak.inf.Oppgave#bestillOppgave(DataObject aDataObject))
	 * of java interface (@link sak.lib.nav.no.nav.lib.sak.inf.Oppgave)	
	 * @see sak.lib.nav.no.nav.lib.sak.inf.Oppgave#bestillOppgave(DataObject aDataObject)	
	 */
	public void onBestillOppgaveResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link sak.lib.nav.no.nav.lib.sak.inf.Oppgave#opprettOppgaveListe(DataObject aDataObject))
	 * of java interface (@link sak.lib.nav.no.nav.lib.sak.inf.Oppgave)	
	 * @see sak.lib.nav.no.nav.lib.sak.inf.Oppgave#opprettOppgaveListe(DataObject aDataObject)	
	 */
	public void onOpprettOppgaveListeResponse(Ticket __ticket,
			String returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link sak.lib.nav.no.nav.lib.sak.inf.Oppgave#hentFerdigstiltOppgaveListe(DataObject aDataObject))
	 * of java interface (@link sak.lib.nav.no.nav.lib.sak.inf.Oppgave)	
	 * @see sak.lib.nav.no.nav.lib.sak.inf.Oppgave#hentFerdigstiltOppgaveListe(DataObject aDataObject)	
	 */
	public void onHentFerdigstiltOppgaveListeResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

}