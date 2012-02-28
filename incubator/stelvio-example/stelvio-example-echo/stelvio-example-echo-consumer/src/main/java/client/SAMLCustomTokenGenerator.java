package client;

import java.util.ArrayList;
import com.ibm.websphere.wssecurity.wssapi.token.SAMLTokenFactory;
import com.ibm.websphere.wssecurity.wssapi.token.SecurityToken;
import com.ibm.wsspi.wssecurity.saml.config.CredentialConfig;
import com.ibm.wsspi.wssecurity.saml.config.RequesterConfig;
import com.ibm.wsspi.wssecurity.saml.config.ProviderConfig;
import com.ibm.wsspi.wssecurity.saml.data.SAMLAttribute;

public class SAMLCustomTokenGenerator {

	public static SecurityToken getSamlToken() throws RuntimeException {
		SecurityToken samlToken = null;
		try {
			SAMLTokenFactory samlFactory = SAMLTokenFactory.getInstance(SAMLTokenFactory.WssSamlV20Token11);

    		ProviderConfig providerCfg = samlFactory.newDefaultProviderConfig("WAS Self-issued");
	    		
    		CredentialConfig credentialCfg = samlFactory.newCredentialConfig();
    		credentialCfg.setRequesterNameID("test.local:389/utvPensjon");
    		ArrayList al = new ArrayList();
    		al.add (new SAMLAttribute ("Kontor", 						/* name */
    									new String [] {"Sannergata 2"},	/* stringAttributeValue */
    									null,							/* xmlAttributeValue */
    									null,							/* attributeNamespace */
    									null,							/* nameFormat */
    									null));							/* friendlyName */
    		credentialCfg.setSAMLAttributes (al);

    		RequesterConfig requesterCfg = samlFactory.newBearerTokenGenerateConfig();
	    		
    		samlToken = samlFactory.newSAMLToken(credentialCfg, requesterCfg, providerCfg);
	    	} catch (Exception e) {
	   			e.printStackTrace();
	   			throw new RuntimeException(e);
	    	}
	    	return samlToken;
	    }
}