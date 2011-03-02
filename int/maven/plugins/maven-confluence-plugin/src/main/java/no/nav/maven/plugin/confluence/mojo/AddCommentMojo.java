package no.nav.maven.plugin.confluence.mojo;

import java.util.HashMap;
import java.util.Map;

import no.nav.maven.plugin.confluence.util.Constants;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.swizzle.confluence.Comment;
import org.codehaus.swizzle.confluence.Page;

/**
 * An abstract class for goals adding comments to confluence pages. The template pattern is used, and all inheriting classes
 * must implement the getComment method which merely feeds this class with the comment. 
 * 
 * @author test@example.com
 */
@SuppressWarnings("unchecked")
public abstract class AddCommentMojo extends ConfluenceMojo {

	protected abstract String getComment();

    /**
     * Executes the addComment goal. This method is called from the execute() method in the ConfluenceMojo class. 
     *
     * @throws MojoExecutionException if the plugin failes to run. Causes an "BUILD ERROR" message
     * @throws MojoFailureException if the plugin failes to run. Causes an "BUILD FAILURE" message
     */
	protected void doExecute() throws MojoExecutionException,
			MojoFailureException {

		Page page = null;
		try {
			page = confluence.getPage("stelvio", Constants.ENVMAPPING
					.get(environment));
		} catch (Exception e) {
			throw new MojoExecutionException(
					"An error occured trying to retrieve environment page", e);
		}

		Map map = new HashMap<String, String>();
		map.put("pageId", page.getId());
		map.put("content", getComment());
		Comment newcomment = new Comment(map);
		try {
			getLog().info("Trying to add the following comment: " + getComment());
			confluence.addComment(newcomment);
		} catch (Exception e) {
			throw new MojoExecutionException(
					"An error occured trying to add a comment to the environment page",
					e);
		}
	}
};