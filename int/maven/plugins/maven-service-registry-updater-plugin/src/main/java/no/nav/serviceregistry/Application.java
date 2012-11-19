package no.nav.serviceregistry;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.namespace.QName;

import no.nav.serviceregistry.Service;

public class Application {
	private Map<QName, Service> services = new LinkedHashMap<QName, Service>();
	
	@XmlElement(name="service")
	public Collection<Service> getServices() {
		return services.values();
	}
}
