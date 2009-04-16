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
public abstract class AddCommentMojo extends AbstractMojo {

	/**
	 * 
	 * @parameter expression="${username}" default-value="deployer"
	 * @required
	 */
	protected String userName;

	/**
	 * 
	 * @parameter expression="${password}" default-value="deployer"
	 * @required
	 */
	protected String password;

	/**
	 * 
	 * @parameter expression="${endpoint}"
	 *            default-value="http://confluence.adeo.no"
	 * @required
	 */
	protected String endPoint;

	/**
	 * @parameter expression="${environment}"
	 * @required
	 */
	protected String environment;

	/**
	 * @parameter expression="${comment}"
	 */
	protected String comment;

	/**
	 * @parameter expression="${project.dependencyArtifacts}"
	 * @required
	 */
	protected Set<Artifact> dependencyArtifacts;

	protected String deployString = "";

	protected String configurationString = "";

	public void execute() throws MojoExecutionException, MojoFailureException {

		for (Artifact a : dependencyArtifacts) {
			if (a.getArtifactId().equals("bus-deploy")) {
				deployString = a.getVersion();
			}
			if (a.getArtifactId().equals("busconfiguration")) {
				configurationString = a.getVersion();
			}
		}

		Confluence confluence = null;
		try {
			confluence = new Confluence(endPoint);
		} catch (MalformedURLException e) {
			throw new RuntimeException(
					"The URL: " + endPoint + " is not valid", e);
		}

		try {
			confluence.login(userName, password);
		} catch (Exception e) {
			throw new RuntimeException("Unable to log in to confluence", e);
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

	protected abstract String getComment();
};