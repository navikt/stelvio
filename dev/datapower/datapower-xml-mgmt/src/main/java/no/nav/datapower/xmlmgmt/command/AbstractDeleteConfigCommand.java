package no.nav.datapower.xmlmgmt.command;

public abstract class AbstractDeleteConfigCommand extends AbstractBaseCommand {

	protected void openCommand(StringBuffer builder) {
		builder.append("<dp:del-config>\r\n");
	}

	protected void closeCommand(StringBuffer builder) {
		builder.append("</dp:del-config>\r\n");
	}
}
