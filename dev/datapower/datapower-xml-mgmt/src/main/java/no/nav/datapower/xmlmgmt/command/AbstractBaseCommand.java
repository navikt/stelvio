package no.nav.datapower.xmlmgmt.command;


public abstract class AbstractBaseCommand implements XMLMgmtCommand {
	
	protected abstract void openCommand(StringBuffer builder);
	
	protected abstract void addCommandBody(StringBuffer builder);

	protected abstract void closeCommand(StringBuffer builder);

	public String format() {
		StringBuffer commandBuilder = new StringBuffer();
		openCommand(commandBuilder);
		addCommandBody(commandBuilder);
		closeCommand(commandBuilder);
		return commandBuilder.toString();
	}
}
