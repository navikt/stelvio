/**
 * 
 */
package no.stelvio.common.bpel;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import com.ibm.bpe.api.BusinessFlowManager;
import com.ibm.bpe.api.BusinessFlowManagerHome;
import com.ibm.bpe.api.BusinessFlowManagerService;
import com.ibm.bpe.api.OID;
import com.ibm.bpe.api.PIID;
import com.ibm.bpe.api.ProcessException;
import com.ibm.bpe.api.ProcessInstanceData;
import com.ibm.bpe.api.ProcessTemplateData;
import com.ibm.bpe.api.QueryResultSet;
import com.ibm.bpe.api.StaffResultSet;
import com.ibm.websphere.sca.Service;
import com.ibm.websphere.sca.ServiceManager;
import com.ibm.websphere.sca.ServiceRuntimeException;
import com.ibm.websphere.sca.addressing.EndpointReference;
import com.ibm.websphere.sca.scdl.Reference;
import com.ibm.websphere.security.auth.WSSubject;
import commonj.sdo.DataObject;
import commonj.sdo.Property;

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

	private final static String WS_INITIAL_CONTEXT_FACTORY = "com.ibm.websphere.naming.WsnInitialContextFactory";

	private final static String BFM_EJB_LOOKUP = "com/ibm/bpe/api/BusinessFlowManagerHome";

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
	public static Service getBprocModuleEndpoint(Service currentService, String processTemplateNameBase, String boTagName,
			String boTagValue) {
		// validate input parameters
		if (currentService == null | boTagName == null || boTagValue == null) {
			log
					.logp(Level.SEVERE, className, "getBprocModuleEndpoint()",
							"All or one of the provided input parameter is null!");
			// throw because we can't do anything without the right parameters
			throw new ServiceRuntimeException("All or one of the provided input parameter is null!");
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

		try {
			BusinessFlowManagerService bfm = getBusinessFlowManagerService();

			ProcessTemplateData[] processTemplates = getProcessTemplates(bfm, processTemplateNameBase);

			if (processTemplates != null) {
				for (int i = 0; i < processTemplates.length; i++) {
					ProcessTemplateData data = processTemplates[i];
					log.logp(Level.FINE, className, "getBprocModuleEndpoint()", "PROCESS_TEMPLATE: ID=" + data.getID()
							+ " VALID=" + convertCalendar(data.getValidFromTime()) + " MODULE=" + data.getApplicationName());
					String moduleName = data.getApplicationName();
					moduleName = moduleName.replaceFirst(SCA_MODULE_POST_ID, "");
					String selectClause = "DISTINCT PROCESS_INSTANCE.PIID, PROCESS_INSTANCE.CREATED";
					String whereClause = "PROCESS_INSTANCE.STATE = " + ProcessInstanceData.STATE_RUNNING
							+ " AND PROCESS_INSTANCE.PTID = ID('" + data.getID() + "')";
					String orderClause = "PROCESS_INSTANCE.CREATED ASC";

					QueryResultSet piResult = null;
					// query if security not enabled because than everyone see
					// the instances with api and queryAll doesn't work without
					// security
					if (WSSubject.getCallerPrincipal() == null) {
						log.logp(Level.FINEST, className, "getBprocModuleEndpoint()",
								"Execute query method because server security is diasbled.");
						piResult = bfm.query(selectClause, whereClause, orderClause, (Integer) null, (Integer) null,
								(TimeZone) null);
					} else {
						// queryALL trenger BPESYSMON otherwise CWWBE0027E: No
						// authorization for the requested action.
						piResult = bfm.queryAll(selectClause, whereClause, orderClause, (Integer) null, (Integer) null,
								(TimeZone) null);
					}

					while (piResult.next()) {
						log.logp(Level.FINE, className, "getBprocModuleEndpoint()", " PROCESS_INSTANCE: ID="
								+ piResult.getOID(1) + " IDATE: " + piResult.getString(2));
						DataObject bo = getBoSpecification((PIID) piResult.getOID(1), bfm);

						if (bo != null) {
							boolean hasMatchingProperty = hasMatchingProperty(bo, boTagName, boTagValue, true);
							log.logp(Level.FINE, className, "getBprocModuleEndpoint()", " PROCESS_INSTANCE: ID="
									+ piResult.getOID(1) + " Analyze BO against tagName=" + boTagName + " and tagValue="
									+ boTagValue + " return=" + hasMatchingProperty);
							// don't look further we found the instance
							if (hasMatchingProperty) {
								// set the new service
								int start = endpointReferenceAddress.indexOf(SCA_MODULE_PRE_ID);
								int end = endpointReferenceAddress.lastIndexOf("/");
								log.logp(Level.FINEST, className, "getBprocModuleEndpoint()", " PROCESS_INSTANCE: ID="
										+ piResult.getOID(1) + " CALC new servicename from " + "START=" + start + " END=" + end
										+ " NEW MODULE=" + moduleName);
								endpointReferenceAddress = endpointReferenceAddress.replaceFirst(endpointReferenceAddress
										.substring(start, end), moduleName);
								log.logp(Level.FINE, className, "getBprocModuleEndpoint()", " PROCESS_INSTANCE: ID="
										+ piResult.getOID(1) + " RETURN with new module endpoint reference url="
										+ endpointReferenceAddress);
								endpointReference.setAddress(endpointReferenceAddress);
								Service targetService = (Service) ServiceManager.INSTANCE.getService(reference,
										endpointReference);
								return targetService;
							}
						} else {
							log.logp(Level.WARNING, className, "getBprocModuleEndpoint()", " PROCESS_INSTANCE: ID="
									+ piResult.getOID(1) + " Input DataObject could not retrieved!");
						}
					}
				}
			} else {
				// no process template found, we do nothing just log and return
				// current service
				log.logp(Level.WARNING, className, "getBprocModuleEndpoint()",
						"Couldn't find any process template for match name " + processTemplateNameBase
								+ ", return current service reference!");
				return currentService;
			}

		} catch (Exception e) {
			log.logp(Level.SEVERE, className, "getBprocModuleEndpoint()", "Error: " + e.getLocalizedMessage());
			throw new ServiceRuntimeException(e);
		}

		// return the current service none of the matching bo criteria can't
		// found therefor return the original service
		log.logp(Level.WARNING, className, "getBprocModuleEndpoint()",
				"Couldn't find any matching DataObject criteria, return current service reference!");
		return currentService;
	}

	private static ProcessTemplateData[] getProcessTemplates(BusinessFlowManagerService bfm, String ptName) {
		try {
			String whereClause = null;
			String orderClause = null;

			// could have performance impact on DB if the template name not
			// provided, order the oldest to the newest
			if (ptName != null) {
				whereClause = "PROCESS_TEMPLATE.STATE = " + ProcessTemplateData.STATE_STARTED
						+ " AND PROCESS_TEMPLATE.NAME LIKE '" + ptName + "%'";
				orderClause = "PROCESS_TEMPLATE.VALID_FROM ASC";
			} else {
				whereClause = "PROCESS_TEMPLATE.STATE = " + ProcessTemplateData.STATE_STARTED;
				orderClause = "PROCESS_TEMPLATE.VALID_FROM ASC";
			}

			// query process templates
			ProcessTemplateData[] ptd = bfm.queryProcessTemplates(whereClause, orderClause, (Integer) null, (TimeZone) null);
			return ptd;
		} catch (ProcessException e) {
			throw new ServiceRuntimeException(e);
		} catch (RemoteException e) {
			throw new ServiceRuntimeException(e);
		} catch (EJBException e) {
			throw new ServiceRuntimeException(e);
		}
	}

	private static BusinessFlowManager getBusinessFlowManagerService() {
		try {
			// lookup the local home interface
			Hashtable<String, String> jndiConfig = new Hashtable<String, String>();
			jndiConfig.put(Context.INITIAL_CONTEXT_FACTORY, WS_INITIAL_CONTEXT_FACTORY);

			// this are the default context values but we run in the context of
			// PoJo and security subject should be available, if not we throw
			// SRE
			// jndiConfig.put(Context.PROVIDER_URL, "iiop:localhost:2809");
			// jndiConfig.put(Context.SECURITY_PRINCIPAL, "wid");
			// jndiConfig.put(Context.SECURITY_CREDENTIALS, "wid");

			InitialContext initialcontext = new InitialContext(jndiConfig);
			Object result = initialcontext.lookup(BFM_EJB_LOOKUP);
			BusinessFlowManagerHome bfmHome = (BusinessFlowManagerHome) PortableRemoteObject.narrow(result,
					com.ibm.bpe.api.BusinessFlowManagerHome.class);
			return bfmHome.create();
		} catch (NamingException e) {
			throw new ServiceRuntimeException(e);
		} catch (RemoteException e) {
			throw new ServiceRuntimeException(e);
		} catch (CreateException e) {
			throw new ServiceRuntimeException(e);
		}
	}

	/**
	 * @param processInstance
	 * @param bfm
	 * @return
	 */
	@SuppressWarnings("unused")
	private static DataObject getBoSpecification(PIID pid, BusinessFlowManagerService bfm) {
		Object in = null;
		DataObject bo = null;
		try {
			in = bfm.getInputMessage(pid).getObject();
		} catch (Exception e) {
			log.logp(Level.SEVERE, className, "getBoSpecification()", e.getLocalizedMessage());
			e.printStackTrace();
			return bo;
		}
		if (in == null) {
			log.logp(Level.WARNING, className, "setBoSpecification()", "Couldn't get input Message of process instance.");
			return bo;
		}
		if (!DataObject.class.isAssignableFrom(in.getClass()))
			return bo;
		bo = (DataObject) in;
		return bo;
	}

	/**
	 * Returns a string representation of a given field's value.
	 * 
	 * @param value
	 *            of a property
	 * @param converter
	 *            to use, maybe null
	 * @param locale
	 *            to use, if converter is specified
	 * @return string representation of a given field's value
	 */
	@SuppressWarnings("unused")
	private static String getPropertyValue(Object value) {
		if (value == null)
			return "null";
		else if (Calendar.class.isAssignableFrom(value.getClass()))
			return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(((Calendar) value).getTime());
		else if (Boolean.class.isAssignableFrom(value.getClass()))
			return ((Boolean) value).toString();
		else if (boolean.class.isAssignableFrom(value.getClass()))
			return ((Boolean) value).toString();
		else if (OID.class.isAssignableFrom(value.getClass()))
			return ((OID) value).toString();
		else if (Integer.class.isAssignableFrom(value.getClass()))
			return ((Integer) value).toString();
		else if (int.class.isAssignableFrom(value.getClass()))
			return ((Integer) value).toString();
		else if (String.class.isAssignableFrom(value.getClass()))
			return (String) value;
		else if (int[].class.isAssignableFrom(value.getClass()))
			return Arrays.toString((int[]) value);
		else if (List.class.isAssignableFrom(value.getClass()))
			return Arrays.toString(((List) value).toArray());
		else if (StaffResultSet.class.isAssignableFrom(value.getClass()))
			return Arrays.toString(((StaffResultSet) value).getGroupIDs())
					+ Arrays.toString(((StaffResultSet) value).getUserIDs());
		else
			throw new ServiceRuntimeException("Unsupported Type, Class: " + value.getClass().getName());
	}

	/**
	 * @param calendar
	 * @return
	 */
	@SuppressWarnings("unused")
	private static String convertCalendar(Calendar calendar) {
		if (calendar != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
			return sdf.format(calendar.getTime());
		} else
			return "null";
	}

	/**
	 * <p>
	 * Returns true if and only if a property with the criterias is found.
	 * </p>
	 * 
	 * @param bo
	 *            The DataObject to search in (we not search in arrays, list,
	 *            ...)
	 * @param nameRegex
	 *            The name of the property to search for, can be a regular
	 *            expression, can be null to disable this criteria
	 * @param nameValueRegex
	 *            The value of the object to search for, can be a regular
	 *            expression, can be null to disable this criteria
	 * @param childBo
	 *            Criteria to search also into child object
	 * @return returns true if and only if a property with the criterias is
	 *         found
	 */
	private static boolean hasMatchingProperty(DataObject bo, String nameRegex, String nameValueRegex, Boolean childBo) {

		log.logp(Level.FINEST, className, "hasMatchingProperty()", "PARAM: nameRegex=" + nameRegex + " nameValueRegex="
				+ nameValueRegex + " childBo=" + childBo);

		for (Iterator i = bo.getType().getProperties().iterator(); i.hasNext();) {
			// iterate over bo properties
			Property property = (Property) i.next();
			Object object = bo.get(property);
			// match name?
			if (property.getName().matches(nameRegex)) {
				// is a simple type, because only on this type we can look into.
				if (!property.isContainment()) {
					// convert to string
					String strValue = (String) object;
					if (strValue.matches(nameValueRegex)) {
						log.logp(Level.FINE, className, "hasMatchingProperty()", "MATCHED: " + property.getName() + "="
								+ strValue);
						return true;
					}
				} else {
					log.logp(Level.WARNING, className, "hasMatchingProperty()",
							"Can't match value against none simple type, return false!");
					return false;
				}

			}

			// recursive over child bo's?
			if (object instanceof DataObject && childBo) {
				return hasMatchingProperty((DataObject) object, nameRegex, nameValueRegex, true);
			}

		}
		return false;
	}

}
