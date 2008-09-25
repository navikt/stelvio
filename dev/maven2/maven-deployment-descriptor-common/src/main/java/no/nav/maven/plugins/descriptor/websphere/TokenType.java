package no.nav.maven.plugins.descriptor.websphere;


import com.ibm.etools.webservice.wscbnd.BasicAuth;
import com.ibm.etools.webservice.wscommonbnd.CallbackHandler;
import com.ibm.etools.webservice.wscommonbnd.TokenGenerator;
import com.ibm.etools.webservice.wscommonbnd.ValueType;

public class TokenType {

	private final ValueType valueType;
	private final TokenGenerator tokenGenerator;
	private final CallbackHandler callbackHandler;
	
	private TokenType(String localName, String uri, String name, String username, String password) {
		this.valueType = WebSphereFactories.getWscommonbndFactory().createValueType();
		valueType.setLocalName(localName);
		valueType.setUri(uri);
		valueType.setName(name);

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
	}
	
	public ValueType valueType() {
		return valueType;
	}
	
	public TokenGenerator tokenGenerator() {
		return tokenGenerator;
	}
	
	public CallbackHandler callbackHandler() {
		return callbackHandler;
	}
	
	public static TokenType createLTPA() {
		return new TokenType(
					"LTPA",
					"http://www.ibm.com/websphere/appserver/tokentype/5.0.2",
					"LTPA Token", null, null);
	}
}
