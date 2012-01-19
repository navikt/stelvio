//------------------------------------------------------------------- Environment Properties --------------------------------------------------------------------------------------------

// Default PSELV junction name, usually "/dinpensjon".
var pselvJunction = "/dinpensjon";

// Default PSELV context root, usually "/pselv".
var pselvContextRoot = "/pselv";

// Path to "access denied" page in PSELV.
var pselvTilgangNektetURI = "/tilleggsfunksjonalitet/tilgangnektet.jsf";

// Path to "login" page in PSELV.
var endagspassordLoginURI = "/tilleggsfunksjonalitet/innlogging.jsf";

// Endagspassord login is usually set up identically to PSELV.
var endagspassordJunction = pselvJunction;
var endagspassordContextRoot = pselvContextRoot;

// Endpoint for federated single log-off.  Environment specific.
var minSideLogoutURL = "https://applikasjoner-t0.nav.no/FIM/sps/PSELVIDPorten/saml20/soapinitial?RequestBinding=HTTPRedirect";

//------------------------------------------------------------------- End Properties --------------------------------------------------------------------------------------------

// Returns the junction name for the current request.  This is needed to use a shared WebSEAL in test environments.
function getRequestedJunction(){
		var url = document.URL;
		var array = url.split("/");
		if(array.length > 3){
				var junction = array[3]
				return "/" + junction;
		} else {
				return "";
		}
}

// Builds the base URL for the PSELV instance serving the current request
function getPselvURL(){
		return getRequestedJunction() + pselvContextRoot;
}

// Returns the logout URL (for the default PSELV instance).
function getMinSideLogoutURL(){
		return minSideLogoutURL;
}

// Builds the URL to the default login page for the PSELV instance serving the current request.
function endagsPassordLoginURL() {
		var requestedJunction = getRequestedJunction();
		if (requestedJunction == pselvJunction) {
				return endagspassordJunction + endagspassordContextRoot + endagspassordLoginURI;
		} else {
				return requestedJunction + endagspassordContextRoot + endagspassordLoginURI;
		}
}

// Builds the URL to the default access denied page for the PSELV instance serving the current request.
function pselvTilgangNektetURL() {
		return getPselvURL() + pselvTilgangNektetURI;
}

// Change URL string from http to https.
function redirectToHttps(){
		var url = document.URL;
		var str = url.replace(/http:/,"https:");
		return str;
}

// After authentication, just hand control back to PSELV.
function getDefaultURLAfterAuthentication(){
		return getPselvURL();
}

// Before authentication, send the user to the correct login page.
function getAuthenticationURL() {
		return endagsPassordLoginURL();
}