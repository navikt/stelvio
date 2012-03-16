package client;

import java.net.MalformedURLException;

import javax.jws.HandlerChain;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;

import no.stelvio.common.context.RequestContext;
import no.stelvio.common.context.RequestContextHolder;
import no.stelvio.common.context.support.RequestContextSetter;
import no.stelvio.common.context.support.SimpleRequestContext;
import no.stelvio.example.services.echo.v1.binding.Echo;
import no.stelvio.example.services.echo.v1.binding.EchoQuackUnsupported;
import no.stelvio.example.services.echo.v1.binding.EchoServiceUnavailable;

@WebServiceClient(name = "EchoService", targetNamespace = "http://stelvio.no/example/services/echo/v1/Binding/")
@HandlerChain(file = "/no/stelvio/consumer/ws/ContextHandlerChain.xml")
public class EchoClient extends Service {

	private static String PROVIDER_ENDPOINT_URI = "http://localhost:9080/stelvio-example-echo-provider/Echo";

	public EchoClient() throws MalformedURLException {
		super(Echo.class.getResource("no/stelvio/example/services/V1/stelvio-example-echo-service_EchoServiceWSEXP.wsdl"),
				new QName("http://stelvio.no/example/services/echo/v1/Binding/", "EchoService"));
	}

	@WebEndpoint(name = "EchoServiceHttpPort")
	public Echo getEchoServiceWSEXPEchoServiceHttpPort() {
		return (Echo) super.getPort(Echo.class);
	}

	public static String echo(String input) throws EchoQuackUnsupported, EchoServiceUnavailable {
		
		setRequestContextIfMissing();
		
		try {
			EchoClient service = new EchoClient();
			Echo port = service.getEchoServiceWSEXPEchoServiceHttpPort();
			((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, PROVIDER_ENDPOINT_URI);
			String output = port.echo(input);
			return output;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * The RequestContext must be set on the current thread as it is used to set
	 * the Stelvio Context header in the Echo JAX-WS calls.
	 */
	private static void setRequestContextIfMissing() {
		if (!RequestContextHolder.isRequestContextSet()) {
			RequestContext requestContext = new SimpleRequestContext.Builder()
											.userId("echo")
											.componentId("stelvio-example-echo-consumer")
											.build();
			RequestContextSetter.setRequestContext(requestContext);
		}
	}

}
