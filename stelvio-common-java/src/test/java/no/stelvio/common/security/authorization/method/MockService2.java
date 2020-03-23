package no.stelvio.common.security.authorization.method;

import org.springframework.security.access.annotation.Secured;

/**
 * Mock service.
 * 
 *
 */
@Secured({ "IdThatDoesNotExistInMapping" })
public interface MockService2 {

	/**
	 * Some protected method.
	 */
	@Secured({ "Id2", "AnotherIdThatDoesNotExistInMapping" })
	void someProtectedMethod();
}
