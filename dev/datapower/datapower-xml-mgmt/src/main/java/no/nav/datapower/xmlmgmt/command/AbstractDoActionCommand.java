package no.nav.datapower.xmlmgmt.command;

public abstract class AbstractDoActionCommand extends AbstractBaseCommand {

	protected void openCommand(StringBuffer builder) {
		builder.append("<dp:do-action>\r\n");
	}

	protected void closeCommand(StringBuffer builder) {
		builder.append("</dp:do-action>\r\n");
	}
}
