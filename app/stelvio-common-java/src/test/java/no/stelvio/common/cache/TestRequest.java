package no.stelvio.common.cache;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import no.stelvio.common.transferobject.ServiceRequest;

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
	
	@Override
	public String toString() {
		return new ToStringBuilder(this).toString();
	}
}