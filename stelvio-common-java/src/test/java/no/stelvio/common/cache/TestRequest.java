package no.stelvio.common.cache;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import no.stelvio.common.transferobject.ServiceRequest;

/**
 * Dummy ServiceRequest. This is meant to be used to test different cache key generators with use of hashCode and toString.
 * 
 */
public class TestRequest extends ServiceRequest {

	private static final long serialVersionUID = 3281621295913005169L;

	private String ansattId;
	private String enhetsId;

	public TestRequest(String ansattId, String enhetsId) {
		this.ansattId = ansattId;
		this.enhetsId = enhetsId;
	}

	public String getAnsattId() {
		return ansattId;
	}

	public void setAnsattId(String ansattId) {
		this.ansattId = ansattId;
	}

	public String getEnhetsId() {
		return enhetsId;
	}

	public void setEnhetsId(String enhetsId) {
		this.enhetsId = enhetsId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (!(o instanceof TestRequest)) {
			return false;
		}

		TestRequest other = (TestRequest) o;
		return new EqualsBuilder() //
				.append(this.ansattId, other.ansattId) //
				.append(this.enhetsId, other.enhetsId) //
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(ansattId).append(enhetsId).toHashCode();
	}

	/**
	 * Returns a string with the following format:
	 * 
	 * <pre>
	 * TestRequest[ansattId=D131445,enhetsId=3563]
	 * </pre>
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE) //
				.append(ansattId) //
				.append(enhetsId) //
				.toString();
	}
}