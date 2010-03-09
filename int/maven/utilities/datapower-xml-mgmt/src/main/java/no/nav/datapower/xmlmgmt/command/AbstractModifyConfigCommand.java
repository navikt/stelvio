package no.nav.datapower.xmlmgmt.command;

public abstract class AbstractModifyConfigCommand extends AbstractBaseCommand {

	protected void openCommand(StringBuffer builder) {
		builder.append("<dp:modify-config>\r\n");
	}
	
	//public abstract void addCommandBody(StringBuffer builder);

	
	protected void closeCommand(StringBuffer builder) {
		builder.append("</dp:modify-config>\r\n");
	}
}
