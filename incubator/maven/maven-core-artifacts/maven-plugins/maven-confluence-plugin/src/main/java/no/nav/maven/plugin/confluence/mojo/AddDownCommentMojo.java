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
		return environment + " is going down for deploy of: " + deployString
				+ ", and configuration: " + configurationString;
	}

	@Override
	protected String getGoalPrettyPrint() {
		return "Add environment going down comment";
	}
};