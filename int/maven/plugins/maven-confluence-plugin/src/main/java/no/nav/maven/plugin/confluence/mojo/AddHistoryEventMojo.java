package no.nav.maven.plugin.confluence.mojo;

import java.util.Date;

import no.nav.maven.plugin.confluence.util.Constants;

import org.apache.maven.artifact.Artifact;
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
	private static final String EAR_ARTIFACT_TYPE = "ear";

	@Override
	protected void doExecute() throws MojoExecutionException, MojoFailureException {
		Page page = null;
		try {
			page = confluence.getPage("stelvio", no.nav.maven.plugin.confluence.util.Constants.ENVMAPPING.get(environment) + "-history");
		} catch (Exception e) {
			throw new RuntimeException("An error occured retrieving confluence page in stelvio space: " + Constants.ENVMAPPING.get(environment) + "-history", e);
		}		
	
		String content = page.getContent();
		
		content +=  "|" + new Date().toString() + "|" + deployString + "|" + configurationString + "|" + addModules() + "|" + "\n";
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

	private final String addModules() {
		StringBuffer buf = new StringBuffer();
		
		for(Artifact a : artifacts) {
			if(a.getType().equals(EAR_ARTIFACT_TYPE)) {
				buf.append( " " + a.getArtifactId() + "-" + a.getVersion());
			}
		}
		
		return buf.toString();
	}
};