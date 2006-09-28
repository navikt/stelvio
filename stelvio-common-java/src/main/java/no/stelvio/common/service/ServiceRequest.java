package no.stelvio.common.service;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import no.stelvio.common.core.TransferObject;

/**
 * ServiceRequest is an abstract class representing the input to a <i>Service</i>.
 * Each implementation of a <i>Service</i> depends on a concrete subclass of ServiceRequest.
 * <p/>
 * ServiceRequest implements the <i>Transfer Object</i> pattern.
 * 
 * @author person7553f5959484
 * @version $Revision: 2570 $ $Author: psa2920 $ $Date: 2005-10-19 18:07:03 +0200 (Wed, 19 Oct 2005) $
 */
public class ServiceRequest extends TransferObject {

	private String serviceName = null;

	/**
	 * Constructs a ServiceRequest where serviceName is unspecified.
	 */
	public ServiceRequest() {
	}

	/**
	 * Constructs a ServiceRequest for specified service.
	 * 
	 * @param serviceName the name of the requested service
	 */
	public ServiceRequest(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * Constructs a ServiceRequest for specified service and 
	 * adds the data to the transfer object using the key.
	 * 
	 * @param serviceName 	the name of the requested service
	 * @param key				key that uniquely identifies the data 
	 * @param data				the actual data object
	 */
	public ServiceRequest(String serviceName, String key, Object data) {
		this.serviceName = serviceName;
		this.setData(key, data);
	}

	/**
	 * Retrive the name of the requested service.
	 * 
	 * @return name of the requested service.
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * Assign the name of the requested service.
	 * 
	 * @param name name of the requested service.
	 */
	public void setServiceName(String name) {
		serviceName = name;
	}

	/**
	 * Compares the name and content for equality.
	 * 
	 * {@inheritDoc}
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {
		if (!(other instanceof ServiceRequest)) {
			return false;
		}
		ServiceRequest castOther = (ServiceRequest) other;
		return new EqualsBuilder().appendSuper(super.equals(other)).append(this.serviceName, castOther.serviceName).isEquals();
	}

	/**
	 * Returns the name and content's hash code.
	 * 
	 * {@inheritDoc}
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder().appendSuper(super.hashCode()).append(serviceName).toHashCode();
	}

	/**
	 * Returns a String representation of this instance.
	 * 
	 * {@inheritDoc}
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this, TransferObjectToStringStyle.INSTANCE)
			.append("serviceName", serviceName)
			.appendSuper(super.toString())
			.toString();
	}
}