/**
 * 
 */
package no.stelvio.common.bpel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.bpe.api.PIID;
import com.ibm.bpe.api.ProcessTemplateData;
import com.ibm.websphere.sca.Service;
import com.ibm.websphere.sca.ServiceManager;
import com.ibm.websphere.sca.ServiceRuntimeException;
import com.ibm.websphere.sca.addressing.EndpointReference;
import com.ibm.websphere.sca.scdl.Reference;
import com.ibm.websphere.security.auth.WSSubject;

/**
 * @author persona2c5e3b49756 Schnell
 * @author Erik Godding Boye (test@example.com)
 * 
 */
public class BPELHelperUtil {
	private final static String className = BPELHelperUtil.class.getName();

	private final static Logger log = Logger.getLogger(className);

	private final static String SCA_ADDRESS_URL = "sca:";

	private final static String SCA_MODULE_POST_ID = "App";

	private final static String SCA_MODULE_PRE_ID = "nav-";

	/**
	 * 
	 */
	public BPELHelperUtil() {
		log.logp(Level.FINE, className, "BPELHelperUtil()", "Init constructor");
	}

	/**
	 * @param currentService
	 * @param boTagName
	 * @param boTagValue
	 * @return
	 */
	public static Service getBprocModuleEndpoint(Service currentService, String processTemplateNameBase,
			Map<String, String> customProperties) {
		if (currentService == null) {
			log.logp(Level.SEVERE, className, "getBprocModuleEndpoint()", "Input parameter currentService is null!");
			throw new ServiceRuntimeException("Input parameter currentService is null!");
		}
		if (customProperties == null || customProperties.isEmpty()) {
			log.logp(Level.SEVERE, className, "getBprocModuleEndpoint()", "Input parameter customProperties is null or empty!");
			throw new ServiceRuntimeException("Input parameter customProperties is null or empty!");
		}

		// determine current endpoint -> should be sca and none of the parameter
		// should null if not return currentService
		Reference reference = currentService.getReference();
		EndpointReference endpointReference = currentService.getEndpointReference();
		if (reference == null || endpointReference == null) {
			log.logp(Level.WARNING, className, "getBprocModuleEndpoint()",
					"Couldn't determine SCA Service Endpoint reference and address, return current service reference!");
			return currentService;
		}

		String endpointReferenceAddress = endpointReference.getAddress();
		if (endpointReferenceAddress == null || endpointReferenceAddress.indexOf(SCA_ADDRESS_URL) == -1) {
			log
					.logp(Level.WARNING, className, "getBprocModuleEndpoint()",
							"Can't change SCA address binding because import isn't from type sca binding, return current service reference!");
			return currentService;
		}

		log.logp(Level.FINE, className, "getBprocModuleEndpoint()", "CURRENT SCA REFERENCE: " + reference.getName());
		log.logp(Level.FINE, className, "getBprocModuleEndpoint()", "CURRENT SCA URL: " + endpointReference.getAddress());
		log.logp(Level.FINE, className, "getBprocModuleEndpoint()", "CURRENT SCA CALLER SUBJECT: "
				+ WSSubject.getCallerPrincipal());

		BusinessFlowManagerServiceAdapter bfm = new BusinessFlowManagerServiceAdapter();
		ProcessTemplateData[] processTemplates = bfm.findProcessTemplates(processTemplateNameBase);
		if (processTemplates != null) {
			for (ProcessTemplateData processTemplate : processTemplates) {
				String applicationName = processTemplate.getApplicationName();
				log.logp(Level.FINE, className, "getBprocModuleEndpoint()", "PROCESS_TEMPLATE: ID=" + processTemplate.getID()
						+ " VALID=" + convertCalendar(processTemplate.getValidFromTime()) + " MODULE=" + applicationName);

				PIID piid = bfm.findProcessInstance(processTemplate, customProperties);

				if (piid != null) {
					String moduleName = applicationName.replaceFirst(SCA_MODULE_POST_ID, "");
					// set the new service
					int start = endpointReferenceAddress.indexOf(SCA_MODULE_PRE_ID);
					int end = endpointReferenceAddress.lastIndexOf("/");
					log.logp(Level.FINEST, className, "getBprocModuleEndpoint()", " PROCESS_INSTANCE: ID=" + piid
							+ " CALC new servicename from " + "START=" + start + " END=" + end + " NEW MODULE=" + moduleName);
					endpointReferenceAddress = endpointReferenceAddress.replaceFirst(endpointReferenceAddress.substring(start,
							end), moduleName);
					log.logp(Level.FINE, className, "getBprocModuleEndpoint()", " PROCESS_INSTANCE: ID=" + piid
							+ " RETURN with new module endpoint reference url=" + endpointReferenceAddress);
					endpointReference.setAddress(endpointReferenceAddress);
					Service targetService = (Service) ServiceManager.INSTANCE.getService(reference, endpointReference);
					return targetService;
				}
			}
		} else {
			// no process template found, we do nothing just log and return
			// current service
			log.logp(Level.WARNING, className, "getBprocModuleEndpoint()", "Couldn't find any process template for match name "
					+ processTemplateNameBase + ", return current service reference!");
			return currentService;
		}

		// return the current service none of the matching bo criteria can't
		// found therefor return the original service
		log.logp(Level.WARNING, className, "getBprocModuleEndpoint()",
				"Couldn't find any matching DataObject criteria, return current service reference!");
		return currentService;
	}

	/**
	 * @param calendar
	 * @return
	 */
	private static String convertCalendar(Calendar calendar) {
		if (calendar != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
			return sdf.format(calendar);
		} else
			return "null";
	}
}
