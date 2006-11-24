package no.stelvio.common.security.authorization.method;

import org.acegisecurity.annotation.Secured;

@Secured({"IdThatDoesNotExistInMapping"})
public interface MockService2 {
	 
	@Secured({"Id2","AnotherIdThatDoesNotExistInMapping"})
	 public void someProtectedMethod();
}
