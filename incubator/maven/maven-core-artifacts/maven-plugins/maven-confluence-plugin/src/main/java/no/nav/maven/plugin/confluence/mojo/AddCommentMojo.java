package no.nav.maven.plugin.confluence.mojo;

import java.util.HashMap;
import java.util.Map;

import no.nav.maven.plugin.confluence.util.Constants;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.swizzle.confluence.Comment;
import org.codehaus.swizzle.confluence.Page;

/**
 * @author test@example.com
 */
@SuppressWarnings("unchecked")
public abstract class AddCommentMojo extends ConfluenceMojo {

	protected abstract String getComment();
	
	protected void doExecute() throws MojoExecutionException, MojoFailureException {
		


		Page page = null;
		try {
			page = confluence.getPage("stelvio", Constants.ENVMAPPING
					.get(environment));
		} catch (Exception e) {
			throw new RuntimeException(
					"An error occured trying to retrieve environment page", e);
		}

		Map map = new HashMap<String, String>();
		map.put("pageId", page.getId());
		map.put("content", getComment());
		Comment newcomment = new Comment(map);
		try {
			confluence.addComment(newcomment);
		} catch (Exception e) {
			throw new RuntimeException(
					"An error occured trying to add a comment to the environment page",
					e);
		}
	}
};