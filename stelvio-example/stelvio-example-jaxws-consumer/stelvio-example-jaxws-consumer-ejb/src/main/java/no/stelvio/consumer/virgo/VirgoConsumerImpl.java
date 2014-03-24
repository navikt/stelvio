package no.stelvio.consumer.virgo;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.jws.HandlerChain;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceRef;

import no.nav.tjeneste.virksomhet.virgo.v2.VirgoPortType;
import no.nav.tjeneste.virksomhet.virgo.v2.VirgoV2;
import no.stelvio.common.context.support.RequestContextSetter;
import no.stelvio.common.context.support.SimpleRequestContext;


/**
 * Implementasjon av {@link VirgoConsumer}
 */
@Stateless
public class VirgoConsumerImpl implements VirgoConsumer {

	@HandlerChain(file="/no/stelvio/consumer/ws/ContextHandlerChain.xml")
	@WebServiceRef(wsdlLocation = "/wsdl/Virgo_v2.wsdl", value = VirgoV2.class, name="jaxws/VirgoConsumer")
	private VirgoPortType virgoPortType;

    @PostConstruct
    private void postConstruct() {
		BindingProvider bp = (BindingProvider) virgoPortType;
		bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, System.getProperty("endpoint.url"));
    }

	@Override    
    public String echo(String echoRequest) {

		// In a normal stelvio application setting the RequestContext like this would require
		// the use of RequestContextFilter, SecurityContextFilter with the required
		// Spring config to go along with these
		// So this one line of cheatcode simplifies a lot
		RequestContextSetter.setRequestContext(new SimpleRequestContext.Builder().userId("WPS2812").componentId("PP01").build());

        return virgoPortType.echo(echoRequest);
    }
    
    @Override
    public void ping() {
    	
    	virgoPortType.ping();
    }
    
}
