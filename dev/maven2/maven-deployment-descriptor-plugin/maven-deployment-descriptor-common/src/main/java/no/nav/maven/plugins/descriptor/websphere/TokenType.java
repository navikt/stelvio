package no.nav.maven.plugins.descriptor.websphere;


import com.ibm.etools.webservice.wscbnd.BasicAuth;
import com.ibm.etools.webservice.wscommonbnd.CallbackHandler;
import com.ibm.etools.webservice.wscommonbnd.JAASConfig;
import com.ibm.etools.webservice.wscommonbnd.PartReference;
import com.ibm.etools.webservice.wscommonbnd.TokenConsumer;
import com.ibm.etools.webservice.wscommonbnd.TokenGenerator;
import com.ibm.etools.webservice.wscommonbnd.ValueType;

public class TokenType {

	public final static String USERNAME_LOCALNAME = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#UsernameToken";
	private final static String USERNAME_URI = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#UsernameToken";
	private final static String LTPA_URI = "http://www.ibm.com/websphere/appserver/tokentype/5.0.2";
	private final ValueType valueType;
	private TokenConsumer ltpaTokenConsumer;
	private TokenConsumer usernameTokenConsumer;
	private TokenGenerator tokenGenerator;
	private CallbackHandler callbackHandler;
	private String localName;
	private String uri;
	private String name;
	private String username;
	private String password;
	private String partRef;
	
	private TokenType(String localName, String uri, String name, String username, String password, String partRef) {
		this.localName = localName;
		this.uri = uri;
		this.name = name;
		this.username = username;
		this.password = password;
		this.partRef = partRef;
		
		this.valueType = WebSphereFactories.getWscommonbndFactory().createValueType();
		valueType.setLocalName(localName);
		valueType.setUri(uri);
		valueType.setName(name);	
	}
	
	public ValueType valueType() {
		return valueType;
	}
	
	public TokenGenerator tokenGenerator() {
		this.callbackHandler = WebSphereFactories.getWscommonbndFactory().createCallbackHandler();
		callbackHandler.setClassname("com.ibm.wsspi.wssecurity.auth.callback.LTPATokenCallbackHandler");
		BasicAuth basicAuth = WebSphereFactories.getWscbndFactory().createBasicAuth();
		basicAuth.setUserid(username);
		basicAuth.setPassword(password);
		callbackHandler.setBasicAuth(basicAuth);

		this.tokenGenerator = WebSphereFactories.getWscommonbndFactory().createTokenGenerator();
		tokenGenerator.setName("LTPATokenGenerator");
		tokenGenerator.setClassname("com.ibm.wsspi.wssecurity.token.LTPATokenGenerator");
		tokenGenerator.setValueType(valueType);
		tokenGenerator.setCallbackHandler(callbackHandler);	
		
		return tokenGenerator;
	}
	
	public TokenGenerator usernameTokenGenerator() {
		this.callbackHandler = WebSphereFactories.getWscommonbndFactory().createCallbackHandler();
		callbackHandler.setClassname("com.ibm.wsspi.wssecurity.auth.callback.NonPromptCallbackHandler");
		BasicAuth basicAuth = WebSphereFactories.getWscbndFactory().createBasicAuth();
		basicAuth.setUserid(username);
		basicAuth.setPassword(password);
		callbackHandler.setBasicAuth(basicAuth);

		this.tokenGenerator = WebSphereFactories.getWscommonbndFactory().createTokenGenerator();
		tokenGenerator.setName("UsernameTokenGenerator");
		tokenGenerator.setClassname("com.ibm.wsspi.wssecurity.token.UsernameTokenGenerator");
		tokenGenerator.setValueType(valueType);
		tokenGenerator.setCallbackHandler(callbackHandler);	
		
		return tokenGenerator;
	}
	
	
	public TokenConsumer getLtpaTokenConsumer() {
		PartReference newPartRef = WebSphereFactories.getWscommonbndFactory().createPartReference();
		newPartRef.setPart(partRef);
		
		this.ltpaTokenConsumer = WebSphereFactories.getWscommonbndFactory().createTokenConsumer();
		ltpaTokenConsumer.setName("LTPATokenConsumer");
		ltpaTokenConsumer.setClassname("com.ibm.wsspi.wssecurity.token.LTPATokenConsumer");
		ltpaTokenConsumer.setValueType(valueType);		
		ltpaTokenConsumer.setPartReference(newPartRef);
		
		return ltpaTokenConsumer;
	}
	
	public TokenConsumer getUsernameTokenConsumer(){
		PartReference newPartRef = WebSphereFactories.getWscommonbndFactory().createPartReference();
		newPartRef.setPart(partRef);
		this.usernameTokenConsumer = WebSphereFactories.getWscommonbndFactory().createTokenConsumer();
		usernameTokenConsumer.setName("UsernameTokenConsumer");
		usernameTokenConsumer.setClassname("com.ibm.wsspi.wssecurity.token.UsernameTokenConsumer");
		usernameTokenConsumer.setValueType(valueType);
		JAASConfig jaasConfig = WebSphereFactories.getWscommonbndFactory().createJAASConfig();
		jaasConfig.setConfigName("system.wssecurity.UsernameToken");
		usernameTokenConsumer.setJAASConfig(jaasConfig);
		usernameTokenConsumer.setPartReference(newPartRef);		
		return usernameTokenConsumer;
	}
	
	public CallbackHandler callbackHandler() {
		return callbackHandler;
	}
	
	public static TokenType createLTPA() {
		return new TokenType(
					"LTPA",
					LTPA_URI,
					"LTPA Token", null, null, null);
	}
	
	public static TokenType createUsername(final String username, final String password) {
		return new TokenType(
					USERNAME_URI,
					"",
					"Username Token", username, password, null);
	}
	
	public static TokenConsumer createLTPATokenConsumer(String partRef){
		TokenType t = new TokenType("LTPA", LTPA_URI, "LTPA Token", null, null, partRef);		
		return t.getLtpaTokenConsumer();		
	}
	
	public static TokenConsumer createUsernameTokenConsumer(String partRef){
		TokenType t = new TokenType(USERNAME_URI, "", "Username Token", null, null, partRef);
		return t.getUsernameTokenConsumer();		
	}

	public String getLocalName() {
		return localName;
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}
}
