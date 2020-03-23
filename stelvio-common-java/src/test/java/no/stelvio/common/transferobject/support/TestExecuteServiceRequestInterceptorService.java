package no.stelvio.common.transferobject.support;

/**
 * Pojo used for testing the {@link ExecuteServiceRequestInterceptor}.
 * 
 *
 */
public class TestExecuteServiceRequestInterceptorService {

	/**
	 * Sends a hallo request and returns the response.
	 * 
	 * @param request
	 *            request
	 * @return response
	 */
	public TestServiceResponse sayHelloTo(TestServiceRequest request) {
		return new TestServiceResponse("Hello there, " + request.getName());
	}

}
