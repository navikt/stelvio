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
	
	/**
	 * @parameter expression="${nodeport}" default-value=9080
	 * @required
	 */
	protected int nodePort;
	
	private final static String RELATIVE_URL = "/nav-cons-deploy-verifikasjonWebClient/jsp/TestClient.jsp";
	private static final String[] VERIFICATIONS = {"SCA verifikasjon", "WS verfikasjon", "CEI verifikasjon", "FEM verifikasjon"};
	
	public final void applyToWebSphere(final Commandline commandLine) throws MojoExecutionException, MojoFailureException {
		WebConversation wc = new WebConversation();
		WebRequest request = new GetMethodWebRequest("http://" + nodeHost + ":" + nodePort + RELATIVE_URL);
		
		WebResponse response = null;
		try {
			response = wc.getResponse(request);
		} catch (Exception e) {
			throw new RuntimeException("An error occured getting the response from request: " + request.getQueryString());
		}
		
		WebResponse menu = wc.getFrameContents( "methods");

		for(String verification : VERIFICATIONS) {
			WebLink verificationLink = null;
			try {
				verificationLink = menu.getFirstMatchingLink(WebLink.MATCH_CONTAINED_TEXT, verification);
			} catch (SAXException e) {
				throw new RuntimeException("Did not find the " +  verification + " link at the required url", e);
			}
			
			try {
				response = verificationLink.click();
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
			
			if(!"ok".equalsIgnoreCase(status)) {
				throw new RuntimeException("The status of the verification is: " + status + ". Required action is: " + action);
			}
			
			getLog().info("Verification of: " + verification + " is successfull with status " + status + " and action " + action);
		}
	}
	
	@Override
	protected String getGoalPrettyPrint() {
		return "Verify depoyment";
	}	


}	