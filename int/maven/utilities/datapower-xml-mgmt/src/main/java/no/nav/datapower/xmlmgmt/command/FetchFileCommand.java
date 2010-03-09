package no.nav.datapower.xmlmgmt.command;

import java.net.URL;

/**
 *	<FetchFile>
 *  	<URL>Source file URL</URL>
 *  	<File>Destination file URL</File>
 *  	<!--Optional:-->
 *  	<Overwrite>toggleValue</Overwrite>
 *  </FetchFile>
 * 
 * Example for local copy:
 *	<FetchFile>
 *  	<URL>local:///transform.xsl</URL>
 *  	<File>local:///xslt/transform.xsl</File>
 *  	<!--Optional:-->
 *  	<Overwrite>on</Overwrite>
 *  </FetchFile>
 * 
 * @author Torbjørn Staff, Accenture Technology Consulting
 *
 */
public class FetchFileCommand extends AbstractDoActionCommand {

	private final URL source;
	private final URL destination;
	private String overwrite;
	
	public FetchFileCommand(final URL source, final URL destination) {
		this(source, destination, true);
	}
	
	public FetchFileCommand(final URL source, final URL destination, final boolean overwrite) {
		this.source = source;
		this.destination = destination;
		this.overwrite = (overwrite ? "on" : "off");
	}
/*
	public String format() {
		StringBuffer req = new StringBuffer();
		req.append("<dp:do-action>\r\n");
		req.append("<FetchFile>\r\n");
		req.append("<URL>" + source.toString() + "</URL>\r\n");
		req.append("<File>" + destination.toString() + "</File>\r\n");
		req.append("<Overwrite>" + overwrite + "</Overwrite>\r\n");
		req.append("</FetchFile>\r\n");
		req.append("</dp:do-action>\r\n");
		return req.toString();
	}
*/

	protected void addCommandBody(StringBuffer builder) {
		builder.append("<FetchFile>\r\n");
		builder.append("<URL>" + source.toString() + "</URL>\r\n");
		builder.append("<File>" + destination.toString() + "</File>\r\n");
		builder.append("<Overwrite>" + overwrite + "</Overwrite>\r\n");
		builder.append("</FetchFile>\r\n");
	}	
}
