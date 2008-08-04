/*
 * Created on Feb 8, 2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package no.nav.datapower.xmlmgmt.command;

/**
 * @author utvikler
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RestartThisDomainCommand extends AbstractDoActionCommand {

	/* (non-Javadoc)
	 * @see no.nav.maven.plugins.datapower.command.XMLMgmtCommand#format()
	 */
/*	public String format() {
		StringBuffer req = new StringBuffer();
		req.append("<dp:do-action>\r\n");
		req.append("<RestartThisDomain/>\r\n");
		req.append("</dp:do-action>\r\n");
		return req.toString();
	}
*/
	protected void addCommandBody(StringBuffer builder) {
		builder.append("<RestartThisDomain/>\r\n");
	}
}
