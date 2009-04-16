package no.nav.maven.plugin.confluence.mojo;

/**
 * @author@author test@example.com
 * 
 * 
 * @goal add-comment-up
 */
@SuppressWarnings("unchecked")
public class AddUpCommentMojo extends AddCommentMojo {

	protected String getComment() {

		/*
		 * if ((deployString.length() < 1) || (configurationString.length() <
		 * 1)) { getLog() .warn( "Bus-deploy or Bus-configuration flag is not
		 * set. Confluence is not updated with comment"); }
		 */

		return "Test: " + environment + " is up again with: " + deployString
				+ ", and configuration: " + configurationString;
	}
};