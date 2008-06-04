/**
 * ##[lineageStampBegin]##
 * ##[generatedBy:/nav-lib-frg/no/nav/lib/frg/inf/FinnTjenestepensjonsforhold.wsdl]##
 * ##[lineageStampEnd]##
 */
package frg.lib.nav.no.nav.lib.frg.inf.finn.tjenestepensjonsforhold;

import commonj.sdo.DataObject;

import com.ibm.websphere.sca.ServiceBusinessException;

/**
 * @generated (com.ibm.wbit.java.core)
 * An interface used for enabling a static programming model for the service. 
 * The methods provide synchronous invocations of the service operations.
 */
public interface FinnTjenestepensjonsforhold {
	public static final String PORTTYPE_NAME = "{http://nav-lib-frg/no/nav/lib/frg/inf/FinnTjenestepensjonsforhold}FinnTjenestepensjonsforhold";

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "finnTjenestepensjonsforhold" operation on WSDL port "FinnTjenestepensjonsforhold"
	 */
	public DataObject finnTjenestepensjonsforhold(
			DataObject finnTjenestepensjonsforholdRequest)
			throws ServiceBusinessException;

}
