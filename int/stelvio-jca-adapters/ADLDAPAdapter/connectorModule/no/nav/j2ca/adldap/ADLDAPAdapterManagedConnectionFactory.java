package no.nav.j2ca.adldap;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.j2ca.base.WBIManagedConnectionFactory;
import javax.resource.spi.*;
import javax.security.auth.Subject;


/**
 * @author lsb2812
 *
 */
public class ADLDAPAdapterManagedConnectionFactory extends WBIManagedConnectionFactory {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5980426091808443427L;
	// for logging
	private static Logger log = Logger.getLogger(ADLDAPAdapterManagedConnectionFactory.class.getName());
	private static final String CLASSNAME = "ADLDAPAdapterManagedConnectionFactory"; 

	// all ra.xml properties for connection handling
	
	private String serverUserId;
	private String serverUserIdPassword;
	private String serverHost;
	private String serverURL;
	private Integer serverPort;
	private String serverConnectionPooling;
	private String serverAuthenticationMode;	
	private String serverBasedDistinguishedName;
	private String serverBindDistinguishedName;
	private String serverSearchBaseContext;
	private String directoryProvider;
	private String ssl;
	
	/**
	 * 
	 */
	public ADLDAPAdapterManagedConnectionFactory() 
	{
		serverUserId = new String("");
		serverUserIdPassword = new String("");
		serverHost = new String("localhost");
		serverPort = new Integer(389);
		serverURL = new String("ldap://localhost:389");
		serverConnectionPooling = "false";
		serverAuthenticationMode = new String("simple");
		serverBasedDistinguishedName = new String("DC=test,DC=local");
		serverBindDistinguishedName = new String("CN=srvPensjon,OU=ServiceAccounts,DC=test,DC=local");
		serverSearchBaseContext = new String("DC=test,DC=local");
	}

	/* (non-Javadoc)
	 * @see javax.resource.spi.ManagedConnectionFactory#createManagedConnection(javax.security.auth.Subject, javax.resource.spi.ConnectionRequestInfo)
	 */
	public ManagedConnection createManagedConnection(Subject subject, ConnectionRequestInfo conReqInfo) throws javax.resource.ResourceException 
	{
		log.logp(Level.FINE, CLASSNAME, "createManagedConnection()","Managed connection entered.");
		ADLDAPAdapterManagedConnection managedConnection = null;
		ADLDAPAdapterResourceAdapter ra = (ADLDAPAdapterResourceAdapter)getResourceAdapter();
		log.logp(Level.FINE, CLASSNAME, "createManagedConnection()","Got resource adapter meta data for ID=" + ra.getAdapterID());
		managedConnection = new ADLDAPAdapterManagedConnection(this,subject, (ADLDAPAdapterConnectionRequestInfo)conReqInfo);
		log.logp(Level.FINE, CLASSNAME, "createManagedConnection()","Instanced managedConnection=" + managedConnection.toString());
		log.logp(Level.FINE, CLASSNAME, "createManagedConnection()","Managed connection exit.");
		return managedConnection;
	}

	/* (non-Javadoc)
	 * @see javax.resource.spi.ManagedConnectionFactory#createConnectionFactory(javax.resource.spi.ConnectionManager)
	 */
	public java.lang.Object createConnectionFactory(
			javax.resource.spi.ConnectionManager connMgr)
			throws javax.resource.ResourceException {
		return new ADLDAPAdapterConnectionFactory(connMgr, this);
	}
	
