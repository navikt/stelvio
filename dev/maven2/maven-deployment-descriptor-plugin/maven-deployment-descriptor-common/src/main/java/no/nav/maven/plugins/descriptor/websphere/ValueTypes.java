package no.nav.maven.plugins.descriptor.websphere;

import com.ibm.etools.webservice.wscommonbnd.ValueType;

public class ValueTypes {

	public static final ValueType LTPA;
	public static final ValueType USERNAME;
	
	static {
		LTPA = WebSphereFactories.getWscommonbndFactory().createValueType();
		LTPA.setLocalName("LTPA");
		LTPA.setUri("http://www.ibm.com/websphere/appserver/tokentype/5.0.2");
		LTPA.setName("LTPA Token");
		
		USERNAME = WebSphereFactories.getWscommonbndFactory().createValueType();
		USERNAME.setLocalName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#UsernameToken");
		USERNAME.setName("Username Token");
	}
	
	private ValueTypes() {}
	
	public static boolean equals(ValueType a, ValueType b) {
		return a.getUri().equals(b.getUri())
		|| a.getLocalName().equals(b.getLocalName());
	}
}
