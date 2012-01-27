package stelvio.example.echo.provider;

import javax.jws.HandlerChain;
import javax.jws.WebService;

import no.stelvio.common.context.RequestContextHolder;
import no.stelvio.example.services.echo.v1.binding.EchoQuackUnsupported;
import no.stelvio.example.services.echo.v1.binding.EchoServiceUnavailable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import stelvio.example.echo.service.EchoServiceBi;

@WebService(endpointInterface = "no.stelvio.example.services.v1.echo.binding.Echo",
			wsdlLocation="WEB-INF/wsdl/example/services/V1/Echo/Binding.wsdl",
			targetNamespace="http://stelvio.no/example/services/V1/Echo/Binding/",
			serviceName="Echo")
@HandlerChain(file = "EchoHandler.xml")
public class Echo implements no.stelvio.example.services.echo.v1.binding.Echo {

	protected final Log log = LogFactory.getLog(this.getClass());
	
	EchoServiceBi echoServiceBi;

	public String echo(String input) throws EchoQuackUnsupported, EchoServiceUnavailable {

		log.debug("Echoing message:" + input);
		log.debug("Context: " + RequestContextHolder.currentRequestContext());

		echoServiceBi = new EchoServiceBi();
		return echoServiceBi.echo(input);
	}

}
