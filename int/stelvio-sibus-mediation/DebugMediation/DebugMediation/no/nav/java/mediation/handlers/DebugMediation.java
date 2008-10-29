package no.nav.java.mediation.handlers;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.rpc.handler.MessageContext;

import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.websphere.sib.SIApiConstants;
import com.ibm.websphere.sib.SIMessage;
import com.ibm.websphere.sib.mediation.handler.MediationHandler;
import com.ibm.websphere.sib.mediation.handler.MessageContextException;
import com.ibm.websphere.sib.mediation.messagecontext.SIMessageContext;
import com.ibm.websphere.sib.mediation.session.SIMediationSession;
import commonj.sdo.DataObject;


/**
 * @author persona2c5e3b49756 Schnell
 * @use For logging SIBUS message contet 
 */
public class DebugMediation implements MediationHandler {

	//	Sample - log.logp(Level.FINEST, className, <yourMethod>, <yourText>);
	private final static String className = DebugMediation.class.getName();
	private final Logger log = Logger.getLogger(className);

	private final String PROP_logContent = "logContent";
	private final String PROP_logContent_FILE = "FILE";
	private final String PROP_logContent_CONSOLE = "CONSOLE";
	private final int MATCH_FILE = 1;
	private final int MATCH_CONSOLE = 0;
	private final int MATCH_BOTH = 2; 
	
	public static String DEFAULTDATEFORMAT = "yyyyMMddHHmms";
	private final static String PROP_logFilePath = "logFilePath";


	/* (non-Javadoc)
	 * @see com.ibm.websphere.sib.mediation.handler.MediationHandler#handle(javax.xml.rpc.handler.MessageContext)
	 */
	public boolean handle(MessageContext msgCtx) throws MessageContextException {
		SIMessageContext siMsgCtx = (SIMessageContext) msgCtx;
		boolean logContent = false;
		int match = 0;
		String logFilePath = "";
		String msgInfo = "";
		String sessInfo = "";
		
		try
		{
			// properties set?
			for (Iterator it = siMsgCtx.getPropertyNames(); it.hasNext();) {

				String propName = (String) it.next();
				if (propName.equalsIgnoreCase(PROP_logContent)) {
					String value = (String) msgCtx.getProperty(propName);
					if (value.indexOf(PROP_logContent_FILE) >=0  && value.indexOf(PROP_logContent_CONSOLE) >= 0)
					{
						match = MATCH_BOTH;
						logContent = true;
						log.logp(Level.INFO, className, "handle()", "DebugMediationProperties: " + "MATCH_BOTH");
					}
					else if (value.equalsIgnoreCase(PROP_logContent_CONSOLE))
					{
						match = MATCH_CONSOLE;
						logContent = true;
						log.logp(Level.INFO, className, "handle()", "DebugMediationProperties: " + "MATCH_CONSOLE");
					}
					else if (value.equalsIgnoreCase(PROP_logContent_FILE))
					{
						match = MATCH_FILE;
						logContent = true;
						log.logp(Level.INFO, className, "handle()", "DebugMediationProperties: " + "MATCH_FILE");
					}
					else
						logContent = false;
				} else if (propName.indexOf(PROP_logFilePath) >= 0) {
					// we assume that the path is right
					String value = (String) msgCtx.getProperty(propName);
					logFilePath = value;
				} 
			}
		} catch (Exception e) {
			log.logp(Level.SEVERE, className, "handle()", "DebugMediationError: " + e.getMessage());
		}	
		
		if (log.isLoggable(Level.INFO)) 
		{
			msgInfo = "Message Info: " + getMessageInfo(siMsgCtx.getSIMessage());
			sessInfo = "Session Info: " + getSessionInfo(siMsgCtx.getSession());

			log.info(msgInfo);
			log.info(sessInfo);
			log.info("Mediation Context Info: " + getContextProperties(siMsgCtx));

			//depend on the context properties we dump out content
			if (logContent) 
			{
				String msgCnt = getMessageContent(siMsgCtx.getSIMessage()); 
				if (match == MATCH_CONSOLE || match==MATCH_BOTH)
				{	
					log.info("Message Context Info: " + msgCnt);
				}
				
				if (match == MATCH_FILE)
				{
					Date now = new Date();
					String id = siMsgCtx.getSIMessage().getApiMessageId();
					String fileContent = msgInfo + "\n" + sessInfo + "\n" + msgCnt;
					
					if (id.indexOf(":") >= 0)
					{
						id = id.replace(":", "");
					}
					SimpleDateFormat sdf = new SimpleDateFormat(DEFAULTDATEFORMAT);
					String logFileName = logFilePath + "/" + id + "_"+ sdf.format(now) + ".dmp";
					binaryWriteFile(fileContent.getBytes(), logFileName);
				}
			}
		}
		return true;
	}