	/**
	 * @return Returns the serverAuthenticationMode.
	 */
	public String getServerAuthenticationMode() {
		return serverAuthenticationMode;
	}
	/**
	 * @param serverAuthenticationMode The serverAuthenticationMode to set.
	 */
	public void setServerAuthenticationMode(String serverAuthenticationMode) {
		String oldValue = this.serverAuthenticationMode;
		this.serverAuthenticationMode = serverAuthenticationMode;
		super.getPropertyChangeSupport().firePropertyChange("serverAuthenticationMode", oldValue, serverAuthenticationMode);
	}
	/**
	 * @return Returns the serverBasedDistinguishedName.
	 */
	public String getServerBasedDistinguishedName() {
		return serverBasedDistinguishedName;
	}
	/**
	 * @param serverBasedDistinguishedName The serverBasedDistinguishedName to set.
	 */
	public void setServerBasedDistinguishedName(String serverBasedDistinguishedName) 
	{
		String oldValue = this.serverBasedDistinguishedName;
		this.serverBasedDistinguishedName = serverBasedDistinguishedName;
		super.getPropertyChangeSupport().firePropertyChange("serverBasedDistinguishedName", oldValue, serverBasedDistinguishedName);
	}
	/**
	 * @return Returns the serverBindDistinguishedName.
	 */
	public String getServerBindDistinguishedName() {
		return serverBindDistinguishedName;
	}
	/**
	 * @param serverBindDistinguishedName The serverBindDistinguishedName to set.
	 */
	public void setServerBindDistinguishedName(String serverBindDistinguishedName) 
	{
		String oldValue = this.serverBindDistinguishedName;
		this.serverBindDistinguishedName = serverBindDistinguishedName;
		super.getPropertyChangeSupport().firePropertyChange("serverBindDistinguishedName", oldValue, serverBindDistinguishedName);
	}
	/**
	 * @return Returns the serverConnectionPooling.
	 */
	public String getServerConnectionPooling() {
		return serverConnectionPooling;
	}
	/**
	 * @param serverConnectionPooling The serverConnectionPooling to set.
	 */
	public void setServerConnectionPooling(String serverConnectionPooling) 
	{
		String oldValue = this.serverConnectionPooling;
		this.serverConnectionPooling = serverConnectionPooling;
		super.getPropertyChangeSupport().firePropertyChange("serverConnectionPooling", oldValue, serverConnectionPooling);

	}
	
	/**
	 * @return Returns the serverHost.
	 */
	public String getServerHost() {
		return serverHost;
	}
	
	/**
	 * @param serverHost The serverHost to set.
	 */
	public void setServerHost(String serverHost) {
		String oldValue = this.serverHost;
		this.serverHost = serverHost;
		super.getPropertyChangeSupport().firePropertyChange("serverHost", oldValue, serverHost);
	}
	
	/**
	 * @return Returns the serverPort.
	 */
	public Integer getServerPort() {
		return serverPort;
	}
	
	/**
	 * @param serverPort The serverPort to set.
	 */
	public void setServerPort(Integer serverPort) {
		Integer oldValue = this.serverPort;
		this.serverPort = serverPort;
		super.getPropertyChangeSupport().firePropertyChange("serverPort", oldValue, serverPort);
	}
	
	/**
	 * @return Returns the serverSearchBaseContext.
	 */
	public String getServerSearchBaseContext() {
		return serverSearchBaseContext;
	}
	/**
	 * @param serverSearchBaseContext The serverSearchBaseContext to set.
	 */
	public void setServerSearchBaseContext(String serverSearchBaseContext) {
		String oldValue = this.serverSearchBaseContext; 
		this.serverSearchBaseContext = serverSearchBaseContext;
		super.getPropertyChangeSupport().firePropertyChange("serverSearchBaseContext", oldValue, serverSearchBaseContext);
	}
	/**
	 * @return Returns the serverUserId.
	 */
	public String getServerUserId() {
		return serverUserId;
	}
	/**
	 * @param serverUserId The serverUserId to set.
	 */
	public void setServerUserId(String serverUserId) {
		String oldValue = this.serverUserId;
		this.serverUserId = serverUserId;
		super.getPropertyChangeSupport().firePropertyChange("serverUserId", oldValue, serverUserId);
	}
	/**
	 * @return Returns the serverUserIdPassword.
	 */
	public String getServerUserIdPassword() {
		return serverUserIdPassword;
	}
	/**
	 * @param serverUserIdPassword The serverUserIdPassword to set.
	 */
	public void setServerUserIdPassword(String serverUserIdPassword) {
		String oldValue = this.serverUserIdPassword;
		this.serverUserIdPassword = serverUserIdPassword;
		super.getPropertyChangeSupport().firePropertyChange("serverUserIdPassword", oldValue, serverUserIdPassword);
	}
	
	/**
	 * @return Returns the serverURL.
	 */
	public String getServerURL() {
		this.serverURL = "ldap://" + getServerHost() + ":" + getServerPort();
		return serverURL;
	}

	/**
	 * @return the directoryProvider
	 */
	public String getDirectoryProvider() {
		return directoryProvider;
	}

	/**
	 * @param directoryProvider the directoryProvider to set
	 */
	public void setDirectoryProvider(String directoryProvider) {
		String oldValue = this.directoryProvider;
		this.directoryProvider = directoryProvider;
		super.getPropertyChangeSupport().firePropertyChange("directoryProvider", oldValue, directoryProvider);
	}

	/**
	 * @return the s
	 */
	public String getSsl() {
		return ssl;
	}

	/**
	 * @param ssl the ssl to set
	 */
	public void setSsl(String ssl) {
		String oldValue = this.ssl;
		this.ssl = ssl;
		super.getPropertyChangeSupport().firePropertyChange("ssl", oldValue, ssl);
	}

	
	
}
