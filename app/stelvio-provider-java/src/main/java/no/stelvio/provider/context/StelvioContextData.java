package no.stelvio.provider.context;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author test@example.com
 */
@XmlRootElement(name = "StelvioContext", namespace = "http://www.nav.no/StelvioContextPropagation")
public class StelvioContextData {
	private String applicationId;
	private String correlationId;
	private String languageId;
	private String userId;

	@XmlElement(name = "applicationId")
	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	@XmlElement(name = "correlationId")
	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	@XmlElement(name = "languageId")
	public String getLanguageId() {
		return languageId;
	}

	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}

	@XmlElement(name = "userId")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
