package no.stelvio.common.security.authorization.method;

import org.acegisecurity.annotation.Secured;

/**
 * Mock service.
 * 
 * @author ??
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
