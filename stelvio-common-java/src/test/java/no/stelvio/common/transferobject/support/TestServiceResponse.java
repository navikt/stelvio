package no.stelvio.common.transferobject.support;

import no.stelvio.common.transferobject.ServiceResponse;

/**
 * Test class for ServiceResponse.
 * 
 *
 */
public class TestServiceResponse extends ServiceResponse {

	private static final long serialVersionUID = 1L;
	
	private String msg;

	/**
	 * Creates a new instance of TestServiceResponse.
	 * 
	 * @param msg
	 *            message
	 */
	public TestServiceResponse(String msg) {
		this.msg = msg;
	}

	/**
	 * Get message.
	 * 
	 * @return message
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * Set message.
	 * 
	 * @param msg
	 *            message
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

}
