package no.nav;

import java.util.Hashtable;
import java.util.Iterator;
import javax.security.auth.Subject;

import com.ibm.websphere.security.WSSecurityException;
import com.ibm.websphere.security.auth.WSSubject;

public final class OIDCUtil {

	 private static final String ID_TOKEN = "id_token";
	 private static final String ACCESS_TOKEN = "access_token";
	 private static final String REFRESH_TOKEN = "refresh_token";
	 private static final String TOKEN_TYPE = "token_type";
	 private static final String SCOPE = "scope";

	public static String getAccessToken() throws WSSecurityException {
	  String idToken = null;  
	  Subject subject = WSSubject.getCallerSubject();  

	  if (subject != null) {
	    Iterator<Hashtable> authIterator = subject.getPrivateCredentials(Hashtable.class).iterator();
	    if ( authIterator.hasNext() ) {
	      Hashtable creds = (Hashtable) authIterator.next();
	      if ( creds != null ) {
	        idToken = (String) creds.get(ACCESS_TOKEN);
	      }
	    }
	  }
	  return idToken;
	}
}
