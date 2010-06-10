package service;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import no.stelvio.example.services.v1.echoservice.binding2.EchoEchoFault1Msg;
import no.stelvio.example.services.v1.echoservice.binding2.EchoServiceWSEXP_EchoServiceHttpPortProxy;

public class Echo {

	public static String echo(String input) throws EchoEchoFault1Msg, NamingException, NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		UserTransaction userTransaction = null;
		InitialContext context = new InitialContext();
		userTransaction = (UserTransaction) context.lookup("java:comp/UserTransaction");
		// start transaction
		//userTransaction.begin();
		EchoServiceWSEXP_EchoServiceHttpPortProxy proxy = new EchoServiceWSEXP_EchoServiceHttpPortProxy();
		proxy._getDescriptor().setEndpoint("http://localhost:8080/");
		String output = proxy.echo(input);
		//userTransaction.commit();
		return output;
	}
}
