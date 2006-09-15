package no.nav.common.framework;

import no.nav.common.framework.error.ErrorCode;

/**
 * Framework error code constants.
 * <p/>
 * Constant naming conventions:
 * <pre>
 * ------------------------------------------------------------
 * - Configuration            CONFIG_*
 * - Service Location         SERVICE_*
 * - Service Delegation       SERVICE_FACADE_*
 * - ...
 * ------------------------------------------------------------ 
 * </pre>
 * 
 * @author person7553f5959484
 * @version $Revision: 2763 $ $Author: skb2930 $ $Date: 2006-02-09 14:38:13 +0100 (Thu, 09 Feb 2006) $
 */
public class FrameworkError extends ErrorCode {

	/**
	 * Allow constructions of new error codes 
	 * only inside this class and sub classes.
	 * 
	 * @param code the error code
	 */
	protected FrameworkError(int code) {
		super(code);
	}

	/** @deprecated bruk ErrorCode.UNSPECIFIED_ERROR istedet. */
	public static final FrameworkError UNEXPECTED_RESULT = new FrameworkError(9);

	// ------------------------------ Configuration Errors ------------------------------

	/** Konfigurasjonsfilen {0} ble ikke funnet. */
	public static final FrameworkError CONFIG_FILE_NOT_FOUND = new FrameworkError(10000);

	/** Konfigurasjonsfilen {0} kunne ikke lastes. */
	public static final FrameworkError CONFIG_FILE_LOAD_ERROR = new FrameworkError(10001);

	/** Konfigurasjonen {0} ble ikke funnet i konfigurasjonsfilen. */
	public static final FrameworkError CONFIG_NAME_NOT_FOUND = new FrameworkError(10002);

	/** Konfigurasjonen {0} kunne ikke bli opprettet. */
	public static final FrameworkError CONFIG_NAME_CREATE_ERROR = new FrameworkError(10003);

	/** Følgende feil oppsto under lesing av konfigurasjonen: {0}. */
	public static final FrameworkError CONFIG_READ_ERROR = new FrameworkError(10004);

	/** Følgende feil oppsto under skriving av konfigurasjonen: {0}. */
	public static final FrameworkError CONFIG_WRITE_ERROR = new FrameworkError(10005);

	/** Konfigurasjonen kunne ikke oppdateres. */
	public static final FrameworkError CONFIG_MBEAN_UPDATE_ERROR = new FrameworkError(10006);

	// ------------------------------ Cache Errors ------------------------------

	/** Kunne ikke konfigurere cache for kategori {1} ut i fra konfigurasjonsfilen {0}. */
	public static final FrameworkError CACHE_CONFIGURATION_ERROR = new FrameworkError(10100);

	/** Kunne ikke lese {0} fra det interne minnet med navn {1}. */
	public static final FrameworkError CACHE_GET_ERROR = new FrameworkError(10101);

	/** Kunne ikke skrive {1} til det interne minnet med navnet {1}. */
	public static final FrameworkError CACHE_PUT_ERROR = new FrameworkError(10102);

	/** Kunne ikke slette {0} fra det interne minnet med navn {1}. */
	public static final FrameworkError CACHE_REMOVE_ERROR = new FrameworkError(10103);

	/** Kunne ikke tømme det interne minnet med navn {0}. */
	public static final FrameworkError CACHE_CLEAR_ERROR = new FrameworkError(10104);

	/** Kunne ikke slette det interne minnet med navn {0}. */
	public static final FrameworkError CACHE_DESTROY_ERROR = new FrameworkError(10105);

	// ------------------------------ Service Errors ------------------------------

	/** Navnet på tjenesten er ikke spesifisert. */
	public static final FrameworkError SERVICE_NAME_MISSING = new FrameworkError(10200);

	/** Tjenesten {0} ble ikke funnet. */
	public static final FrameworkError SERVICE_NAME_NOT_FOUND = new FrameworkError(10201);

	/** Tjenesten kunne ikke opprettes. */
	public static final FrameworkError SERVICE_CREATION_ERROR = new FrameworkError(10202);

	/** Tjenesten er ufullstendig konfigurert, JNDI navnet er ikke spesifisert. */
	public static final FrameworkError SERVICE_CONFIG_ERROR = new FrameworkError(10203);

