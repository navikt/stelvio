
//------------------------------------------------------------------- Properties --------------------------------------------------------------------------------------------

//Pensjon properties
var pselvJunction = "/dinpensjon";
var pselvContextRoot = "/pselv";
var pselvTilgangNektetURI = "/tilleggsfunksjonalitet/tilgangnektet.jsf?_flowId=tilgangnektet-flow";
var endagspassordJunction = pselvJunction;
var endagspassordContextRoot = pselvContextRoot;
var endagspassordLoginURI = "/tilleggsfunksjonalitet/innlogging.jsf?_flowId=innlogging-flow"; 
var minsideLogoutUrl = "https://applikasjoner-t.nav.no/fim/sps/PSELV-MinIDtest/saml20/sloinitial?RequestBinding=HTTPArtifact";

//------------------------------------------------------------------- End Properties --------------------------------------------------------------------------------------------

//Returns the URL for the PSELV application
function getPselvURL(){
	return pselvJunction + pselvContextRoot;
}

function getMinSideLogoutURL(){
	return minsideLogoutUrl;
}

//Returns the URL for the endagspassord authentication login page.
function endagsPassordLoginURL() {
	return endagspassordJunction + endagspassordContextRoot + endagspassordLoginURI;
}

function defaultLoginURL(){
	return endagsPassordLoginURL();
}


//Returns the URL to the page that a user should receive when denied access to PSELV.
function pselvTilgangNektetURL() {
	return pselvJunction + pselvContextRoot + pselvTilgangNektetURI;
}

// Returns the junction name for the current request
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


function getDefaultURLAfterAuthentication(){
	var requestedJunction = getRequestedJunction();
	if (requestedJunction == pselvJunction){
	
		return getPselvURL();
		
	} else if (requestedJunction == "/junctionToAnotherApplication" ){
		
		return "/junctionToAnotherEai/default.html";
		
	} else {
		
		return "";
	}
}

function redirectToHttps(){
	var url = document.URL;
	var str = url.replace(/http/,"https");
	return str; 
}

//Returns the URL to the login page of the appropriate External Authentication Interface matching the junction the user is trying to access. 
function getAuthenticationURL() {
	var requestedJunction = getRequestedJunction();
	if (requestedJunction == pselvJunction){
	
		return endagsPassordLoginURL();
		
	} else if (requestedJunction == "/junctionToAnotherApplication" ){
		
		return "/junctionToAnotherEai/loginpage.html";
		
	} else {
		
		return defaultLoginURL();
	}
}



