package no.nav.maven.plugin.websphere.plugin.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;
import org.xml.sax.SAXException;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.SubmitButton;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebLink;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;


/**
 * Goal that uses httpunit to call the esb for a health check
 * 
 * @author test@example.com
 * 
 * @goal verify-deployment
 * @requiresDependencyResolution
 */
public class VerifyDeploymentMojo extends WebsphereUpdaterMojo {
	/**
	 * @parameter expression="${nodehost}"
	 * @required
	 */
	protected String nodeHost;
	
	private final static String relativeUrl = "/nav-cons-deploy-verifikasjonWebClient/jsp/TestClient.jsp";
	
	public final void applyToWebSphere(final Commandline commandLine) throws MojoExecutionException, MojoFailureException {
		WebConversation wc = new WebConversation();
		WebRequest request = new GetMethodWebRequest("http://" + nodeHost + ":9080" + relativeUrl);
		
		WebResponse response = null;
		try {
			response = wc.getResponse(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		WebResponse menu = wc.getFrameContents( "methods");
		
		WebLink scaVerificationLink = null;
		try {
			scaVerificationLink = menu.getFirstMatchingLink(WebLink.MATCH_CONTAINED_TEXT, "SCA verifikasjon");
		} catch (SAXException e) {
			throw new RuntimeException("Did not find the SCA verification link at the required url", e);
		}
		
		try {
			response = scaVerificationLink.click();
		} catch (Exception e) {
			e.printStackTrace();
		}

		WebForm form = null;
		try {
			form = response.getForms()[0];
		} catch (SAXException e) {
			throw new RuntimeException("Unable to get the invoke form from the url", e);
		}
		
		SubmitButton button = form.getSubmitButtons()[0];
		try {
			response = form.submit(button);
		} catch (Exception e) {
			throw new RuntimeException("An error occured pushing the \"Invoke\" button", e);
		}
	
		WebResponse result = wc.getFrameContents( "result");

		WebTable table = null;
		try {
			table = result.getTables()[0];
		} catch (SAXException e) {
			throw new RuntimeException("An error occured trying to get the result table from the result frame");
		}
		
		String status = table.getCellAsText(1, 3);
		String action = table.getCellAsText(2, 3);
		
		if(!"osk".equalsIgnoreCase(status)) {
			throw new RuntimeException("The status of the verification is: " + status + ". Required action is: " + action);
		}
	
	}
	
	@Override
	protected String getGoalPrettyPrint() {
		return "Verify depoyment";
	}	


}	