	/** Kunne ikke slå opp den distribuerte tjenesten {0}. */
	public static final FrameworkError SERVICE_LOOKUP_ERROR = new FrameworkError(10204);

	/** Kunne ikke oversette objektet til spesifiserte type. */
	public static final FrameworkError SERVICE_TYPE_ERROR = new FrameworkError(10205);

	/** Kunne ikke opprette konteksten. */
	public static final FrameworkError SERVICE_CONTEXT_CREATION_ERROR = new FrameworkError(10206);

	/** Kunne ikke lukke konteksten. */
	public static final FrameworkError SERVICE_CONTEXT_DELETION_ERROR = new FrameworkError(10207);

	/** Initialisering av tjenesten {0} feilet. */
	public static final FrameworkError SERVICE_INIT_ERROR = new FrameworkError(10208);

	/** Stenging av tjenesten feilet. */
	public static final FrameworkError SERVICE_DESTROY_ERROR = new FrameworkError(10209);

	/** Tjenesten kan ikke kalles fordi argumentet {0} mangler. */
	public static final FrameworkError SERVICE_INPUT_MISSING = new FrameworkError(10210);

	/** Tjenesten kan ikke kalles fordi argumentet {0} er av feil type. */
	// TODOS not in use
	public static final FrameworkError SERVICE_INPUT_WRONG_TYPE = new FrameworkError(10211);

	/** Kunne ikke finne den distribuerte tjenestefasaden {0}. */
	public static final FrameworkError SERVICE_FACADE_NOT_FOUND = new FrameworkError(10212);

	/** Den distribuerte tjenestefasaden {0} er av feil type. */
	public static final FrameworkError SERVICE_FACADE_TYPE_ERROR = new FrameworkError(10213);

	/** Kommunikasjonen med tjenestefasaden {0} feilet. */
	public static final FrameworkError SERVICE_FACADE_COMMUNICATION_ERROR = new FrameworkError(10214);

	//	------------------------------ Performance Monitoring Errors ------------------------------

	/** Kunne ikke konfigurere ytelsesmåling vha {0}. */
	public static final FrameworkError PERFORMANCE_CONFIGURATION_ERROR = new FrameworkError(10300);

	//	------------------------------ Error Handling Errors ------------------------------

	/** Kunne ikke konfigurere feilhåndtering vha {0}. */
	public static final FrameworkError ERROR_HANDLING_CONFIGURATION_ERROR = new FrameworkError(10400);

	/** Initialisering av feilhåndteringen feilet. */
	public static final FrameworkError ERROR_HANDLING_INITIALIZATION_ERROR = new FrameworkError(10401);

	//  ------------------------------ Monitoring Errors ------------------------------

	/** Ingen monitorer er konfigurert for monitorkjeden. */
	public static final FrameworkError MONITORING_CHAIN_NO_MONITORS_ERROR = new FrameworkError(10500);

	/** Ingen rapportmottakere er konfigurert for monitoren. */
	public static final FrameworkError MONITORING_NO_REPORT_DESTINATION_ERROR = new FrameworkError(10501);

	/** Rapportnavn er ikke spesifisert for monitoren. */
	public static final FrameworkError MONITORING_NO_REPORT_NAME_ERROR = new FrameworkError(10502);

	/** Feilmonitoreringen er ugyldig konfigurert. Antall feil må være større enn 0 og mindre enn måleintervallet. */
	public static final FrameworkError MONITORING_ERROR_MEASUREMENT_ERROR = new FrameworkError(10503);

	/** Tjenesten er midlertidig slått av. */
	public static final FrameworkError MONITORING_ERROR_SERVICE_DISABLED = new FrameworkError(10504);

	/** Antall rapporter må være større enn 0 for feilmonitoreringens rapportmottaker. */
	public static final FrameworkError MONITORING_ERROR_INVALID_REPORT_SIZE_ERROR = new FrameworkError(10505);

	/** Feilmonitoreringens rapportmottaker kunne ikke registreres i MBeanServer'en fordi navnet er ugyldig. */
	public static final FrameworkError MONITORING_ERROR_INVALID_OBJECT_NAME_ERROR = new FrameworkError(10506);

