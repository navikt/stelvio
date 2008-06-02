/**
 * ##[lineageStampBegin]##
 * ##[generatedBy:/nav-cons-frg-tps/no/nav/inf/TPSPersonHendelse.wsdl]##
 * ##[lineageStampEnd]##
 */
package tps.frg.cons.nav.no.nav.inf.tps.person.hendelse;

import commonj.sdo.DataObject;

import com.ibm.websphere.sca.ServiceBusinessException;

/**
 * @generated (com.ibm.wbit.java.core)
 * An interface used for enabling a static programming model for the service. 
 * The methods provide synchronous invocations of the service operations.
 */
public interface TPSPersonHendelse {
	public static final String PORTTYPE_NAME = "{http://nav-cons-frg-tps/no/nav/inf/TPSPersonHendelse}TPSPersonHendelse";

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "TPSEndringsmelding" operation on WSDL port "ns1:TPSPersonHendelse"
	 */
	public void TPSEndringsmelding(DataObject distribusjonsMelding)
			throws ServiceBusinessException;

}
