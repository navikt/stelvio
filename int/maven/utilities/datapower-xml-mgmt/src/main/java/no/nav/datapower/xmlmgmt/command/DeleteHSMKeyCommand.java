package no.nav.datapower.xmlmgmt.command;

public class DeleteHSMKeyCommand extends AbstractDoActionCommand{

	
	private String keyHandle;
	private String keyType;
	
	
	public DeleteHSMKeyCommand(String keyHandle, String keyType) {
		this.keyHandle = keyHandle;
		this.keyType = keyType;
	}
	@Override
	protected void addCommandBody(StringBuffer builder) {
		builder.append("<DeleteHSMKey>\r\n");
		builder.append("<KeyHandle>");
		builder.append(keyHandle);
		builder.append("</KeyHandle>\r\n");
		builder.append("<KeyType>");
		builder.append(keyType);
		builder.append("</KeyType>\r\n");
		builder.append("</DeleteHSMKey>\r\n");
		
	}

}
