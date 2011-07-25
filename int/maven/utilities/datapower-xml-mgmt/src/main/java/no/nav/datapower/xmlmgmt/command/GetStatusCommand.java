package no.nav.datapower.xmlmgmt.command;

/**
 * Builds a command that returns the status on a given object class, 
 * see store:/xml-mgmt.xsd for a complete list of possible options 
 * for the objectClass parameter.  
 * 
 * If objectClass = ObjectStatus, the command will return all objects 
 * in the domain and their status on the form:  
 * 
 * <ObjectStatus xmlns:env="http://www.w3.org/2003/05/soap-envelope">
 *	<Class>WSStylePolicy</Class>
 *	<OpState>up</OpState>
 *	<AdminState>enabled</AdminState>
 *	<Name>ELSAMInboundProcessingPolicyURL</Name>
 *	<EventCode>0x00000000</EventCode>
 *	<ErrorCode/>
 *  <ConfigState>saved</ConfigState>
 * </ObjectStatus>
 * 
 * @author person4fdbf4cece95, Accenture
 *
 */
public class GetStatusCommand extends AbstractBaseCommand{

	private String commandBody;
	
	public GetStatusCommand(){
		commandBody = "";
	}
	
	public GetStatusCommand(String objectClass){
		commandBody = "class=\"" + objectClass + "\"";
	}
	
	protected void openCommand(StringBuffer builder) {
		builder.append("<dp:get-status ");
	}

	protected void closeCommand(StringBuffer builder) {
		builder.append("/>\r\n");
	}

	protected void addCommandBody(StringBuffer builder) {
		builder.append(commandBody);
		
	}
}
