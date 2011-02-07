package stelvio.example.echo.provider;

import javax.jws.WebService;

@WebService(name="EchoService", serviceName="EchoServiceWSEXP_EchoServiceHttpService", portName="EchoServiceWSEXP_EchoServiceHttpPort", targetNamespace="http://www.stelvio.no/example/services/V1/EchoService")
public class Echo {

	public String echo(String input) {
		return input;
	}
}
