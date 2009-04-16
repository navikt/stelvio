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
public abstract class ConfluenceMojo extends AbstractMojo {
	
	/** @component */
	protected Prompter prompter;
	
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
	 * @parameter expression="${project.dependencyArtifacts}"
	 * @required
	 */
	protected Set<Artifact> dependencyArtifacts;

	/**
	 * @parameter expression="${interactiveMode}" default-value="false"
	 * @required
	 */
	protected Boolean interactiveMode;

	protected Confluence confluence;
	
	public void execute() throws MojoExecutionException, MojoFailureException {

		if(interactiveMode == true) {
			String answer=null;
			try {
				answer = prompter.prompt("Do you want to perform step \"" + getGoalPrettyPrint() + "\" (y/n)? ", "n");
			} catch (PrompterException e) {
				throw new MojoFailureException(e, "An error occured during prompt input","An error occured during prompt input");
			}
			if("n".equalsIgnoreCase(answer)) {
				getLog().info("Skipping step: " + getGoalPrettyPrint());
				return;
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
		
		doExecute();
	}

	protected abstract String getGoalPrettyPrint();
	protected abstract void doExecute() throws MojoExecutionException, MojoFailureException;
};