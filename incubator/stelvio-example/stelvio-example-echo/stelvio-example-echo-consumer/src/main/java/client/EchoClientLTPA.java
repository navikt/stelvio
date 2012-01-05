package client;

import java.net.MalformedURLException;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;

import no.stelvio.example.services.v1.echoservice.binding2.EchoEchoFault1Msg;
import no.stelvio.example.services.v1.echoservice.binding2.EchoService;

@WebServiceClient(name = "EchoLTPAService", targetNamespace = "http://www.stelvio.no/example/services/V1/EchoService/Binding2")
public class EchoClientLTPA extends Service {

	    public EchoClientLTPA() throws MalformedURLException {
	    	super(EchoService.class.getResource("no/stelvio/example/services/V1/stelvio-example-echo-service_EchoServiceWSEXP.wsdl"), new QName("http://www.stelvio.no/example/services/V1/EchoService/Binding2", "EchoLTPAService"));
	    }
	    
	    @WebEndpoint(name = "EchoServiceHttpPort")
	    public EchoService getEchoServiceWSEXPEchoServiceHttpPort() {
	        return (EchoService)super.getPort(EchoService.class);
	    }


		public static String echo(String input) throws EchoEchoFault1Msg { 
		try {
			EchoClientLTPA service = new EchoClientLTPA();
			EchoService port = service.getEchoServiceWSEXPEchoServiceHttpPort();
			((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "http://localhost:9081/stelvio-example-echo-provider/EchoLTPAService");
			String output = port.echo(input);
			return output;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}