	/** Feilmonitoreringens rapportmottaker kunne ikke registreres i MBeanServer'en. */
	public static final FrameworkError MONITORING_ERROR_MBEAN_REGISTRATION_ERROR = new FrameworkError(10507);

	// -------------------------------- Codes table errors ---------------------------------

	/** Kodetabellen {0} finnes ikke eller er tom. */
	public static final FrameworkError CODES_TABLE_NOT_FOUND = new FrameworkError(10700);

	/** Initialisering av kodetabellen feilet. */
	public static final FrameworkError CODES_TABLE_INIT_ERROR = new FrameworkError(10701);

	// -------------------------------- Locale errors ---------------------------------

	/** Språkkoden '{0}' er ikke definert i ISO-639. */
	public static final FrameworkError LOCALE_LANGUAGE_NOT_SUPPORTED = new FrameworkError(10800);

	/** Landkoden '{0}' er ikke definert i ISO-3166. */
	public static final FrameworkError LOCALE_COUNTRY_NOT_SUPPORTED = new FrameworkError(10801);

	/** Språk og landkoden '{0}' er ikke tilgjengelig, benytt en annen tilgjengelig kode eller installer den ønskede koden på systemet. */
	public static final FrameworkError LOCALE_NOT_AVAILABLE = new FrameworkError(10802);

	/** Språk og landkode kan ikke endres pga manglende rettigheter. Legg til grant {\n\tpermission java.util.PropertyPermission \"user.language\", \"write\";\n} */
	public static final FrameworkError LOCALE_SECURITY_FAILURE = new FrameworkError(10803);

	//	-------------------------------- Utility errors ---------------------------------
	/** En feil oppsto under behandling av objektet. Kunne ikke lese attributtet {0} på {1}. */
	public static final FrameworkError REFLECTION_GET_PROPERTY_ERROR = new FrameworkError(10900);

	/** En feil oppsto under behandling av objektet. Kunne ikke endre attributtet {0} på {1} */
	public static final FrameworkError REFLECTION_SET_PROPERTY_ERROR = new FrameworkError(10901);

	/** En feil oppsto under oppretting av objektet. Kunne ikke finne klassen {0}. */
	public static final FrameworkError REFLECTION_INSTANTIATION_ERROR = new FrameworkError(10902);

	// -------------------------------- Filter errors ---------------------------------

	/** Filteret med navn {0} feilet. */
	public static final FrameworkError FILTER_PROCESSING_FAILED = new FrameworkError(20000);

	// -------------------------------- Web errors ---------------------------------

	/** Kunne ikke tolke informasjonen sendt fra brukeren. Dette kan skyldes dobbelt-klikk i skjermbilde. */
	// TODOS put this into db
	public static final FrameworkError WEB_DOUBLE_CLICK = new FrameworkError(21000);

	// ------------------------------ Business Errors ------------------------------

	/** Transaksjonen ble ikke fullført. Dette kan skyldes at operasjonen tok for lang tid. */
	public static final FrameworkError TRANSACTION_ROLLED_BACK_ERROR = new FrameworkError(30000);

	// ------------------------------ Hibernate Errors ------------------------------

	/**
	 * Hibernate kunne ikke finne konfigurasjonen for attributtet {0} i klassen {1} i mappingfilen.
	 *
	 * @deprecated Use {@link #JCA_MAPPING_ERROR}
	 */
	public static final FrameworkError HIBERNATE_MAPPING_ERROR = new FrameworkError(30100);

	/** Hibernate fant ikke spørringen med navn {0} i konfigurasjonsfilen. */
	public static final FrameworkError HIBERNATE_QUERY_CREATE_ERROR = new FrameworkError(30101);

	/** Hibernate kunne ikke fullføre spørringen. {0}. */
	public static final FrameworkError HIBERNATE_QUERY_EXECUTION_ERROR = new FrameworkError(30102);

	/** Hibernate kunne ikke fullføre lagreoperasjonen. */
	public static final FrameworkError HIBERNATE_UPDATE_EXECUTION_ERROR = new FrameworkError(30103);

	/** Hibernate kunne ikke initialisere avhengigheten til {0}. */
	public static final FrameworkError HIBERNATE_INITIALIZATION_ERROR = new FrameworkError(30104);

	// -------------------------------- Batch errors ---------------------------------

