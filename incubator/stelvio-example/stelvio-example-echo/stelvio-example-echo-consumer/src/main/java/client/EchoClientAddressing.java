package client;

import java.net.MalformedURLException;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;

import no.stelvio.example.services.echo.v1.binding.Echo;
import no.stelvio.example.services.echo.v1.binding.EchoQuackUnsupported;
import no.stelvio.example.services.echo.v1.binding.EchoServiceUnavailable;

@WebServiceClient(name = "EchoAddressingService", targetNamespace = "http://stelvio.no/example/services/echo/v1/Binding/")
public class EchoClientAddressing extends Service {

	    public EchoClientAddressing() throws MalformedURLException {
//	    	super(EchoService.class.getResource("no/stelvio/example/services/V1/stelvio-example-echo-service_EchoServiceWSEXP.wsdl"), new QName("http://www.stelvio.no/example/services/V1/EchoService/Binding2", "EchoService"));
	    	super(null, new QName("http://stelvio.no/example/services/echo/v1/Binding/", "EchoAddressingService"));
//	    	super(EchoClient.class.getResource("WEB-INF/wsdl/stelvio-example-echo-service_EchoServiceWSEXP.wsdl"), new QName("http://www.stelvio.no/example/services/V1/EchoService/Binding2", "EchoService"));
	    }
	    
	    @WebEndpoint(name = "EchoServiceHttpPort")
	    public Echo getEchoServiceWSEXPEchoServiceHttpPort() {
	        return (Echo)super.getPort(Echo.class);
//	        return (EchoService)super.getPort(EchoService.class, new AddressingFeature());
	    }


		public static String echo(String input) throws EchoQuackUnsupported, EchoServiceUnavailable { 
		try {
			EchoClientAddressing service = new EchoClientAddressing();
			Echo port = service.getEchoServiceWSEXPEchoServiceHttpPort();
			((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "http://localhost:9080/stelvio-example-echo-provider/EchoService");
			String output = port.echo(input);
			return output;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
