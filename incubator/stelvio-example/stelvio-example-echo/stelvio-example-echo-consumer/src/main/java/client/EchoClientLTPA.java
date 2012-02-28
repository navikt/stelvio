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

@WebServiceClient(name = "EchoLTPAService", targetNamespace = "http://stelvio.no/example/services/echo/v1/Binding/")
public class EchoClientLTPA extends Service {

		private static String PROVIDER_ENDPOINT_URI = "http://localhost:9080/stelvio-example-echo-provider/EchoLTPAService";
		
	    public EchoClientLTPA() throws MalformedURLException {
	    	super(Echo.class.getResource("no/stelvio/example/services/V1/stelvio-example-echo-service_EchoServiceWSEXP.wsdl"), new QName("http://stelvio.no/example/services/echo/v1/Binding/", "EchoLTPAService"));
	    }
	    
	    @WebEndpoint(name = "EchoServiceHttpPort")
	    public Echo getEchoServiceWSEXPEchoServiceHttpPort() {
	        return (Echo)super.getPort(Echo.class);
	    }


		public static String echo(String input) throws EchoQuackUnsupported, EchoServiceUnavailable { 
		try {
			EchoClientLTPA service = new EchoClientLTPA();
			Echo port = service.getEchoServiceWSEXPEchoServiceHttpPort();
			((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, PROVIDER_ENDPOINT_URI);
			String output = port.echo(input);
			return output;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
