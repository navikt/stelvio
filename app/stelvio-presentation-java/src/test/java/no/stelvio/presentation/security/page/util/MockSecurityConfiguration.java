package no.stelvio.presentation.security.page.util;

import java.util.HashMap;

import no.stelvio.presentation.security.page.parse.JSFApplication;
import no.stelvio.presentation.security.page.parse.JSFPage;
import no.stelvio.presentation.security.page.parse.SSLConfig;
import no.stelvio.presentation.security.page.parse.SecurityConfiguration;

public class MockSecurityConfiguration implements SecurityConfiguration{

	 private JSFApplication secureApp;
	 
	 public MockSecurityConfiguration(JSFApplication jsfApp){
		 secureApp = jsfApp;
	 }
	 public JSFApplication getJsfApplication(){
		 return secureApp;
	 } 
	 public void setUpJSFApplication(){
		 
	 }
}