	/**
	 * Returns a printable representation of the SIMediationSession.
	 *
	 * @param session
	 *            see
	 * @com.ibm.websphere.sib.mediation.session.SIMediationSession
	 */
	private String getSessionInfo(SIMediationSession session) {
		StringBuffer data = new StringBuffer();
		data.append("Mediation: ");
		data.append(session.getMediationName());
		data.append(" @ Destination: ");
		data.append(session.getDestinationName());
		if (session.getDiscriminator() != null
				&& !session.getDiscriminator().equals("")) {
			data.append(" (discriminator = ");
			data.append(session.getDiscriminator());
			data.append(')');
		}
		if (session.getMessageSelector() != null
				&& !session.getMessageSelector().equals("")) {
			data.append(" (message selector = ");
			data.append(session.getMessageSelector());
			data.append(')');
		}
		data.append(" on Bus: ");
		data.append(session.getBusName());
		data.append(" in ME: ");
		data.append(session.getMessagingEngineName());
		return data.toString();
	}

	/**
	 * Returns a printable representation of the SIMessage.
	 *
	 * @param message
	 * @see com.ibm.websphere.sib.SIMessage
	 */
	private String getMessageInfo(SIMessage message) {
		StringBuffer data = new StringBuffer();
		data.append("API message id = ");
		data.append(message.getApiMessageId());
		data.append(", System message id = ");
		data.append(message.getSystemMessageId());
		data.append(", Correlation id = ");
		data.append(message.getCorrelationId());
		data.append(", Message format = \"");
		data.append(message.getFormat());
		data.append('\"');
		data.append(", Message descriminator = \"");
		data.append(message.getDiscriminator());
		data.append('\"');
		List list = message.getUserPropertyNames();
		if (list != null && !list.isEmpty()) {
			data.append(", User properties = ");
			data.append(list.toString());
		}
		list = message.getForwardRoutingPath();
		if (list != null && !list.isEmpty()) {
			data.append(", Forward routing path = ");
			data.append(list.toString());
		}
		list = message.getReverseRoutingPath();
		if (list != null && !list.isEmpty()) {
			data.append(", Reverse routing path = ");
			data.append(list.toString());
		}
		data.append(", Reliability = ");
		data.append(message.getReliability());
		data.append(", Priority = ");
		data.append(message.getPriority());
		data.append(", Redelivered Count = ");
		data.append(message.getRedeliveredCount());
		data.append(", TimeToLive = ");
		data.append(message.getRemainingTimeToLive());
		data.append(", User id = ");
		data.append(message.getUserId());
		return data.toString();
	}

	/**
	 * Returns a printable representation of the SIMediationContext.
	 * 
	 * @param msgCtx
	 * @see com.ibm.websphere.sib.mediation.messagecontext.SIMessageContext
	 * @return java.lang.String a textual representation of context properties
	 */
	private String getContextProperties(SIMessageContext msgCtx) {
		StringBuffer data = new StringBuffer();
		Iterator i = msgCtx.getPropertyNames();
		data.append("Mediation context: ");
		String propName;
		while (i.hasNext()) {
			propName = (String) i.next();
			data.append(propName);
			data.append(" = ");
			data.append(msgCtx.getProperty(propName));
			if (i.hasNext())
				data.append(", ");
		}
		return data.toString();
	}
	
	/**
	 * Returns a printable representation of the SIMessage content.
	 * @param message
	 * @see com.ibm.websphere.sib.SIMessage
	 */
	private String getMessageContent(SIMessage message) {
		StringBuffer data = new StringBuffer();
		formatJMSMessage(data, message, "   ");
		return data.toString();
	}	
	
	/**
	 * formatJMSMessage
	 * @param buf
	 * @param msg
	 * @param indent
	 */
	public void formatJMSMessage(StringBuffer buf, SIMessage msg, String indent) {
	    String msgfmt = msg.getFormat();
	    DataObject msgRoot;
	    String newLine = '\n' + indent;
	    try {
	        msgRoot = msg.getDataGraph().getRootObject();
	        if (msgfmt.equals(SIApiConstants.JMS_FORMAT_TEXT)) {
	            buf.append("\nJMS TextMessage:" + newLine);
	            String msgText = msgRoot.getString("data/value");
	            buf.append(msgText.replaceAll("\n", newLine));
	        } else if (msgfmt.equals(SIApiConstants.JMS_FORMAT_BYTES)) {
	            buf.append("\nJMS BytesMessage: ");
	            appendBytes(buf, msgRoot.getBytes("data/value"), indent);
	        } else if (msgfmt.equals(SIApiConstants.JMS_FORMAT_OBJECT)) {
	            buf.append("\nJMS ObjectMessage: (");
	            appendObjectMessage(buf, indent, msgRoot, newLine);
	        } else if (msgfmt.equals(SIApiConstants.JMS_FORMAT_STREAM)) {
	            buf.append("\nJMS StreamMessage:");
	            appendStreamMessage(buf, indent, msgRoot, newLine);
	        }
	    } catch (Exception e) {
	    	log.logp(Level.SEVERE, className, "formatJMSMessage", "DebugMediationError: " + e.getMessage());
	        buf.append("Cannot format message (" + msg.getApiMessageId());
	        buf.append(") body using format " + msgfmt + " due to " + e);
	    }
	}

