package no.stelvio.common.security.acegi;

import no.stelvio.common.security.acegi.annotation.Secured;
@Secured({"IdThatDoesNotExistInMapping"})
public interface MockService2 {
	 
	@Secured({"Id2","AnotherIdThatDoesNotExistInMapping"})
	 public void someProtectedMethod();
}