	/** Kunne ikke starte transaksjonen */
	public static final FrameworkError BATCH_TRANSACTION_BEGIN_ERROR = new FrameworkError(30200);

	/** Kunne ikke rulle tilbake transaksjonen */
	public static final FrameworkError BATCH_TRANSACTION_ROLLBACK_ERROR = new FrameworkError(30201);

	/** Kunne ikke fullføre transaksjonen */
	public static final FrameworkError BATCH_TRANSACTION_COMMIT_ERROR = new FrameworkError(30202);

	/** Kunne ikke lese egenskapene for batchen */
	public static final FrameworkError BATCH_PROPETIES_READ_ERROR = new FrameworkError(30203);

	/** Kunne ikke sette egenskapene for batchen */
	public static final FrameworkError BATCH_PROPETIES_WRITE_ERROR = new FrameworkError(30204);

	/** Alle atomiske arbeidsenheter (LUW) feilet for batchen */
	public static final FrameworkError BATCH_TRANSACTION_ALL_LUW_FAILED = new FrameworkError(30205);

	//	------------------------------ JMS Errors ------------------------------

	/** Initialisering av meldingstjenesten feilet fordi {0} ikke er spesifisert. */
	public static final FrameworkError JMS_SERVICE_PROPERTY_MISSING = new FrameworkError(40000);

	/** Meldingen kan ikke håndteres fordi den er av en type som ikke er støttet. */
	public static final FrameworkError JMS_INVALID_MESSAGE_TYPE = new FrameworkError(40002);

	/** Kunne ikke opprette forbindelse med meldingskøen. */
	public static final FrameworkError JMS_CONNECTION_ERROR = new FrameworkError(40003);

	/** Kunne ikke opprette en sesjon med meldingskøen. */
	public static final FrameworkError JMS_SESSION_CREATION_ERROR = new FrameworkError(40004);

	/** Kunne ikke opprette en meldingssender mot køen. */
	public static final FrameworkError JMS_SENDER_CREATION_ERROR = new FrameworkError(40005);

	/** Meldingen kunne ikke sendes til køen. */
	public static final FrameworkError JMS_SEND_ERROR = new FrameworkError(40006);

	/** Konfigurasjonen av svarkøen feilet. */
	public static final FrameworkError JMS_INVALID_RETURN_QUEUE = new FrameworkError(40007);

	/** Kunne ikke opprette en meldingsmottaker på køen. */
	public static final FrameworkError JMS_RECEIVER_CREATION_ERROR = new FrameworkError(40008);

	/** Meldingen kunne ikke mottas fra køen. */
	public static final FrameworkError JMS_RECEIVE_ERROR = new FrameworkError(40009);

	/** Kunne ikke opprette meldingen. */
	public static final FrameworkError JMS_MESSAGE_CREATE_ERROR = new FrameworkError(40010);

	/** Initialisering av meldingstjenesten feilet fordi {0} ikke er spesifisert. */
	public static final FrameworkError JMS_INVALID_SYNCH_CONFIG_ERROR = new FrameworkError(40011);

	/** Den midlertidige svarkøen kunne ikke opprettes. */
	public static final FrameworkError JMS_ERROR_CREATE_TEMP_QUEUE = new FrameworkError(40012);

	/** Konfigurasjonen av meldingsformatereren feilet pga {0}. */
	public static final FrameworkError JMS_XML_FORMATTER_CONFIG_ERROR = new FrameworkError(40013);

	/** Meldingsformatereren kunne ikke lese filen {0}. */
	public static final FrameworkError JMS_XML_FORMATTER_READ_ERROR = new FrameworkError(40014);

	/** Meldingsformattereren kunne ikke finne argumentet {0}. */
	public static final FrameworkError JMS_MISSING_INPUT_ERROR = new FrameworkError(40015);

	/** Meldingsformattereren kunne ikke finne malen {0}. */
	public static final FrameworkError JMS_XML_MISSING_TEMPLATE_ERROR = new FrameworkError(40016);

	/** Kunne ikke lese {0} egenskapen. */
	public static final FrameworkError JMS_PROPERTY_READ_ERROR = new FrameworkError(40017);

	/** Kunne ikke sette {0} i headeren til meldingen. */
	public static final FrameworkError JMS_HEADER_SET_ERROR = new FrameworkError(40018);

