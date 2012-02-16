package client;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;

import no.stelvio.example.services.echo.v1.binding.EchoQuackUnsupported;
import no.stelvio.example.services.echo.v1.binding.EchoServiceUnavailable;
import no.stelvio.example.services.echo.v1.binding.Echo;

@WebServiceClient(name = "EchoSAMLService", targetNamespace = "http://stelvio.no/example/services/echo/v1/Binding/")
public class EchoClientSAML extends Service {
	
		private static Echo port;

		static {
			EchoClientSAML service = new EchoClientSAML();
			port = service.getEchoServiceWSEXPEchoServiceHttpPort();
			((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "http://localhost:9080/stelvio-example-echo-provider/EchoSAMLService");
		}

	    public EchoClientSAML() {
	    	super(Echo.class.getResource("no/stelvio/example/services/V1/stelvio-example-echo-service_EchoServiceWSEXP.wsdl"), new QName("http://stelvio.no/example/services/echo/v1/Binding/", "EchoSAMLService"));
	    }
	    
	    @WebEndpoint(name = "EchoServiceHttpPort")
	    public Echo getEchoServiceWSEXPEchoServiceHttpPort() {
	        return (Echo)super.getPort(Echo.class);
	    }


		public static String echo(String input) throws EchoServiceUnavailable, EchoQuackUnsupported { 
			String output = port.echo(input);
			return output;
		}
}