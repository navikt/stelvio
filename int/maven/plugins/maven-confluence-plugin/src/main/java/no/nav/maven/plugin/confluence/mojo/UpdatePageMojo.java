package no.nav.maven.plugin.confluence.mojo;

import no.nav.maven.plugin.confluence.util.Constants;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.swizzle.confluence.Page;

/**
 * @author test@example.com
 * 
 * @requiresDependencyResolution
 * @goal update-page
 */
@SuppressWarnings("unchecked")
public class UpdatePageMojo extends ConfluenceMojo {

	public void doExecute() throws MojoExecutionException, MojoFailureException {

		Page page = null;
		try {
			page = confluence.getPage("stelvio", Constants.ENVMAPPING
					.get(environment));
		} catch (Exception e) {
			throw new RuntimeException(
					"An error occured trying to retrieve environment page", e);
		}
		String content = page.getContent();

		String oldline = "";
		for (String line : content.split("\n")) {
			if (line.contains("WPS services")) {
				oldline = line;
			}
		}
		String newline = "|| WPS services | Bus tag " + deployString
				+ " \\\\ | ||";
		getLog().info("Writing " + newline + " to confluence page");
		content = content.replace(oldline, newline);
		page.setContent(content);

		try {
			confluence.storePage(page);
		} catch (Exception e) {
			throw new RuntimeException(
					"An error occured trying to add a comment to the environment page",
					e);
		}
	}

	@Override
	protected String getGoalPrettyPrint() {
		return "Update environment page";
	}

};