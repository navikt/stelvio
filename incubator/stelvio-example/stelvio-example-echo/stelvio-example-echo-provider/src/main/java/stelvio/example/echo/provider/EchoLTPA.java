package stelvio.example.echo.provider;

import javax.jws.WebService;

import no.stelvio.example.services.v1.echoservice.binding2.EchoEchoFault1Msg;

// name="EchoService", wsdlLocation="WEB-INF/wsdl/stelvio-example-echo-service_EchoServiceWSEXP.wsdl", targetNamespace="http://www.stelvio.no/example/services/V1/EchoService/Binding2", serviceName="EchoServiceWSEXP_EchoServiceHttpService", portName="EchoServiceWSEXP_EchoServiceHttpPort" 
@WebService(endpointInterface="no.stelvio.example.services.v1.echoservice.binding2.EchoService")
public class EchoLTPA {

	public String echo(String input) throws EchoEchoFault1Msg {
		return input;
	}
}
