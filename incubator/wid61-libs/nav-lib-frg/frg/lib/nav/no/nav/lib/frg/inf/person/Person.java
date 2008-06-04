/**
 * ##[lineageStampBegin]##
 * ##[generatedBy:/nav-lib-frg/no/nav/lib/frg/inf/Person.wsdl]##
 * ##[lineageStampEnd]##
 */
package frg.lib.nav.no.nav.lib.frg.inf.person;

import commonj.sdo.DataObject;

import com.ibm.websphere.sca.ServiceBusinessException;

/**
 * @generated (com.ibm.wbit.java.core)
 * An interface used for enabling a static programming model for the service. 
 * The methods provide synchronous invocations of the service operations.
 */
public interface Person {
	public static final String PORTTYPE_NAME = "{http://nav-lib-frg/no/nav/lib/frg/inf/Person}Person";

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "hentPerson" operation on WSDL port "ns1:Person"
	 */
	public DataObject hentPerson(DataObject hentPersonRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "hentKontoinformasjon" operation on WSDL port "ns1:Person"
	 */
	public DataObject hentKontoinformasjon(
			DataObject hentKontoinformasjonRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "hentPersonUtland" operation on WSDL port "ns1:Person"
	 */
	public DataObject hentPersonUtland(DataObject hentPersonUtlandRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "hentRelasjonsListe" operation on WSDL port "ns1:Person"
	 */
	public DataObject hentRelasjonsListe(DataObject hentRelasjonsListeRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "hentHistorikk" operation on WSDL port "ns1:Person"
	 */
	public DataObject hentHistorikk(DataObject hentHistorikkRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "hentBrukerprofil" operation on WSDL port "ns1:Person"
	 */
	public DataObject hentBrukerprofil(DataObject hentBrukerprofilRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "lagreEpost" operation on WSDL port "ns1:Person"
	 */
	public DataObject lagreEpost(DataObject lagreEpostRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "lagreTelefonnumre" operation on WSDL port "ns1:Person"
	 */
	public DataObject lagreTelefonnumre(DataObject lagreTelefonnumreRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "lagreSprak" operation on WSDL port "ns1:Person"
	 */
	public DataObject lagreSprak(DataObject lagreSprakRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "lagreAdresse" operation on WSDL port "ns1:Person"
	 */
	public DataObject lagreAdresse(DataObject lagreAdresseRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "lagreSamboerforhold" operation on WSDL port "ns1:Person"
	 */
	public DataObject lagreSamboerforhold(DataObject lagreSamboerforholdRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "lagreKontoinformasjon" operation on WSDL port "ns1:Person"
	 */
	public DataObject lagreKontoinformasjon(
			DataObject lagreKontoinformasjonRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "lagreBrukerprofil" operation on WSDL port "ns1:Person"
	 */
	public DataObject lagreBrukerprofil(DataObject lagreBrukerprofilRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "finnPerson" operation on WSDL port "ns1:Person"
	 */
	public DataObject finnPerson(DataObject finnPersonRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "erEgenansatt" operation on WSDL port "ns1:Person"
	 */
	public Boolean erEgenansatt(DataObject erEgenansattRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "hentEnhetId" operation on WSDL port "ns1:Person"
	 */
	public DataObject hentEnhetId(DataObject hentEnhetIdRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "hentFamilierelasjoner" operation on WSDL port "ns1:Person"
	 */
	public DataObject hentFamilierelasjoner(
			DataObject hentFamilierelasjonerRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "opprettSamboerforhold" operation on WSDL port "ns1:Person"
	 */
	public DataObject opprettSamboerforhold(
			DataObject opprettSamboerforholdRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "slettSamboerforhold" operation on WSDL port "ns1:Person"
	 */
	public DataObject slettSamboerforhold(DataObject slettSamboerforholdRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "hentSamboerforhold" operation on WSDL port "ns1:Person"
	 */
	public DataObject hentSamboerforhold(DataObject hentSamboerforholdRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "slettAdresse" operation on WSDL port "ns1:Person"
	 */
	public DataObject slettAdresse(DataObject slettAdresseRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "hentFamilierelasjonsHistorikk" operation on WSDL port "ns1:Person"
	 */
	public DataObject hentFamilierelasjonsHistorikk(
			DataObject hentFamilierelasjonsHistorikkRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "lagreStatsborgerskap" operation on WSDL port "ns1:Person"
	 */
	public DataObject lagreStatsborgerskap(
			DataObject lagreStatsborgerskapRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "lagreDodsdato" operation on WSDL port "ns1:Person"
	 */
	public DataObject lagreDodsdato(DataObject lagreDodsdatoRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "lagreSivilstand" operation on WSDL port "ns1:Person"
	 */
	public DataObject lagreSivilstand(DataObject lagreSivilstandRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "lagreNavn" operation on WSDL port "ns1:Person"
	 */
	public DataObject lagreNavn(DataObject lagreNavnRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "lagreFamilierelasjon" operation on WSDL port "ns1:Person"
	 */
	public DataObject lagreFamilierelasjon(
			DataObject lagreFamilierelasjonRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "opprettFamilierelasjon" operation on WSDL port "ns1:Person"
	 */
	public DataObject opprettFamilierelasjon(
			DataObject opprettFamilierelasjonRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "opphorKontoinformasjon" operation on WSDL port "ns1:Person"
	 */
	public DataObject opphorKontoinformasjon(
			DataObject opphorKontoinformasjonRequest)
			throws ServiceBusinessException;

}
