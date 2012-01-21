package stelvio.example.echo.provider;

import javax.jws.WebService;

import no.stelvio.example.services.v1.echoservice.exampleprovider.EchoFault;

// name="EchoService", wsdlLocation="WEB-INF/wsdl/stelvio-example-echo-service_EchoServiceWSEXP.wsdl", targetNamespace="http://www.stelvio.no/example/services/V1/EchoService/exampleprovider", serviceName="EchoServiceWSEXP_EchoServiceHttpService", portName="EchoServiceWSEXP_EchoServiceHttpPort" 
@WebService(endpointInterface="no.stelvio.example.services.v1.echoservice.exampleprovider.EchoService")
public class EchoSAML {

	public String echo(String input) throws EchoFault {
		return input;
	}
}
