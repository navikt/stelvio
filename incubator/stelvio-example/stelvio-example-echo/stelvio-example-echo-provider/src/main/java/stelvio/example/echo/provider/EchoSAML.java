package stelvio.example.echo.provider;

import javax.jws.WebService;

// name="EchoService", wsdlLocation="WEB-INF/wsdl/stelvio-example-echo-service_EchoServiceWSEXP.wsdl", targetNamespace="http://www.stelvio.no/example/services/V1/EchoService/exampleprovider", serviceName="EchoServiceWSEXP_EchoServiceHttpService", portName="EchoServiceWSEXP_EchoServiceHttpPort" 
@WebService(endpointInterface="no.stelvio.example.services.echo.v1.binding.Echo",
		targetNamespace="http://stelvio.no/example/services/echo/v1/Binding/")
public class EchoSAML {

	public String echo(String input) {
		return input;
	}
}
