/*
 * Created on Feb 13, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package no.stelvio.consumer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.i18n.LocaleContextHolder;

import no.stelvio.common.context.RequestContextHolder;
import no.stelvio.common.security.SecurityContextHolder;


/**
 * @author lschnell
 *
 */
public class ConsumerContextHandler extends GenericHandler {

	
	//private static SimpleDateFormat zulu =
	//       new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	protected final Log log = LogFactory.getLog(this.getClass());
	
	public ConsumerContextHandler(){
		
	}
	protected HandlerInfo info = null;

	/* (non-Javadoc)
	 * @see javax.xml.rpc.handler.Handler#getHeaders()
	 */
	public QName[] getHeaders() {
		return info.getHeaders();
	}
	public void init(HandlerInfo arg) {
		info = arg; 
	}

	public boolean handleRequest(MessageContext context) {
		try {

/*			<StelvioContext>
				<userId>LSW0001</userId>
				<correlationId>4LQA7VQPH2YQEU1R5TYU</correlationId>
				<languageId>no</languageId>
				<applicationId>PSELV</languageId>
			</StelvioContext>*/

			  SOAPMessageContext smc = (SOAPMessageContext)context;
			  SOAPEnvelope se = smc.getMessage().getSOAPPart().getEnvelope();
			  SOAPHeader sh = se.getHeader();
			  SOAPHeaderElement mainElement = sh.addHeaderElement(se.createName("StelvioContext"));
			  SOAPElement el=mainElement.addChildElement("userId");
			  el.addTextNode(SecurityContextHolder.currentSecurityContext().getUserId());
			  el=mainElement.addChildElement("correlationId");
			  el.addTextNode(RequestContextHolder.currentRequestContext().getTransactionId());
			  el=mainElement.addChildElement("languageId");
			  if (LocaleContextHolder.getLocaleContext()!=null && LocaleContextHolder.getLocaleContext().getLocale()!=null)
				  el.addTextNode(LocaleContextHolder.getLocaleContext().getLocale().getLanguage());
			  else{
				  el.addTextNode("unknown");
			  }
			  el=mainElement.addChildElement("applicationId");
			  el.addTextNode(RequestContextHolder.currentRequestContext().getModuleId());
			  
			  /*Iterator headers = sh.examineHeaderElements("");
			  while (headers.hasNext()) 
			  {
			  	SOAPHeaderElement he = (SOAPHeaderElement)headers.next();
			  	System.out.println("ClientHandler: Header element name is "+he.getElementName().getQualifiedName());
				System.out.println("ClientHandler element value is "+he.getValue());
              }*/	
			  
		} catch (SOAPException x) {
			log.error("Soap Error while creating request context header",x );			
		}
		return true;
	}
	
 

}