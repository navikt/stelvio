/*
 * Created on Feb 8, 2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package no.nav.maven.plugins.datapower.command;

/**
 * @author utvikler
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SaveConfigCommand implements XMLMgmtCommand {

	public String format() {
		StringBuffer req = new StringBuffer();
		req.append("<dp:do-action>\r\n");
		req.append("<SaveConfig/>\r\n");
		req.append("</dp:do-action>\r\n");
		return req.toString();
	}
}
