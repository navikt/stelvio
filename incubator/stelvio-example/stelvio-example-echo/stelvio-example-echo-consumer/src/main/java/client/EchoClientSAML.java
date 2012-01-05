package client;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;

import no.stelvio.example.services.v1.echoservice.binding2.EchoEchoFault1Msg;
import no.stelvio.example.services.v1.echoservice.binding2.EchoService;

@WebServiceClient(name = "EchoSAMLService", targetNamespace = "http://www.stelvio.no/example/services/V1/EchoService/Binding2")
public class EchoClientSAML extends Service {
	
		private static EchoService port;

		static {
			EchoClientSAML service = new EchoClientSAML();
			port = service.getEchoServiceWSEXPEchoServiceHttpPort();
			((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "http://localhost:9081/stelvio-example-echo-provider/EchoSAMLService");
		}

	    public EchoClientSAML() {
	    	super(EchoService.class.getResource("no/stelvio/example/services/V1/stelvio-example-echo-service_EchoServiceWSEXP.wsdl"), new QName("http://www.stelvio.no/example/services/V1/EchoService/Binding2", "EchoSAMLService"));
	    }
	    
	    @WebEndpoint(name = "EchoServiceHttpPort")
	    public EchoService getEchoServiceWSEXPEchoServiceHttpPort() {
	        return (EchoService)super.getPort(EchoService.class);
	    }


		public static String echo(String input) throws EchoEchoFault1Msg { 
			String output = port.echo(input);
			return output;
		}
}