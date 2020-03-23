package no.stelvio.common.security.authorization.method;

import org.springframework.security.access.annotation.Secured;

/**
 * Mock service.
 * 
 *
 */
@Secured({ "Id1" })
@Roles({ "ROLE_ALL" })
public interface MockService {

	/**
	 * Some protected method.
	 */
	@Secured({ "Id2" })
	@Roles({ "ROLE1", "ROLE2" })
	void someProtectedMethod();

	/**
	 * Another protected method.
	 */
	@Secured({ "Id3" })
	void anotherProtectedMethod();

}
