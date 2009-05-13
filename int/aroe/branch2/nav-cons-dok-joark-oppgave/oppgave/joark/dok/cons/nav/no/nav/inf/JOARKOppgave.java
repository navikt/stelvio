/**
 * ##[lineageStampBegin]##
 * ##[generatedBy:/nav-cons-dok-joark-oppgave/no/nav/inf/JOARKOppgave.wsdl]##
 * ##[lineageStampEnd]##
 */
package oppgave.joark.dok.cons.nav.no.nav.inf;

import commonj.sdo.DataObject;

import com.ibm.websphere.sca.ServiceBusinessException;

/**
 * @generated (com.ibm.wbit.java.core)
 * An interface used for enabling a static programming model for the service. 
 * The methods provide synchronous invocations of the service operations.
 */
public interface JOARKOppgave {
	public static final String PORTTYPE_NAME = "{http://nav-cons-dok-joark-oppgave/no/nav/inf}JOARKOppgave";

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "hentOppgaveListe" operation on WSDL port "JOARKOppgave"
	 */
	public DataObject hentOppgaveListe(DataObject hentOppgaveListeRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "opprettOppgave" operation on WSDL port "JOARKOppgave"
	 */
	public DataObject opprettOppgave(DataObject opprettOppgaveRequest)
			throws ServiceBusinessException;

}
