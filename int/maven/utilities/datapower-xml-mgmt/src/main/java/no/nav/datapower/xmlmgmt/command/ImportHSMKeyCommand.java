package no.nav.datapower.xmlmgmt.command;

public class ImportHSMKeyCommand extends AbstractDoActionCommand {

	
	private String keyName;
	private String keyFilePath;
	private String keyPassword;
	private boolean kWKExportable;

	public ImportHSMKeyCommand(String keyName, String keyFilePath, String keyPassword, boolean kWKExportable) {

		this.keyName = keyName;
		this.keyFilePath = keyFilePath;
		this.keyPassword = keyPassword;
		this.kWKExportable = kWKExportable;
	}
	
	@Override
	protected void addCommandBody(StringBuffer builder) {

		builder.append("<CryptoImport>\r\n");
		
		builder.append("<ObjectType>key</ObjectType>\r\n");
		
		builder.append("<ObjectName>");
		builder.append(keyName);
		builder.append("</ObjectName>\r\n");
		
		builder.append("<InputFilename>");
		builder.append(keyFilePath);
		builder.append("</InputFilename>\r\n");
		
		builder.append("<ImportPassword>");
		builder.append(keyPassword);
		builder.append("</ImportPassword>\r\n");
// Removed in Firmware:XI52.7.0.0.5 and mgmt version 3.0
//		builder.append("<KwkExportable>");
//		if(kWKExportable){
//			builder.append("on");
//		}
//		else{
//			builder.append("off");
//		}
//		builder.append("</KwkExportable>\r\n");
		
		builder.append("</CryptoImport>\r\n");
	}

}
