package no.nav.maven.plugin.confluence.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * @author test@example.com
 * 
 * @requiresDependencyResolution
 * @goal add-history-event
 */
@SuppressWarnings("unchecked")
public class AddHistoryEventMojo  extends ConfluenceMojo {

	@Override
	protected void doExecute() throws MojoExecutionException, MojoFailureException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String getGoalPrettyPrint() {
		return "Add deploy history event";
	}

};