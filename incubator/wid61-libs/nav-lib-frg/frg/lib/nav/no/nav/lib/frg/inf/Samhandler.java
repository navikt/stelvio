/**
 * ##[lineageStampBegin]##
 * ##[generatedBy:/nav-lib-frg/no/nav/lib/frg/inf/Samhandler.wsdl]##
 * ##[lineageStampEnd]##
 */
package frg.lib.nav.no.nav.lib.frg.inf;

import commonj.sdo.DataObject;

import com.ibm.websphere.sca.ServiceBusinessException;

/**
 * @generated (com.ibm.wbit.java.core)
 * An interface used for enabling a static programming model for the service. 
 * The methods provide synchronous invocations of the service operations.
 */
public interface Samhandler {
	public static final String PORTTYPE_NAME = "{http://nav-lib-frg/no/nav/lib/frg/inf}Samhandler";

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "hentSamhandler" operation on WSDL port "ns1:Samhandler"
	 */
	public DataObject hentSamhandler(DataObject hentSamhandlerRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "lagreSamhandler" operation on WSDL port "ns1:Samhandler"
	 */
	public DataObject lagreSamhandler(DataObject lagreSamhandlerRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "opprettSamhandler" operation on WSDL port "ns1:Samhandler"
	 */
	public DataObject opprettSamhandler(DataObject opprettSamhandlerRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "finnSamhandler" operation on WSDL port "ns1:Samhandler"
	 */
	public DataObject finnSamhandler(DataObject finnSamhandlerRequest)
			throws ServiceBusinessException;

}
