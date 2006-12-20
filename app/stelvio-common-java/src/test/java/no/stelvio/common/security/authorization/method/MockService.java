package no.stelvio.common.security.authorization.method;

import org.acegisecurity.annotation.Secured;

@Secured({"Id1"})
@Roles({"ROLE_ALL"})
public interface MockService {

	 @Secured({"Id2"})
	 @Roles({"ROLE1","ROLE2"})
	 public void someProtectedMethod();

	 @Secured({"Id3"})
	 public void anotherProtectedMethod();

}