	/**
	 * @param buf
	 * @param indent
	 * @param msgRoot
	 * @param newLine
	 */
	private void appendStreamMessage(StringBuffer buf, String indent, DataObject msgRoot, String newLine) {
	 List streamList = msgRoot.getList("data/value");
	 Object streamItem;
	 if (streamList != null && !streamList.isEmpty()) {
	     for (int i = 1; i <= streamList.size(); i++) {
	         streamItem = msgRoot.get("data/value[" + i + "]");
	         buf.append(newLine);
	         buf.append("Stream item " + i + " : (");
	         if (streamItem instanceof byte[]) {
	             buf.append("byte[])");
	             appendBytes(buf, (byte[]) streamItem, "    " + indent);
	         } else {
	             buf.append(streamItem.getClass().getName());
	             buf.append(") " + streamItem.toString());
	         }
	     }
	 } else {
	     buf.append("[]");
	 }
	}

	/**
	 * @param buf
	 * @param indent
	 * @param msgRoot
	 * @param newLine
	 */
	private void appendObjectMessage(StringBuffer buf, String indent, DataObject msgRoot, String newLine) {
	 Object obj = null;
	 try {
	     obj = getObject(msgRoot, "data/value");
	     buf.append(obj.getClass().getName());
	     String objString = obj.toString();
	     if (objString.indexOf('\n')>0){
	         buf.append(")\n");
	         objString = indent + objString.replaceAll("\n", newLine);
	         buf.append(objString);
	     } else {
	         buf.append(") " + objString);    
	     }
	 } catch (RuntimeException e) {
	     buf.append(e.getMessage() + " due to " + e.getCause());
	 }
	}

	/**
	 * @param msgRoot
	 * @param string
	 * @return
	 */
	private Object getObject(DataObject msgRoot, String string) {
	    Object result = null;
	    byte[] msgBodyBytes = msgRoot.getBytes("data/value");
	    try {
	        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(msgBodyBytes));
	        result = in.readObject();
	    } catch (Throwable e) {
	        // Error de-serialising object
	    	log.logp(Level.SEVERE, className, "getObject", "DebugMediationError: " + e.getMessage());
	        throw new RuntimeException("Error deserialising object", e);
	    }
	    return result;
	}

	/**
	 * @param buf
	 * @param bytes
	 * @param indent
	 */
	private void appendBytes(StringBuffer buf, byte[] bytes, String indent) {
	    int length = bytes.length;
	    String newLine = '\n' + indent;      
	    for (int lineStart = 0; lineStart<length; lineStart += 16) {
	        int lineEnd = Math.min(lineStart+16, length);
	        StringBuffer hex=new StringBuffer();
	        StringBuffer ascii=new StringBuffer();
	        for (int i=lineStart; i<lineEnd; i++) {
	          int b = bytes[i];
	          b=(b+256)%256;
	          int c1=b/16;
	          int c2=b%16;
	          hex.append((char)(c1<10 ? '0'+c1 : 'a'+c1-10));
	          hex.append((char)(c2<10 ? '0'+c2 : 'a'+c2-10));
	          if (i%2 == 1) hex.append(' ');
	          if ((b>=0x20 && b<=0x7e)) ascii.append((char)b);
	          else ascii.append('.');
	        }
	        int pad=16-(lineEnd-lineStart);
	        int spaces=(pad*5 + pad%2)/2;
	        spaces+=3;
	        for (int i=0; i<spaces; i++) hex.append(' ');       
	        String offset="0000"+Integer.toHexString(lineStart);
	        offset=offset.substring(offset.length()-4);
	        buf.append(newLine);
	        buf.append(offset);
	        buf.append("  ");
	        buf.append(hex.toString());
	        buf.append(ascii.toString());
	      }
	}
	
	 /**
	  * Write binary data to file
	  * @param byte
	  * @return void
	  * @exception none
	  */
	  private void binaryWriteFile(byte inByte[], String s)
	  {
	   try
	   {
	    @SuppressWarnings("unused")
		File file = new File(s);
	    BufferedOutputStream bufferedoutputstream = new BufferedOutputStream(new FileOutputStream(s));
	    ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
	    bytearrayoutputstream.write(inByte, 0, inByte.length);
	    bytearrayoutputstream.writeTo(bufferedoutputstream);
	    bufferedoutputstream.close();
	   }
	   catch(IOException e)
	   {
		   log.logp(Level.SEVERE, className, "binaryWriteFile", "DebugMediationError: " + e.getMessage());
	   }
	  }

}
