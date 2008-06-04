/**
 * ##[lineageStampBegin]##
 * ##[generatedBy:/nav-lib-frg/no/nav/lib/frg/inf/Tjenestepensjon.wsdl]##
 * ##[lineageStampEnd]##
 */
package frg.lib.nav.no.nav.lib.frg.inf.tjenestepensjon;

import commonj.sdo.DataObject;

import com.ibm.websphere.sca.ServiceBusinessException;

/**
 * @generated (com.ibm.wbit.java.core)
 * An interface used for enabling a static programming model for the service. 
 * The methods provide synchronous invocations of the service operations.
 */
public interface Tjenestepensjon {
	public static final String PORTTYPE_NAME = "{http://nav-lib-frg/no/nav/lib/frg/inf/Tjenestepensjon}Tjenestepensjon";

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "hentStillingsprosentListe" operation on WSDL port "Tjenestepensjon"
	 */
	public DataObject hentStillingsprosentListe(
			DataObject hentStillingsprosentListeRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "hentTjenestepensjonForholdYtelse" operation on WSDL port "Tjenestepensjon"
	 */
	public DataObject hentTjenestepensjonForholdYtelse(
			DataObject hentTjenestepensjonForholdYtelseRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "hentTjenestepensjonInfo" operation on WSDL port "Tjenestepensjon"
	 */
	public DataObject hentTjenestepensjonInfo(
			DataObject hentTjenestepensjonInfoRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "lagreTjenestepensjonsforhold" operation on WSDL port "Tjenestepensjon"
	 */
	public DataObject lagreTjenestepensjonsforhold(
			DataObject lagreTjenestepensjonsforholdRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "lagreTjenestepensjonYtelse" operation on WSDL port "Tjenestepensjon"
	 */
	public DataObject lagreTjenestepensjonYtelse(
			DataObject lagreTjenestepensjonYtelseRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "opprettTjenestepensjonsforhold" operation on WSDL port "Tjenestepensjon"
	 */
	public DataObject opprettTjenestepensjonsforhold(
			DataObject opprettTjenestepensjonsforholdRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "opprettTjenestepensjonSimulering" operation on WSDL port "Tjenestepensjon"
	 */
	public DataObject opprettTjenestepensjonSimulering(
			DataObject opprettTjenestepensjonSimuleringRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "opprettTjenestepensjonYtelse" operation on WSDL port "Tjenestepensjon"
	 */
	public DataObject opprettTjenestepensjonYtelse(
			DataObject opprettTjenestepensjonYtelseRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "simulerOffentligTjenestepensjon" operation on WSDL port "Tjenestepensjon"
	 */
	public DataObject simulerOffentligTjenestepensjon(
			DataObject simulerOffentligTjenestepensjonRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "slettTjenestepensjonsforhold" operation on WSDL port "Tjenestepensjon"
	 */
	public void slettTjenestepensjonsforhold(
			DataObject slettTjenestepensjonsforholdRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "slettTjenestepensjonSimulering" operation on WSDL port "Tjenestepensjon"
	 */
	public void slettTjenestepensjonSimulering(
			DataObject slettTjenestepensjonSimuleringRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "slettTjenestepensjonYtelse" operation on WSDL port "Tjenestepensjon"
	 */
	public void slettTjenestepensjonYtelse(
			DataObject slettTjenestepensjonYtelseRequest)
			throws ServiceBusinessException;

}
