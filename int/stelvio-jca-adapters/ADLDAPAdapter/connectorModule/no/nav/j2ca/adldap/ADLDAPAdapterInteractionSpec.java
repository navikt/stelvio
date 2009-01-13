package no.nav.j2ca.adldap;

import com.ibm.j2ca.base.WBIInteractionSpec;

/**
 * @author utvikler
 *
 */
public class ADLDAPAdapterInteractionSpec extends WBIInteractionSpec {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1368795244484718621L;
	// ra definition
	public String serverUserId;
	public String serverUserIdPassword;
	public String serverHost;
	public String serverURL;
	public Integer serverPort;
	public String serverConnectionPooling;
	public String serverAuthenticationMode;	
	public String serverBasedDistinguishedName;
	public String serverBindDistinguishedName;
	public String serverSearchBaseContext;
	public String directoryProvider;
	public String ssl;

	//basic authentication
    public String username;
    public String password;
	
	/**
	 * 
	 */
	public ADLDAPAdapterInteractionSpec() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.ibm.j2ca.base.WBIInteractionSpec#getFunctionName()
	 */
	public String getFunctionName(){
		return super.getFunctionName();
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.j2ca.base.WBIInteractionSpec#setFunctionName(java.lang.String)
	 */
	public void setFunctionName(String arg0){
		super.setFunctionName(arg0);
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
		this.serverAuthenticationMode = serverAuthenticationMode;
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
	public void setServerBasedDistinguishedName(
			String serverBasedDistinguishedName) {
		this.serverBasedDistinguishedName = serverBasedDistinguishedName;
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
	public void setServerBindDistinguishedName(
			String serverBindDistinguishedName) {
		this.serverBindDistinguishedName = serverBindDistinguishedName;
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
	public void setServerConnectionPooling(String serverConnectionPooling) {
		this.serverConnectionPooling = serverConnectionPooling;
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
		this.serverHost = serverHost;
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
		this.serverPort = serverPort;
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
		this.serverSearchBaseContext = serverSearchBaseContext;
	}
	/**
	 * @return Returns the serverURL.
	 */
	public String getServerURL() {
		return serverURL;
	}
	/**
	 * @param serverURL The serverURL to set.
	 */
	public void setServerURL(String serverURL) {
		this.serverURL = serverURL;
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
		this.serverUserId = serverUserId;
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
		this.serverUserIdPassword = serverUserIdPassword;
	}
	
	 /**
	 * @return
	 */
	public String getPassword()
	{
	  return password;
	}

	/**
	 * @param password
	 */
	public void setPassword(String password)
	{
	  this.password = password;
	}

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
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
		this.directoryProvider = directoryProvider;
	}

	/**
	 * @return the ssl
	 */
	public String getSsl() {
		return ssl;
	}

	/**
	 * @param ssl the ssl to set
	 */
	public void setSsl(String ssl) {
		this.ssl = ssl;
	}
	
	
}