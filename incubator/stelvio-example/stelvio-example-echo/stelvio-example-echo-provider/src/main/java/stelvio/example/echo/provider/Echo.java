package stelvio.example.echo.provider;

import javax.jws.HandlerChain;
import javax.jws.WebService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import no.stelvio.example.services.v1.echoservice.binding2.EchoEchoFault1Msg;

// name="EchoService", wsdlLocation="WEB-INF/wsdl/stelvio-example-echo-service_EchoServiceWSEXP.wsdl", targetNamespace="http://www.stelvio.no/example/services/V1/EchoService/Binding2", serviceName="EchoServiceWSEXP_EchoServiceHttpService", portName="EchoServiceWSEXP_EchoServiceHttpPort" 
@WebService(endpointInterface="no.stelvio.example.services.v1.echoservice.binding2.EchoService")
@HandlerChain(file="EchoHandler.xml")
public class Echo {

	protected final Log log = LogFactory.getLog(this.getClass());
	
	public String echo(String input) throws EchoEchoFault1Msg {

		if( log.isDebugEnabled() ) {
			   log.debug("Echoing message:" + input);
			}
		
		return input;
	}
}
