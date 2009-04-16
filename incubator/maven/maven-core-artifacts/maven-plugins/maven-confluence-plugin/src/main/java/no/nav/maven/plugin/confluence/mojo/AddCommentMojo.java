package no.nav.maven.plugin.confluence.mojo;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import no.nav.maven.plugin.confluence.util.Constants;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;
import org.codehaus.swizzle.confluence.Comment;
import org.codehaus.swizzle.confluence.Confluence;
import org.codehaus.swizzle.confluence.Page;

/**
 * @author test@example.com
 * 
 * 
 * @goal add-comment
 */
@SuppressWarnings("unchecked")
public abstract class AddCommentMojo extends ConfluenceMojo {

	protected String deployString = "";
	protected String configurationString = "";

	protected abstract String getComment();
	
	protected void doExecute() throws MojoExecutionException, MojoFailureException {
		
		for (Artifact a : dependencyArtifacts) {
			if (a.getArtifactId().equals("bus-deploy")) {
				deployString = a.getVersion();
			}
			if (a.getArtifactId().equals("busconfiguration")) {
				configurationString = a.getVersion();
			}
		}

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