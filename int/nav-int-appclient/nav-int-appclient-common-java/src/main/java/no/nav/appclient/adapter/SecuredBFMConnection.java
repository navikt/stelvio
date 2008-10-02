package no.nav.appclient.adapter;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.Properties;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import no.nav.appclient.util.Constants;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bpe.api.BusinessFlowManager;
import com.ibm.bpe.api.BusinessFlowManagerHome;
import com.ibm.bpe.api.BusinessFlowManagerService;
import com.ibm.bpe.clientmodel.BFMConnection;

/**
 * This class encapsulate the EJB lookup perspective.
 * 
 * Since an instance of the <code>BFMConnection</code> class is needed to make
 * use of the API, and the its contructurs don't handle with secuity must this
 * class encapsulate that.
 * 
 * @author Andreas Roe
 */
public class SecuredBFMConnection extends BFMConnection {

	/**
	 * Logger instance
	 */
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	private Properties properties = null;

	public SecuredBFMConnection(Properties properties) {
		this.properties = properties;
	}

	@Override
	public BusinessFlowManagerService getBusinessFlowManagerService() throws NamingException, CreateException, RemoteException {
		String username = properties.getProperty(Constants.username);
		String password = properties.getProperty(Constants.password);

		// Build context
		Hashtable<String, String> ctxProps = new Hashtable<String, String>();
		ctxProps.put(Context.INITIAL_CONTEXT_FACTORY, "com.ibm.websphere.naming.WsnInitialContextFactory");
		ctxProps.put("java.naming.provider.url", properties.getProperty("java.naming.provider.url"));
		ctxProps.put(Context.PROVIDER_URL, properties.getProperty("providerURL"));

		if (!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)) {
			ctxProps.put(Context.SECURITY_PRINCIPAL, username);
			ctxProps.put(Context.SECURITY_CREDENTIALS, password);
		}

		Context ctx = new InitialContext(ctxProps);

		// Lookup the BusinessFlowManager bean
		LOGGER.info("Looking up BusinessFlowManager bean with JNDI '" + getJndiName() + "'");
		BusinessFlowManager bfm = ((BusinessFlowManagerHome) PortableRemoteObject.narrow(ctx.lookup(getJndiName()),
				BusinessFlowManagerHome.class)).create();

		// Report possible issues with the lookup
		if (null == bfm) {
			LOGGER.error("Error with EJB lookup. The response object is 'null'");
		}

		return bfm;
	}
}
