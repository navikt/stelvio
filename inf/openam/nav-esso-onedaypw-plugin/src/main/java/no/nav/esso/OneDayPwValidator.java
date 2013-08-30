package no.nav.esso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;

import no.nav.virksomhet.tjenester.endagspassord.meldinger.v1.VerifiserEndagspassordRequest;
import no.nav.virksomhet.tjenester.endagspassord.meldinger.v1.VerifiserEndagspassordResponse;
import no.nav.virksomhet.tjenester.endagspassord.v1.binding.Endagspassord;
import no.nav.virksomhet.tjenester.endagspassord.v1.binding.Endagspassord_Service;
import no.nav.virksomhet.tjenester.endagspassord.v1.binding.VerifiserEndagspassordFeilBrukerIdEllerPassord;
import no.nav.virksomhet.tjenester.endagspassord.v1.binding.VerifiserEndagspassordPassordSperret;
import no.nav.virksomhet.tjenester.endagspassord.v1.binding.VerifiserEndagspassordPassordUtlopt;

public class OneDayPwValidator implements AuthValidator {

	private Endagspassord portType;
	// TODO: get from config
	private String untUserName;
	private String untPassword;
	private static final String DEFAULT_ENDPOINT = "http://e26apvl037.test.local:9080/nav-tjeneste-endagspassord_v1Web/sca/EndagspassordWSEXP";
	private String endpoint;
	
	public static void main(String[] args) {
		AuthValidator o = new OneDayPwValidator("untUserName", "untPassword", DEFAULT_ENDPOINT);
		ValidationResult result = o.validate("username", "password");
		System.out.println("\nPassord gyldig?: " + result.isValid());
	}
	
	public OneDayPwValidator(String untUserName, String untPassword, String endpoint) {
		this.untUserName = untUserName;
		this.untPassword = untPassword;
		this.endpoint = endpoint;
	}
	/* (non-Javadoc)
	 * @see no.nav.esso.AuthValidator#validate(java.lang.String, java.lang.String)
	 */
	@Override
	public ValidationResult validate(String userName, String password) {
		URL wsdlurl = OneDayPwValidator.class.getClassLoader().getResource("wsdl/no/nav/virksomhet/tjenester/endagspassord/v1/Binding.wsdl");
		Endagspassord_Service svc = new Endagspassord_Service(wsdlurl, new QName("http://nav.no/virksomhet/tjenester/endagspassord/v1/Binding/", "Endagspassord"));
		svc.setHandlerResolver(new HandlerResolver() {
			
			@Override
			public List<Handler> getHandlerChain(PortInfo portInfo) {
				// TODO Auto-generated method stub
				UsernameTokenHandler unthandler = new UsernameTokenHandler(untUserName, untPassword);
				List<Handler> handlerchain = new ArrayList<Handler>();
				handlerchain.add(unthandler);
				return handlerchain;
			}
		});
		
		portType = svc.getPort(Endagspassord.class);
		BindingProvider bp = (BindingProvider) portType;
		bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpoint);
		
		try {
			VerifiserEndagspassordRequest request = new VerifiserEndagspassordRequest();
			request.setBrukerId(userName);
			request.setPassord(password);
			VerifiserEndagspassordResponse reply = portType.verifiserEndagspassord(request);
			return new ValidationResult(
					reply.getEndagspassord().getBrukerId(), 
					reply.getEndagspassord().isGyldig().booleanValue(),
					reply.getEndagspassord().getPaloggingsnivaKode());
		} catch (VerifiserEndagspassordFeilBrukerIdEllerPassord e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (VerifiserEndagspassordPassordSperret e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (VerifiserEndagspassordPassordUtlopt e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ValidationResult(null, false, null);
	}

}
