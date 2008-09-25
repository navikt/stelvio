package no.nav.maven.plugins.descriptor.websphere;

import com.ibm.etools.webservice.wscommonbnd.ValueType;

public class ValueTypes {

	public static final ValueType LTPA;
	
	static {
		LTPA = WebSphereFactories.getWscommonbndFactory().createValueType();
		LTPA.setLocalName("LTPA");
		LTPA.setUri("http://www.ibm.com/websphere/appserver/tokentype/5.0.2");
		LTPA.setName("LTPA Token");
	}
	
	private ValueTypes() {}
	
	public static boolean equals(ValueType a, ValueType b) {
		return a.getUri().equals(b.getUri());
	}
}
