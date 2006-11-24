package no.stelvio.common.security.authorization.method;

import org.acegisecurity.annotation.Secured;

@Secured({"Id1"})
public interface MockService {

	 @Secured({"Id2"})
	 public void someProtectedMethod();

	 @Secured({"Id3"})
	 public void anotherProtectedMethod();

}