	/** Konfigurasjonen av meldingshåndtereren feilet pga {0}. */
	public static final FrameworkError JMS_HANDLER_CONFIG_ERROR = new FrameworkError(40019);

	/** Konfigurasjon av meldingshåndtererens XML-parser feilet. */
	public static final FrameworkError JMS_XML_HANDLER_PARSER_ERROR = new FrameworkError(40020);

	/** Meldingshåndtereren kunne ikke lese meldingen. */
	public static final FrameworkError JMS_MESSAGE_READ_ERROR = new FrameworkError(40021);

	/** Meldingshåndtereren kunne ikke opprette objektet. */
	public static final FrameworkError JMS_OBJECT_CREATE_ERROR = new FrameworkError(40022);

	/** Meldingshåndtereren kunne ikke kalle metoden til objektet. */
	public static final FrameworkError JMS_XML_HANDLER_METHOD_CALL_ERROR = new FrameworkError(40023);

	/** Meldingshåndtereren kunne ikke lese headeren til meldingen. */
	public static final FrameworkError JMS_HEADER_READ_ERROR = new FrameworkError(40024);

	/** Meldingssystemet feilet med følgende forklaring: {0}. */
	public static final FrameworkError JMS_ENTERPRISE_SYSTEM_ERROR = new FrameworkError(40025);

	/** Meldingsformattreren kunne ikke convertere verdi {0} i klasse {1} */
	public static final FrameworkError JMS_XML_CONVERT_ERRROR = new FrameworkError(40026);

	//	------------------------------ JCA Errors ------------------------------

	/** Adaptertjenesten kunne ikke opprette forbindelse med JNDI-navnet {0}. */
	public static final FrameworkError JCA_GET_CONNECTION_ERROR = new FrameworkError(40100);

	/** Adaptertjenesten kunne ikke lukke forbindelsen til systemet. */
	// TODOS update text in db
	public static final FrameworkError JCA_CLOSE_CONNECTION_ERROR = new FrameworkError(40101);

	/** Adaptertjenesten kunne ikke opprette en interaksjon med adapteren. */
	public static final FrameworkError JCA_CREATE_INTERACTION_ERROR = new FrameworkError(40102);

	/** Adaptertjenesten kunne ikke lese konfigurasjonen fra {0}. */
	// TODOS feil bruk av denne
	public static final FrameworkError JCA_GET_CONFIG_ERROR = new FrameworkError(40103);

	/** Adaptertjenestens interaksjon med baksystemet feilet. */
	public static final FrameworkError JCA_INTERACT_ERROR = new FrameworkError(40104);

	/** Initialiseringen av adaptertjenesten feilet. Konfigurasjon av {0} mangler. */
	public static final FrameworkError JCA_SERVICE_PROPERTY_MISSING = new FrameworkError(40105);

	/** CICS Transaction Gateway har for mye å gjøre. */
	// TODOS put this into db
	public static final FrameworkError JCA_WORK_WAS_REFUSED_ERROR = new FrameworkError(40106);

	/** Feil i mapping-filen for klassen {0}: kunne ikke finne konfigurasjonen for attributtet {1}. */
	// TODOS put this into db
	public static final FrameworkError JCA_MAPPING_ERROR = new FrameworkError(40107);

	/** Feil i konverteringen mellom JCA-recorden og objekt-instansen (type: {0}). */
	// TODOS put this into db
	public static final FrameworkError JCA_RECORD_CONVERSION_ERROR = new FrameworkError(40108);

	// -------------------------------- JNDI errors ---------------------------------

	/** Katalogsøket feilet. Konteksten var '{0}', filteret '{1}'. */
	public static final FrameworkError DIRECTORY_SEARCH_FAILED = new FrameworkError(40200);

	/** Kunne ikke lese attributtet {0} i svaret fra katalogtjenesten. */
	public static final FrameworkError DIRECTORY_ATTRIBUTE_VALUE_ERROR = new FrameworkError(40201);

	/** Kunne ikke skrive attributet {0} til konteksten '{1}'. */
	public static final FrameworkError DIRECTORY_ATTRIBUTE_ADD_ERROR = new FrameworkError(40202);

