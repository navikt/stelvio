package no.nav.appclient.adapter;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.Properties;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import no.nav.appclient.util.ConfigPropertyNames;

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
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private Properties properties = null;

	public SecuredBFMConnection(Properties properties) {
		this.properties = properties;
	}

	@Override
	public BusinessFlowManagerService getBusinessFlowManagerService() throws NamingException, CreateException, RemoteException {
		String username = properties.getProperty(ConfigPropertyNames.username);
		String password = properties.getProperty(ConfigPropertyNames.password);

		// Build context
		Context ctx;
		if (!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)) {
			Hashtable<String, String> ctxProps = new Hashtable<String, String>();
			ctxProps.put(Context.SECURITY_PRINCIPAL, username);
			ctxProps.put(Context.SECURITY_CREDENTIALS, password);
			ctx = new InitialContext(ctxProps);
		} else {
			ctx = new InitialContext();
		}

		// Lookup the BusinessFlowManager bean
		if (logger.isDebugEnabled()) {
			logger.info("Looking up BusinessFlowManager bean with JNDI '{}'", getJndiName());
		}
		BusinessFlowManager bfm = ((BusinessFlowManagerHome) PortableRemoteObject.narrow(ctx.lookup(getJndiName()),
				BusinessFlowManagerHome.class)).create();

		// Report possible issues with the lookup
		if (null == bfm) {
			logger.error("Error with EJB lookup. The response object is 'null'");
		}

		return bfm;
	}
}
