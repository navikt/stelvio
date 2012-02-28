package client;

import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;

import no.stelvio.example.services.echo.v1.binding.EchoQuackUnsupported;
import no.stelvio.example.services.echo.v1.binding.EchoServiceUnavailable;
import no.stelvio.example.services.echo.v1.binding.Echo;

@WebServiceClient(name = "EchoSAMLServiceTest", targetNamespace = "http://stelvio.no/example/services/echo/v1/Binding/")
public class EchoClientSAMLTest extends Service {

		private static Echo port;
		private static String PROVIDER_ENDPOINT_URI = "http://localhost:9080/stelvio-example-echo-provider/EchoSAMLService";
		//private static String PROVIDER_ENDPOINT_URI = "http://e26apvl036.test.local:9080/stelvio-example-echo-provider/EchoSAMLService";
		
		static {
			try {
				EchoClientSAMLTest service = new EchoClientSAMLTest();
				port = service.getEchoServiceWSEXPEchoServiceHttpPort();
				Map<String, Object> requestContext = ((BindingProvider) port).getRequestContext();
				requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, PROVIDER_ENDPOINT_URI);
				
				// magic saml-token manufactory
				requestContext.put(com.ibm.wsspi.wssecurity.saml.config.SamlConstants.SAMLTOKEN_IN_MESSAGECONTEXT, SAMLCustomTokenGenerator.getSamlToken());
			} catch (Exception e) {
	   			e.printStackTrace();
	   			throw new RuntimeException(e);
			}
		}
		
	    public EchoClientSAMLTest() {
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