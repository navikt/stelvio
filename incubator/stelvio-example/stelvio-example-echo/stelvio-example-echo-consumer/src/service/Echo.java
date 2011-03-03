package service;
//import javax.naming.InitialContext;
//import javax.naming.NamingException;
//import javax.transaction.HeuristicMixedException;
//import javax.transaction.HeuristicRollbackException;
//import javax.transaction.NotSupportedException;
//import javax.transaction.RollbackException;
//import javax.transaction.SystemException;
//import javax.transaction.UserTransaction;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;

import no.stelvio.example.services.v1.echoservice.binding2.EchoEchoFault1Msg;
import no.stelvio.example.services.v1.echoservice.binding2.EchoService;
import no.stelvio.example.services.v1.echoservice.binding2.EchoServiceWSEXPEchoServiceHttpService;

public class Echo {
	
	public static String echo(String input) throws EchoEchoFault1Msg, MalformedURLException { // , NamingException, NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException 
//		UserTransaction userTransaction = null;
//		InitialContext context = new InitialContext();
//		userTransaction = (UserTransaction) context.lookup("java:comp/UserTransaction");
		// start transaction
		//userTransaction.begin();
		EchoServiceWSEXPEchoServiceHttpService service = new EchoServiceWSEXPEchoServiceHttpService(
				new URL("http://localhost:9080/stelvio-example-echo-serviceWeb/sca/EchoServiceWSEXP?wsdl"),
				new QName("http://www.stelvio.no/example/services/V1/EchoService/Binding2", "EchoServiceWSEXP_EchoServiceHttpService"));
		EchoService port = service.getEchoServiceWSEXPEchoServiceHttpPort();
		String output = port.echo(input);
		//userTransaction.commit();
		return output;
	}
}
