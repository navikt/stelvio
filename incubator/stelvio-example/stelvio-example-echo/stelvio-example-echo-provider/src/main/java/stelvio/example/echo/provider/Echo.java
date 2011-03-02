package stelvio.example.echo.provider;

import javax.jws.WebService;

// name="EchoService", serviceName="EchoServiceWSEXP_EchoServiceHttpService", portName="EchoServiceWSEXP_EchoServiceHttpPort", targetNamespace="http://www.stelvio.no/example/services/V1/EchoService", wsdlLocation="WEB-INF/wsdl/stelvio-example-echo-service_EchoServiceWSEXP.wsdl", 
@WebService(endpointInterface="no.stelvio.example.services.v1.echoservice.binding2.EchoService") 
public class Echo {

	public String echo(String input) {
		return input;
	}
}