	/** Kunne ikke slette attributtet {0} i konteksten '{1}'. */
	public static final FrameworkError DIRECTORY_ATTRIBUTE_DEL_ERROR = new FrameworkError(40203);

	/** Kunne ikke legge til attributtet {0} i konteksten '{1}'. attributtet finnes allerede*/
	public static final FrameworkError DIRECTORY_ATTRIBUTE_ADD_ERROR_NAME_ALREADY_BOUND = new FrameworkError(40204);

	/** Kunne ikke legge til attributtet {0} i konteksten '{1}' brukerid finnes ikke.*/
	public static final FrameworkError DIRECTORY_ATTRIBUTE_ADD_ERROR_NAME_NOT_FOUND = new FrameworkError(40205);
	
	/** Kunne ikke fjerne til attributtet {0} i konteksten '{1}' attributtet er allerede fjernet*/
	public static final FrameworkError DIRECTORY_ATTRIBUTE_REMOVE_NOT_EXISTING = new FrameworkError(40206);


	// -------------------------------- Legacy System errors ---------------------------------
	/** {0} er ikke tilgjengelig. */
	public static final FrameworkError SYSTEM_UNAVAILABLE_ERROR = new FrameworkError(40300);

	/** {0} svarer ikke. */
	public static final FrameworkError SYSTEM_NOT_ANSWERING = new FrameworkError(40301);

	// -------------------------------- ELDOK Errors -----------------------------------------
	/** Kunne ikke aksessere dokumentet i ELDOK. */
	public static final FrameworkError ELDOK_DOC_ERROR = new FrameworkError(40400);

	/** Kunne ikke aksessere journalpost i ELDOK. */
	public static final FrameworkError ELDOK_JOURNALPOST_ERROR = new FrameworkError(40401);

	/** Kunne ikke aksessere sak i ELDOK. */
	public static final FrameworkError ELDOK_SAK_ERROR = new FrameworkError(40402);

	/** Kunne ikke opprette forbindelse med ELDOK. */
	public static final FrameworkError ELDOK_CONNECTIVITY_ERROR = new FrameworkError(40403);

	/** Konfigurasjon av integrasjonen mot ELDOK feilet. Feilen skyldes: {0}. */
	public static final FrameworkError ELDOK_CONFIG_ERROR = new FrameworkError(40404);

	/** Integrasjonen mot ELDOK støtter ikke tjenesten {0}. */
	public static final FrameworkError ELDOK_FUNCTION_ERROR = new FrameworkError(40405);

	// -------------------------------- OPPDRAG Errors -----------------------------------------
	/** Konfigurasjon av integrasjonen mot Oppdrag feilet. Konfigurasjon av {0} er ikke spesifisert. */
	public static final FrameworkError OPPDRAG_SERVICE_PROPERTY_MISSING = new FrameworkError(40500);

	/** Svaret fra Oppdrag inneholder ikke data. */
	public static final FrameworkError OPPDRAG_RECEIVE_ERROR = new FrameworkError(40501);

	/** Oppdrag ble ikke kontaktet fordi forespørselen mangler data. */
	public static final FrameworkError OPPDRAG_MISSING_INPUT_ERROR = new FrameworkError(40502);
	
	// -------------------------------- TSS Errors -----------------------------------------
	/** Svaret fra TSS inneholder ikke data. */
	// TODOS should be in the db/spreadsheet
	public static final FrameworkError TSS_RECEIVE_ERROR = new FrameworkError(40503);
	
	// -------------------------------- TPS Errors -----------------------------------------
	/** Feil fra TPS. Statuskode: 08. Feilmelding {0}. */
	// TODOS the error code and the db table should be corrected to be in the correct range (40600)
	public static final FrameworkError TPS_ERROR_08 = new FrameworkError(100140);

	/** Feil fra TPS. Statuskode: 09. Feilmelding {0}. */
	// TODOS the error code and the db table should be corrected to be in the correct range (40601)
	public static final FrameworkError TPS_ERROR_09 = new FrameworkError(100141);

	/** Feil fra TPS. Statuskode: 12. Feilmelding {0}. */
	// TODOS the error code and the db table should be corrected to be in the correct range (40602)
	public static final FrameworkError TPS_ERROR_12 = new FrameworkError(100142);
}
