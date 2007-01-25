package no.stelvio.presentation.security.page.definition;

import no.stelvio.presentation.security.page.definition.parse.SecurityConfiguration;
import no.stelvio.presentation.security.page.definition.parse.support.JsfApplication;

public class MockSecurityConfiguration implements SecurityConfiguration{

	 private JsfApplication secureApp;
	 
	 public MockSecurityConfiguration(JsfApplication jsfApp){
		 secureApp = jsfApp;
	 }
	 public JsfApplication getJsfApplication(){
		 return secureApp;
	 } 
	 public void setUpJSFApplication(){
		 
	 }
}
