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
		return environment + " is up again with: " + deployString
				+ ", and configuration: " + configurationString;
	}

	@Override
	protected String getGoalPrettyPrint() {
		return "Add environment up again comment";
	}
};