package no.stelvio.common.security.acegi;

import no.stelvio.common.security.acegi.annotation.Secured;

@Secured({"Id1"})
public interface MockService {

	 @Secured({"Id2"})
	 public void someProtectedMethod();

	 @Secured({"Id3"})
	 public void anotherProtectedMethod();

}
