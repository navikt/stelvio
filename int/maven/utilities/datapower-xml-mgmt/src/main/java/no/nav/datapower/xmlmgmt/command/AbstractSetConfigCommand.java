package no.nav.datapower.xmlmgmt.command;

public abstract class AbstractSetConfigCommand extends AbstractBaseCommand {

	protected void openCommand(StringBuffer builder) {
		builder.append("<dp:set-config>\r\n");
	}
	
	protected void closeCommand(StringBuffer builder) {
		builder.append("</dp:set-config>\r\n");
	}
}
