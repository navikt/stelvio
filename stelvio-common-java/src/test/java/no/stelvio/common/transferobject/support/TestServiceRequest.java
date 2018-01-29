package no.stelvio.common.transferobject.support;

import no.stelvio.common.transferobject.ServiceRequest;

/**
 * Test class for ServiceRequest.
 * 
 * @author MA
 * 
 */
public class TestServiceRequest extends ServiceRequest {

	private static final long serialVersionUID = -3545252005545321556L;

	private String name;

	/**
	 * Creates a new instance of TestServiceRequest.
	 * 
	 * @param name
	 *            name
	 */
	public TestServiceRequest(String name) {
		this.name = name;
	}

	/**
	 * Get name.
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set name.
	 * 
	 * @param name
	 *            name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return super.toString();
	}

}
