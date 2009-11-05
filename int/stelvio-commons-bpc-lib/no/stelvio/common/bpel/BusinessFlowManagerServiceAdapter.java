package no.stelvio.common.bpel;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import com.ibm.bpe.api.BusinessFlowManagerHome;
import com.ibm.bpe.api.BusinessFlowManagerService;
import com.ibm.bpe.api.PIID;
import com.ibm.bpe.api.ProcessException;
import com.ibm.bpe.api.ProcessInstanceData;
import com.ibm.bpe.api.ProcessTemplateData;
import com.ibm.bpe.api.QueryResultSet;
import com.ibm.websphere.sca.ServiceRuntimeException;
import com.ibm.websphere.security.auth.WSSubject;
import commonj.sdo.DataObject;
import commonj.sdo.Property;

/**
 * @author persona2c5e3b49756 Schnell
 * @author Erik Godding Boye (test@example.com)
 * 
 */
public class BusinessFlowManagerServiceAdapter {
	private final static String WS_INITIAL_CONTEXT_FACTORY = "com.ibm.websphere.naming.WsnInitialContextFactory";

	private final static String BFM_EJB_LOOKUP = "com/ibm/bpe/api/BusinessFlowManagerHome";

	private final String className = getClass().getName();

	private final Logger logger = Logger.getLogger(className);

	private BusinessFlowManagerService adaptee;

	public BusinessFlowManagerServiceAdapter() {
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
			adaptee = bfmHome.create();
		} catch (NamingException e) {
			throw new ServiceRuntimeException(e);
		} catch (RemoteException e) {
			throw new ServiceRuntimeException(e);
		} catch (CreateException e) {
			throw new ServiceRuntimeException(e);
		}
	}

	public ProcessTemplateData[] findProcessTemplates(String processTemplateNameBase) {
		try {
			String whereClause = null;
			String orderClause = null;

			// could have performance impact on DB if the template name not
			// provided, order the oldest to the newest
			if (processTemplateNameBase != null) {
				whereClause = "PROCESS_TEMPLATE.STATE = " + ProcessTemplateData.STATE_STARTED
						+ " AND PROCESS_TEMPLATE.NAME LIKE '" + processTemplateNameBase + "%'";
				orderClause = "PROCESS_TEMPLATE.VALID_FROM ASC";
			} else {
				whereClause = "PROCESS_TEMPLATE.STATE = " + ProcessTemplateData.STATE_STARTED;
				orderClause = "PROCESS_TEMPLATE.VALID_FROM ASC";
			}

			// query process templates
			ProcessTemplateData[] ptd = adaptee
					.queryProcessTemplates(whereClause, orderClause, (Integer) null, (TimeZone) null);
			return ptd;
		} catch (ProcessException e) {
			throw new ServiceRuntimeException(e);
		} catch (RemoteException e) {
			throw new ServiceRuntimeException(e);
		} catch (EJBException e) {
			throw new ServiceRuntimeException(e);
		}
	}

	public PIID findProcessInstance(ProcessTemplateData processTemplate, String boTagName, String boTagValue) {
		try {
			String selectClause = "DISTINCT PROCESS_INSTANCE.PIID, PROCESS_INSTANCE.CREATED";
			String whereClause = "PROCESS_INSTANCE.STATE = " + ProcessInstanceData.STATE_RUNNING
					+ " AND PROCESS_INSTANCE.PTID = ID('" + processTemplate.getID() + "')";
			String orderClause = "PROCESS_INSTANCE.CREATED ASC";

			QueryResultSet piResult = null;
			// query if security not enabled because than everyone see
			// the instances with api and queryAll doesn't work without
			// security
			if (WSSubject.getCallerPrincipal() == null) {
				logger.logp(Level.FINEST, className, "findMatchingProcessInstance()",
						"Execute query method because server security is diasbled.");
				piResult = adaptee.query(selectClause, whereClause, orderClause, (Integer) null, (Integer) null,
						(TimeZone) null);
			} else {
				// queryALL trenger BPESYSMON otherwise CWWBE0027E: No
				// authorization for the requested action.
				piResult = adaptee.queryAll(selectClause, whereClause, orderClause, (Integer) null, (Integer) null,
						(TimeZone) null);
			}

			while (piResult.next()) {
				PIID piid = (PIID) piResult.getOID(1);
				String created = piResult.getString(2);
				logger.logp(Level.FINE, className, "findMatchingProcessInstance()", " PROCESS_INSTANCE: ID=" + piid + " IDATE: "
						+ created);
				DataObject bo = getBoSpecification(piid);

				if (bo != null) {
					boolean hasMatchingProperty = hasMatchingProperty(bo, boTagName, boTagValue, true);
					logger.logp(Level.FINE, className, "findMatchingProcessInstance()", " PROCESS_INSTANCE: ID=" + piid
							+ " Analyze BO against tagName=" + boTagName + " and tagValue=" + boTagValue + " return="
							+ hasMatchingProperty);
					// don't look further we found the instance
					if (hasMatchingProperty) {
						return piid;
					}
				} else {
					logger.logp(Level.WARNING, className, "findMatchingProcessInstance()", " PROCESS_INSTANCE: ID=" + piid
							+ " Input DataObject could not retrieved!");
				}
			}
			return null;
		} catch (ProcessException e) {
			throw new ServiceRuntimeException(e);
		} catch (RemoteException e) {
			throw new ServiceRuntimeException(e);
		} catch (EJBException e) {
			throw new ServiceRuntimeException(e);
		}
	}

	/**
	 * @param processInstance
	 * @param bfm
	 * @return
	 */
	private DataObject getBoSpecification(PIID pid) {
		Object in = null;
		DataObject bo = null;
		try {
			in = adaptee.getInputMessage(pid).getObject();
		} catch (Exception e) {
			logger.logp(Level.SEVERE, className, "getBoSpecification()", e.getLocalizedMessage());
			e.printStackTrace();
			return bo;
		}
		if (in == null) {
			logger.logp(Level.WARNING, className, "getBoSpecification()", "Couldn't get input Message of process instance.");
			return bo;
		}
		if (!DataObject.class.isAssignableFrom(in.getClass()))
			return bo;
		bo = (DataObject) in;
		return bo;
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
	private boolean hasMatchingProperty(DataObject bo, String nameRegex, String nameValueRegex, Boolean childBo) {
		logger.logp(Level.FINEST, className, "hasMatchingProperty()", "PARAM: nameRegex=" + nameRegex + " nameValueRegex="
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
						logger.logp(Level.FINE, className, "hasMatchingProperty()", "MATCHED: " + property.getName() + "="
								+ strValue);
						return true;
					}
				} else {
					logger.logp(Level.WARNING, className, "hasMatchingProperty()",
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
