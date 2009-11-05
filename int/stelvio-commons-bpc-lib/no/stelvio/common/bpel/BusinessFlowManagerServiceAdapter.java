package no.stelvio.common.bpel;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.Map;
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

/**
 * @author persona2c5e3b49756 Schnell
 * @author Erik Godding Boye (test@example.com)
 * 
 */
class BusinessFlowManagerServiceAdapter {
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

	/**
	 * Find matching process templates based on input parameter(s)
	 * 
	 * @param processTemplateNameBase
	 *            the base name of the process template (will be used with the
	 *            like operator to find matching process templates)
	 * @return array of process templates
	 */
	public ProcessTemplateData[] findProcessTemplates(String processTemplateNameBase) {
		try {
			String whereClause = null;
			String orderByClause = null;

			// could have performance impact on DB if the template name not
			// provided, order the oldest to the newest
			if (processTemplateNameBase != null) {
				whereClause = "PROCESS_TEMPLATE.STATE = " + ProcessTemplateData.STATE_STARTED
						+ " AND PROCESS_TEMPLATE.NAME LIKE '" + processTemplateNameBase + "%'";
				orderByClause = "PROCESS_TEMPLATE.VALID_FROM ASC";
			} else {
				whereClause = "PROCESS_TEMPLATE.STATE = " + ProcessTemplateData.STATE_STARTED;
				orderByClause = "PROCESS_TEMPLATE.VALID_FROM ASC";
			}

			return adaptee.queryProcessTemplates(whereClause, orderByClause, null, null);
		} catch (ProcessException e) {
			throw new ServiceRuntimeException(e);
		} catch (RemoteException e) {
			throw new ServiceRuntimeException(e);
		} catch (EJBException e) {
			throw new ServiceRuntimeException(e);
		}
	}

	/**
	 * Find process instance (ID) based on input parameter(s)
	 * 
	 * @param processTemplate
	 *            the process template to search for instances of
	 * @param customProperties
	 *            custom properties that you want to filter process instance on
	 *            (map key should be set to the custom property name, map value
	 *            should be set to the custom property value)
	 * @return process instance ID
	 */
	public PIID findProcessInstance(ProcessTemplateData processTemplate, Map<String, String> customProperties) {
		try {
			String selectClause = "DISTINCT PROCESS_INSTANCE.PIID, PROCESS_INSTANCE.CREATED";
			StringBuilder sb = new StringBuilder();
			sb.append("PROCESS_INSTANCE.STATE = ").append(ProcessInstanceData.STATE_RUNNING);
			sb.append(" AND PROCESS_INSTANCE.PTID = ID('").append(processTemplate.getID()).append("')");
			int index = 1;
			for (Map.Entry<String, String> customProperty : customProperties.entrySet()) {
				String viewName = "PROCESS_ATTRIBUTE" + index++;
				sb.append(" AND ").append(viewName).append(".NAME").append(" = '").append(customProperty.getKey()).append("'");
				sb.append(" AND ").append(viewName).append(".VALUE").append(" = '").append(customProperty.getValue()).append(
						"'");
			}
			String whereClause = sb.toString();
			String orderByClause = "PROCESS_INSTANCE.CREATED ASC";

			QueryResultSet queryResultSet = null;
			// query if security not enabled because than everyone see
			// the instances with api and queryAll doesn't work without
			// security
			if (WSSubject.getCallerPrincipal() == null) {
				logger.logp(Level.FINEST, className, "findMatchingProcessInstance()",
						"Execute query method because server security is diasbled.");
				queryResultSet = adaptee.query(selectClause, whereClause, orderByClause, 1, null);
			} else {
				// queryALL trenger BPESYSMON otherwise CWWBE0027E: No
				// authorization for the requested action.
				queryResultSet = adaptee.queryAll(selectClause, whereClause, orderByClause, 0, 1, null);
			}

			if (queryResultSet.next()) {
				PIID piid = (PIID) queryResultSet.getOID(1);
				String created = queryResultSet.getString(2);
				logger.logp(Level.FINE, className, "findMatchingProcessInstance()", " PROCESS_INSTANCE: ID=" + piid
						+ " IDATE: " + created);
				return piid;
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
}
