package stelvio.example.echo.provider;

import javax.ejb.Stateless;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import stelvio.example.echo.service.EchoServiceBi;

import no.stelvio.common.context.RequestContextHolder;
import no.stelvio.example.services.v1.echoservice.binding2.EchoEchoFault1Msg;
import no.stelvio.example.services.v1.echoservice.binding2.EchoService;

// name="EchoService", wsdlLocation="WEB-INF/wsdl/stelvio-example-echo-service_EchoServiceWSEXP.wsdl", targetNamespace="http://www.stelvio.no/example/services/V1/EchoService/Binding2", serviceName="EchoServiceWSEXP_EchoServiceHttpService", portName="EchoServiceWSEXP_EchoServiceHttpPort" 
@WebService(endpointInterface = "no.stelvio.example.services.v1.echoservice.binding2.EchoService")
@HandlerChain(file = "EchoHandler.xml")
@Stateless
public class Echo implements EchoService {

	protected final Log log = LogFactory.getLog(this.getClass());
	
	EchoServiceBi echoServiceBi;

	public String echo(String input) throws EchoEchoFault1Msg {

		log.debug("Echoing message:" + input);
		log.debug("Context: " + RequestContextHolder.currentRequestContext());

		echoServiceBi = new EchoServiceBi();
		return echoServiceBi.echo(input);
	}

}
