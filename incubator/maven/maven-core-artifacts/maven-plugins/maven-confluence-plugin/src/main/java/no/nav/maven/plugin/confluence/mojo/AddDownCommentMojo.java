package no.nav.maven.plugin.confluence.mojo;

/**
 * @author test@example.com
 * 
 * @requiresDependencyResolution
 * @goal add-comment-down
 */
@SuppressWarnings("unchecked")
public class AddDownCommentMojo extends AddCommentMojo {

	protected String getComment() {

		/*
		 * if ((deployString.length() < 1) || (configurationString.length() <
		 * 1)) { getLog() .warn( "Bus-deploy or Bus-configuration flag is not
		 * set. Confluence is not updated with comment"); }
		 */

		return "Test: " + environment + " is going down for deploy of: " + deployString
				+ ", and configuration: " + configurationString;
	}
};