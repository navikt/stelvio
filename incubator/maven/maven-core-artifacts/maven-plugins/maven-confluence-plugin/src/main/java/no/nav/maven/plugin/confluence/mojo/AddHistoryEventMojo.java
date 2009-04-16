package no.nav.maven.plugin.confluence.mojo;

import java.util.Date;

import no.nav.maven.plugin.confluence.util.Constants;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.swizzle.confluence.Page;

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
		Page page = null;
		try {
			page = confluence.getPage("stelvio", Constants.ENVMAPPING.get(environment) + "-history");
		} catch (Exception e) {
			throw new RuntimeException("An error occured retrieving confluence page in stelvio space: " + Constants.ENVMAPPING.get(environment) + "-history", e);
		}		
	
		String content = page.getContent();
		
		content +=  "|" + new Date().toString() + "|" + deployString + "|" + configurationString + "|" + "\n";
		page.setContent(content);
		
		try {
			confluence.storePage(page);
		} catch (Exception e) {
			throw new RuntimeException("An error occured savinfg confluence page after an update", e);
		}
	}

	@Override
	protected String getGoalPrettyPrint() {
		return "Add deploy history event";
	}

};