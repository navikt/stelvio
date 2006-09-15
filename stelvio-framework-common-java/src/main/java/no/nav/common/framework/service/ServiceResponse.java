package no.nav.common.framework.service;

import no.nav.common.framework.core.TransferObject;

/**
 * ServiceResponse is an abstract class representing the output from a <i>Service</i>.
 * Each implementation of a <i>Service</i> depends on a concrete subclass of ServiceResponse.
 * <p/>
 * ServiceResponse implements the <i>Transfer Object</i> pattern.
 * 
 * @author person7553f5959484
 * @version $Revision: 655 $ $Author: psa2920 $ $Date: 2004-06-17 16:45:47 +0200 (Thu, 17 Jun 2004) $
 */
public class ServiceResponse extends TransferObject {
	/**
	 * Constructs an empty ServiceResponse.
	 */
	public ServiceResponse() {
	}

	/**
	 * Constructs a ServiceResponse and 
	 * adds the data to the transfer object using the key.
	 * 
	 * @param key				key that uniquely identifies the data 
	 * @param data				the actual data object
	 */
	public ServiceResponse(String key, Object data) {
		this.setData(key, data);
	}
}